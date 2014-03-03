package com.xebia.sikuli;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EnvironmentFixture {
    private static final Logger LOG = LoggerFactory.getLogger(SikuliDriverFixture.class);
    
    //From http://stackoverflow.com/questions/3282498/how-can-i-detect-a-unix-like-os-in-java

    public boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;

    }

    public boolean isMac() {
        return SystemUtils.IS_OS_MAC_OSX;

    }

    public boolean isUnix() {
        return SystemUtils.IS_OS_UNIX;

    }

    public boolean isLinux() {
        return SystemUtils.IS_OS_LINUX;
    }
    
    public boolean open(String fileToOpen) throws IOException {
        File f=new File(fileToOpen);
        LOG.info("Opening "+f.getAbsolutePath());
        Desktop.getDesktop().open(f);
        return true;
    }
    
    public boolean execute(String command) throws IOException {
        Runtime.getRuntime().exec(command);
        return true;
    }

    public int executeAndWait(String command) throws IOException, InterruptedException {
        Process p=Runtime.getRuntime().exec(command);
        int ret=p.waitFor();
        return ret;
    }

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        EnvironmentFixture o=new EnvironmentFixture();
        o.open("/Applications/eclipse/Eclipse.app");
    }

}
