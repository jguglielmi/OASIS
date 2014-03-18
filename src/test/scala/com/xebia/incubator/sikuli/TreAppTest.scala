package com.xebia.incubator.sikuli

import org.junit.Assert._
import org.junit._

import java.util.prefs.Preferences
class TreAppTest {
    val treApp=new TreApp(Preferences.userNodeForPackage(getClass()));
    @Test
    def stripTestWithComma() {
        val actual=treApp.strip("Manhattan Food Brands, Inc.")
        assertEquals("Manhattan Food Brands",actual);
    }
    
    @Test
    def stripTestWithDot() {
        val actual=treApp.strip("Manhattan Food Brands. Inc.")
        assertEquals("Manhattan Food Brands",actual);
    }
    
    @Test
    def stripTestNone() {
        val actual=treApp.strip("Manhattan Food Brands Inc")
        assertEquals("Manhattan Food Brands Inc",actual);
    }
    
    @Test
    @Ignore
    def openAuction {
        println("Testing sikuli calls")
//        highlightSearchButton
//        refreshLiveAuctions
//        Thread.sleep(5000)
//        refreshLiveAuctions
//        Thread.sleep(2000)
        treApp.openAuction(17887)
    }
    
}