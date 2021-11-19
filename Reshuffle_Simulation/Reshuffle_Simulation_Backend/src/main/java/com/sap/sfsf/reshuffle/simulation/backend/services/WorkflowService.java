package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.repository.CandidateRepository;

@Service
public class WorkflowService {
	private static int CASEIDSTRUL = 50;
	private static int CASEIDSTRLL = 1;

	@Value( "${workflowservice.workflow.name}" )
	private String workflowName; // SCP workflow name to be indicated in the application.properties files (in lowercase)
	
	private Logger LOG = LoggerFactory.getLogger(WorkflowService.class);

	JSONObject obj = new JSONObject(System.getenv("VCAP_SERVICES"));
	JSONArray arr = obj.getJSONArray("workflow");
	
	@Autowired
	CandidateRepository candidateRepo;

	public String invokeWorkflow(String caseID) {
		//Checking the CaseId string length
		int length = caseID.length();
		if(length < CASEIDSTRLL || length > CASEIDSTRUL) {
			LOG.error("Wrong size of Case ID : " + length + " chars, caseID : " + caseID);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List <Candidate> candidates = candidateRepo.findByCaseid(caseID);
		String payload = buildPayload(candidates, caseID);
		LOG.debug("workflow service payload:" + payload);

		String workfowResponse = null;
		try {
			workfowResponse = createWorkflowInstance(payload);
		} catch (Exception e) {
			LOG.error("Error creating the workflow instance:" + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		//update cahdidate status OK=>WF
		for (Candidate c : candidates) {
			c.setCheckStatus("APPL");
		}
		candidateRepo.saveAll(candidates);

		return workfowResponse;
	}

	// get the workflow service URL
	private String getWorkflowURL() {
		String url = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("endpoints").getString("workflow_rest_url");
		return url + "/v1/workflow-instances";
	}


	// building the payload in the following format
//	{
//		"definitionId" : "workflowtest",
//		"context": {
//			"currentManagers": [
//				{
//					"manager": "kuroda",
//					"candidates": [
//						{"id":"1000001", "name":"shinagawa", "nextPositionName":"JY25"},
//						{"id":"1000002", "name":"osaki", "nextPositionName":"JY24"},
//						{"id":"1000003", "name":"gotanda", "nextPositionName":"JY23"}
//					]
//				},
//				{
//					"manager": "mori",
//					"candidates": [
//						{"id":"1000004", "name":"meguro", "nextPositionName":"JY22"},
//						{"id":"1000005", "name":"ebisu", "nextPositionName":"JY21"},
//						{"id":"1000006", "name":"shibuya", "nextPositionName":"JY20"}
//					]
//			  }
//			],
//			 "nextManagers": [
//				{        
//					"manager": "wang",
//					"candidates": [
//						{"id":"1000007", "name":"harajuku", "currentPositionName":"JY19"},
//						{"id":"1000008", "name":"yoyogi", "currentPositionName":"JY18"},
//						{"id":"1000009", "name":"shinjuku", "currentPositionName":"JY17"}
//					]
//				},
//				{
//					"manager": "hiramoto",
//					"candidates": [
//						{"id":"1000010", "name":"takadanobaba", "currentPositionName":"JY15"},
//						{"id":"1000011", "name":"mejiro", "currentPositionName":"JY14"},
//						{"id":"1000012", "name":"ikebukuro", "currentPositionName":"JY13"}
//					]
//				}
//			]
//		}
//	}
	
	private String buildPayload(List<Candidate> candidates, String caseID) {
		LOG.debug("Building payload to call workflow named:" + workflowName);

		Map<String, Object> payloadMap = new HashMap<String, Object>();  // map for payload
		payloadMap.put("definitionId", workflowName); // definition (name) of the workflow to be imported from somewhere	
		Map<String, Object> contextMap = new HashMap<String, Object>(); // context object to contain SCP workflow service payload (lists of candidates for current and next managers)
        payloadMap.put("context", contextMap);
        contextMap.put("caseID", caseID);

		ArrayList<Object> curManArray = new ArrayList<Object>(); // current Managers list
		contextMap.put("currentManagers", curManArray);
		ArrayList<Object> nextManArray = new ArrayList<Object>(); // next Managers list
		contextMap.put("nextManagers", nextManArray);

		// read the candidates and place then in a map with manager as key to later gather the candidates by distinct manager in an array
		Map<String, ArrayList<Object>> curManMap = new HashMap<String, ArrayList<Object>>();  
		Map<String, ArrayList<Object>> nextManMap = new HashMap<String, ArrayList<Object>>();  

		for (Candidate c : candidates) {
			if(!curManMap.containsKey(c.getCurrentManager())) {
				curManMap.put(c.getCurrentManager(), new ArrayList<Object>());
			}
			
			Map<String, Object> cinfo1 = new HashMap<String, Object>();
			cinfo1.put("id", c.getCandidateID());
			cinfo1.put("name", c.getCandidateName());
			cinfo1.put("nextPositionName", c.getNextPositionName());
			(curManMap.get(c.getCurrentManager())).add(cinfo1);

			if(!nextManMap.containsKey(c.getNextManager())) {
				nextManMap.put(c.getNextManager(), new ArrayList<Object>());
			}
			
			Map<String, Object> cinfo2 = new HashMap<String, Object>();
			cinfo2.put("id", c.getCandidateID());
			cinfo2.put("name", c.getCandidateName());
			cinfo2.put("currentPositionName", c.getCurrentPositionName());
			(nextManMap.get(c.getNextManager())).add(cinfo2);
		}
		
		// add array of candidates by distinct managers to the arrays of current and next managers
		for (Map.Entry<String, ArrayList<Object>> entry : curManMap.entrySet()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("manager", entry.getKey());
			m.put("candidates", entry.getValue());
			curManArray.add(m);
		}
		
		for (Map.Entry<String, ArrayList<Object>> entry : nextManMap.entrySet()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("manager", entry.getKey());
			m.put("candidates", entry.getValue());
			nextManArray.add(m);
		}
		
		return new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create().toJson(payloadMap);
	}


	// create a SCP workflow service instance to execute the workflow passing the payload
	private String createWorkflowInstance(String payload) throws URISyntaxException{
		String accessToken = getAccessToken();
		URI uri = new URI(getWorkflowURL());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
		String response = new RestTemplate().postForObject(uri, entity, String.class);
		LOG.debug("create workflow response:" + response);
		return response;
	}

	// get the URL to obtain a token
	private String getAccessTokenURL() {
		String url = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("uaa").getString("url");
		return url + "/oauth/token?grant_type=client_credentials";
	}

	// get OAuth token using the urltaken from VCAP
	private String getAccessToken()  throws URISyntaxException {
		String authCode = getAuthCode();
		URI uri = new URI(getAccessTokenURL());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(authCode);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String response = new RestTemplate().postForObject(uri, entity, String.class);
		
		JSONObject objResponse = new JSONObject(response);
		String accessToken = objResponse.getString("access_token");
		LOG.debug("access token:" + accessToken);

		return accessToken;
	}
	
	// to get OAuth token authenticate with the client id and secret taken from VCAP
	private String getAuthCode() {
		String clientid = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("uaa").getString("clientid");
		String clientsecret = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("uaa").getString("clientsecret");

		byte[] bytes = (clientid + ":" + clientsecret).getBytes();
		String encoded = Base64.getEncoder().encodeToString(bytes);
		LOG.debug("base64 auth code:" + encoded);
		return encoded;
	}
}
