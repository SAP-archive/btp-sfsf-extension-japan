package com.sap.sfsf.reshuffle.configuration.backend.model;

import lombok.Data;

import com.sap.cloud.sdk.result.ElementName;

@Data
public class RateForm {
	@ElementName("formTemplateId")
	private String formTemplateId;
	
	@ElementName("formTemplateName")
	private String formTemplateName;
}