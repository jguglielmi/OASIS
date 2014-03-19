
// Name: SshDriverFixture
// Author: Edward Jakubowski ejakubowski@qed-sys.com
// Last update: 12/23/2013
// Description: This Fixture adds support to connect to ssh servers and execute commands.
// Requirements: jsch library
// 	<dependency>
// 		<groupId>com.jcraft</groupId>
// 		<artifactId>jsch</artifactId>
// 		<version>0.1.48</version>
// 	</dependency>
// Examples:

package org.qedsys.fitnesse;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
import com.jcraft.jsch.*;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;


public class SshDriverFixture {
	Session session = null;

	public static void main(String[] args) {
		System.out.println("starting driver...");
		System.out.println("done.");
	}

	public SshDriverFixture () {
	}
	
	public boolean connectToWithUserAndPassword(String hostname, String sshUser, String sshPwd) {
		sshPwd = org.oasis.plugin.Util.processDecryptionString(sshPwd);
		return connect(hostname, sshUser, sshPwd);
	}
	
	public boolean connect(String hostname, String sshUser, String sshPwd) {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sshUser, hostname, 22);
			session.setPassword(sshPwd);
			//ignore hostkeychecking (adding the host to the ~/.ssh/known_hosts
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String executeCommand(String command) {
		String output = "";
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			//((ChannelExec)channel).setPty(true);
			channel.connect();

			byte[] tmp=new byte[1024];
			while(true) {
				while(in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if ( i < 0 )
						break;
					String line = new String(tmp, 0, i);
					output += line;
					//System.out.print(line);
				}
				if(channel.isClosed()){
					//System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try{Thread.sleep(1000);}catch(Exception ee){}
			}
			channel.disconnect();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return output;
	}
	
	public boolean executeCommandEqualsResult(String command, String expectString) { 
		String result = executeCommand(command);
		return result.equals(expectString);
	}

	public boolean executeCommandContainsResult(String command, String expectString) { 
		String result = executeCommand(command);
		return result.contains(expectString);
	}
	
	public static String newString(String val) {
		return new String(val);
	}
	
	public boolean stringContains(String str, String expectString) { 
		return str.contains(expectString);
	}

	public boolean stringDoesNotContain(String str, String expectString) { 
		return !str.contains(expectString);
	}

	public boolean stringMatches(String str, String regexString) { 
		return str.matches(regexString);
	}

	public boolean stringDoesNotMatch(String str, String regexString) { 
		return !str.matches(regexString);
	}
	
	public boolean disconnect() {
		try {
			if (session == null)
				return false;
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean getSshPowerState(String hostname, String sshUser, String sshPwd) {
		JSch jsch = new JSch();
		Session session;
		try {
			session = jsch.getSession(sshUser, hostname, 22);
			session.setPassword(sshPwd);
			//ignore hostkeychecking (adding the host to the ~/.ssh/known_hosts
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(5000);
		}
		catch (Exception ex) {
			return false;
		}
		session.disconnect();
		return true;
	}

}