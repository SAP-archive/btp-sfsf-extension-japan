package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyBusinessUnit;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyCompany;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDepartment;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDivision;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyPosition;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.FilterService;

@Controller
public class FilterController {
	@Autowired
	FilterService filterService;
	
	@Autowired
	CandidateService candidateService;

	@RequestMapping(value = "/company", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String company() {
		List<MyCompany> list = filterService.getCompanyByQuery();
		return new Gson().toJson(list);
	}

	@RequestMapping(value = "/businessunit", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String businessUnit() {
		List<MyBusinessUnit> list = filterService.getBusinessUnitByQuery();
		return new Gson().toJson(list);
	}


	@RequestMapping(value = "/division", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String division() {
		List<MyDivision> divisionList = filterService.getDivisionByQuery();
		return new Gson().toJson(divisionList);
	}

	@RequestMapping(value = "/department", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String department() {
		List<MyDepartment> list = filterService.getDepartmentByQuery();
		return new Gson().toJson(list);
	}

	@RequestMapping(value = "/position", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	String position() {
		List<MyPosition> list = filterService.getPositionByQuery();
		return new Gson().toJson(list);
	}

}
