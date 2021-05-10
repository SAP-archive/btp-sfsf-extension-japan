package com.sap.sfsf.reshuffle.simulation.backend.model;

import com.sap.cloud.sdk.result.ElementName;

public class MyEmpJob {
	@ElementName("userId")
	private String userId;
	@ElementName("position")
	private String position;
	@ElementName("department")
	private String department;
	
	@ElementName("startDate")
	private String startDate;
	@ElementName("endDate")
	private String endDate;
	@ElementName("event")
	private String event;
	@ElementName("eventReason")
	private String eventReason;
	@ElementName("positionNav")
	private MyPosition positionNav;

	public String getDepartment() {
		return department;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getEvent() {
		return event;
	}

	public String getEventReason() {
		return eventReason;
	}

	public String getPosition() {
		return position;
	}

	public MyPosition getPositionNav() {
		return positionNav;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setPositionNav(MyPosition positionNav) {
		this.positionNav = positionNav;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
