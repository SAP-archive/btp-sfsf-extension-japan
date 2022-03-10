package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.model.jobschedule.MyJob;
import com.sap.sfsf.reshuffle.simulation.backend.model.jobschedule.Schedule;

@Service
public class JobSchedulerService {

	@Autowired
	ConfigService configService;

	private Logger LOG = LoggerFactory.getLogger(JobSchedulerService.class);

	JSONObject obj = new JSONObject(System.getenv("VCAP_SERVICES"));
	JSONArray arr = obj.getJSONArray("jobscheduler");

	/**
	 * Email送信ジョブを作成
	 * 
	 * @param requestURL Email送信API
	 * @param list       候補者リスト
	 */
	public void createEmailJob(String requestURL, List<Candidate> list, String startDT) throws Exception {
		startDT = startDT == null ? configService.getStartDTStr() : startDT;
		String payload = new GsonBuilder().serializeNulls().create().toJson(list);

		String api = requestURL + "/mail";
		MyJob job = null;
		try {
			job = getJob("emailJob");
		} catch (Exception e) {
			LOG.error("Error retreiving Job");
		}
		if (job != null) {
			LOG.debug("Found emailJob");
			Schedule schedule = new MyJob().createSchedule(startDT, payload);
			String schedulePayload = new GsonBuilder().serializeNulls().create().toJson(schedule);
			LOG.debug("shedule:" + schedulePayload);
			LOG.debug("job json:" + new Gson().toJson(job));
			Integer jobId = job.getId();
			createSchedule(jobId, schedulePayload);
		} else {
			LOG.debug("EmailJob not found, create one!");
			MyJob emailJob = new MyJob(api, startDT, payload);
			String jobPayload = new GsonBuilder().serializeNulls().create().toJson(emailJob);
			LOG.debug(jobPayload);
			createJob(jobPayload);
		}
	}

	/**
	 * ジョブを取得
	 * 
	 * @param jobName
	 * @return
	 */
	private MyJob getJob(String jobName) throws Exception {

		// String authCode = getAuthCode();
        String accessToken = getAccessToken();
		URI uri = new URI(getURL() + "?name=" + jobName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		// headers.setBasicAuth(authCode);
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<MyJob> response = new RestTemplate().exchange(uri, HttpMethod.GET, entity, MyJob.class);
		MyJob job = response.getBody();
		return job;
	}

	/**
	 * ジョブを作成
	 * 
	 * @param payload
	 */
	private void createJob(String payload) throws Exception {
        String accessToken = getAccessToken();
		URI uri = new URI(getURL());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
		new RestTemplate().postForEntity(uri, entity, MyJob.class);

	}

	/**
	 * ジョブスケジュールを作成
	 * 
	 * @param jobId   ジョブID
	 * @param payload スケジュールJSONデータ
	 */
	private void createSchedule(Integer jobId, String payload) throws Exception {
        String accessToken = getAccessToken();
		URI uri = new URI(getURL() + "/" + jobId + "/schedules");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
		String response = new RestTemplate().postForObject(uri, entity, String.class);
		LOG.debug("Post schedule response:" + response);
	}

	/**
	 * 認証コードの取得
	 * 
	 * @return
	 */
	private String getAuthCode() {
		String clientid = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("uaa").getString("clientid");
		String clientsecret = arr.getJSONObject(0).getJSONObject("credentials").getJSONObject("uaa").getString("clientsecret");

		byte[] bytes = (clientid + ":" + clientsecret).getBytes();
		String encoded = Base64.getEncoder().encodeToString(bytes);
		LOG.debug("base64 auth code:" + encoded);
		return encoded;

    }

	/**
	 * Job Scheduler APIのURLを取得
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	private String getURL() throws URISyntaxException {
		String url = arr.getJSONObject(0).getJSONObject("credentials").getString("url");
		return url + "/scheduler/jobs";
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

}
