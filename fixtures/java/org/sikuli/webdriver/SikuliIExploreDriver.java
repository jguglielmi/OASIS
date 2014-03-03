	package org.sikuli.webdriver;

	import java.awt.Rectangle;
	import java.io.File;
	import java.io.IOException;
	import java.net.URL;

	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.TakesScreenshot;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.ie.InternetExplorerDriver;
	import org.sikuli.api.DefaultScreenRegion;
	import org.sikuli.api.ImageTarget;
	import org.sikuli.api.ScreenLocation;
	import org.sikuli.api.ScreenRegion;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	public class SikuliIExploreDriver extends InternetExplorerDriver{

		static Logger logger = LoggerFactory.getLogger(SikuliFirefoxDriver.class);

		private static final int DEFAULT_WAIT_TIMEOUT_MSECS = 10000;
		ScreenRegion webdriverRegion;

		public SikuliIExploreDriver(){
			WebDriverScreen webDriverScreen;
			try {
				webDriverScreen = new WebDriverScreen(this);
			} catch (IOException e1) {
				throw new RuntimeException("unable to initialize SikuliFirefoxDriver");
			}
			webdriverRegion = new DefaultScreenRegion(webDriverScreen);
		}

		public WebElement findElementByLocation(int x, int y){
			return (WebElement) ((JavascriptExecutor) this).executeScript("return document.elementFromPoint(" + x + "," + y + ")");
		}

		public ImageElement findImageElement(URL imageUrl) {
			ImageTarget target = new ImageTarget(imageUrl);
			final ScreenRegion imageRegion = webdriverRegion.wait(target, DEFAULT_WAIT_TIMEOUT_MSECS);
			
			
			if (imageRegion != null){
				Rectangle r = imageRegion.getBounds();
			}else{
				return null;
			}


			ScreenLocation center = imageRegion.getCenter();
			WebElement foundWebElement = findElementByLocation(center.getX(), center.getY());
			Rectangle r = imageRegion.getBounds();
			return new DefaultImageElement(this, foundWebElement, 
					r.x,
					r.y,
					r.width,
					r.height);
		}
		
	}

