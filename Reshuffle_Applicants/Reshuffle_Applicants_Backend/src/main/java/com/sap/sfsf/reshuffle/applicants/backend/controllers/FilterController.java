package com.sap.sfsf.reshuffle.applicants.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.AllFilters;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.BusinessUnitFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CompanyFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DepartmentFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DivisionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.PositionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.RatingFilter;
import com.sap.sfsf.reshuffle.applicants.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.applicants.backend.services.FilterService;

@Controller
public class FilterController {
	@Autowired
	FilterService filterService;

	@Autowired
	CandidateService candidateService;
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String all() throws Exception {
		AllFilters allFilters = new AllFilters();
		allFilters.setCompanyList(filterService.getCompanyFilter());
		allFilters.setBusinessUnitList(filterService.getBUFilter());
		allFilters.setDivisionList(filterService.getDivisionFilter());
		allFilters.setDepartmentList(filterService.getDepartmentFilter());
		allFilters.setPositionList(filterService.getPositionFilter());
		allFilters.setRatingList(filterService.getRatingFilter());
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(allFilters);
	}

	@GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String company() throws ODataException, Exception {
		List<CompanyFilter> list = filterService.getCompanyFilter();
		
		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}

	@GetMapping(value = "/businessunits", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String businessunit() throws ODataException, Exception {
		List<BusinessUnitFilter> list = filterService.getBUFilter();
		
		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}

	@GetMapping(value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String division() throws ODataException, Exception {
		List<DivisionFilter> list = filterService.getDivisionFilter();

		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}

	@GetMapping(value = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String department() throws ODataException, Exception {
		List<DepartmentFilter> list = filterService.getDepartmentFilter();

		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}

	@GetMapping(value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String position() throws ODataException, Exception {
		List<PositionFilter> list = filterService.getPositionFilter();

		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}

	@GetMapping(value = "/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String rating() throws ODataException, Exception {
		List<RatingFilter> list = filterService.getRatingFilter();
		
		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}	

	@GetMapping(value = "/caseids", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String caseId() {
		List<String> list = candidateService.findDistinctCaseId();

		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(list);
	}
}