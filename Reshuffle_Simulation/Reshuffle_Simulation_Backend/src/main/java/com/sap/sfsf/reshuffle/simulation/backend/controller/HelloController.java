package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.EmployeeService;
import com.sap.sfsf.reshuffle.simulation.backend.services.HistoryService;
import com.sap.sfsf.reshuffle.simulation.backend.services.SimulationCheckService;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;

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


	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String list() {
		List<Candidate> list = candidateService.findAll();
		return new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create().toJson(list);
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	String check(HttpServletResponse response) {
		List<Candidate> list = candidateService.findAll();
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
	String status() {
		Candidate candidate = candidateService.findCheckedOne();
		List<Candidate> list = candidateService.findAll();
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
			long ngCnt = candidateService.ngCnt(list);
			long okCnt = candidateService.okCnt(list);
			long warnCnt = candidateService.warnCnt(list);
			String status = candidateService.checkStatus(list);
			//@formatter:off
			return "{" +
			"\"status\":\""+ status + "\","+
			"\"checkedDateTime\":\"" +  DateTimeUtil.formatDateTokyo(candidate.getCheckDateTime())+"\","+
			"\"TOTAL\":\"" + list.size() + "\","+
			"\"NG\":\"" + ngCnt + "\","+
			"\"WARN\":\"" + warnCnt + "\","+
			"\"OK\":\"" + okCnt + "\""+
			"}";
			//@formatter:on
		}
	}

}
