
// Name: WinApiDriverFixture
// Author: Edward Jakubowski ejakubowski7@gmail.com
// Last update: 3/08/2014
// Description: This Fixture adds support for running synthuse's commands from Fitnesse
// Requirements: Jna library
// Examples:
//   

package org.synthuse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.synthuse.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynthuseDriverFixture {

	private CommandProcessor cmdProcessor = null;
	private static final Logger LOG = LoggerFactory.getLogger(SynthuseDriverFixture.class);
	

	public static void main(String[] args) {
		System.out.println("starting driver...");
		System.out.println("done.");
	} 
	
	public SynthuseDriverFixture () {
		cmdProcessor = new CommandProcessor();
	}
	
	public Object doOn(String action, String onArg) {
	    LOG.info("doOn " + action + ", " + onArg);
		return cmdProcessor.execute(action, new String[] {onArg});
	}

	public Object doOnWith(String action, String onArg, String withArg) {
	    LOG.info("doOnWith " + action + ", " + onArg + ", " + withArg);
		return cmdProcessor.execute(action, new String[] {onArg, withArg});
	}
	
	public String getWindowsXml() {
		return WindowsEnumeratedXml.getXml();
	}
	
	public String queryWindowInfo(String xpath) {
		return WindowsEnumeratedXml.queryWindowInfoXml(xpath);
	}
}
