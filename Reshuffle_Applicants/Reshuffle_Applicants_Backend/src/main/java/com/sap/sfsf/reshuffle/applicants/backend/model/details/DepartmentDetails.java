package com.sap.sfsf.reshuffle.applicants.backend.model.details;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class DepartmentDetails {
	@ElementName("description_ja_JP")
	private String description_ja_JP;
	
}
