package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class RatingFilter {
	@ElementName("formTemplateId")
	private String formTemplateId;
	
	@ElementName("formTemplateName")
	private String formTemplateName;
}
