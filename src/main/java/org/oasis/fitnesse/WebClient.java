//  Name: WebClient.java
//  Written by: Ed Jakubowski  ejakubowski7@gmail.com
//  Last Updated: 09/09/2014
//  
//  Description:  This class is a helper for using Java Web calls
//  Requirements: Java Runtime
//  Example Usage:  

//
//


package org.oasis.fitnesse;

import java.io.*;
import java.net.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.*;
import javax.net.ssl.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class WebClient {
	
	private int readTimeout = 30000; //15 seconds
	private String userAgent = "";
	//Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2
	private String lastResults = "";
	private String paramList = "";
	private String charset = "UTF-8";

	
	public static void main(String[] args) {
		System.out.println("starting driver...");

		System.out.println("done.");
	}
	
	//init
	public void WebClient() {
	    ignoreBadSSLCerts();
		setCookieManager();
		System.setProperty("http.agent", ""); //clear java user agent
	}
	
	public void setUserAgent(String agent) {
		this.userAgent = agent;
	}
	
	public void setReadTimeout(int timeout) {
		this.readTimeout = timeout;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getLastResults() {
		return lastResults;
	}
	
	public void setLastResults(String results) {
		lastResults = results;
	}
	
	public void setCookieManager() {
		CookieManager cman = new CookieManager();
	    CookieHandler.setDefault(cman);
	}
	
	public void ignoreBadSSLCerts() {
	    try {
	        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] chain, String authType) {
	            }
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        }};
	        final SSLContext sslContext = SSLContext.getInstance("SSL");
	        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
	        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
	        
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void SslSetProtocols(String protocol) {
		//SSLSocket.setEnabledProtocols(new String[] { "SSLv3", "TLSv1", "TLSv1.1" });
		System.setProperty("https.protocols", protocol);//"TLSv1");
	}
	
	public String getCookieString(String html) {
	    //'SESSION_COOKIE' : 'hjk;hjklhkjhkjhlkjuhoij8hp9k00'
	    Pattern p = Pattern.compile("'SESSION_COOKIE' : '(.+?)'");
	    //JOptionPane.showMessageDialog(null, "test1:");
	    Matcher m = p.matcher(html);
	    String cookieStr = "";
	    if(m.find()) {
	        cookieStr = "SessionCookie=" + m.group(1) + ";";
	    }
	    return cookieStr;
	}
	
	public String getHttp(String urlStr) {
		return getHttpWithAuth(urlStr, "");
	}
	
	public void getHttpToFile(String urlStr, String filename) {
		stringToFile(filename, getHttpWithAuth(urlStr, ""));
	}
	
	public String getHttpWithAuth(String urlStr, String encodedAuth) {
	    URL url;
	    HttpURLConnection conn = null;
	    BufferedReader rd;
	    String line;
	    StringBuilder result = new StringBuilder();
	    try {
	        url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", this.userAgent);
	        //conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	        conn.setReadTimeout(readTimeout);
	        //if (cookie != "") {
	        //    conn.setRequestProperty("Cookie", cookie);
	        //}
			if (encodedAuth != "") {
			    conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
				//System.out.println("Authcode: " + encodedAuth);
			}
			conn.connect();
			String newLine = System.getProperty("line.separator");
	        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        while ((line = rd.readLine()) != null) {
	        	result.append(line + newLine);
	        }
	        rd.close();
	    }
	    catch (Exception ex) {
	        //ex.printStackTrace();
	        logWrite("Failed to getHttp " + ex.getMessage());
	        Scanner s = new Scanner(conn.getErrorStream());
	        s.useDelimiter("\\Z");
	        logWrite(s.next());
	        s.close();
	        result = new StringBuilder();;
	    }
	    finally {
	        if(conn != null)
	        	conn.disconnect(); 
	    }
		lastResults = result.toString();
	    return result.toString();
	}

	public String urlEncodeString(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logWrite("Exception urlEncode of " + str + ": " + e.getMessage());
		}
		return "";
	}
	
	public String urlEncodeNameWithValue(String paramName, String paramValue) {
		try {
			paramValue = org.oasis.plugin.Util.processDecryptionString(paramValue);
			return paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logWrite("Exception urlEncode of " + paramName + "=" + paramValue + ": " + e.getMessage());
		}
		return "";
	}
	
	public void clearParamList() {
		this.paramList = "";
	}
	
	public void addParamListNameWithValue(String paramName, String paramValue) {
		if (this.paramList.length() == 0)
			this.paramList = urlEncodeNameWithValue(paramName, paramValue);
		else
			this.paramList += "&" + urlEncodeNameWithValue(paramName, paramValue);
	}
	
	public String postHttpParamList(String urlStr) {
		return postHttp(urlStr, this.paramList, "application/x-www-form-urlencoded");
	}
	
	//String urlParameters = "fName=" + URLEncoder.encode("???", "UTF-8") + "&lName=" + URLEncoder.encode("???", "UTF-8")
	public String postHttpWithParams(String urlStr, String urlParameters) {
		return postHttp(urlStr, urlParameters, "application/x-www-form-urlencoded");
	}
	
	public String postHttp(String urlStr, String urlParameters, String contentType) {
		//logWrite("postHttp to " + urlStr + " with params: " + urlParameters);
	    URL url;
	    HttpURLConnection conn = null;
	    BufferedReader rd;
	    String line;
	    StringBuilder result = new StringBuilder();
	    try {
	        url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", this.userAgent);
	        conn.setRequestProperty("Content-Type", contentType); // "application/x-www-form-urlencoded"
	        //conn.setRequestProperty("Content-Type", "application/json");
	        //conn.setRequestProperty("Content-Language", "en-US");
	        conn.setUseCaches (false);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setReadTimeout(readTimeout);
	        //if (cookie != "") {
	        //    conn.setRequestProperty("Cookie", cookie);
	        //}

	        //Send request
	        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	        wr.writeBytes (urlParameters);
	        wr.flush ();
	        wr.close ();

			String newLine = System.getProperty("line.separator");
	        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        while ((line = rd.readLine()) != null) {
	        	result.append(line + newLine);
	        }
	        rd.close();
	    }
	    catch (Exception ex) {
	        //ex.printStackTrace();
	        logWrite("Failed to postHtml " + ex.getMessage());
	        Scanner s = new Scanner(conn.getErrorStream());
	        s.useDelimiter("\\Z");
	        logWrite(s.next());
	        s.close();
	        //logWrite(ex);
	        result = new StringBuilder();;
	    }
	    finally {
	        if(conn != null)
	        	conn.disconnect(); 
	    }
		lastResults = result.toString();
	    return result.toString();
	}
	
	public String deleteHttpWithParams(String urlStr, String urlParameters) {
		return customHttp(urlStr, urlParameters, "DELETE", "application/x-www-form-urlencoded") ;
	}
	
	// Example:
	// | custom http | http://www.google.com | with params | q=test | using method | POST | and content type | application/x-www-form-urlencoded |
	public String customHttpWithParamsUsingMethodAndContentType(String urlStr, String urlParameters, String method, String contentType) {
		return customHttp(urlStr, urlParameters, method, contentType);
	}
	
	public String customHttp(String urlStr, String urlParameters, String method, String contentType) {
		//logWrite("customHttp " + method + " to " + urlStr + " with params: " + urlParameters);
	    URL url;
	    HttpURLConnection conn = null;
	    BufferedReader rd;
	    String line;
	    StringBuilder result = new StringBuilder();
	    try {
	        url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", this.userAgent);
			conn.setRequestProperty("accept-charset", charset);
	        conn.setRequestProperty("Content-Type", contentType); // "application/x-www-form-urlencoded"
	        //conn.setRequestProperty("Content-Type", "application/json");
	        //conn.setRequestProperty("Content-Language", "en-US");
	        conn.setUseCaches (false);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setReadTimeout(readTimeout);
	        //if (cookie != "") {
	        //    conn.setRequestProperty("Cookie", cookie);
	        //}

	        //Send request
	        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	        wr.writeBytes (urlParameters);
	        wr.flush ();
	        wr.close ();

			String newLine = System.getProperty("line.separator");
	        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        while ((line = rd.readLine()) != null) {
	        	result.append(line + newLine);
	        }
	        rd.close();
	    }
	    catch (Exception ex) {
	        //ex.printStackTrace();
	        logWrite("Failed to postHtml " + ex.getMessage());
	        Scanner s = new Scanner(conn.getErrorStream());
	        s.useDelimiter("\\Z");
	        logWrite(s.next());
	        s.close();
	        //logWrite(ex);
	        result = new StringBuilder();;
	    }
	    finally {
	        if(conn != null)
	        	conn.disconnect(); 
	    }
		lastResults = result.toString();
	    return result.toString();
	}
	
	public String soapMessageToWithActionAndXml(String urlStr, String soapAction, String soapXml) {
		//logWrite("customHttp " + method + " to " + urlStr + " with params: " + urlParameters);
	    URL url;
	    HttpURLConnection conn = null;
	    BufferedReader rd;
	    String line;
	    StringBuilder result = new StringBuilder();
		String urlParameters = soapXml;
		String method = "POST";
		String contentType = "text/xml; charset=utf-8";
	    try {
	        url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent", this.userAgent);
			conn.setRequestProperty("accept-charset", charset);
	        conn.setRequestProperty("Content-Type", contentType); // "application/x-www-form-urlencoded"
			conn.setRequestProperty("SOAPAction", soapAction);
	        conn.setUseCaches (false);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setReadTimeout(readTimeout);

	        //Send request
	        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	        wr.writeBytes (urlParameters);
	        wr.flush ();
	        wr.close ();

			String newLine = System.getProperty("line.separator");
	        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        while ((line = rd.readLine()) != null) {
	        	result.append(line + newLine);
	        }
	        rd.close();
	    }
	    catch (Exception ex) {
	        //ex.printStackTrace();
	        logWrite("Failed to postHtml " + ex.getMessage());
	        Scanner s = new Scanner(conn.getErrorStream());
	        s.useDelimiter("\\Z");
	        logWrite(s.next());
	        s.close();
	        //logWrite(ex);
	        result = new StringBuilder();;
	    }
	    finally {
	        if(conn != null)
	        	conn.disconnect(); 
	    }
		lastResults = result.toString();
	    return result.toString();
	}
	
	public String getIpAddress(String hostname) {
		String result = "";
		try {
			InetAddress ipAddr = InetAddress.getByName(hostname);
			result = ipAddr.getHostAddress();
		} catch (Exception e) {
	        logWrite(e);
		}
		return result;
	}
	
	public String replaceAllWith(String pattern, String replacement) {
		//(".*[^\\d](\\d+).*", "$1")
		return lastResults.replaceAll(pattern, replacement);
	}

	public String matchOnFirstPattern(String pattern) {
		logWrite("matchOnFirstPattern: " + pattern );
		//.input .*? id="sbhost" .*? value="([^"]*)"
		//<input id="sbhost" class="lst" type="text" name="q" maxlength="2048" autocomplete="off" title="Search" value="test">
		Pattern p = Pattern.compile(pattern);
	    //JOptionPane.showMessageDialog(null, "test1:");
	    Matcher m = p.matcher(lastResults);
	    String resultStr = "";
	    if(m.find()) {
	        resultStr = m.group(1);
	    }
		else 
			logWrite("pattern not found.");
	    return resultStr;
	}

	public String matchOnGroupNumberWithPattern(int group, String pattern) {
		logWrite("matchOnFirstPattern: " + pattern );
		//.input .*? id="sbhost" .*? value="([^"]*)"
		//<input id="sbhost" class="lst" type="text" name="q" maxlength="2048" autocomplete="off" title="Search" value="test">
		Pattern p = Pattern.compile(pattern);
	    //JOptionPane.showMessageDialog(null, "test1:");
	    Matcher m = p.matcher(lastResults);
	    String resultStr = "";
	    if(m.find()) {
	        resultStr = m.group(group);
	    }
		else 
			logWrite("pattern not found.");
	    return resultStr;
	}
	
	public String matchOnFirstPatternOf(String pattern, String content) {
		//.input .*? id="sbhost" .*? value="([^"]*)"
		//<input id="sbhost" class="lst" type="text" name="q" maxlength="2048" autocomplete="off" title="Search" value="test">
		Pattern p = Pattern.compile(pattern);//"'SESSION_COOKIE' : '(.+?)'");
	    //JOptionPane.showMessageDialog(null, "test1:");
	    Matcher m = p.matcher(content);
	    String resultStr = "";
	    if(m.find()) {
	        resultStr = m.group(1);
	    }
	    return resultStr;
	}
	
	public String matchOnGroupNumberWithPatternOf(int group, String pattern, String content) {
		logWrite("matchOnFirstPattern: " + pattern );
		//.input .*? id="sbhost" .*? value="([^"]*)"
		//<input id="sbhost" class="lst" type="text" name="q" maxlength="2048" autocomplete="off" title="Search" value="test">
		Pattern p = Pattern.compile(pattern);
	    //JOptionPane.showMessageDialog(null, "test1:");
	    Matcher m = p.matcher(content);
	    String resultStr = "";
	    if(m.find()) {
	        resultStr = m.group(group);
	    }
		else 
			logWrite("pattern not found.");
	    return resultStr;
	}
	
	public String evaluateXpath(String xpathExpr) {
		String result = "";
		try {
			InputSource inSource = new InputSource(new StringReader(lastResults));
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node node = (Node) xpath.evaluate(xpathExpr, inSource, XPathConstants.NODE);
			if (node != null)
				result = node.getNodeValue();
			
		} catch(Exception e) {
			logWrite(e);
		}
		return result;
	}
	
	public void stringToFile(String filename, String content) {
	    try {
	        FileWriter fw = new FileWriter(filename, false);
	        BufferedWriter out = new BufferedWriter(fw);
	        out.write(content);
	        out.flush();
	        fw.close();
	    }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public static String fileToString(String filename) {
		String result = null;
        DataInputStream in = null;
        try {
            byte[] buffer = new byte[(int) new File(filename).length()];
            in = new DataInputStream(new FileInputStream(filename));
            in.readFully(buffer);
            result = new String(buffer);
            in.close();
        } 
        catch (Exception e) {
            logWrite(e);
        }
        return result;
	}
	
	public static void appendToFile(String filename, String txt) {
	    try {
	        FileWriter fw = new FileWriter(filename, true);
	        BufferedWriter out = new BufferedWriter(fw);
	        out.write(txt);
			if (!txt.contains("\n"))
				out.newLine();
	        out.flush();
	        fw.close();
	    }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public static void logWrite(Exception e)
	{
	    StringWriter sWriter = new StringWriter();
	    PrintWriter pWriter = new PrintWriter(sWriter);
	    e.printStackTrace(pWriter);
	    logWrite(sWriter.toString());
	}

	public static void logWrite(String s)
	{
	    System.out.println(s);
	}
}
