package com.sap.sfsf.reshuffle.simulation.backend.model;

import com.sap.cloud.sdk.result.ElementName;

public class MyDisciplinary {
	@ElementName("User")
	private String user;
	@ElementName("cust_Reason")
	private String reason;
	@ElementName("cust_Severity")
	private String severity;
	@ElementName("cust_dateofincident")
	private String dateOfIncident;
	@ElementName("cust_AffectedEmployee")
	private String affectedEmployee;
	@ElementName("cust_IncidentStatus")
	private String incidentStatus;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getDateOfIncident() {
		return dateOfIncident;
	}
	public void setDateOfIncident(String dateOfIncident) {
		this.dateOfIncident = dateOfIncident;
	}
	public String getAffectedEmployee() {
		return affectedEmployee;
	}
	public void setAffectedEmployee(String affectedEmployee) {
		this.affectedEmployee = affectedEmployee;
	}
	public String getIncidentStatus() {
		return incidentStatus;
	}
	public void setIncidentStatus(String incidentStatus) {
		this.incidentStatus = incidentStatus;
	}

	
}
