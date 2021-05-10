package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;

public class MyDivision {
	@ElementName("externalCode")
	private String externalCode;
	@ElementName("startDate")
	private String startDate;
	@ElementName("endDate")
	private String endDate;
	@ElementName("name_ja_JP")
	private String name;
	
	@ElementName("cust_toBusinessUnit")
	private MyBusinessUnit.MyBusinessUnitContainer businessUnit;
	
	public static class MyDivisionContainer{
		@ElementName("results")
		private List<MyDivision> divisionList;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
