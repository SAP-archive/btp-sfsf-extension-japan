package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextPositionReqParamsValidator extends Validator {

	private boolean hasDivision = false;
	private boolean hasDepartment = false;
	private boolean hasCaseId = false;

	private static final String COMPANY = "company";
	private static final String BUSINESSUNIT = "businessUnit";
	private static final String DIVISION = "division";
	private static final String DEPARTMENT = "department";
	private static final String POSITION = "position";
	private static final String RETIREMENT = "retirement";
	private static final String CASEID = "caseId";

	private static int STRUL = 50;
	private static int STRLL = 1;
	
	private static final String LENGTHERRMSG = " length is not between " + STRLL + "to " + STRUL;
	private static final String MANDTERRMSG = " is mandatory";

	public NextPositionReqParamsValidator(Map<String, String> params) {

		params.forEach((k, v) ->{
			int length =  v.length();
			
			switch (k) {
            case CASEID:
				setHasCaseId(true);
				break;
			case COMPANY:
				if(length < STRLL || length > STRUL)
					addErrorList(COMPANY + LENGTHERRMSG);
				break;
			
			case BUSINESSUNIT:
				if(length < STRLL || length > STRUL)
					addErrorList(BUSINESSUNIT + LENGTHERRMSG);
				break;
				
			case DIVISION:
				if(length < STRLL || length > STRUL)
					addErrorList(DIVISION + LENGTHERRMSG);
				setHasDivision(true);
				break;

			case DEPARTMENT:
				if(length < STRLL || length > STRUL)
					addErrorList(DEPARTMENT + LENGTHERRMSG);
				setHasDepartment(true);
				break;
				
			case POSITION:
				if(length < STRLL || length > STRUL)
					addErrorList(POSITION + LENGTHERRMSG);
				break;
				
			case RETIREMENT:
			    String regexRetire = "^(yes|no){1}$" ;
			    Pattern pRetire = Pattern.compile(regexRetire);
			    Matcher mRetire = pRetire.matcher(v); 
			    boolean resultRetire = mRetire.matches();
			    if(resultRetire != true)
			    	addErrorList(RETIREMENT + " is not yes, or no");
				break;
				
			default:
				addErrorList(k + " is not a filter key");
			}
		});

		if(hasCaseId != true) {
			if(hasDivision != true) addErrorList(DIVISION + MANDTERRMSG);
			if(hasDepartment != true) addErrorList(DEPARTMENT + MANDTERRMSG);
		}
	}
	
	private void setHasDivision(boolean bool) {
		this.hasDivision = bool;
	}
	
	private void setHasDepartment(boolean bool) {
		this.hasDepartment = bool;
	}

	private void setHasCaseId(boolean bool) {
		this.hasCaseId = bool;
	}

	public boolean hasCaseId() {
		return this.hasCaseId;
	}
}
