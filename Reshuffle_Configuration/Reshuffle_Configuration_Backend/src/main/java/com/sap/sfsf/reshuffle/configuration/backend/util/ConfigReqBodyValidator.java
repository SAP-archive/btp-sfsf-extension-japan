package com.sap.sfsf.reshuffle.configuration.backend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class ConfigReqBodyValidator extends Validator {
	
	public ConfigReqBodyValidator(JSONObject jsonObj) {
		Utils utils = new Utils();
		String strDate = utils.getOptinalStringField(jsonObj, "startDateTime");
		int span = utils.getOptionalIntField(jsonObj, "span");
		// these are unused now...
		// String rateFormKey1 = utils.getOptinalStringField(jsonObj, "rateFormKey1");
		// String rateFormKey2 = utils.getOptinalStringField(jsonObj, "rateFormKey2");
		// String rateFormKey3 = utils.getOptinalStringField(jsonObj, "rateFormKey3");
		// String mailTemplate = utils.getOptinalStringField(jsonObj, "mailTemplate");
		String presidentName = utils.getOptinalStringField(jsonObj, "presidentName");
		int compentecyThreshold = utils.getOptionalIntField(jsonObj, "competencyThreshold");
		
	    String regexDate = "^\\d{4}/\\d{2}/\\d{2}$" ;
	    Pattern pDate = Pattern.compile(regexDate);
	    Matcher mDate = pDate.matcher(strDate); 
	    boolean resultDate = mDate.matches();
	    if(resultDate != true)
	    	addErrorList("startDate is not valid");

	    if(span < 1 || span > 9999) {
	    	addErrorList("span must be between 1 to 9999");
	    }
	    
	    if(compentecyThreshold < 0 || compentecyThreshold > 5) {
	    	addErrorList("compentecy threshold must be between 0 and 5");
	    }
	    
	    if(presidentName.length() < 1 || presidentName.length() > 32) {
	    	addErrorList("presidentName must be between 1 and 32");
	    }
 	}
}
