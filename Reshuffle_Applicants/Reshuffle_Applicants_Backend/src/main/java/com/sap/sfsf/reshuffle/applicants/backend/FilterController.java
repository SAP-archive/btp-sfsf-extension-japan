package com.sap.sfsf.reshuffle.applicants.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.BusinessUnitFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CompanyFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DepartmentFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DivisionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.PositionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.services.FilterService;

@Controller
public class FilterController {
	@Autowired
	FilterService filterService;

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
}
