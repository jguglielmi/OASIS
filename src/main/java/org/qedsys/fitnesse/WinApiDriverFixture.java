
// Name: WinApiDriverFixture
// Author: Edward Jakubowski ejakubowski@qed-sys.com
// Last update: 12/27/2013
// Description: This Fixture add support to run Windows Api Functions in Fitnesse
// Requirements: Jna library
// Examples:
//   

package org.qedsys.fitnesse;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.LARGE_INTEGER;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

import java.util.ArrayList;
import java.util.List;

public class WinApiDriverFixture {
	public User32 user32;
	
    public static class WindowInfo {
    	HWND hwnd;
        RECT rect;
        String title;
        String className = "";
        boolean isChild = false;
        
        public WindowInfo(HWND hWnd, boolean isChild) {
            byte[] buffer = new byte[1024];
            User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
            title = Native.toString(buffer);
            
            char[] buffer2 = new char[1026];
			User32.instance.GetClassName(hWnd, buffer2, 1026);
			className = Native.toString(buffer2);
			
			rect = new RECT();
			User32.instance.GetWindowRect(hWnd, rect);
			
			this.isChild = isChild;

        }
        
        public WindowInfo(HWND hwnd, RECT rect, String title){ 
        	this.hwnd = hwnd; this.rect = rect; this.title = title;
        }
        
        public WindowInfo(HWND hwnd, RECT rect, String title, String className){ 
        	this.hwnd = hwnd; this.rect = rect; this.title = title; this.className = className;
        }


        public String toString() {
            return String.format("(%d,%d)-(%d,%d) : \"%s\" [%s]", rect.left,rect.top,rect.right,rect.bottom,title,className);
        }
    }

	public interface User32 extends W32APIOptions {  
		User32 instance = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);  
		    
		boolean ShowWindow(HWND hWnd, int nCmdShow);  
		boolean SetForegroundWindow(HWND hWnd);  
		HWND FindWindow(String winClass, String title);
		LRESULT SendMessage(HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);
		
		boolean EnumWindows (WNDENUMPROC wndenumproc, int lParam);
		boolean EnumChildWindows(HWND hWnd, WNDENUMPROC lpEnumFunc, Pointer data);
		HWND GetParent(HWND hWnd);
		boolean IsWindowVisible(HWND hWnd);
		int GetWindowRect(HWND hWnd, RECT r);
		void GetWindowTextA(HWND hWnd, byte[] buffer, int buflen);
		int GetTopWindow(HWND hWnd);
		int GetWindow(HWND hWnd, int flag);
		final int GW_HWNDNEXT = 2;
		int GetClassName(HWND hWnd, char[] buffer2, int i);
	}
	
	public interface Kernel32 extends W32APIOptions {
		Kernel32 instance = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class, DEFAULT_OPTIONS);  
		boolean GetDiskFreeSpaceEx(String lpDirectoryName, LARGE_INTEGER.ByReference lpFreeBytesAvailable, LARGE_INTEGER.ByReference lpTotalNumberOfBytes, LARGE_INTEGER.ByReference lpTotalNumberOfFreeBytes);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("starting driver...");
		
		WinApiDriverFixture wadf = new WinApiDriverFixture();
		System.out.println("disk space used: " + wadf.getDiskUsedPercentage() + "%");
		//System.out.print(wadf.GetWindowInfoXml());
		//HWND handle = wadf.findWindowByTitle("Calculator");
		//wadf.activateWindow(handle);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		//wadf.minimizeWindow(handle);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		//wadf.restoreWindow(handle);
		System.out.println("done.");
	}
	
	public WinApiDriverFixture() {
		user32 = User32.instance;
	}

	public HWND findWindowByTitle(String title) {
		HWND handle = user32.FindWindow(null, title);
		return handle;
	}
	
	public boolean activateWindow(HWND handle) {
		boolean result = user32.SetForegroundWindow(handle);
		return result;
	}
	
	public boolean showWindow(HWND handle) {
		return user32.ShowWindow(handle, WinUser.SW_SHOW); 
	}
	
	public boolean minimizeWindow(HWND handle) {
		return user32.ShowWindow(handle, WinUser.SW_MINIMIZE); 
	}
	
	public boolean restoreWindow(HWND handle) {
		return user32.ShowWindow(handle, WinUser.SW_RESTORE); 
	}
	
	public boolean sendMessageClick(HWND handle){
		//user32.SendMessage(handle, );
		return true;
	}

	public int getDiskUsedPercentage() {
		return getDiskUsedPercentage(null);
	}
	
	public int getDiskUsedPercentage(String target) {
		LARGE_INTEGER.ByReference lpFreeBytesAvailable = new LARGE_INTEGER.ByReference();
        LARGE_INTEGER.ByReference lpTotalNumberOfBytes = new LARGE_INTEGER.ByReference();
        LARGE_INTEGER.ByReference lpTotalNumberOfFreeBytes = new LARGE_INTEGER.ByReference();
        Kernel32.instance.GetDiskFreeSpaceEx(target, lpFreeBytesAvailable, lpTotalNumberOfBytes, lpTotalNumberOfFreeBytes);
        double freeBytes = lpTotalNumberOfFreeBytes.getValue();
        double totalBytes = lpTotalNumberOfBytes.getValue();
        //System.out.println("freespace " + humanReadableByteCount(freeBytes) + "/ totalspace " + humanReadableByteCount(totalBytes));
        return (int)(((totalBytes-freeBytes)/totalBytes) * 100.0);
	}
	
	public static String humanReadableByteCount(double bytes) {
		boolean si = true;
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public String GetWindowInfoXml() {
		
		final List<WindowInfo> infoList = new ArrayList<WindowInfo>();
	    
	    class ChildWindowCallback implements WinUser.WNDENUMPROC {
			public boolean callback(HWND hWnd, Pointer lParam) {
				infoList.add(new WindowInfo(hWnd, true));
				return true;
			}
	    }
	    class ParentWindowCallback implements WinUser.WNDENUMPROC {
			public boolean callback(HWND hWnd, Pointer lParam) {
				infoList.add(new WindowInfo(hWnd, false));
				user32.EnumChildWindows(hWnd, new ChildWindowCallback(), new Pointer(0));
				return true;
			}
	    }	    
	    user32.EnumWindows(new ParentWindowCallback(), 0);

	    // convert list to string xml
	    StringBuilder sb = new StringBuilder("");
	    for (WindowInfo w : infoList) {
	    	//System.out.println(w);
	    	sb.append(w + "\r\n");
	    }
	    sb.append("count - " + infoList.size() + "\r\n");
	    return sb.toString();
	}
	

}
