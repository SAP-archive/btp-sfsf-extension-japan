package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.simulation.backend.util.PDFUtil;

@Service
public class PDFService {
	private Logger LOG = LoggerFactory.getLogger(PDFService.class);
	private PDFUtil pdfUtil = new PDFUtil();

	@Autowired
	private ConfigService configService;

	public String getPDFContent(List<Candidate> candidateList) {
		HttpDestination destination = DestinationAccessor.getDestination("ADS").asHttp();
		for (String prop : destination.getPropertyNames()) {
			LOG.debug(prop);
		}
		HttpClient client = HttpClientAccessor.getHttpClient(destination);
		String url = destination.get("URL", String.class).get();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		//@formatter:off
		/*
		<?xml version="1.0" encoding="UTF-8"?>
		<form1>
			<page>
			<DateField1>2021年4月1日</DateField1>
			<CurrentDivision>営業本部</CurrentDivision>
			<CurrentDepartment>営業一部 営業一課</CurrentDepartment>
			<CandidateName>竈門 炭治郎</CandidateName>
			<contents1>2021年4月1日付で、〇〇〇〇部 〇〇〇〇課に配属を命じます。</contents1>
			</page>
		</form1>
		*/
        String previewDate = DateTimeUtil.formatDateTokyoJP(new Date());
        String orderDate = configService.getJpStartDate();        
		String form = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form1>%s</form1>";
		StringBuilder pages = new StringBuilder("");
		for(Candidate c : candidateList){
			String page = "<page>" +
			"<DateField1>%s</DateField1>" +
			"<CurrentDivision>%s</CurrentDivision>" +
			"<CurrentDepartment>%s</CurrentDepartment>" +
			"<CandidateName>%s</CandidateName>" +
			"<contents1>%s付で、%s %sに配属を命じます。</contents1>" +
			"</page>";
			pages.append(String.format(page,
				previewDate,
				c.getCandidateDivisionName(),
				c.getCandidateDepartmentName(),
				c.getCandidateName(),
				orderDate,
				c.getDivisionName(),
				c.getDepartmentName())
			);
		}
		form = String.format(form, pages.toString());
		LOG.debug("Form Content:"+form);
		String xmlData = Base64.getEncoder().encodeToString(form.getBytes());
		StringEntity input;
		try {
			input = new StringEntity("{\n" + 
					"    \"xdpTemplate\": \"SFSF_Resignation/Template1\",\n" + 
					"    \"xmlData\": \""+ xmlData +"\"}");
			httpPost.setEntity(input);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//@formatter:on

		/* SEND AND RETRIEVE RESPONSE */
		HttpResponse response = null;
		try {
			response = client.execute(httpPost);
			LOG.debug("Status Code:" + response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* RESPONSE AS STRING */
		String result = null;
		try {
			result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			LOG.debug("Response Content:" + result);
			return pdfUtil.getPDFContent(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
