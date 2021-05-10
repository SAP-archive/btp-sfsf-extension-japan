package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class PositionFilter {
	@ElementName("code")
	private String code;
	
	@ElementName("effectiveStartDate")
	private String effectiveStartDate;
	
	@ElementName("effectiveEndDate")
	private String effectiveEndDate;
	
	@ElementName("parentPosition")
	private PositionFilter parentPosition;
	
	@ElementName("externalName_ja_JP")
	private String name;
	
	@ElementName("company")
	private String company;
	
	@ElementName("businessUnit")
	private String businessUnit;
	
	@ElementName("division")
	private String division;
	
	@ElementName("department")
	private String department;
}