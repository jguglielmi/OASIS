package com.xebia.sikuli;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.sikuli.slides.api.SlideExecutionException;
import org.sikuli.slides.api.Slides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SikuliSlidesFixture {
	
	private static final Logger LOG = LoggerFactory.getLogger(SikuliSlidesFixture.class);

	public SikuliSlidesFixture() {
	}
	
	public boolean doOnWith(String folder, String powerPoint) throws SlideExecutionException{
		File powerPointLocation = new File("FitnesseRoot/files/images/"+ folder + "/" + powerPoint + ".pptx");
		if(!powerPointLocation.exists()){
			LOG.error("Image or folder does not exist.");
			return false;
		}
		else{
			LOG.info("Slide location set to: " + powerPointLocation.getAbsolutePath());
			Slides.execute(powerPointLocation);
			return true;
		}
	}

}
