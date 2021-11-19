package com.sap.sfsf.reshuffle.applicants.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CONFIG")
public class Config {
	@Column(name = "STARTDATETIME")
	private Date startDateTime;
	
	@Id
	@Column(name = "SPAN")
	private int span;
	
	@Column(name = "RATEFORMKEY1")
	private String rateFormKey1;
	
	@Column(name = "RATEFORMKEY2")
	private String rateFormKey2;
	
	@Column(name = "RATEFORMKEY3")
	private String rateFormKey3;

	@Column(name = "COMPETENCYTHRESHOLD")
	private int competencyThreshold;
}
