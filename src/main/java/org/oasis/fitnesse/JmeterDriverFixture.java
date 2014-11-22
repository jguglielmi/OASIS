
// Name: JmeterDriverFixture
// Author: Edward Jakubowski ejakubowski7@gmail.com
// Last update: 11/12/2013
// Description: This Fixture add support to run Jmeter projects in Fitnesse
// Requirements: Jmeter's Lib directory
// Examples:
//   

package org.oasis.fitnesse;

import org.oasis.plugin.Util;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.control.ReplaceableController;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.threads.RemoteThreadsListenerTestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.jorphan.util.JOrphanUtils;


public class JmeterDriverFixture {

    public String jmeterHomePath = ".";
	public String jmeterPropertiesFile = "./jmeter/jmeter.properties";
	public static String jmeterLogPath = "./FitNesseRoot/files/testResults/";
	//public String testPlanFilename = "./jmeter/SimpleHttpTest1.jmx";
	public JMeterEngine jEngine = null;
	public HashTree currentHashTree = null;
	
	private String jmeterResults = "";
	private String lastJmeterLog = "null";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("done.");
	}
	
	public void createJmeterEngine() {
		//JMeterEngine jEngine = null;
		try {
			String currentPath = new File(jmeterHomePath).getCanonicalPath();
			JMeterUtils.setJMeterHome(currentPath);
			System.out.println("Setting Jmeter Home: " + currentPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.setProperty(JMeter.JMETER_NON_GUI, "true");
		jEngine = new StandardJMeterEngine();
        // jmeter.properties
        JMeterUtils.loadJMeterProperties(jmeterPropertiesFile);
		JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
		JMeterUtils.initLocale();
        //return jEngine;
	}
	
	public void setJmeterHome(String homePath) {
		jmeterPropertiesFile = homePath;
		jEngine.exit();
		createJmeterEngine();
	}
	
	public JmeterDriverFixture() {
		createJmeterEngine();
	}
	
	public boolean runTestPlan(String testPlanFilename) {

		jmeterResults = "";
		createJmeterEngine();
	    File f = new File(testPlanFilename);
	    if (!f.exists() || !f.isFile()) {
	    	jmeterResults += "Could not open " + testPlanFilename;
	        System.out.println(jmeterResults);
	        return false;
	    }

		FileInputStream reader = null;
		try {
			reader = new FileInputStream(new File(testPlanFilename));
			currentHashTree = SaveService.loadTree(reader); 
			//store log file in ./FitNesseRoot/files/testResults/testPlanFilename.log
			String logFile = new File(jmeterLogPath, (new File(testPlanFilename).getName() + ".log")).getCanonicalPath();
			lastJmeterLog = logFile;
			
	        @SuppressWarnings("deprecation") // Deliberate use of deprecated ctor
	        JMeterTreeModel treeModel = new JMeterTreeModel(new Object());// Create non-GUI version to avoid headless problems
	        JMeterTreeNode root = (JMeterTreeNode) treeModel.getRoot();
	        treeModel.addSubTree(currentHashTree, root);

	        // Hack to resolve ModuleControllers in non GUI mode
	        SearchByClass<ReplaceableController> replaceableControllers = new SearchByClass<ReplaceableController>(ReplaceableController.class);
	        currentHashTree.traverse(replaceableControllers);
	        Collection<ReplaceableController> replaceableControllersRes = replaceableControllers.getSearchResults();
	        for (Iterator<ReplaceableController> iter = replaceableControllersRes.iterator(); iter.hasNext();) {
	            ReplaceableController replaceableController = iter.next();
	            replaceableController.resolveReplacementSubTree(root);
	        }

	        // Remove the disabled items
	        // For GUI runs this is done in Start.java
	        JMeter.convertSubTree(currentHashTree);

	        Summariser summer = null;
	        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");//$NON-NLS-1$
	        if (summariserName.length() > 0) {
	            //log.info("Creating summariser <" + summariserName + ">");
	            //println("Creating summariser <" + summariserName + ">");
	            summer = new Summariser(summariserName);
	        }

	        if (logFile != null) {
	            ResultCollector logger = new ResultCollector(summer);
	            logger.setFilename(logFile);
	            currentHashTree.add(currentHashTree.getArray()[0], logger);
	        }
	        else {
	            // only add Summariser if it can not be shared with the ResultCollector
	            if (summer != null) {
	            	currentHashTree.add(currentHashTree.getArray()[0], summer);
	            }
	        }

	        // Used for remote notification of threads start/stop,see BUG 54152
	        // Summariser uses this feature to compute correctly number of threads 
	        // when NON GUI mode is used
	        currentHashTree.add(currentHashTree.getArray()[0], new RemoteThreadsListenerTestElement());
			
	        jEngine.configure(currentHashTree);
	        jEngine.runTest();
			//reader.close();
	        JOrphanUtils.closeQuietly(reader);
	        Util.waitForFileToExists(logFile, 5); //wait up to 5 seconds for file to exist
	        String logStr = Util.fileToString(logFile);
	        //logStr = logStr.replaceAll("\n", "<br/>\n");
	        jmeterResults += logStr;
	        jmeterResults += "Test " + testPlanFilename + " completed.";
	        System.out.println("Test " + testPlanFilename + " completed.");
	        
		} catch (Exception e) {
			e.printStackTrace();
			jmeterResults += "\r\nException: " +e.getMessage();
			return false;
		}
		return true;
	}
	
	public String getLastLog() {
		return jmeterResults;
	}
	
	public boolean deleteLastLog() {
		File lstLog = new File(lastJmeterLog);
		return lstLog.delete();
	}

	
	public boolean setProperty(String propertyName, String propertyValue) {
		System.setProperty(propertyName, propertyValue);
		return true;
	}
	
	public void startGui() throws IOException {
		//String currentPath = new File(jmeterHomePath).getCanonicalPath();
		//JMeterUtils.setJMeterHome(currentPath);
		org.apache.jmeter.NewDriver.main(new String[]{"-p" + jmeterPropertiesFile});
	}
}
