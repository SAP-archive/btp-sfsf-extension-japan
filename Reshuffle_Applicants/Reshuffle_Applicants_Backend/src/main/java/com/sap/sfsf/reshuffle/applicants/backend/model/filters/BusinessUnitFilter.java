package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;

public class BusinessUnitFilter {
	@ElementName("externalCode")
	private String externalCode;
	
	@ElementName("startDate")
	private String startDate;
	
	@ElementName("endDate")
	private String endDate;
	
	@ElementName("name_ja_JP")
	private String name;
	
	public static class BusinessUnitFilterContainer {
		@ElementName("results")
		private List<BusinessUnitFilter> businessUnitList = null;
	}
}
