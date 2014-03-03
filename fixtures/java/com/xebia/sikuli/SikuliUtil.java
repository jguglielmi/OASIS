package com.xebia.sikuli;

import java.io.File;

public class SikuliUtil {
    public static final String defaultScriptDir = "FitnesseRoot/files/sikuliScripts";
    
    public static File sikuliScript(String path) {
        File script=new File(path);
        if (! script.getName().contains(".")) {
            script=new File(script.getParent(),script.getName()+".sikuli");
        }
        return script;
    }
    public static String imgPath(File sikuliScriptDir, String imgName) {
        return new File(sikuliScriptDir, imgName).getPath();
    }
}
