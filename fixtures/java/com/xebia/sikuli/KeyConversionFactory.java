package com.xebia.sikuli;

//import java.awt.event.KeyEvent;
import org.sikuli.script.*;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.*;
import java.awt.AWTException;
//import org.sikuli.api.robot.Keyboard;
//import org.sikuli.api.robot.desktop.DesktopKeyboard;

public class KeyConversionFactory  {
	Keyboard keyboard = null;
	
	public KeyConversionFactory() throws AWTException {
		keyboard = new DesktopKeyboard();
    }
	
	public void keyDown(String key) {
		if(key.contains("SPACE")){keyboard.keyDown(Key.SPACE);}
		else if(key.contains("ENTER")){keyboard.keyDown(Key.ENTER);}
		else if(key.contains("BACKSPACE")){keyboard.keyDown(Key.BACKSPACE);}
		else if(key.contains("TAB")){keyboard.keyDown(Key.TAB);}
		else if(key.contains("ESC")){keyboard.keyDown(Key.ESC);}
		else if(key.contains("C_ESC")){keyboard.keyDown(Key.C_ESC);}
		else if(key.contains("PAGE_UP")){keyboard.keyDown(Key.PAGE_UP);}
		else if(key.contains("C_PAGE_UP")){keyboard.keyDown(Key.C_PAGE_UP);}
		else if(key.contains("PAGE_DOWN")){keyboard.keyDown(Key.PAGE_DOWN);}
		else if(key.contains("C_PAGE_DOWN")){keyboard.keyDown(Key.C_PAGE_DOWN);}
		else if(key.contains("UP")) {keyboard.keyDown(Key.UP);}
		else if(key.contains("C_UP")){keyboard.keyDown(Key.C_UP);} 
		else if(key.contains("RIGHT")){keyboard.keyDown(Key.RIGHT);}
		else if(key.contains("C_RIGHT")){keyboard.keyDown(Key.C_RIGHT);}
		else if(key.contains("DOWN")){keyboard.keyDown(Key.DOWN);}
		else if(key.contains("C_DOWN")){keyboard.keyDown(Key.C_DOWN);}
		else if(key.contains("LEFT")){keyboard.keyDown(Key.LEFT);}
		else if(key.contains("C_LEFT")){keyboard.keyDown(Key.C_LEFT);}
		else if(key.contains("DELETE")){keyboard.keyDown(Key.DELETE);}
		else if(key.contains("C_DELETE")){keyboard.keyDown(Key.C_DELETE);}
		else if(key.contains("END")){keyboard.keyDown(Key.END);}
		else if(key.contains("C_END")){keyboard.keyDown(Key.C_END);}
		else if(key.contains("HOME")){keyboard.keyDown(Key.HOME);}
		else if(key.contains("C_HOME")){keyboard.keyDown(Key.C_HOME);}
		else if(key.contains("INSERT")){keyboard.keyDown(Key.INSERT);}
		else if(key.contains("C_INSERT")){keyboard.keyDown(Key.C_INSERT);}
		else if(key.contains("F1")){keyboard.keyDown(Key.F1);}
		else if(key.contains("C_F1")){keyboard.keyDown(Key.C_F1);}
		else if(key.contains("F2")){keyboard.keyDown(Key.F2);}
		else if(key.contains("C_F2")){keyboard.keyDown(Key.C_F2);}
		else if(key.contains("F3")){keyboard.keyDown(Key.F3);}
		else if(key.contains("C_F3")){keyboard.keyDown(Key.C_F3);}
		else if(key.contains("F4")){keyboard.keyDown(Key.F4);}
		else if(key.contains("C_F4")){keyboard.keyDown(Key.C_F4);}
		else if(key.contains("F5")){keyboard.keyDown(Key.F5);}
		else if(key.contains("C_F5")){keyboard.keyDown(Key.C_F5);}
		else if(key.contains("F6")){keyboard.keyDown(Key.F6);}
		else if(key.contains("C_F6")){keyboard.keyDown(Key.C_F6);}
		else if(key.contains("F7")){keyboard.keyDown(Key.F7);}
		else if(key.contains("C_F7")){keyboard.keyDown(Key.C_F7);}
		else if(key.contains("F8")){keyboard.keyDown(Key.F8);}
		else if(key.contains("C_F8")){keyboard.keyDown(Key.C_F8);}
		else if(key.contains("F9")){keyboard.keyDown(Key.F9);}
		else if(key.contains("C_F9")){keyboard.keyDown(Key.C_F9);}
		else if(key.contains("F10")){keyboard.keyDown(Key.F10);}
		else if(key.contains("C_F10")){keyboard.keyDown(Key.C_F10);}
		else if(key.contains("F11")){keyboard.keyDown(Key.F11);}
		else if(key.contains("C_F11")){keyboard.keyDown(Key.C_F11);}
		else if(key.contains("F12")){keyboard.keyDown(Key.F12);}
		else if(key.contains("C_F12")){keyboard.keyDown(Key.C_F12);}
		else if(key.contains("F13")){keyboard.keyDown(Key.F13);}
		else if(key.contains("C_F13")){keyboard.keyDown(Key.C_F13);}
		else if(key.contains("F14")){keyboard.keyDown(Key.F14);}
		else if(key.contains("C_F14")){keyboard.keyDown(Key.C_F14);}
		else if(key.contains("F15")){keyboard.keyDown(Key.F15);}
		else if(key.contains("C_F15")){keyboard.keyDown(Key.C_F15);}
		else if(key.contains("SHIFT")){keyboard.keyDown(Key.SHIFT);}
		else if(key.contains("C_SHIFT")){keyboard.keyDown(Key.C_SHIFT);}
		else if(key.contains("CTRL")){keyboard.keyDown(Key.CTRL);}
		else if(key.contains("C_CTRL")){keyboard.keyDown(Key.C_CTRL);}
		else if(key.contains("ALT")){keyboard.keyDown(Key.ALT);}
		else if(key.contains("C_ALT")){keyboard.keyDown(Key.C_ALT);}
		else if(key.contains("META")){keyboard.keyDown(Key.META);}
		else if(key.contains("C_META")){keyboard.keyDown(Key.C_META);}
		else if(key.contains("CMD")){keyboard.keyDown(Key.CMD);}
		else if(key.contains("C_CMD")){keyboard.keyDown(Key.C_CMD);}
		else if(key.contains("WIN")){keyboard.keyDown(Key.WIN);}
		else if(key.contains("C_WIN")){keyboard.keyDown(Key.C_WIN);}
		else if(key.contains("PRINTSCREEN")){keyboard.keyDown(Key.PRINTSCREEN);}
		else if(key.contains("C_PRINTSCREEN")){keyboard.keyDown(Key.C_PRINTSCREEN);}
		else if(key.contains("SCROLL_LOCK")){keyboard.keyDown(Key.SCROLL_LOCK);}
		else if(key.contains("C_SCROLL_LOCK")){keyboard.keyDown(Key.C_SCROLL_LOCK);}
		else if(key.contains("PAUSE")){keyboard.keyDown(Key.PAUSE);}
		else if(key.contains("C_PAUSE")){keyboard.keyDown(Key.C_PAUSE);}
		else if(key.contains("CAPS_LOCK")){keyboard.keyDown(Key.CAPS_LOCK);}
		else if(key.contains("C_CAPS_LOCK")){keyboard.keyDown(Key.C_CAPS_LOCK);}
		else if(key.contains("NUM0")){keyboard.keyDown(Key.NUM0);}
		else if(key.contains("C_NUM0")){keyboard.keyDown(Key.C_NUM0);}
		else if(key.contains("NUM1")){keyboard.keyDown(Key.NUM1);}
		else if(key.contains("C_NUM1")){keyboard.keyDown(Key.C_NUM1);}
		else if(key.contains("NUM2")){keyboard.keyDown(Key.NUM2);}
		else if(key.contains("C_NUM2")){keyboard.keyDown(Key.C_NUM2);}
		else if(key.contains("NUM3")){keyboard.keyDown(Key.NUM3);}
		else if(key.contains("C_NUM3")){keyboard.keyDown(Key.C_NUM3);}
		else if(key.contains("NUM4")){keyboard.keyDown(Key.NUM4);}
		else if(key.contains("NUM4")){keyboard.keyDown(Key.NUM4);}
		else if(key.contains("C_NUM4")){keyboard.keyDown(Key.C_NUM4);}
		else if(key.contains("NUM5")){keyboard.keyDown(Key.NUM5);}
		else if(key.contains("C_NUM5")){keyboard.keyDown(Key.C_NUM5);}
		else if(key.contains("NUM6")){keyboard.keyDown(Key.NUM6);}
		else if(key.contains("C_NUM6")){keyboard.keyDown(Key.C_NUM6);}
		else if(key.contains("NUM7")){keyboard.keyDown(Key.NUM7);}
		else if(key.contains("C_NUM7")){keyboard.keyDown(Key.C_NUM7);}
		else if(key.contains("NUM8")){keyboard.keyDown(Key.NUM8);}
		else if(key.contains("C_NUM8")){keyboard.keyDown(Key.C_NUM8);}
		else if(key.contains("NUM9")){keyboard.keyDown(Key.NUM9);}
		else if(key.contains("C_NUM9")){keyboard.keyDown(Key.C_NUM9);}
		else if(key.contains("SEPARATOR")){keyboard.keyDown(Key.SEPARATOR);}
		else if(key.contains("C_SEPARATOR")){keyboard.keyDown(Key.C_SEPARATOR);}
		else if(key.contains("NUM_LOCK")){keyboard.keyDown(Key.NUM_LOCK);}
		else if(key.contains("C_NUM_LOCK")){keyboard.keyDown(Key.C_NUM_LOCK);}
		else if(key.contains("ADD")){keyboard.keyDown(Key.ADD);}
		else if(key.contains("C_ADD")){keyboard.keyDown(Key.C_ADD);}
		else if(key.contains("MINUS")){keyboard.keyDown(Key.MINUS);}
		else if(key.contains("C_MINUS")){keyboard.keyDown(Key.C_MINUS);}
		else if(key.contains("MULTIPLY")){keyboard.keyDown(Key.MULTIPLY);}
		else if(key.contains("C_MULTIPLY")){keyboard.keyDown(Key.C_MULTIPLY);}
		else if(key.contains("DIVIDE")){keyboard.keyDown(Key.DIVIDE);}
		else if(key.contains("C_DIVIDE")){keyboard.keyDown(Key.C_DIVIDE);}
	}

	public void keyUp(String key){
		if(key.contains("SPACE")){keyboard.keyUp(Key.SPACE);}
		else if(key.contains("ENTER")){keyboard.keyUp(Key.ENTER);}
		else if(key.contains("BACKSPACE")){keyboard.keyUp(Key.BACKSPACE);}
		else if(key.contains("TAB")){keyboard.keyUp(Key.TAB);}
		else if(key.contains("ESC")){keyboard.keyUp(Key.ESC);}
		else if(key.contains("C_ESC")){keyboard.keyUp(Key.C_ESC);}
		else if(key.contains("PAGE_UP")){keyboard.keyUp(Key.PAGE_UP);}
		else if(key.contains("C_PAGE_UP")){keyboard.keyUp(Key.C_PAGE_UP);}
		else if(key.contains("PAGE_DOWN")){keyboard.keyUp(Key.PAGE_DOWN);} 
		else if(key.contains("C_PAGE_DOWN")){keyboard.keyUp(Key.C_PAGE_DOWN);}
		else if(key.contains("UP")) {keyboard.keyUp(Key.UP);}
		else if(key.contains("C_UP")){keyboard.keyUp(Key.C_UP);} 
		else if(key.contains("RIGHT")){keyboard.keyUp(Key.RIGHT);}
		else if(key.contains("C_RIGHT")){keyboard.keyUp(Key.C_RIGHT);}
		else if(key.contains("DOWN")){keyboard.keyUp(Key.DOWN);}
		else if(key.contains("C_DOWN")){keyboard.keyUp(Key.C_DOWN);}
		else if(key.contains("LEFT")){keyboard.keyUp(Key.LEFT);}
		else if(key.contains("C_LEFT")){keyboard.keyUp(Key.C_LEFT);}
		else if(key.contains("DELETE")){keyboard.keyUp(Key.DELETE);}
		else if(key.contains("C_DELETE")){keyboard.keyUp(Key.C_DELETE);}
		else if(key.contains("END")){keyboard.keyUp(Key.END);}
		else if(key.contains("C_END")){keyboard.keyUp(Key.C_END);}
		else if(key.contains("HOME")){keyboard.keyUp(Key.HOME);}
		else if(key.contains("C_HOME")){keyboard.keyUp(Key.C_HOME);}
		else if(key.contains("INSERT")){keyboard.keyUp(Key.INSERT);}
		else if(key.contains("C_INSERT")){keyboard.keyUp(Key.C_INSERT);}
		else if(key.contains("F1")){keyboard.keyUp(Key.F1);}
		else if(key.contains("C_F1")){keyboard.keyUp(Key.C_F1);}
		else if(key.contains("F2")){keyboard.keyUp(Key.F2);}
		else if(key.contains("C_F2")){keyboard.keyUp(Key.C_F2);}
		else if(key.contains("F3")){keyboard.keyUp(Key.F3);}
		else if(key.contains("C_F3")){keyboard.keyUp(Key.C_F3);}
		else if(key.contains("F4")){keyboard.keyUp(Key.F4);}
		else if(key.contains("C_F4")){keyboard.keyUp(Key.C_F4);}
		else if(key.contains("F5")){keyboard.keyUp(Key.F5);}
		else if(key.contains("C_F5")){keyboard.keyUp(Key.C_F5);}
		else if(key.contains("F6")){keyboard.keyUp(Key.F6);}
		else if(key.contains("C_F6")){keyboard.keyUp(Key.C_F6);}
		else if(key.contains("F7")){keyboard.keyUp(Key.F7);}
		else if(key.contains("C_F7")){keyboard.keyUp(Key.C_F7);}
		else if(key.contains("F8")){keyboard.keyUp(Key.F8);}
		else if(key.contains("C_F8")){keyboard.keyUp(Key.C_F8);}
		else if(key.contains("F9")){keyboard.keyUp(Key.F9);}
		else if(key.contains("C_F9")){keyboard.keyUp(Key.C_F9);}
		else if(key.contains("F10")){keyboard.keyUp(Key.F10);}
		else if(key.contains("C_F10")){keyboard.keyUp(Key.C_F10);}
		else if(key.contains("F11")){keyboard.keyUp(Key.F11);}
		else if(key.contains("C_F11")){keyboard.keyUp(Key.C_F11);}
		else if(key.contains("F12")){keyboard.keyUp(Key.F12);}
		else if(key.contains("C_F12")){keyboard.keyUp(Key.C_F12);}
		else if(key.contains("F13")){keyboard.keyUp(Key.F13);}
		else if(key.contains("C_F13")){keyboard.keyUp(Key.C_F13);}
		else if(key.contains("F14")){keyboard.keyUp(Key.F14);}
		else if(key.contains("C_F14")){keyboard.keyUp(Key.C_F14);}
		else if(key.contains("F15")){keyboard.keyUp(Key.F15);}
		else if(key.contains("C_F15")){keyboard.keyUp(Key.C_F15);}
		else if(key.contains("SHIFT")){keyboard.keyUp(Key.SHIFT);}
		else if(key.contains("C_SHIFT")){keyboard.keyUp(Key.C_SHIFT);}
		else if(key.contains("CTRL")){keyboard.keyUp(Key.CTRL);}
		else if(key.contains("C_CTRL")){keyboard.keyUp(Key.C_CTRL);}
		else if(key.contains("ALT")){keyboard.keyUp(Key.ALT);}
		else if(key.contains("C_ALT")){keyboard.keyUp(Key.C_ALT);}
		else if(key.contains("META")){keyboard.keyUp(Key.META);}
		else if(key.contains("C_META")){keyboard.keyUp(Key.C_META);}
		else if(key.contains("CMD")){keyboard.keyUp(Key.CMD);}
		else if(key.contains("C_CMD")){keyboard.keyUp(Key.C_CMD);}
		else if(key.contains("WIN")){keyboard.keyUp(Key.WIN);}
		else if(key.contains("C_WIN")){keyboard.keyUp(Key.C_WIN);}
		else if(key.contains("PRINTSCREEN")){keyboard.keyUp(Key.PRINTSCREEN);}
		else if(key.contains("C_PRINTSCREEN")){keyboard.keyUp(Key.C_PRINTSCREEN);}
		else if(key.contains("SCROLL_LOCK")){keyboard.keyUp(Key.SCROLL_LOCK);}
		else if(key.contains("C_SCROLL_LOCK")){keyboard.keyUp(Key.C_SCROLL_LOCK);}
		else if(key.contains("PAUSE")){keyboard.keyUp(Key.PAUSE);}
		else if(key.contains("C_PAUSE")){keyboard.keyUp(Key.C_PAUSE);}
		else if(key.contains("CAPS_LOCK")){keyboard.keyUp(Key.CAPS_LOCK);}
		else if(key.contains("C_CAPS_LOCK")){keyboard.keyUp(Key.C_CAPS_LOCK);}
		else if(key.contains("NUM0")){keyboard.keyUp(Key.NUM0);}
		else if(key.contains("C_NUM0")){keyboard.keyUp(Key.C_NUM0);}
		else if(key.contains("NUM1")){keyboard.keyUp(Key.NUM1);}
		else if(key.contains("C_NUM1")){keyboard.keyUp(Key.C_NUM1);}
		else if(key.contains("NUM2")){keyboard.keyUp(Key.NUM2);}
		else if(key.contains("C_NUM2")){keyboard.keyUp(Key.C_NUM2);}
		else if(key.contains("NUM3")){keyboard.keyUp(Key.NUM3);}
		else if(key.contains("C_NUM3")){keyboard.keyUp(Key.C_NUM3);}
		else if(key.contains("NUM4")){keyboard.keyUp(Key.NUM4);}
		else if(key.contains("NUM4")){keyboard.keyUp(Key.NUM4);}
		else if(key.contains("C_NUM4")){keyboard.keyUp(Key.C_NUM4);}
		else if(key.contains("NUM5")){keyboard.keyUp(Key.NUM5);}
		else if(key.contains("C_NUM5")){keyboard.keyUp(Key.C_NUM5);}
		else if(key.contains("NUM6")){keyboard.keyUp(Key.NUM6);}
		else if(key.contains("C_NUM6")){keyboard.keyUp(Key.C_NUM6);}
		else if(key.contains("NUM7")){keyboard.keyUp(Key.NUM7);}
		else if(key.contains("C_NUM7")){keyboard.keyUp(Key.C_NUM7);}
		else if(key.contains("NUM8")){keyboard.keyUp(Key.NUM8);}
		else if(key.contains("C_NUM8")){keyboard.keyUp(Key.C_NUM8);}
		else if(key.contains("NUM9")){keyboard.keyUp(Key.NUM9);}
		else if(key.contains("C_NUM9")){keyboard.keyUp(Key.C_NUM9);}
		else if(key.contains("SEPARATOR")){keyboard.keyUp(Key.SEPARATOR);}
		else if(key.contains("C_SEPARATOR")){keyboard.keyUp(Key.C_SEPARATOR);}
		else if(key.contains("NUM_LOCK")){keyboard.keyUp(Key.NUM_LOCK);}
		else if(key.contains("C_NUM_LOCK")){keyboard.keyUp(Key.C_NUM_LOCK);}
		else if(key.contains("ADD")){keyboard.keyUp(Key.ADD);}
		else if(key.contains("C_ADD")){keyboard.keyUp(Key.C_ADD);}
		else if(key.contains("MINUS")){keyboard.keyUp(Key.MINUS);}
		else if(key.contains("C_MINUS")){keyboard.keyUp(Key.C_MINUS);}
		else if(key.contains("MULTIPLY")){keyboard.keyUp(Key.MULTIPLY);}
		else if(key.contains("C_MULTIPLY")){keyboard.keyUp(Key.C_MULTIPLY);}
		else if(key.contains("DIVIDE")){keyboard.keyUp(Key.DIVIDE);}
		else if(key.contains("C_DIVIDE")){keyboard.keyUp(Key.C_DIVIDE);}
	}
	
	public static void main (String args[]){
	}
}


