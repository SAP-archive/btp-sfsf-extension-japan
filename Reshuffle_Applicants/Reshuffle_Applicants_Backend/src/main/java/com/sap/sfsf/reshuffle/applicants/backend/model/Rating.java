package com.sap.sfsf.reshuffle.applicants.backend.model;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class Rating {
	@ElementName("formSubjectId")
	private String userId;
	
	@ElementName("rating")
	private String rating;
	
	@ElementName("formTitle")
	private String formTitle;
	
	@ElementName("formTemplateId")
	private String formTemplateId;
}
