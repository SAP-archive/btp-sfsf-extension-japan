package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.EmployeeService;
import com.sap.sfsf.reshuffle.simulation.backend.services.HistoryService;
import com.sap.sfsf.reshuffle.simulation.backend.services.SimulationCheckService;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.simulation.backend.config.PlaceholderConfig;
import com.sap.sfsf.reshuffle.simulation.backend.services.WorkflowService;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJob;

@Controller
public class HelloController {

	@Autowired
	CandidateService candidateService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	SimulationCheckService simulationCheckService;

	@Autowired
	HistoryService historyService;

	@Autowired
	PlaceholderConfig placeholderConfig;
	
	@Autowired
	WorkflowService workflowService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String home() {
		List<EmpJob> jobList = employeeService.getEmpJobs();
		return new Gson().toJson(jobList);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String list() {
		List<Candidate> list = candidateService.findAll();
		return new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create().toJson(list);
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	String check(@RequestBody String caseID, HttpServletResponse response) {
		// List<Candidate> list = candidateService.findAll();
		List<Candidate> list = candidateService.findByCaseid(caseID);
		simulationCheckService.preCheck(list);
		simulationCheckService.checkEmploymentInformation(list);
		simulationCheckService.checkPersonInformation(list);
		simulationCheckService.finalCheck(list);
		String checkResult = candidateService.checkStatus(list);
		historyService.saveHistory(list);

		response.setHeader("checkResult", checkResult);
		return new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create().toJson(list);
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String status(@RequestParam(name = "caseID", required = true) String caseID) {
		// Candidate candidate = candidateService.findCheckedOne();
		Candidate candidate = candidateService.findCheckedOnebyCaseID(caseID);
		// List<Candidate> list = candidateService.findAll();
        List<Candidate> list = candidateService.findByCaseid(caseID);
		
		if (candidate == null) {
			//@formatter:off
			return "{" +
					"\"status\":null,"+
					"\"checkedDateTime\": null,"+
					"\"TOTAL\":\"" + list.size() + "\","+
					"\"NG\":\"" + 0 + "\","+
					"\"WARN\":\"" + 0 + "\","+
					"\"OK\":\"" + 0 + "\""+
					"}";

			//@formatter:on
		} else {
            String status = candidateService.checkStatus(list);
            long ngCnt = candidateService.ngCnt(list);
			long okCnt = candidateService.okCnt(list);
			long warnCnt = candidateService.warnCnt(list);
			long applCnt = candidateService.applCnt(list);
            long appdCnt = candidateService.appdCnt(list);
            long denyCnt = candidateService.denyCnt(list);
            
			//@formatter:off
			return "{" +
			"\"status\":\""+ status + "\","+
			"\"checkedDateTime\":\"" +  DateTimeUtil.formatDateTokyo(candidate.getSimulationCheckDatetime())+"\","+
			"\"TOTAL\":\"" + list.size() + "\","+
			"\"NG\":\"" + ngCnt + "\","+
			"\"WARN\":\"" + warnCnt + "\","+
			"\"OK\":\"" + okCnt + "\","+
			"\"APPL\":\"" + applCnt + "\","+
            "\"APPD\":\"" + appdCnt + "\","+
            "\"DENY\":\"" + denyCnt + "\""+
			"}";
			//@formatter:on
		}
	}
	
	@RequestMapping(value = "/placeholderexamples", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String placeholderexamples() {
		String strPlcExamples = placeholderConfig.readPlaceholderExamples();
		return strPlcExamples;
	}

	@RequestMapping(value = "/workflow", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	String workflow(@RequestBody String caseID, 
			HttpServletResponse response) {
		return workflowService.invokeWorkflow(caseID);
	}

	@RequestMapping(value = "/caseid", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String caseid() {
		List <String> caseIDs = candidateService.findDistinctCaseid();
		return new Gson().toJson(caseIDs);
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	void approve(@RequestBody String caseID, HttpServletResponse response) {
		candidateService.approval(caseID);
	}

    @RequestMapping(value = "/denial", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	void denial(@RequestBody String caseID, HttpServletResponse response) {
		candidateService.denial(caseID);
	}
}
