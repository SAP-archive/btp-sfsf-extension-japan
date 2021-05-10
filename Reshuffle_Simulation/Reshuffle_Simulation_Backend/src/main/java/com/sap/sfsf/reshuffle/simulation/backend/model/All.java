package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.List;

public class All {

	String checkResult;
	
	List<MyDivision> divisionList;
	List<MyDepartment> departmentList;
	List<MyPosition> positionList;

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public List<MyDivision> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<MyDivision> divisionList) {
		this.divisionList = divisionList;
	}

	public List<MyDepartment> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<MyDepartment> departmentList) {
		this.departmentList = departmentList;
	}

	public List<MyPosition> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<MyPosition> positionList) {
		this.positionList = positionList;
	}
	
}
