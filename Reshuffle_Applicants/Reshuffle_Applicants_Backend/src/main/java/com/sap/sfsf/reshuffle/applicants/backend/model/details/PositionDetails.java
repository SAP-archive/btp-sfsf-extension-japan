package com.sap.sfsf.reshuffle.applicants.backend.model.details;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class PositionDetails {
	@ElementName("externalName_ja_JP")
	private String externalName_ja_JP;

}
