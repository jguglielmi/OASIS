
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
import com.jcraft.jsch.*;

public class SshDriverFixture {
	Session session = null;
	String passwordPromptStr = " password";
	int shellCommandResponseDelay = 10; // seconds
	String lastResults = "";

	public static void main(String[] args) {
		System.out.println("starting driver...");
		System.out.println("done.");
	}

	public SshDriverFixture () {
	}
	
	//example:
	//| connect to | host | with user | username | and password | password | 
	public boolean connectToWithUserAndPassword(String hostname, String sshUser, String sshPwd) {
		return connect(hostname, sshUser, sshPwd);
	}
	
	public boolean connect(String hostname, String sshUser, String sshPwd) {
		try {
			sshPwd = org.oasis.plugin.Util.processDecryptionString(sshPwd);
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
		lastResults = "";
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
		lastResults = output;
		return output;
	}
	
	public void setShellCommandResponseDelay(int delay) {
		shellCommandResponseDelay = delay;
	}
	
	public void setPasswordPromptString(String passwordPrompt) {
		passwordPromptStr = passwordPrompt;
	}
	
	//this is similar to sshpass: sshpass -ptest1324 ssh -o "StrictHostKeyChecking no" user@host ls -l /tmp
	//example:
	//| execute command | ssh -o "StrictHostKeyChecking no" user@host ls -l /tmp | provide password | test1234 |
	
	public String executeCommandProvidePassword(String command, String password) {
	    String output = "";
		lastResults = "";
	    try {
	    	password = org.oasis.plugin.Util.processDecryptionString(password);
	    	//System.out.println("SSH Session executing command: " + command);
	        Channel channel = session.openChannel("shell");
	        InputStream in=channel.getInputStream();
	        OutputStream out=channel.getOutputStream();
	        channel.connect();
	        //Thread.sleep(1000);
	        out.write((command + "\n").getBytes());
	        out.flush();
	        //Thread.sleep(1000);
	        boolean pwdFlg = false;
	        
	        int timeout = shellCommandResponseDelay; //10 seconds
	        byte[] tmp=new byte[1024];
	        while(true) {
	            while(in.available() > 0) {
	                int i = in.read(tmp, 0, 1024);
	                if ( i < 0 )
	                    break;
	                String line = new String(tmp, 0, i);
	                output += line;
	                //System.out.print(line);
	                timeout = shellCommandResponseDelay;
	            }
				// if password was requested, then send the password
				if (output.contains(passwordPromptStr) && !pwdFlg) {
					pwdFlg = true;
					//System.out.println("SSH Session sending password");
			        out.write((password + "\n").getBytes());
			        out.flush();
				}
	            if(channel.isClosed()){
	                //System.out.println("exit-status: " + channel.getExitStatus());
	                break;
	            }
	            try{Thread.sleep(1000);}catch(Exception ee){}
	            --timeout;
	            if (timeout <= 0)
	                break;
	        }
	        channel.disconnect();
	    }
	    catch (Exception ex) {
			ex.printStackTrace();
	    }
	    //System.out.println("SSH Session command completed");
		lastResults = output;
	    return output;
	}
	
	public String getLastResults() {
		return lastResults;
	}
	
	public void setLastResults(String results) {
		lastResults = results;
	}

	//clear out any unicode or weird ascii codes 
	public String sanitizeResults() {
		lastResults = lastResults.replaceAll("[^x09-x0bx20-x7e]", "");
		return lastResults;
	}

	public String replaceAllWith(String pattern, String replacement) {
		//(".*[^\\d](\\d+).*", "$1")
		return lastResults.replaceAll(pattern, replacement);
	}
	
	public boolean executeCommandEqualsResult(String command, String expectString) { 
		String result = executeCommand(command);
		return result.equals(expectString);
	}

	public boolean executeCommandContainsResult(String command, String expectString) { 
		String result = executeCommand(command);
		return result.contains(expectString);
	}
	
	public String usingStringReplaceAllWith(String source, String pattern, String replacement) {
		//(".*[^\\d](\\d+).*", "$1")
		//("(?m)^\s+$", "")
		return source.replaceAll(pattern, replacement);
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