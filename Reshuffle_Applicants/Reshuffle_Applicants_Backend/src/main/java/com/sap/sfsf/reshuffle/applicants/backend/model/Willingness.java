package com.sap.sfsf.reshuffle.applicants.backend.model;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class Willingness {
	@ElementName("User")
	private String userId;
	
	@ElementName("cust_willingness")
	private boolean willingness;
	
}
