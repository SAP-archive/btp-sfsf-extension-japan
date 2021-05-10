package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.Date;

import com.sap.cloud.sdk.result.ElementName;

public class MyEmpJobUpsert {
	private String userId;
	private Date startDate;
	private String event;
	private String eventReason;

	private Metadata __metadata;
	
	public MyEmpJobUpsert() {
		this.__metadata = new Metadata();
		this.event = "3681";
		this.eventReason = "TransNoPay";
	}
	
	public MyEmpJobUpsert(String userId, Date startDate) {
		this.__metadata = new Metadata();
		this.userId = userId;
		this.startDate = startDate;
		this.event = "3681";
		this.eventReason = "TransNoPay";
	}
	
	
	public class Metadata{
		@ElementName("uri")
		private String uri;
		
		public Metadata() {
			this.uri = "EmpJob";
		}
	}
	
	public void setMetadata(Metadata metadata) {
		this.__metadata = metadata;
	}
	
	public Metadata getMetadata() {
		return this.__metadata;
	}
	
	public String getEvent() {
		return event;
	}

	public String getEventReason() {
		return eventReason;
	}


	public Date getStartDate() {
		return startDate;
	}

	public String getUserId() {
		return userId;
	}



	public void setEvent(String event) {
		this.event = event;
	}

	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
