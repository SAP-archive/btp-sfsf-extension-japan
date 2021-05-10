package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJobUpsert;
import com.sap.sfsf.reshuffle.simulation.backend.model.UpsertResult;

@Service
public class EmpJobUpsertService {
	private Logger LOG = LoggerFactory.getLogger(EmpJobUpsertService.class);
	Destination dest = DestinationAccessor.getDestination("SFSF_2nd");
	Gson gson = new GsonBuilder().serializeNulls().create();

	/**
	 * EmpJobをSFSFへ更新しに行く
	 * 
	 * @param list
	 */
	public void upsert(List<MyEmpJobUpsert> list) throws Exception {
		for (String prop : dest.getPropertyNames()) {
			LOG.debug(prop);
		}
		String authCode = getAuthCode();
		String payload = gson.toJson(list);
		LOG.debug("upsert payload:" + payload);
		URI uri = getURI();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(authCode);
		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
		ResponseEntity<UpsertResult> response = new RestTemplate().postForEntity(uri, entity, UpsertResult.class);
		LOG.debug(gson.toJson(response));
	}

	private String getAuthCode() {
		String user = dest.get("User").getOrElse("").toString();
		String password = dest.get("Password").getOrElse("").toString();
		byte[] bytes = (user + ":" + password).getBytes();
		String encoded = Base64.getEncoder().encodeToString(bytes);
		return encoded;
	}

	private URI getURI() throws URISyntaxException {
		String url = dest.get("URL").getOrElse("").toString();
		if (url.endsWith("/")) {
			url = url + "odata/v2/upsert";
		} else {
			url = url + "/odata/v2/upsert";
		}
		return new URI(url);
	}

}