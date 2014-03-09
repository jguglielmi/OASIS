package com.xebia.sikuli;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.lang.reflect.*;

import javax.imageio.ImageIO;

import org.sikuli.api.*;
import org.sikuli.api.robot.*;
import org.sikuli.api.robot.desktop.*;
import org.sikuli.api.visual.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xebia.sikuli.ExtendedSikuliCommands; 

import javax.crypto.*;
import javax.crypto.spec.*;
import org.synthuse.*; //for showing status window

/**
 * This fixture uses the Sikuli API to perform UI automation.
 * 
 * @author gvandieijen
 *
 */

public class SikuliDriverFixture extends SikuliCommandProcessor{


	private static final Logger LOG = LoggerFactory.getLogger(SikuliDriverFixture.class);

	File imgDir=null; 

	Stack<ScreenRegion> matchesRegion=new Stack<ScreenRegion>();
	private static Mouse mouse;
	private static Keyboard keyboard = new DesktopKeyboard();
	private int waitTimeMs=60000;
	private int highlightTime=3;
	private Target target1;
	private Target target2;
	private ScreenRegion screenRegion1;
	private ScreenRegion screenRegion2;
	private int xOffSet=0;
	private int yOffSet=0;	
	private double matching=0.75;
	public ExtendedSikuliCommands extendedSikuliCommands;

	
	public String sikuliScriptsDir = SikuliUtil.defaultScriptDir;
	
	public boolean showStatusText = true;

	private static DesktopScreenRegion screen;

	
	public SikuliDriverFixture() {
		refreshDesktopMouseScreen();
	}
	
	public SikuliDriverFixture(boolean startRefresh) {
		if (startRefresh)
			refreshDesktopMouseScreen();
	}
	
	public void refreshDesktopMouseScreen() {
		mouse = new DesktopMouse();
		screen = new DesktopScreenRegion();
		matchesRegion = new Stack<ScreenRegion>();
		if (org.oasis.plugin.Util.isShutdownPressed()) { // shutdown fitnesse so exit
			LOG.info("Shutdown in progress exiting fixture");
			//LOG.info("My args: " + System.getProperty("sun.java.command"));
			System.exit(0);
		}
			
	}
	
	public String getScreenSize() {
		Rectangle rect = new DesktopScreen(0).getBounds();
		String screenSize = rect.width + "x" + rect.height;
		LOG.info("screen size: " + screenSize);
		return screenSize;
	}
	
	public void showStatusText() {
		showStatusText = true;
	}
	
	public void hideStatusText() {
		showStatusText = false;
	}	

	private StatusWindow statusText(String text) {
		return statusText(text, -1);
	}
	
	private StatusWindow statusText(String text, int displayTime) {
		if (!showStatusText)
			return null;
		//<img src="TestPage?sik&img=startButton.png" />
		text = text.replaceAll("(<img src=\")([^\\?]*)(\\?sik&img=)([^\"]*)(\" />)", "$4"); // remove image html
		return new StatusWindow(text, displayTime);
	}
	
	public StatusWindow displayText(String text) {
		return new StatusWindow(text, 3);
	}

	public void setWaitTimeTo(int milliseconds){
		waitTimeMs=milliseconds;
		LOG.info("wait time set to:" + milliseconds);
	}

	
	public void setTargetXOffsetToSetTargetYOffsetTo(int x, int y){
		xOffSet=x;
		yOffSet=y;
		LOG.info("offset x: " + xOffSet + " offset y: " + yOffSet);
	}
	
	public void setTheMatchingSimilarityTo(double matching){
		this.matching=matching;
		LOG.info("matching level set to: " + matching);
	}

	public boolean highlight(String imgOrText, int highlightTime) throws IOException {
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1!=null) {
			Canvas canvas = new DesktopCanvas();
			canvas.addBox(screenRegion1).display(highlightTime);
			return true;
		} else {
			return false;
		}

	}
	public boolean highlight(String imgOrText) throws IOException {
		return highlight(imgOrText,highlightTime);
	}
	
	private String getSikuliImageName(String imgStr) {
		String sikuliResponderStr = "?sik&img=";
		if (imgStr.contains(sikuliResponderStr)) {
			imgStr = imgStr.substring(imgStr.indexOf(sikuliResponderStr) + sikuliResponderStr.length());
			if (imgStr.contains("\""))
				imgStr = imgStr.substring(0,imgStr.indexOf("\""));
		}
		return imgStr;
	}
	
	private void setImagePathFromSikuliImageName(String imgStr) {
		String sikuliResponderStr = "?sik&img=";
		String sikuliResponderStr2 = "<img src=\"";
		if (imgStr.contains(sikuliResponderStr)) {
			String imgPath = imgStr.substring(0, imgStr.indexOf(sikuliResponderStr));
			imgPath = imgPath.substring(imgPath.indexOf(sikuliResponderStr2) + sikuliResponderStr2.length());
			File dir = new File(sikuliScriptsDir, imgPath);
			if (! dir.exists()) {
				//LOG.debug("Failed to set Sikuli Image Path to: " + dir.getAbsolutePath());
				dir = new File(sikuliScriptsDir, imgPath + ".sikuli");
			}
			
			if (dir.exists())
				imgDir = dir;
		}
	}
	
	public Target fuzzyTarget(String imgOrText) {
		setImagePathFromSikuliImageName(imgOrText);// added to pull imageDir when sikuli responder url is used
		imgOrText = getSikuliImageName(imgOrText); // added to pull image name when sikuli responder url is used
		
		File imgFile = imgFileOrNone(imgOrText);
		if (imgFile!=null) {
			return new ImageTarget(imgFile);
		} else {
			return new TextTarget(imgOrText);
		}
	}

	protected File imgFileOrNone(String imgOrText) {
		if (! imgOrText.contains(".") ) {
			imgOrText=imgOrText+".png";
		}
		File imgFile;
		if (! imgOrText.startsWith(File.separator)) {
			imgFile=new File(imgDir,imgOrText);
		} else {
			imgFile=new File(imgOrText);
		}
		if (imgFile.exists())
			return imgFile;
		else
			return null;
	}

	public void click() {
		mouse.click(currentRegion().getCenter());
	}

	public boolean click(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("click " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null){
			statusDtr.dispose();
			LOG.error("Cannot find object: " + target1);
			LOG.error("Matching was set to " + matching);
			LOG.error("Offset was set to " + xOffSet + " , " + yOffSet);
			return false;
		}
		else {
			mouse.click(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Click performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}
	
	public boolean waitClick(String imgOrText) throws IOException, AWTException {
		boolean waitResult = wait(imgOrText);
		boolean clickResult = click(imgOrText);
		return (waitResult && clickResult);
	}

	public boolean waitClickTrue(String imgOrText) {
		try {
			wait(imgOrText);
			click(imgOrText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (true);
	}

	public void doubleClick() {
		mouse.doubleClick(currentRegion().getCenter());
	}

	public boolean doubleClick(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("doubleClick " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null){
			statusDtr.dispose();
			LOG.error("Cannot find object: " + target1);
			return false;
		}
		else {
			mouse.doubleClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Double click performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}

	public void rightClick() {
		mouse.rightClick(currentRegion().getCenter());
	}

	public boolean rightClick(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("rightClick " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null) {
			statusDtr.dispose();
			LOG.error("Cannot find object: " + target1);
			return false;
		}
		else {
			mouse.rightClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Right click performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}

	public void hover() {
		mouse.drop(currentRegion().getCenter());
	}

	public boolean hover(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("hover " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null){
			LOG.error("Cannot find object: " + target1);
			statusDtr.dispose();
			return false;
		}
		else {
			mouse.drop(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Hover performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}

	public boolean keyDown(String key) throws AWTException{
		statusText("keyDown " + key, 1);
		KeyConversionFactory keyConversionFactory = new KeyConversionFactory();
		keyConversionFactory.keyDown(key);
		return true;
	}

	public boolean keyUp(String key) throws AWTException{
		statusText("keyUp " + key, 1);
		KeyConversionFactory keyConversionFactory = new KeyConversionFactory();
		keyConversionFactory.keyUp(key);
		return true;
	}
	
	public boolean keyPress(String key) throws AWTException{
		keyDown(key);
		keyUp(key);
		return true;
	}
	public boolean drop(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("drop " + imgOrText);
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null){
			statusDtr.dispose();
			LOG.error("Cannot find object: " + target1);
			return false;
		}
		else {
			mouse.drop(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Drop performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}

	public boolean drag(String imgOrText) throws IOException, AWTException {
		StatusWindow statusDtr = statusText("drop " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		if (screenRegion1==null){
			statusDtr.dispose();
			LOG.error("Cannot find object: " + target1);
			return false;
		}
		else {
			mouse.drag(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Drag performed on " + target1);
			statusDtr.dispose();
			return true;
		}
	}

	public boolean dragDrop(String imgOrText, String imgOrText2) throws IOException, AWTException{
		StatusWindow statusDtr = statusText("dragDrop " + imgOrText + ", " + imgOrText2);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		target2=fuzzyTarget(imgOrText2);
		target2.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		screenRegion2 = currentRegion().find(target2);
		if (screenRegion1==null || screenRegion2==null){
			LOG.error("Cannot find either object: " + target1 + " or " + target2);
			statusDtr.dispose();
			return false;
		}
		else {
			mouse.drop(screenRegion1.getCenter());
			mouse.drag(screenRegion1.getCenter());
			mouse.drop(screenRegion2.getCenter());
			LOG.info("Drag and Drop performed on " + target1 + " " + target2);
			statusDtr.dispose();
			return true;
		}
	}

	public ScreenLocation getLocation(){
		return mouse.getLocation();
	}

	public ScreenRegion currentRegion() {
		if (matchesRegion.isEmpty()) {
			return screen;
		} else {
			return matchesRegion.peek();
		}
	}

	private boolean maybeAddNewMatch(ScreenRegion region) {
		if (region!=null) {
			matchesRegion.push(region);
			LOG.info(target1 + " found.");
			return true;
		} else {
			LOG.error("Could not find " + target1);
			return false;
		}
	}

	public boolean wait(String imgOrText)  {
		StatusWindow statusDtr = statusText("wait (" + waitTimeMs + " ms) " + imgOrText);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText);
		target1.setMinScore(matching);
		boolean result = maybeAddNewMatch(currentRegion().wait(target1,waitTimeMs));
		statusDtr.dispose();
		return result;
	}
	
	public boolean executeCommand(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			//p.waitFor();
			return true;
		} catch ( Exception e) {
			e.printStackTrace(); 
		}
		return false;
	}
	
	public boolean maximizeCurrentWindow() {
		statusText("selectAll", 2);
		try { 
			keyDown("ALT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
			keyPress("SPACE");
			keyUp("ALT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
			type("x");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
		} catch ( Exception e) {
			e.printStackTrace(); 
		}
		return true;
	}
	
	public boolean closeCurrentWindow() {
		statusText("selectAll", 2);
		try { 
			keyDown("ALT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
			keyPress("F4");
			keyUp("ALT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
		} catch ( Exception e) {
			e.printStackTrace(); 
		}
		return true;
	}
	
	public boolean selectAll() {
		statusText("selectAll", 2);
		try { 
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
			keyPress("HOME");
			keyDown("SHIFT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
			keyPress("END");
			keyUp("SHIFT");
			try { Thread.sleep(500); } catch ( Exception e) { e.printStackTrace(); }
		} catch ( Exception e) {
			e.printStackTrace(); 
		}
		return true;
	}
	
	public boolean type(String text) {
		statusText("type " + text, 2);
		text = org.oasis.plugin.Util.processDecryptionString(text);
		keyboard.type(text);
		return true;
	}

	public boolean clearType(String text) {
		//statusText("clear type " + text, 2);
		selectAll();
		type(text);
		return true;
	}
	
	public boolean paste(String imgOrText) {
		keyboard.paste(imgOrText);
		return true;
	}
	
	
	public boolean waitOr(String imgOrText1, String imgOrText2) throws IOException, AWTException{
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText1);
		target1.setMinScore(matching);
		target2=fuzzyTarget(imgOrText2);
		target2.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		screenRegion2 = currentRegion().find(target2);
		if (screenRegion1==null && screenRegion2==null){
			LOG.error("Cannot find object: " + target1 + " or " + target2);
			return false;
		}
		else if(screenRegion1!=null && screenRegion2==null){
			LOG.info("Wait on: " + target1);
			return maybeAddNewMatch(screenRegion1.wait(target1,waitTimeMs));
		}
		
		else if(screenRegion2!=null && screenRegion1==null){
			LOG.info("Wait on: " + target2);
			return maybeAddNewMatch(screenRegion2.wait(target2,waitTimeMs));
		}
		else {
			LOG.info("Wait performed on " + target1 + " and " + target2);
			maybeAddNewMatch(screenRegion1.wait(target1,waitTimeMs));
			return maybeAddNewMatch(screenRegion2.wait(target2,waitTimeMs));
		}
	}


	public boolean clickOr(String imgOrText1, String imgOrText2) throws IOException, AWTException{
		StatusWindow statusDtr = statusText("clickOr " + imgOrText1 + ", " + imgOrText2);
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText1);
		target1.setMinScore(matching);
		target2=fuzzyTarget(imgOrText2);
		target2.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		screenRegion2 = currentRegion().find(target2);
		if (screenRegion1==null && screenRegion2==null){
			LOG.error("Cannot find object: " + target1 + " or " + target2);
			statusDtr.dispose();
			return false;
		}
		else if(screenRegion1!=null && screenRegion2==null){
			mouse.click(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Clicked on: " + target1);
			statusDtr.dispose();
			return true;
		}
		
		else if(screenRegion2!=null && screenRegion1==null){
			mouse.click(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Clicked on: " + target2);
			statusDtr.dispose();
			return true;
		}
		else {
			mouse.click(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			mouse.click(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Click performed on " + target1 + " and " + target2);
			statusDtr.dispose();
			return true;
		}
	}
	
	public boolean doubleClickOnOr(String imgOrText1, String imgOrText2) throws IOException, AWTException{
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText1);
		target1.setMinScore(matching);
		target2=fuzzyTarget(imgOrText2);
		target2.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		screenRegion2 = currentRegion().find(target2);
		if (screenRegion1==null && screenRegion2==null){
			LOG.error("Cannot find object: " + target1 + " or " + target2);
			return false;
		}
		else if(screenRegion1!=null && screenRegion2==null){
			mouse.doubleClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Double clicked on: " + target1);
			return true;
		}
		
		else if(screenRegion2!=null && screenRegion1==null){
			mouse.doubleClick(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Double clicked on: " + target2);
			return true;
		}
		else {
			mouse.doubleClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			mouse.doubleClick(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Double click performed on " + target1 + " and " + target2);
			return true;
		}
	}
	
	public boolean rightClickOnOr(String imgOrText1, String imgOrText2) throws IOException, AWTException{
		refreshDesktopMouseScreen();
		target1=fuzzyTarget(imgOrText1);
		target1.setMinScore(matching);
		target2=fuzzyTarget(imgOrText2);
		target2.setMinScore(matching);
		screenRegion1 = currentRegion().find(target1);
		screenRegion2 = currentRegion().find(target2);
		if (screenRegion1==null && screenRegion2==null){
			LOG.error("Cannot find object: " + target1 + " or " + target2);
			return false;
		}
		else if(screenRegion1!=null && screenRegion2==null){
			mouse.rightClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Right clicked on: " + target1);
			return true;
		}
		
		else if(screenRegion2!=null && screenRegion1==null){
			mouse.rightClick(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Right clicked on: " + target2);
			return true;
		}
		else {
			mouse.rightClick(screenRegion1.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			mouse.rightClick(screenRegion2.getCenter().getRelativeScreenLocation(xOffSet, yOffSet));
			LOG.info("Right click performed on " + target1 + " and " + target2);
			return true;
		}
	}
	
	public void delay(int delayMilliSec) {
		StatusWindow statusDtr = statusText("delay " + delayMilliSec);
		LOG.info("Delaying for " + delayMilliSec + " milliseconds");
		try { Thread.sleep(delayMilliSec); } catch ( Exception e) { e.printStackTrace(); }
		statusDtr.dispose();
	}
	
	
	public String fileToString(String filename) {
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
            e.printStackTrace();
        }
        return result;
	}
	
	public String parsePomXml(String xpathStr) {
		if (!(new File("./pom.xml")).exists()) 
			return "Failed to open: ./pom.xml";
		LOG.info("parsePomXml: " + xpathStr);
		
		String pom = fileToString("./pom.xml");
		//LOG.info("pom: " + pom);
		return org.oasis.plugin.Util.evaluateXpathGetValue(pom, xpathStr);
	}
	
	public static String getFitnesseRootPath() {
		String path = "";
        try {
			File fitRoot = new File("./FitNesseRoot");
			path = fitRoot.getCanonicalPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		return path;
	}
	
	public static String getFitnesseRootPath(String appendedPath) {
		String path = "";
        try {
			File fitRoot = new File("./FitNesseRoot", appendedPath);
			path = fitRoot.getCanonicalPath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		return path;
	}
	
	public static String newString(String val) {
		return new String(val);
	}
		
	public boolean whileIsHiddenThenClick(String hiddenImg, String imgOrText2) {
		StatusWindow statusDtr = statusText("whileIsHiddenThenClick (wait " + waitTimeMs +"ms) " + hiddenImg + "," + imgOrText2);
		boolean sf = this.showStatusText;//only show one status while this is running
		this.showStatusText = false;
		while (wait(hiddenImg) == false) {
			try { click(imgOrText2); } catch ( Exception e) { e.printStackTrace(); }
			LOG.info("Delaying for " + waitTimeMs + " milliseconds");
			try { Thread.sleep(waitTimeMs); } catch ( Exception e) { e.printStackTrace(); }
			if (org.oasis.plugin.Util.isShutdownPressed())
				break;
		}
		this.showStatusText = sf;
		statusDtr.dispose();
		return true;
	}
	
	public boolean whileIsHiddenThenKeypress(String hiddenImg, String imgOrText2) {
		StatusWindow statusDtr = statusText("whileIsHiddenThenKeypress (wait " + waitTimeMs +"ms) " + hiddenImg + "," + imgOrText2);
		boolean sf = this.showStatusText;//only show one status while this is running
		this.showStatusText = false;
		while (wait(hiddenImg) == false) {
			try { keyPress(imgOrText2); } catch ( Exception e) { e.printStackTrace(); }
			LOG.info("Delaying for " + waitTimeMs + " milliseconds");
			try { Thread.sleep(waitTimeMs); } catch ( Exception e) { e.printStackTrace(); }
			if (org.oasis.plugin.Util.isShutdownPressed())
				break;
			//LOG.info("fitnesse.responders.SikuliResponder.isShutdownPressed(): " + fitnesse.responders.SikuliResponder.isShutdownPressed());
		}
		this.showStatusText = sf;
		statusDtr.dispose();
		return true;
	}
	
	public boolean imageDir(String path) {
		File dir;
		dir=new File(path);
		LOG.debug("Trying "+dir.getAbsolutePath());
		if (! dir.exists()) {
			dir=new File(sikuliScriptsDir,path);
			LOG.debug("Trying "+dir.getAbsolutePath());
		}
		if (! dir.exists()) {
			dir=new File(dir.getParent(),dir.getName()+".sikuli");
			LOG.debug("Trying "+dir.getAbsolutePath());
		}
		if (dir.exists()) {
			imgDir=dir;
			return true;
		} else {
			return false;
		}
	}
	
	public void takeScreenCapture(String saveName)  {
		try {
			File screenshostDir = new File("./FitNesseRoot/files/testResults/screenshots");
			screenshostDir.mkdirs();
			if (!saveName.toLowerCase().endsWith(".png"))
				saveName += ".png";
			LOG.debug("take Screen Capture " + new File(screenshostDir, saveName).getAbsolutePath());
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle screenRect = new Rectangle(screenSize);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRect);
			ImageIO.write(image, "png", new File(screenshostDir, saveName));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
		public boolean executeMethod(String methodName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] noparams = {};
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, noparams);
			method.invoke(obj, null);
			return true;
		}
	}

	public boolean executeMethod(String methodName, String target) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramString = new Class[1];
			paramString[0] = String.class;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramString);
			method.invoke(obj, target);
			return true;
		}
	}

	public boolean executeMethod(String methodName, int target) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramInt = new Class[1];
			paramInt[0] = Integer.TYPE;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramInt);
			method.invoke(obj, target);
			return true;
		}
	}

	public boolean executeMethod(String methodName, double target) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramDouble = new Class[1];
			paramDouble[0] = Double.TYPE;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramDouble);
			method.invoke(obj, target);
			return true;
		}
	}

	public boolean executeMethod(String methodName, String target, String target2) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramString = new Class[2];
			paramString[0] = String.class;
			paramString[1] = String.class;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramString);
			method.invoke(obj, target, target2);
			return true;
		}
	}

	public boolean executeMethod(String methodName, String target, int target2) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramStringInt = new Class[2];
			paramStringInt[0] = String.class;
			paramStringInt[1] = Integer.TYPE;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramStringInt);
			method.invoke(obj, target, target2);
			return true;
		}
	}

	public boolean executeMethod(String methodName, int target, int target2) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		extendedSikuliCommands = new ExtendedSikuliCommands(methodName);
		if(!extendedSikuliCommands.isSupportedBySikuliDriver()){
			return false;
		}
		else{
			Class[] paramInt = new Class[2];
			paramInt[0] = Integer.TYPE;
			paramInt[1] = Integer.TYPE;
			Class cls = this.getClass();
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod(methodName, paramInt);
			method.invoke(obj, target, target2);
			return true;
		}
	}

	public boolean doCommand(String command) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		return super.executeMethod(command);
	}

	public boolean doOn(String command, String target) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		if(command=="delay" || command=="setWaitTimeTo"){
			int targetInt = Integer.parseInt(target);
			return super.executeMethod(command, targetInt);
		}
		else if(command=="setTheMatchingSimilarityTo"){
			double targetDouble = Double.parseDouble(target);
			return super.executeMethod(command, targetDouble);
		}
		else{
			LOG.debug(command + " " + target);
			return super.executeMethod(command, target);
		}
	}

	public boolean doOnWith(String command, String target, String target2) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException{
		if(command=="displayTextFor"){
			int targetInt = Integer.parseInt(target2);
			return super.executeMethod(command, target, targetInt);
		}
		else if(command=="setTargetXOffsetToSetTargetYOffsetTo"){
			int targetInt = Integer.parseInt(target);
			int targetInt2 = Integer.parseInt(target2);
			return super.executeMethod(command, targetInt, targetInt2);
		}
		else
			return super.executeMethod(command, target, target2);
	}
	
	public boolean doOnLoop(String[] commands, String[] targets, int iterations) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
		LOG.debug("Iterations set to:" + iterations);
		LOG.debug(commands[0]+ targets[0]);
		for(int h=0; h<iterations;h++){
			int targetCounter=0;
			for(int i=0; i<commands.length;i++){
				ExtendedSikuliCommands extendedSikuliCommands = new ExtendedSikuliCommands(commands[i]);
					if(extendedSikuliCommands.hasNoParameters() && extendedSikuliCommands.isSupportedBySikuliDriver()){
						LOG.debug("No parameters loop entered.");
						doCommand(commands[i]);
						targetCounter=targetCounter;
					}
					else if (extendedSikuliCommands.hasOneParameter() && extendedSikuliCommands.isSupportedBySikuliDriver()){
						LOG.debug("One parameter loop entered.");
						doOn(commands[i], targets[targetCounter]);
						targetCounter+=1;
					}
					else if(extendedSikuliCommands.hasTwoParameters() && extendedSikuliCommands.isSupportedBySikuliDriver()){
						LOG.debug("Two parameter loop entered.");
						doOnWith(commands[i], targets[targetCounter], targets[targetCounter+1]);
						targetCounter+=2;
					}
					else {
						return false;
					}
				}
		}
		return true;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SikuliDriverFixture sik = new SikuliDriverFixture(false);
		
		System.out.println("screen size: " + sik.getScreenSize());
		//sik.displayTextFor("this is a test", 3);
		//StatusWindow x = sik.displayText("This document explains how you can use a Canvas object to draw graphical elements on the screen in order to visualize the results of Sikuli's find operations.");
		//try {Thread.sleep(2000);} catch (Exception e) {e.printStackTrace();}
		//System.out.println("stopping.");
		//x.stop();
		String text = "click <img src=\"TestPage?sik&img=startButton.png\" />";
		text = text.replaceAll("(<img src=\")([^\\?]*)(\\?sik&img=)([^\"]*)(\" />)", "$4");
		System.out.println(text);
		System.out.println("done.");
	}

}
