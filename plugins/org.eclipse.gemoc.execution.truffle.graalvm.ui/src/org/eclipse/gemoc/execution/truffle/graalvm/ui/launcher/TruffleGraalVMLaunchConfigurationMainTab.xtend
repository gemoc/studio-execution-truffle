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

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.jface.window.Window
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.widgets.Text
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog
import org.eclipse.ui.model.WorkbenchLabelProvider

class TruffleGraalVMLaunchConfigurationMainTab extends AbstractLaunchConfigurationTab
{
	public static val SourceFileExtension = 'n'
	public static val GenFileExtension = 'ngen'
	public static val OptionsFileExtension = 'json'
	public static val MoniloggerFileExtension = 'mnlg'
	public static val MainJar =''
	public static val MainClass =''
	public static val ProgramArguments =''
	boolean fDisableUpdate = false

	Text fTxtMainJar
	Text fTxtMainClass
	Text fTxtProgramArguments

	override createControl(Composite parent) 
	{
		val topControl = new Composite(parent, SWT.NONE)
		topControl.setLayout(new GridLayout(1, false))

		val grpMainJar = new Group(topControl, SWT.NONE)
		grpMainJar.setLayout(new GridLayout(2, false))
		grpMainJar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))
		grpMainJar.setText("Main jar")

		fTxtMainJar = new Text(grpMainJar, SWT.BORDER)
		fTxtMainJar.addModifyListener([e | if (!fDisableUpdate) updateLaunchConfigurationDialog])
		fTxtMainJar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))

		val grpMainClass = new Group(topControl, SWT.NONE)
		grpMainClass.setLayout(new GridLayout(2, false))
		grpMainClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))
		grpMainClass.setText("Main class")

		fTxtMainClass = new Text(grpMainClass, SWT.BORDER)
		fTxtMainClass.addModifyListener([e | if (!fDisableUpdate) updateLaunchConfigurationDialog])
		fTxtMainClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))
		
		val grpProgramArguments = new Group(topControl, SWT.NONE)
		grpProgramArguments.setLayout(new GridLayout(2, false))
		grpProgramArguments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))
		grpProgramArguments.setText("Program Arguments")

		fTxtProgramArguments = new Text(grpProgramArguments, SWT.BORDER.bitwiseOr(SWT.MULTI))
		//TODO set several lines for the widget
		fTxtProgramArguments.addModifyListener([e | if (!fDisableUpdate) updateLaunchConfigurationDialog])
		fTxtProgramArguments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1))

		setControl(topControl)
	}
	
	override getName()
	{
		'Global'
	}

	override initializeFrom(ILaunchConfiguration configuration)
	{
		fDisableUpdate = true

		fTxtMainJar.text = ''
		fTxtMainClass.text = ''
		fTxtProgramArguments.text = ''

		try
		{
			fTxtMainJar.text = configuration.getAttribute(TruffleGraalVMLaunchConstants::MAIN_JAR_FILE_LOCATION, '')
			fTxtMainClass.text = configuration.getAttribute(TruffleGraalVMLaunchConstants::MAIN_CLASS_NAME, '')
			fTxtProgramArguments.text = configuration.getAttribute(TruffleGraalVMLaunchConstants::PROGRAM_ARGUMENTS, '')
			
		}
		catch (CoreException e)
		{
		}
		fDisableUpdate = false
	}

	override performApply(ILaunchConfigurationWorkingCopy configuration)
	{
		configuration.setAttribute(TruffleGraalVMLaunchConstants::MAIN_JAR_FILE_LOCATION, fTxtMainJar.text)
		configuration.setAttribute(TruffleGraalVMLaunchConstants::MAIN_CLASS_NAME, fTxtMainClass.text)
		configuration.setAttribute(TruffleGraalVMLaunchConstants::PROGRAM_ARGUMENTS, fTxtProgramArguments.text)
	}

	override setDefaults(ILaunchConfigurationWorkingCopy configuration)
	{
		configuration.setAttribute(TruffleGraalVMLaunchConstants::MAIN_JAR_FILE_LOCATION, '')
		configuration.setAttribute(TruffleGraalVMLaunchConstants::MAIN_CLASS_NAME, '')
		configuration.setAttribute(TruffleGraalVMLaunchConstants::PROGRAM_ARGUMENTS, '')
	}
}

class NablaFileSelectionAdapter extends SelectionAdapter
{
	val Composite parent
	val Text fTxtFile

	new(Composite parent, Text fTxtFile)
	{
		this.parent = parent
		this.fTxtFile = fTxtFile
	}

	override void widgetSelected(SelectionEvent e)
	{
		val dialog = new ElementTreeSelectionDialog(parent.shell, new WorkbenchLabelProvider, new SourceFileContentProvider(TruffleGraalVMLaunchConfigurationMainTab::SourceFileExtension))
		dialog.setTitle("Select Nablab File")
		dialog.setMessage("Select the nablab file to execute:")
		dialog.setInput(ResourcesPlugin.workspace.root)
		if (dialog.open == Window.OK)
			fTxtFile.setText((dialog.firstResult as IFile).fullPath.makeRelative.toPortableString)
	}
}

class NablaGenFileSelectionAdapter extends SelectionAdapter
{
	val Composite parent
	val Text fTxtFile

	new(Composite parent, Text fTxtFile)
	{
		this.parent = parent
		this.fTxtFile = fTxtFile
	}

	override void widgetSelected(SelectionEvent e)
	{
		val dialog = new ElementTreeSelectionDialog(parent.shell, new WorkbenchLabelProvider, new SourceFileContentProvider(TruffleGraalVMLaunchConfigurationMainTab::GenFileExtension))
		dialog.setTitle("Select Nablagen File")
		dialog.setMessage("Select the nablagen file to use for the execution:")
		dialog.setInput(ResourcesPlugin.workspace.root)
		if (dialog.open == Window.OK)
			fTxtFile.setText((dialog.firstResult as IFile).fullPath.makeRelative.toPortableString)
	}
}

class NablaOptionsFileSelectionAdapter extends SelectionAdapter
{
	val Composite parent
	val Text fTxtFile

	new(Composite parent, Text fTxtFile)
	{
		this.parent = parent
		this.fTxtFile = fTxtFile
	}

	override void widgetSelected(SelectionEvent e)
	{
		val dialog = new ElementTreeSelectionDialog(parent.shell, new WorkbenchLabelProvider, new SourceFileContentProvider(TruffleGraalVMLaunchConfigurationMainTab::OptionsFileExtension))
		dialog.setTitle("Select Options File")
		dialog.setMessage("Select the options file to use for the execution:")
		dialog.setInput(ResourcesPlugin.workspace.root)
		if (dialog.open == Window.OK)
			fTxtFile.setText((dialog.firstResult as IFile).fullPath.makeRelative.toPortableString)
	}
}

class MoniloggerFileSelectionAdapter extends SelectionAdapter
{
	val Composite parent
	val Text fTxtFile

	new(Composite parent, Text fTxtFile)
	{
		this.parent = parent
		this.fTxtFile = fTxtFile
	}

	override void widgetSelected(SelectionEvent e)
	{
		val dialog = new ElementTreeSelectionDialog(parent.shell, new WorkbenchLabelProvider, new SourceFileContentProvider(TruffleGraalVMLaunchConfigurationMainTab::MoniloggerFileExtension))
		dialog.setTitle("Select Monilogger File")
		dialog.setMessage("Select a monilogger file to add to the execution:")
		dialog.setInput(ResourcesPlugin.workspace.root)
		if (dialog.open == Window.OK)
			fTxtFile.setText((dialog.firstResult as IFile).fullPath.makeRelative.toPortableString)
	}
}
