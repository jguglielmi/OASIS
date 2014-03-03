
// Name: JmeterDriverFixture
// Author: Edward Jakubowski ejakubowski@qed-sys.com
// Last update: 11/12/2013
// Description: This Fixture add support to run Jmeter projects in Fitnesse
// Requirements: Jmeter's Lib directory
// Examples:
//   

package org.qedsys.fitnesse;

import org.qedsys.jmeter.JmeterBundle;

public class JmeterDriverFixture {

	JmeterBundle jmeter;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("starting driver...");
		JmeterDriverFixture jmDriver = new JmeterDriverFixture();
		
		jmDriver.runTestPlan("SimpleSearchTest.jmx");
		System.out.println("done.");
	}
	
	public void setJmeterHome(String homePath) {
		JmeterBundle.jmeterHomePath = homePath;
		jmeter.jEngine.exit();
		jmeter = new JmeterBundle();
	}
	
	public JmeterDriverFixture() {
		jmeter = new JmeterBundle();
	}
	
	public boolean runTestPlan(String testPlanFilename) {
		return jmeter.runTestPlan(testPlanFilename);
	}
	
	public boolean setProperty(String propertyName, String propertyValue) {
		return jmeter.setProperty(propertyName, propertyValue);
	}
	
	public boolean loadTestPlan(String testPlanFilename) {
		return jmeter.loadTestPlan(testPlanFilename);
	}
		
	public boolean initTestPlan(String testPlanName) {
		return jmeter.initTestPlan(testPlanName);
	}
	
	public boolean setThreadGroup(int numberOfThreads, int rampUp, int loops) {
		return jmeter.setThreadGroup(numberOfThreads, rampUp, loops);
	}
	
	public boolean setThreadGroupUsersToRampUpToAndLoopsTo(int numberOfThreads, int rampUp, int loops) {
		return jmeter.setThreadGroup(numberOfThreads, rampUp, loops);
	}
	
	public boolean httpSampler(String domain, int port, String path, String method) {
		return jmeter.httpSampler(domain, port, path, method);
	}
	
	public boolean httpSamplerWithDomainUsingPortUsingPathUsingMethod(String domain, int port, String path, String method) {
		return jmeter.httpSampler(domain, port, path, method);
	}

	public boolean createSimpleDataWriter(String outputFilename) {
		return jmeter.createSimpleDataWriter(outputFilename);
	}
	
	public boolean createSimpleDataWriterToFile(String outputFilename) {
		return jmeter.createSimpleDataWriter(outputFilename);
	}
	
	public boolean testAction(int action) {
		return jmeter.testAction(action);
	}
	
	public boolean pauseTestAction(String duration) {
		return jmeter.pauseTestAction(duration);
	}
	
	public boolean pauseTestWithDuration(String duration) {
		return jmeter.pauseTestAction(duration);
	}
	
	public boolean runTestPlan() {
		return jmeter.runTestPlan();
	}
	
}
