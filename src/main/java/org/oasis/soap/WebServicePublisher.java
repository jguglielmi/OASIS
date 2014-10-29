package org.oasis.soap;

import javax.xml.ws.Endpoint;

@SuppressWarnings("restriction")
public class WebServicePublisher {

	public static Endpoint ep = null;
	private String urlString = "http://localhost:8080";
	
	public void setUrl(String url) {
		this.urlString = url;
	}
	
	public String getUrl() {
		return this.urlString;
	}
	
	public boolean publishPingWebService() {
		WebServicePublisher.ep = Endpoint.publish(urlString + "/WS/Ping", new PingWsImpl());
		return true;
	}
	
	public boolean stopPingWebService() {
		if (WebServicePublisher.ep != null)
		{
			WebServicePublisher.ep.stop();
			WebServicePublisher.ep = null;
			return true;
		}
		return false;
	}
	
	/*
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("WebService Endpoint starting at http://localhost:8080/WS/Ping");
		Endpoint ep = Endpoint.publish("http://localhost:8080/WS/Ping", new PingWsImpl());
		
		//ep.stop();
		//System.out.println("Shutting down WebService Endpoint.");
	}*/

}