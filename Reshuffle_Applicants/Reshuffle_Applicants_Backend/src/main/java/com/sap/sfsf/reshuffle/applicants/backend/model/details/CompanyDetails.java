package com.sap.sfsf.reshuffle.applicants.backend.model.details;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class CompanyDetails {
	@ElementName("country")
	private String country;

	@ElementName("name_ja_JP")
	private String name_ja_JP;

}
