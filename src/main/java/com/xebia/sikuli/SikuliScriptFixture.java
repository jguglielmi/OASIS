package com.xebia.sikuli;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sikuli.basics.SikuliScript;

import static com.xebia.sikuli.SikuliUtil.*;

/**
 * Use this fixture to run scripts created by the Sikuli IDE directly.
 * 
 * @author gvandieijen
 *
 */
public class SikuliScriptFixture {
    private static final Logger LOG = LoggerFactory.getLogger(SikuliScriptFixture.class);
    public String scriptdir=SikuliUtil.defaultScriptDir;

    public String arguments="";
    public SikuliScriptFixture() {
    }

    public boolean runSikuliScript(String sikuliScriptName) throws IOException, AWTException {
        File script=sikuliScript(scriptdir+File.separator+ sikuliScriptName);
        LOG.info("Executing Sikuli script: "+script.getAbsolutePath());
        String [] sikuliArgs;
        if (! StringUtils.isEmpty(arguments)) {
            sikuliArgs=new String[]{"--args",arguments,"-r",script.getAbsolutePath()};
        } else {
            sikuliArgs=new String[]{script.getAbsolutePath()};
        }
        SikuliScript.main(sikuliArgs);
        return true;
    }

    /**
     * @param args
     * @throws AWTException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, AWTException {
        SikuliScriptFixture fixture=new SikuliScriptFixture();
        fixture.runSikuliScript("EclipseStuff.sikuli");
        
    }

}
