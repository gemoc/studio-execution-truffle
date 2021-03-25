package org.eclipse.gemoc.execution.truffle.graalvm.ui.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gemoc.dsl.debug.ide.IDSLDebugger;
import org.eclipse.gemoc.dsl.debug.ide.event.DSLDebugEventDispatcher;
import org.eclipse.gemoc.dsl.debug.ide.sirius.ui.launch.AbstractDSLLaunchConfigurationDelegateSiriusUI;
import org.eclipse.gemoc.execution.truffle.graalvm.ui.Activator;
import org.eclipse.jdt.internal.launching.LaunchingMessages;
import org.eclipse.jdt.internal.launching.LaunchingPlugin;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;

public class TruffleGraalVMLaunchConfigurationDelegate extends AbstractDSLLaunchConfigurationDelegateSiriusUI {

	
	public final static String TYPE_ID = Activator.PLUGIN_ID + ".launcher";

	private VMRunnerConfiguration getVMRunnerConfiguration(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {

		monitor.beginTask(NLS.bind("{0}...", new String[]{configuration.getName()}), 3); //$NON-NLS-1$
		// check for cancellation
		if (monitor.isCanceled()) {
			return null;
		}
		monitor.subTask(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_Verifying_launch_attributes____1);

		String mainTypeName = configuration.getAttribute(TruffleGraalVMLaunchConstants.MAIN_CLASS_NAME,""); // TODO verifyMainTypeName(configuration);

		File workingDir = null; // verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null) {
			workingDirName = workingDir.getAbsolutePath();
		}

		// Environment variables
		String[] envp = getEnvironment(configuration);

		// Program & VM arguments
		String pgmArgs = configuration.getAttribute(TruffleGraalVMLaunchConstants.PROGRAM_ARGUMENTS,""); // TODO getProgramArguments(configuration);
		String vmArgs = ""; // TODO concat(getVMArguments(configuration), getVMArguments(configuration, mode));
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, pgmArgs);

		// VM-specific attributes
		Map<String, Object> vmAttributesMap = new HashMap<>(); //getVMSpecificAttributesMap(configuration);

		// Create VM config
		// Bug 529435 :to move to getClasspathAndModulepath after java 8 is sunset
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration(mainTypeName, getClasspath(configuration));
		runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
		runConfig.setEnvironment(envp);
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);
		runConfig.setPreviewEnabled(false);
		// Module name not required for Scrapbook page
		/*if (supportsModule() && !mainTypeName.equals("org.eclipse.jdt.internal.debug.ui.snippeteditor.ScrapbookMain")) { //$NON-NLS-1$
			// Module name need not be the same as project name
			try {
				IJavaProject proj = JavaRuntime.getJavaProject(configuration);
				if (proj != null) {
					IModuleDescription module = proj == null ? null : proj.getModuleDescription();
					String modName = module == null ? null : module.getElementName();
					if (modName != null && modName.length() > 0) {
						String defaultModuleName = null;
						String moduleName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MODULE_NAME, defaultModuleName);
						if (moduleName != null) {
							runConfig.setModuleDescription(moduleName);
						} else {
							runConfig.setModuleDescription(modName);
						}
					}
				}
			} catch (CoreException e) {
				// Not a java Project so no need to set module description
			}
		}*/

		// Launch Configuration should be launched by Java 9 or above for modulepath setting
		/*if (!JavaRuntime.isModularConfiguration(configuration)) {
			// Bootpath
			runConfig.setBootClassPath(getBootpath(configuration));
		} else if (supportsModule()) {
			// module path
			String[][] paths = getClasspathAndModulepath(configuration);
			if (paths != null && paths.length > 1) {
				runConfig.setModulepath(paths[1]);
			}
			if (!configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_MODULE_CLI_OPTIONS, true)) {
				runConfig.setOverrideDependencies(configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MODULE_CLI_OPTIONS, "")); //$NON-NLS-1$
			} else {
				runConfig.setOverrideDependencies(getModuleCLIOptions(configuration));
			}
		}*/
		runConfig.setMergeOutput(configuration.getAttribute(DebugPlugin.ATTR_MERGE_OUTPUT, false));

		// check for cancellation
		if (monitor.isCanceled()) {
			return null;
		}
		monitor.worked(1);

		return runConfig;
	}
	
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		IVMInstall vm = verifyVMInstall(configuration);
		IVMRunner runner = vm.getVMRunner(mode); // TODO  actually we run in "run" mode with options for graalVM to expose DAP interface
		if (runner == null) {
			abort(NLS.bind(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_0, new String[]{vm.getName(), mode}), null, IJavaLaunchConfigurationConstants.ERR_VM_RUNNER_DOES_NOT_EXIST);
		}
		return runner;
	}
	/**
	 * Verifies the VM install specified by the given launch configuration
	 * exists and returns the VM install.
	 *
	 * @param configuration
	 *            launch configuration
	 * @return the VM install specified by the given launch configuration
	 * @exception CoreException
	 *                if unable to retrieve the attribute, the attribute is
	 *                unspecified, or if the home location is unspecified or
	 *                does not exist
	 */
	public IVMInstall verifyVMInstall(ILaunchConfiguration configuration)
			throws CoreException {
		IVMInstall vm = getVMInstall(configuration);
		if (vm == null) {
			abort(
					LaunchingMessages.AbstractJavaLaunchConfigurationDelegate_The_specified_JRE_installation_does_not_exist_4,
					null,
					IJavaLaunchConfigurationConstants.ERR_VM_INSTALL_DOES_NOT_EXIST);
		}
		File location = vm.getInstallLocation();
		if (location == null) {
			abort(
					NLS.bind(LaunchingMessages.AbstractJavaLaunchConfigurationDelegate_JRE_home_directory_not_specified_for__0__5, new String[]{vm.getName()}),
					null,
					IJavaLaunchConfigurationConstants.ERR_VM_INSTALL_DOES_NOT_EXIST);
		}
		if (!location.exists()) {
			abort(
					NLS.bind(LaunchingMessages.AbstractJavaLaunchConfigurationDelegate_JRE_home_directory_for__0__does_not_exist___1__6,
									new String[]{vm.getName(),
											location.getAbsolutePath()}),
					null,
					IJavaLaunchConfigurationConstants.ERR_VM_INSTALL_DOES_NOT_EXIST);
		}
		return vm;
	}
	/**
	 * Returns the VM install specified by the given launch configuration, or
	 * <code>null</code> if none.
	 *
	 * @param configuration
	 *            launch configuration
	 * @return the VM install specified by the given launch configuration, or
	 *         <code>null</code> if none
	 * @exception CoreException
	 *                if unable to retrieve the attribute
	 */
	public IVMInstall getVMInstall(ILaunchConfiguration configuration)
			throws CoreException {
		// TODO use only graal VM installations 
		return JavaRuntime.computeVMInstall(configuration);
	}
	
	/**
	 * Returns an array of environment variables to be used when
	 * launching the given configuration or <code>null</code> if unspecified.
	 *
	 * @param configuration launch configuration
	 * @return an array of environment variables to use when launching the given configuration or null if unspecified
	 * @throws CoreException if unable to access associated attribute or if
	 * unable to resolve a variable in an environment variable's value
	 * @since 3.1
	 */
	public String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);
	}
	public String[] getClasspath(ILaunchConfiguration configuration)
			throws CoreException {
		/*IRuntimeClasspathEntry[] entries = JavaRuntime
				.computeUnresolvedRuntimeClasspath(configuration);
		entries = JavaRuntime.resolveRuntimeClasspath(entries, configuration);

		List<String> userEntries = new ArrayList<>(entries.length);
		Set<String> set = new HashSet<>(entries.length);
		for (IRuntimeClasspathEntry entry : entries) {
			if (entry.getClasspathProperty() == IRuntimeClasspathEntry.USER_CLASSES
					|| entry.getClasspathProperty() == IRuntimeClasspathEntry.CLASS_PATH) {
				String location = entry.getLocation();
				if (location != null) {
					if (!set.contains(location)) {
						userEntries.add(location);
						set.add(location);
					}
				}
			}
		}
		return userEntries.toArray(new String[userEntries.size()]);*/
		
		return new String[] {configuration.getAttribute(TruffleGraalVMLaunchConstants.MAIN_JAR_FILE_LOCATION,"")};
	}

	@Override
	public final void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch,
			IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		try {

			VMRunnerConfiguration runConfig = getVMRunnerConfiguration(configuration, mode, monitor);
			if (runConfig == null) {
				return;
			}
			// stop in main
			//prepareStopInMain(configuration);

			// done the verification phase
			monitor.worked(1);

			monitor.subTask(LaunchingMessages.JavaLocalApplicationLaunchConfigurationDelegate_Creating_source_locator____2);
			// set the default source locator if required
			//setDefaultSourceLocator(launch, configuration);
			monitor.worked(1);
			// Launch the configuration - 1 unit of work
			IVMRunner runner = getVMRunner(configuration, mode);
			System.out.println(runner.showCommandLine(runConfig, launch, monitor));
			runner.run(runConfig, launch, monitor);

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}
		}
		finally {
			monitor.done();
		}
	}
	
	
	@Override
	protected String getLaunchConfigurationTypeID() {
		return TYPE_ID;
	}

	@Override
	protected EObject getFirstInstruction(ISelection selection) {
		return EcorePackage.eINSTANCE;
	}

	@Override
	protected EObject getFirstInstruction(IEditorPart editor) {
		return EcorePackage.eINSTANCE;
	}

	@Override
	protected IDSLDebugger getDebugger(ILaunchConfiguration configuration, DSLDebugEventDispatcher dispatcher,
			EObject firstInstruction, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDebugTargetName(ILaunchConfiguration configuration, EObject firstInstruction) {
		return "GEMOC Truffle GraalVM debug target";
	}

	@Override
	protected String getDebugJobName(ILaunchConfiguration configuration, EObject firstInstruction) {
		return "GEMOC Truffle GraalVM debug job";
	}

	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public String getModelIdentifier() {
		return Activator.DEBUG_MODEL_ID;
	}
	
	protected void abort(String message, Throwable exception, int code)
			throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, code, message, exception));
	}

}
