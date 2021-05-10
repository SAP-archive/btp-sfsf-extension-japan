package com.sap.sfsf.reshuffle.applicants.backend.model.details;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class PayGradeDetails {
	@ElementName("paygradeLevel")
	private String paygradeLevel;
	
	@ElementName("name")
	private String name;
}
