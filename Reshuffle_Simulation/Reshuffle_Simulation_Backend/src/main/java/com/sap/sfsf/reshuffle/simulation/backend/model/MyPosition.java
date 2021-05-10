package com.sap.sfsf.reshuffle.simulation.backend.model;

import com.sap.cloud.sdk.result.ElementName;

public class MyPosition {
	@ElementName("code")
	private String code;
	
	@ElementName("effectiveStartDate")
	private String effectiveStartDate;
	
	@ElementName("effectiveEndDate")
	private String effectiveEndDate;
	
	@ElementName("parentPosition")
	private MyPosition parentPosition;
	
	@ElementName("division")
	private String division;
	
	@ElementName("department")
	private String department;
	
	@ElementName("company")
	private String company;
	
	@ElementName("businessUnit")
	private String businessUnit;
	
	@ElementName("externalName_ja_JP")
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MyPosition getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(MyPosition parentPosition) {
		this.parentPosition = parentPosition;
	}

	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeparment() {
		return department;
	}

	public void setDeparment(String department) {
		this.department = department;
	}
	
	
}
