package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import com.sap.cloud.sdk.result.ElementName;

public class CompanyFilter {
	@ElementName("externalCode")
	private String externalCode;
	
	@ElementName("startDate")
	private String startDate;
	
	@ElementName("endDate")
	private String endDate;
	
	@ElementName("country")
	private String country;
	
	@ElementName("name_ja_JP")
	private String name;
}
