package com.sap.sfsf.reshuffle.applicants.backend.model;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class Photo {
	@ElementName("userId")
	private String userId;
	
	@ElementName("photo")
	private String photo;
	
	@ElementName("mimeType")
	private String mimeType;
}
