package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class DivisionFilter {
	@ElementName("externalCode")
	private String externalCode;
	
	@ElementName("startDate")
	private String startDate;
	
	@ElementName("endDate")
	private String endDate;
	
	@ElementName("name_ja_JP")
	private String name;
	
	@ElementName("cust_toBusinessUnit")
	private BusinessUnitFilter.BusinessUnitFilterContainer businessUnit;
	
	public static class DivisionFilterContainer {
		@ElementName("results")
		private List<DivisionFilter> divisionList = null;
	}
}
