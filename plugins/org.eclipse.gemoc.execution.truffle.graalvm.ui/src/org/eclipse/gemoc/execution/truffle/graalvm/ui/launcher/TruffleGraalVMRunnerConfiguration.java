package org.eclipse.gemoc.execution.truffle.graalvm.ui.launcher;

import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class TruffleGraalVMRunnerConfiguration extends VMRunnerConfiguration {

	int dapPort = -1;
	
	public TruffleGraalVMRunnerConfiguration(String classToLaunch, String[] classPath) {
		super(classToLaunch, classPath);
	}

	public int getDapPort() {
		return dapPort;
	}

	public void setDapPort(int dapPort) {
		this.dapPort = dapPort;
	}

	
}
