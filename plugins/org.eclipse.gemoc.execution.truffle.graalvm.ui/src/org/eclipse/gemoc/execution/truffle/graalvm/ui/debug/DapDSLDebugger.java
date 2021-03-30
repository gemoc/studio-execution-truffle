package org.eclipse.gemoc.execution.truffle.graalvm.ui.debug;

import java.io.Serializable;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gemoc.dsl.debug.ide.event.IDSLDebugEvent;

public class DapDSLDebugger implements org.eclipse.gemoc.dsl.debug.ide.IDSLDebugger {

	@Override
	public Object handleEvent(IDSLDebugEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspended(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canStepInto(String threadName, EObject instruction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stepInto(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void steppingInto(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepOver(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void steppingOver(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepReturn(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void steppingReturn(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepped(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resuming(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspend(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBreakPoint(URI instruction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBreakPoint(URI instruction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeBreakPoint(URI instruction, String attribute, Serializable value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breaked(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean control(String threadName, EObject instruction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void spawnRunningThread(String threadName, EObject context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EObject getNextInstruction(String threadName, EObject currentInstruction, Stepping stepping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTerminated(boolean terminated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldBreak(EObject instruction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void variable(String threadName, String stackName, String declarationTypeName, String variableName,
			Object value, boolean supportModifications) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteVariable(String threadName, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData(String threadName, EObject instruction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushStackFrame(String threadName, String frameName, EObject context, EObject instruction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popStackFrame(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentInstruction(String threadName, EObject instruction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminated(String threadName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTerminated(String threadName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateVariableValue(String threadName, String variableName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getVariableValue(String threadName, String stackName, String variableName, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVariableValue(String threadName, String stackName, String variableName, Object value) {
		// TODO Auto-generated method stub
		
	}

}
