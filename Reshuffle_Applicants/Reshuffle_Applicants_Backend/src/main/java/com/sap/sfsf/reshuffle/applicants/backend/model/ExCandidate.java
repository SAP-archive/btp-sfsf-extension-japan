package com.sap.sfsf.reshuffle.applicants.backend.model;

import lombok.Data;

@Data
public class ExCandidate extends Candidate {
	private String photo;
	
    //@Column(name = "WILLINGNESS")
    private String willingness;

	//for GET /candidates
	public ExCandidate(String empId, String empName, String empRetire,
			String department, String division, String managerId, String position, String jobGrade, 
			String departmentName, String divisionName, String positionName,  String jobGradeName,
			int jobTenure, String willingness, String rating1, String rating2, String rating3, String photo) {
		setCurrentEmpId(empId);
		setCurrentEmpName(empName);
		setCurrentEmpRetire(empRetire);
		setCurrentDepartment(department);
		setCurrentDivision(division);
		setCurrentManager(managerId);
		setCurrentPosition(position);
		setCurrentDepartmentName(departmentName);
		setCurrentDivisionName(divisionName);
		setCurrentPositionName(positionName);
		setJobTenure(jobTenure);
		setCurrentJobGrade(jobGrade);
		setCurrentJobGradeName(jobGradeName);
		setWillingness(willingness);
		setRating1(rating1);
		setRating2(rating2);
		setRating3(rating3);
		setPhoto(photo);
	}
}