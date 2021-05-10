package com.sap.sfsf.reshuffle.simulation.backend.model.jobschedule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyJob {
	private Integer _id;
	private String name;
	private String description;
	private String action;
	private boolean active;
	private String httpMethod;
	private List<Schedule> schedules;

	public MyJob(String api, String startDT, String payload) {
		this.name = "emailJob";
		this.description ="Email Job";
		this.action = api;
		this.active = true;
		this.httpMethod = "POST";
		List<Schedule> list = new ArrayList<Schedule>();
		list.add(new Schedule(startDT, payload));
		this.schedules = list;
	}
	
	public MyJob() {
	}

	public Schedule createSchedule(String startDT, String payload) {
		return new Schedule(startDT, payload);
	}


	
	
	@JsonProperty("_id")
	public Integer getId() {
		return _id;
	}

	public void setId(Integer _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	
	/* @formatter:off
	{
	  "name": "validateSalesOrder",
	  "description": "cron job that validates sales order requests",
	  "action": "http://salesOrderApp.hana.ondemand.com:40023/salesOrders/validate",
	  "active": true,
	  "httpMethod": "PUT",
	  "schedules": [
	    {
	      "cron": "* * * * * * /10 0",
	      "description": "this schedule runs every 10 minutes",
	      "data": {
	        "salesOrderId": "1234"
	      },
	      "active": true,
	      "startTime": {
	        "date": "2015-10-20 04:30 +0000",
	        "format": "YYYY-MM-DD HH:mm Z"
	      }
	    }
	  ]
	}
	  @formatter:on
	 */

}
