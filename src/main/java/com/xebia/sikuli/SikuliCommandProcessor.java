package com.xebia.sikuli;

import java.lang.reflect.*;
import com.xebia.sikuli.ExtendedSikuliCommands;

public abstract class SikuliCommandProcessor{

		public ExtendedSikuliCommands extendedSikuliCommands;
		 
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



}