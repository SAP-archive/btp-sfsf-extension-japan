package com.sap.sfsf.reshuffle.simulation.backend.model;

import com.sap.cloud.sdk.result.ElementName;

public class MyRating {

	@ElementName("userId")
	private String userId;
	@ElementName("rating")
	private String rating;
	@ElementName("startDate")
	private String startDate;
	@ElementName("endDate")
	private String endDate;
	
	@ElementName("lastModified")
	private String lastModified;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	
}
