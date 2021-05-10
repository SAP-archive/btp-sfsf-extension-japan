package com.sap.sfsf.reshuffle.configuration.backend.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	private static int EXCEPTIONAL_INT = -1;
	private static boolean EXCEPTIONAL_BOOL = false;
	
	public String getMandtStringField(JSONObject obj, String key) {
		String retVal = null;
		try {
			retVal = obj.getString(key);
		} catch (JSONException e) {
			throw e;
		}
		return retVal;
	}
	
	public String getOptinalStringField(JSONObject obj, String key) {
		String retVal = null;
		try {
			System.out.println("JSON RET: " + key);
			retVal = obj.getString(key);
			
		} catch (JSONException ignoired) {
			ignoired.printStackTrace();
		}
		return retVal;
	}
	
	public int getMandtIntField(JSONObject obj, String key) {
		int retVal = EXCEPTIONAL_INT;
		try {
			retVal = obj.getInt(key);			
		} catch (JSONException e) {
			throw e;
		}
		return retVal;
	}
	
	public int getOptionalIntField(JSONObject obj, String key) {
		int retVal = EXCEPTIONAL_INT;
		try {
			retVal = obj.getInt(key);
		} catch (JSONException ignored) {
			ignored.printStackTrace();
		}
		return retVal;
	}
	
	public boolean getMandtBoolField(JSONObject obj, String key) {
		boolean retVal = EXCEPTIONAL_BOOL;
		try {
			retVal = obj.getBoolean(key);			
		} catch (JSONException e) {
			throw e;
		}
		return retVal;
	}

	public boolean getOptionalBoolField(JSONObject obj, String key) {
		boolean retVal = EXCEPTIONAL_BOOL;
		try {
			retVal = obj.getBoolean(key);			
		} catch (JSONException ignored) {
			ignored.printStackTrace();
		}
		return retVal;
	}
}
