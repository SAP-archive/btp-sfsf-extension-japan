package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONFIG")
public class Config {
	@Id
	@Column(name="startdatetime")
	private Date startDateTime;


	public Config() {
		
	}
	
	public Date getStartDateTime() {
		return startDateTime;
	}


	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

}
