package org.sikuli.webdriver;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.sikuli.api.Screen;

public class WebDriverScreen implements Screen {

	final private TakesScreenshot driver;
	final private Dimension size;

	public WebDriverScreen(TakesScreenshot driver) throws IOException{
		this.driver = driver;		
		File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
		BufferedImage b = ImageIO.read(screenshotFile);
		size = new Dimension(b.getWidth(),b.getHeight());
	}
	
	BufferedImage crop(BufferedImage src, int x, int y, int width, int height){
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = dest.getGraphics();
		g.drawImage(src, 0, 0, width, height, x, y, x + width, y + height, null);
		g.dispose();
		return dest;
	}


	public BufferedImage getScreenshot(int x, int y, int width,
			int height) {
		File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			BufferedImage full = ImageIO.read(screenshotFile);
			BufferedImage cropped = crop(full, x, y, width, height);
			return cropped;
		} catch (IOException e) {
		}
		return null;
	}

	public Dimension getSize() {
		return size;
	}

}