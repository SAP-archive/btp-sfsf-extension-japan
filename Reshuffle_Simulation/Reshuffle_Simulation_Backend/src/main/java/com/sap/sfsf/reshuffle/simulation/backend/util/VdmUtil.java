package com.sap.sfsf.reshuffle.simulation.backend.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class VdmUtil {


	public static <T> String getStringFromVdm(T vdmObject, String filedName) {
		String value = null;
		String json = new Gson().toJson(vdmObject);
		value = new Gson().fromJson(json, JsonObject.class).get(filedName).getAsString();
		return value;
	}
}
