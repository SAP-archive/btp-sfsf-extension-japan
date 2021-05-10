package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class DepartmentFilter {
	@ElementName("externalCode")
	private String externalCode;
	
	@ElementName("startDate")
	private String startDate;
	
	@ElementName("endDate")
	private String endDate;
	
	@ElementName("name_ja_JP")
	private String name;
	
	@ElementName("cust_toDivision")
	private DivisionFilter.DivisionFilterContainer division;
}
