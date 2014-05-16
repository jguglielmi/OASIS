package org.qedsys.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueCompareFixture {
	private static final Logger LOG = LoggerFactory.getLogger(ValueCompareFixture.class);

	public ValueCompareFixture(){
	}

	public boolean compareStringTo(String string1, String string2){
		if(string1.compareTo(string2)==0){
			LOG.info(string1 + " == " + string2);
			return true;
		}
		else{
			LOG.error(string1 + " != " + string2);
			return false;
		}
	}

	public boolean compareIntTo(int num1, int num2){
		if(num1==num2){
			LOG.info(num1 + " == " + num2);
			return true;
		}
		else{
			LOG.error(num1 + " != " + num2);
			return false;
		}
	}

	public boolean compareDoubleTo(double double1, double double2){
		if(double1==double2){
			LOG.info(double1 + " == " + double2);
			return true;
		}
		else{ 
			LOG.error(double1 + " != " + double2);
			return false;
		}
	}

	public boolean compareFloatTo(float float1, float float2){
		if(float1==float2){
			LOG.info(float1 + " == " + float2);
			return true;
		}
		else{
			LOG.error(float1 + " != " + float2);
			return false;
		}
	}

	public boolean compareBooleanTo(boolean bool1, boolean bool2){
		if(bool1==bool2){
			LOG.info(bool1 + " == " + bool2);
			return true;
		}
		else{
			LOG.error(bool1 + " != " + bool2);
			return false;
		}
	}

	public boolean compareArrayTo(String[] array1, String[] array2){
		if(array1.length != array2.length){
			LOG.error(array1 + " != " + array2);
			return false;
		}
		else{
			for(int i=0; i<array1.length; i++){
				if(array1[i]!=array2[i]) {
					LOG.error(array1[i] + " != " + array2[i]);
					return false;
				}
				else {
					LOG.info(array1[i] + " == " + array2[i]);
				}
			}
			return true;
		}
	}
}
