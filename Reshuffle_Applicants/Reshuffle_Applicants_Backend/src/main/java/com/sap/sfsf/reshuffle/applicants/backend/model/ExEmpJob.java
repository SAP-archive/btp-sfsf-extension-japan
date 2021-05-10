package com.sap.sfsf.reshuffle.applicants.backend.model;

import java.util.Date;

import com.sap.cloud.sdk.result.ElementName;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.CompanyDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DepartmentDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DivisionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PayGradeDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PositionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.UserDetails;

import lombok.Data;

@Data
public class ExEmpJob {
	@ElementName("userId")
	private String userId;
	
	@ElementName("startDate")
	private Date startDate;
	
	@ElementName("endDate")
	private Date endDate;
	
	@ElementName("event")
	private String event;
	
	@ElementName("division")
	private String division;
	
	@ElementName("managerId")
	private String managerId;
	
	@ElementName("position")
	private String position;
	
	@ElementName("department")
	private String department;
	
	@ElementName("payGrade")
	private String payGrade;
	
	@ElementName("userNav")
	private UserDetails userDetails = null;
	
	@ElementName("positionNav")
	private PositionDetails positionDetails = null;
	
	@ElementName("divisionNav")
	private DivisionDetails divisionDetails = null;
	
	@ElementName("departmentNav")
	private DepartmentDetails departmentDetails = null;
	
	@ElementName("companyNav")
	private CompanyDetails companyDetails = null;
	
	@ElementName("payGradeNav")
	private PayGradeDetails payGradeDetails = null;
	
}
