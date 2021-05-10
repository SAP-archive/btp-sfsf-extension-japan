package com.sap.sfsf.reshuffle.applicants.backend.model.details;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class UserDetails {
	@ElementName("userId")
	private String userId;
	
	@ElementName("firstName")
	private String firstName;
	
	@ElementName("lastName")
	private String lastName;

}
