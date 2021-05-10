package com.sap.sfsf.reshuffle.simulation.backend.model.jobschedule;

public class StartTime {
	private String date;
	private String format;
	
	public StartTime(String date) {
		this.date = date;
		this.format="YYYYMMDDHHmm";
	}
}