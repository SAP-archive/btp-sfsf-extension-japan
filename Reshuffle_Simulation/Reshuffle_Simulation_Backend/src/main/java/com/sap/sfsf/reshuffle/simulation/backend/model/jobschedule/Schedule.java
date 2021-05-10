package com.sap.sfsf.reshuffle.simulation.backend.model.jobschedule;

public class Schedule {
	public Schedule(String startDT, String payload) {
		this.time = "00am";
		this.active = true;
		this.data = payload;
		this.startTime = new StartTime(startDT);
	}
	private String time;
	private boolean active;
	private StartTime startTime;
	private String data;
}
