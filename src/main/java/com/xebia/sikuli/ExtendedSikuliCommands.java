package com.xebia.sikuli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExtendedSikuliCommands {
	private String methodName;
	public int targetCount=1;

	public ExtendedSikuliCommands(String methodName) {
		this.methodName = methodName;
	}

	public boolean isSupportedBySikuliDriver() {
		return SIKULI_DRIVER_COMMANDS.contains(methodName);
	}
	
	private static final Set<String> NO_PARAMETERS = new HashSet<String>(Arrays.asList(new String[] {
			"getScreenSize",
			"showStatusText",
			"hideStatusText",
			"getLocation",
			"fuzzyTarget",
			"getLocation",
			"closeCurrentWindow",
			"selectAll"
	}));

	private static final Set<String> SIKULI_DRIVER_COMMANDS = new HashSet<String>(Arrays.asList(new String[] {
			"getScreenSize",
			"showStatusText",
			"hideStatusText",
			"displayText",
			"displayTextFor",
			"setWaitTimeTo",
			"setTargetXOffsetToSetTargetYOffsetTo",
			"setTheMatchingSimilarityTo",
			"highlight",
			"fuzzyTarget",
			"imgFileOrNone",
			"click",
			"waitClick",
			"waitClickTrue",
			"doubleClick",
			"rightClick",
			"hover",
			"keyDown",
			"keyUp",
			"keyPress",
			"drop",
			"drag",
			"dragDrop",
			"getLocation",
			"wait",
			"executeCommand",
			"closeCurrentWindow",
			"selectAll",
			"type",
			"clearType",
			"paste",
			"waitOr",
			"clickOr",
			"doubleClickOnOr",
			"rightClickOnOr",
			"delay",
			"processDecryptionString",
			"whileIsHiddenThenClick",
			"whileIsHiddenThenKeypress",
			"takeScreenCapture"	
	}));

	public Set<String> getSikuliDriverCommands(){
		return SIKULI_DRIVER_COMMANDS;
	}


	public boolean isOrCommand(){
		if(methodName.contains("Or") || methodName.contains("OnOr")){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isWhileCommand(){
		if(methodName.contains("while")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean hasNoParameters(){
		return NO_PARAMETERS.contains(methodName);
	}
	
	public boolean hasOneParameter(){
		if(!hasNoParameters()&&!hasTwoParameters()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean hasTwoParameters(){
		if(isOrCommand() || isWhileCommand() || methodName=="displayTextFor" || methodName=="dragDrop" || methodName=="setTargetXOffsetToSetTargetYOffsetTo"){
			return true;
		}
		else{
			return false;
		}
	}
	
	public SikuliDriverFixture newSikuliDriverFixture() {
		return new SikuliDriverFixture();
	}
}
