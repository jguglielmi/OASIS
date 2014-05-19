package com.xebia.incubator.xebium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SeleniumGridFixture {
	public int port = 4444;
	public String host = "localhost";
	public String browserParameters="";

	private static final Logger LOG = LoggerFactory.getLogger(SeleniumGridFixture.class);

	public SeleniumGridFixture(){
		super();
	}

	public void setPort(int port){
		this.port=port;
		LOG.info("Port set to:" + port + ".");
	}

	public void setBrowserParameters(String browserParameters){
		this.browserParameters=browserParameters;
		LOG.info("Browser parameters set to:" + host + ".");
	}

	public boolean startHubUsingJson(String json){
		Runtime run = Runtime.getRuntime();
		try {
			run.exec("java -jar ./FitNesseRoot/files/selenium/standalone/selenium-server-standalone.jar -role hub -hubConfig " + json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		//java -jar selenium-server-standalone.jar -role hub -hubConfig hubconfig.json
	}


	public boolean startHub(){
		Runtime run = Runtime.getRuntime();
		LOG.info("Hub has been started.");
		try {
			run.exec("java -jar ./FitNesseRoot/files/selenium/standalone/selenium-server-standalone.jar -role hub -port " + port);
			//run.exec("cmd.exe /c start cmd.exe /c java -jar ./FitNesseRoot/files/selenium/standalone/selenium-server-standalone.jar -role hub -port " + port);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean startNodeOnHubUrl(String hubUrl){
		Runtime run = Runtime.getRuntime();
		try {
			//java -jar selenium-server-standalone-2.41.0.jar -role webdriver -hub http://localhost:5554/grid/register -browser browserName="iexplore",version=ANY,platform=WINDOWS,maxInstances=5 -port 5555
			run.exec("java -jar ./FitNesseRoot/files/selenium/standalone/selenium-server-standalone.jar -role webdriver -hub " + hubUrl + " -browser " + browserParameters + " -port " + port);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
