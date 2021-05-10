package com.sap.sfsf.reshuffle.simulation.backend.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ListToMapUtil {

    
	/**
	 * Convert List to Map(String, T)
	 * @return
	 */
	public static <T> Map<String, T> getMap(String idField, List<T> list){
		Map<String, T> map = list.stream()      
		.collect(Collectors.toMap(   
			data -> {
				JsonObject jobJSON = new Gson().fromJson(new Gson().toJson(data), JsonObject.class);
				return jobJSON.get(idField).getAsString();
			}, 
			data -> data 
		));
		return map;
    }
    

	/**
	 * Convert List to Map(String,List)
	 * @return
	 */
    public static <T> Map<String, List<T>> getGroupedList(List<T> list) {
        Map<String, List<T>> map = list.stream()
        .collect(Collectors.groupingBy(data->{
			JsonObject jobJSON = new Gson().fromJson(new Gson().toJson(data), JsonObject.class);
			return jobJSON.get("userId").getAsString();
		}));
		return map;
	}

    
}