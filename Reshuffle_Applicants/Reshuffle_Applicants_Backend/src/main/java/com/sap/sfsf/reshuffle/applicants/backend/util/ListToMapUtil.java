package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ListToMapUtil {
	public static <T> Map<String, T> getMap(String fieldId, List<T> list) {
		Map<String, T> map = list.stream()
				.collect(Collectors.toMap(
						data -> {
							JsonObject json = new Gson().fromJson(new Gson().toJson(data), JsonObject.class);
							return json.get(fieldId).getAsString(); 
							},
						data -> data
				));
		return map;
	}
}
