package com.sap.sfsf.reshuffle.configuration.backend.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

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
	
	private static final String NAIVEFORMAT = "yyyy/MM/dd";
	private static final String JPFORMAT = "yyyy年MM月dd日";
	
	public JSONObject getFixedDateConfig() throws ParseException {
		String dateFormatSeed = NAIVEFORMAT;
		return getJsonConfig(dateFormatSeed);
	}
	
	public JSONObject getFixedDateConfig(String locale) throws ParseException {
		String dateFormatSeed = locale == "jp"? JPFORMAT: NAIVEFORMAT;
		return getJsonConfig(dateFormatSeed);
	}
	
	private JSONObject getJsonConfig(String dateFormatSeed) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatSeed);
		String str = dateFormat.format(startDateTime);
		
		JSONObject json = new JSONObject();
		json.put("startDateTime", str);
		json.put("span", this.span);
		json.put("rateFormKey1", this.rateFormKey1);
		json.put("rateFormKey2", this.rateFormKey2);
		json.put("rateFormKey3", this.rateFormKey3);
		
		return json;
	}
	
	public void setUnFixedStartDateTime(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = dateFormat.parse(str);
		this.startDateTime = date;
		
		return;
	}

	public JSONObject getFixedJpDateConfig() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		
		String str = dateFormat.format(startDateTime);
		
		JSONObject json = new JSONObject();
		json.put("startDateTime", str);
		json.put("span", this.span);
		
		return json;
	}
}
