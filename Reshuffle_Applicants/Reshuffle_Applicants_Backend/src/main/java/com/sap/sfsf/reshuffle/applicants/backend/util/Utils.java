package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.util.validator.CandidateReqBodyValidator;

public class Utils {
	Logger logger = LoggerFactory.getLogger(Utils.class);
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
	
	public List<Candidate> reqToCandidateList(String req) {
		List<Candidate> candidateList = new ArrayList<>();

		JSONArray jsonArray = new JSONArray(req);
		boolean isBadRequest = false;
		ArrayList<String> errorsList = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i ++) {
			Gson gson = new Gson();
			JSONObject json = jsonArray.getJSONObject(i);
			Candidate candidate = gson.fromJson(json.toString(), Candidate.class);

			CandidateReqBodyValidator cValidator = new CandidateReqBodyValidator(candidate);
			isBadRequest = cValidator.isBadRequest();
			if(isBadRequest == true) {
				errorsList.addAll(cValidator.getProblemList());
				Optional<String> result = cValidator.getProblemList().stream().reduce(
						(accum, value) -> {
							return accum + ", "  + value;
						});
				logger.error("Candidate Validation Error(s) : " + i + "th candidate: " + result.orElse(""));
			}
			
			candidateList.add(candidate);
		}

		if(isBadRequest == true) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		return candidateList;
	}
	
	public List<Candidate> reqToCandidateListWithoutValidation(String req) {
		List<Candidate> candidateList = new ArrayList<>();

		JSONArray jsonArray = new JSONArray(req);

		for (int i = 0; i < jsonArray.length(); i ++) {
			Gson gson = new Gson();
			JSONObject json = jsonArray.getJSONObject(i);
			Candidate candidate = gson.fromJson(json.toString(), Candidate.class);

			candidateList.add(candidate);
		}

		return candidateList;
	}
	
}
