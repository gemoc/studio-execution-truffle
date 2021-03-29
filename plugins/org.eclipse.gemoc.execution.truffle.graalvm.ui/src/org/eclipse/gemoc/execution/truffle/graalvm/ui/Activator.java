package org.eclipse.gemoc.execution.truffle.graalvm.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.gemoc.execution.truffle.graalvm.ui"; //$NON-NLS-1$
	
	public static final String DEBUG_MODEL_ID = PLUGIN_ID + ".debugModel"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Logs the specified status
	 * @param status the status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	/**
	 * Logs the specified message, by creating a new <code>Status</code>
	 * @param message the message to log as an error status
	 */
	public static void log(String message) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, null));
	}

	/**
	 * Logs the specified exception by creating a new <code>Status</code>
	 * @param e the {@link Throwable} to log as an error
	 */
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, e.getMessage(), e));
	}

}
