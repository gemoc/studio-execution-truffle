/*******************************************************************************
 * Copyright (c) 2020 CEA
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package org.eclipse.gemoc.execution.truffle.graalvm.ui.launcher

import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.debug.core.ILaunchConfiguration

class TruffleGraalVMLaunchConstants {
	package static val LAUNCH_CONFIGURATION_TYPE_ID = 'fr.cea.nabla.ui.graalvm.launchconfig.NablaLaunchConfigurationType'

	package static val MONILOGGER_FILES_LOCATIONS = 'Monilogger Files Locations'

	package static val PROJECT = 'Project name'
	package static val NGEN_FILE_LOCATION = 'Nablagen File location'
	package static val N_FILE_LOCATION = 'Nabla File location'
	package static val JSON_FILE_LOCATION = 'Json file location'
	package static val MAIN_JAR_FILE_LOCATION = 'Main jar file location'
	package static val MAIN_CLASS_NAME = 'Main class name'
	package static val PROGRAM_ARGUMENTS = 'Program arguments'
	package static val ADDITIONAL_VM_ARGUMENTS = 'Additional VM arguments'

	package static def getProject(ILaunchConfiguration configuration) {
		val projectName = configuration.getAttribute(TruffleGraalVMLaunchConstants::PROJECT, '')
		ResourcesPlugin::workspace.root.getProject(projectName)
	}

	package static def getFile(IProject project, ILaunchConfiguration configuration, String constantName) {
		if (project.exists) {
			val fileName = configuration.getAttribute(constantName, '')
			if (!fileName.nullOrEmpty) {
				val file = project.getFile(fileName)
				if (file.exists)
					return file
			}
		}
		return null
	}
}
