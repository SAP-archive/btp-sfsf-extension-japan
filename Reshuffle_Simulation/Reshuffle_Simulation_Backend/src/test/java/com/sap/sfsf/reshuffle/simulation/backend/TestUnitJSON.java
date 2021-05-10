package com.sap.sfsf.reshuffle.simulation.backend;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJobUpsert;

public class TestUnitJSON {

	@Test
	public void testJson() {
		String json = "{\"endDate\":253402214400000,\"event\":\"3669\",\"eventReason\":\"HIRNEW\",\"startDate\":1281312000000,\"userId\":\"mnadal\"}";
		JsonObject currentJobJSON = new Gson().fromJson(json, JsonObject.class);
		System.out.println(currentJobJSON.has("event"));
	}
	
	@Test
	public void testEmpJobUpsert() {
		MyEmpJobUpsert empJob = new MyEmpJobUpsert();
		String json = new GsonBuilder().serializeNulls().create().toJson(empJob);
		System.out.println(json);
	}
	
}
