package org.eclipse.gemoc.truffle.js.interpreter.launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

public final class JSTruffleInterpreterMain {

	private static final String JS = "js";

	/**
	 * The main entry point.
	 */
	public static void main(String[] args) throws IOException {
		Source source;
		final Map<String, String> options = new HashMap<>();
		String jsFile = null;
		for (String arg : args) {
			if (parseOption(options, arg)) {
				continue;
			} else {
				if (jsFile == null) {
					jsFile = arg;
				}
			}
		}

		if (jsFile == null) {
			throw new IllegalArgumentException("Missing .js file");
		}
		
		source = Source.newBuilder(JS, new File(jsFile)).build();
		System.exit(executeSource(source, System.in, System.out, options));
	}

	private static int executeSource(Source source,  InputStream in, PrintStream out, Map<String, String> options)
			throws IOException {
		final Context context;
		final PrintStream err = System.err;

		try {
			context = Context.newBuilder().in(in).out(out) //
					.allowAllAccess(true) //
					.allowExperimentalOptions(true) //
					.options(options) //
					.build();
		} catch (IllegalArgumentException e) {
			err.println(e.getMessage());
			return 1;
		}
		out.println("== running on " + context.getEngine());

		try {
			context.eval(source); 
			return 0;
		} catch (PolyglotException ex) {
			if (ex.isInternalError()) {
				// for internal errors we print the full stack trace
				ex.printStackTrace();
			} else {
				err.println(ex.getMessage());
			}
			return 1;
		} finally {
			context.close();
		}
	}

	private static String readFileAsString(String filePath) throws IOException	{
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

	private static boolean parseOption(Map<String, String> options, String arg) {
		if (arg.length() <= 2 || !arg.startsWith("--")) {
			return false;
		}
		int eqIdx = arg.indexOf('=');
		String key;
		String value;
		if (eqIdx < 0) {
			key = arg.substring(2);
			value = null;
		} else {
			key = arg.substring(2, eqIdx);
			value = arg.substring(eqIdx + 1);
		}

		if (value == null) {
			value = "true";
		}
		int index = key.indexOf('.');
		String group = key;
		if (index >= 0) {
			group = group.substring(0, index);
		}
		options.put(key, value);
		return true;
	}
}
