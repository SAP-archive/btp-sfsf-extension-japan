package com.sap.sfsf.reshuffle.simulation.backend.model;

import com.sap.cloud.sdk.result.ElementName;

public class MyPerGlobalInfoJPN {

	@ElementName("personIdExternal")
	private String personIdExternal;
	
	@ElementName("customString20")
	private String customString20;
	
	@ElementName("customString13")
	private String customString13;

	public String getPersonIdExternal() {
		return personIdExternal;
	}

	public void setPersonIdExternal(String personIdExternal) {
		this.personIdExternal = personIdExternal;
	}

	public String getCustomString20() {
		return customString20;
	}

	public void setCustomString20(String customString20) {
		this.customString20 = customString20;
	}

	public String getCustomString13() {
		return customString13;
	}

	public void setCustomString13(String customString13) {
		this.customString13 = customString13;
	}

	
}
