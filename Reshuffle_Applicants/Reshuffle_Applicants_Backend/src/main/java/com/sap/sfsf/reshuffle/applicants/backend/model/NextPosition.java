package com.sap.sfsf.reshuffle.applicants.backend.model;

import lombok.Data;

@Data
public class NextPosition extends Candidate {
	
	private String photo;

	//for GET /nextpositions
	public NextPosition(String empId, String empName, String isRetire,
			String division, String department, String managerId, String position, String jobGrade,
			String divisionName, String departmentName, String positionName, String jobGradeName, String photo){
		setCurrentEmpId(empId);
		setCurrentEmpName(empName);
		setCurrentEmpRetire(isRetire);
		setNextDivision(division);
		setNextDepartment(department);
		setNextManager(managerId);
		setNextPosition(position);
		setNextJobGrade(jobGrade);
		setNextDivisionName(divisionName);
		setNextDepartmentName(departmentName);
		setNextPositionName(positionName);
		setNextJobGradeName(jobGradeName);
		setPhoto(photo);
	};
}
