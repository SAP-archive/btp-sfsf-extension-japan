package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.security.principal.Principal;
import com.sap.cloud.sdk.cloudplatform.security.principal.PrincipalAccessor;
import com.sap.cloud.sdk.cloudplatform.tenant.Tenant;
import com.sap.cloud.sdk.cloudplatform.tenant.TenantAccessor;
import com.sap.cloud.security.xsuaa.tokenflows.XsuaaTokenFlows;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.EmployeeService;
import com.sap.sfsf.reshuffle.simulation.backend.services.JobSchedulerService;
import com.sap.sfsf.reshuffle.simulation.backend.services.PDFService;


import com.sap.cloud.security.xsuaa.token.Token;

@Controller
public class TestController {
	private Logger LOG = LoggerFactory.getLogger(TestController.class);

	@Autowired
	CandidateService candidateService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	JobSchedulerService jobService;

	@Autowired
	PDFService pdfService;


    /**
     * The injected factory for XSUAA token tokenflows.
     */
	@Autowired
	 private XsuaaTokenFlows tokenFlows;
	

	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@Transactional
	public String createCandidates() {
		List<Candidate> list = new ArrayList<Candidate>();
		Candidate c1 = new Candidate("103108", "50014328");
		Candidate c2 = new Candidate("atomi", "50014328");
		Candidate c3 = new Candidate("mhirayama", "50014328");
		list.add(c1);
		list.add(c2);
		list.add(c3);
		candidateService.deleteAll();
		candidateService.saveAll(list);
		return "";
	}

	@RequestMapping(value = "/clear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@Transactional
	public String clear() {
		candidateService.deleteAll();
		return new Gson().toJson(candidateService.findAll());
	}

	@RequestMapping(value = "/myempjoblist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String testMyEmpJobList() {
		return new Gson().toJson(employeeService.getCurrentJobsByQuery());
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String testUser() {
		final Tenant currentTenant = TenantAccessor.getCurrentTenant();
		final Principal principal = PrincipalAccessor.getCurrentPrincipal();

		String id = principal.getPrincipalId();
		System.out.println("ID:" + id);
		System.out.println("Tenant" + new Gson().toJson(currentTenant));
		return new Gson().toJson(principal);
	}

	@RequestMapping(value = "/pdftest", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> testPDF() {
		Candidate c = new Candidate();
		c.setCandidateName("テスト社員");
		c.setCandidateDepartmentName("テスト元課");
		c.setCandidateDivisionName("テスト元部");
		c.setDepartmentName("テスト先課");
		c.setDivisionName("テスト先部");
		Candidate c2 = new Candidate();
		c2.setCandidateName("テスト社員2");
		c2.setCandidateDepartmentName("テスト元課2");
		c2.setCandidateDivisionName("テスト元部2");
		c2.setDepartmentName("テスト先課2");
		c2.setDivisionName("テスト先部2");
		List<Candidate> list = new ArrayList<Candidate>();
		list.add(c);
		list.add(c2);
		String content = pdfService.getPDFContent(list);
		byte[] contentBytes = Base64.getDecoder().decode(content);
		ByteArrayResource resource = new ByteArrayResource(contentBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=異動命令.pdf");
		//@formatter:off
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentLength(contentBytes.length)
				.contentType(MediaType.APPLICATION_PDF)
				.body(resource);
		//@formatter:on
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getToken(@AuthenticationPrincipal Token token){

        LOG.info("Got the Xsuaa token: {}", token.getAppToken());
        LOG.info(token.toString());

        Map<String, String> result = new HashMap<>();
        result.put("grant type", token.getGrantType());
        result.put("client id", token.getClientId());
        result.put("subaccount id", token.getSubaccountId());
        result.put("zone id", token.getZoneId());
        result.put("logon name", token.getLogonName());
        result.put("family name", token.getFamilyName());
        result.put("given name", token.getGivenName());
		result.put("email", token.getEmail());
		//result.put("xs_user_uuid",token.getXSUserAttribute("user_uuid")[0]);
        result.put("authorities", String.valueOf(token.getAuthorities()));
        result.put("scopes", String.valueOf(token.getScopes()));
		return new Gson().toJson(result);
	}
}