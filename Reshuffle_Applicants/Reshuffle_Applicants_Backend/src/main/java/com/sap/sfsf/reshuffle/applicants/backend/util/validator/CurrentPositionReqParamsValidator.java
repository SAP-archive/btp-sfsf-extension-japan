package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentPositionReqParamsValidator extends Validator {
	
	private boolean hasDivision = false;
	private boolean hasDepartment = false;
	
	private static final String COMPANY = "company";
	private static final String BUSINESSUNIT = "businessUnit";
	private static final String DIVISION = "division";
	private static final String DEPARTMENT = "department";
	private static final String POSITION = "position";
	private static final String TENURELL = "tenureLL";
	private static final String TENUREUL = "tenureUL";
	private static final String WILLINGNESS = "willingness";

	private static int STRUL = 50;
	private static int STRLL = 1;
	
	private static final String LENGTHERRMSG = " length is not between " + STRLL + "to " + STRUL;
	private static final String MANDTERRMSG = " is mandatory";

	public CurrentPositionReqParamsValidator(Map<String, String> params) {

		params.forEach((k, v) ->{
			int length =  v.length();
			
			switch (k) {			
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
				
			case TENURELL:
			    String regexLL = "^(([1-9]\\d{0,3})|0)$" ;
			    Pattern pLL = Pattern.compile(regexLL);
			    Matcher mLL = pLL.matcher(v); 
			    boolean resultLL = mLL.matches();
			    if(resultLL != true)
			    	addErrorList(TENURELL + " is not between 0 and 9999");
				break;
				
			case TENUREUL:
			    String regexUL = "^[1-9]\\d{0,3}$" ;
			    Pattern pUL = Pattern.compile(regexUL);
			    Matcher mUL = pUL.matcher(v); 
			    boolean resultUL = mUL.matches();
			    if(resultUL != true)
			    	addErrorList(TENUREUL + " is not between 1 and 9999");
				break;
				
			case WILLINGNESS:
			    String regexWill = "^(yes|no|-){1}$" ;
			    Pattern pWill = Pattern.compile(regexWill);
			    Matcher mWill = pWill.matcher(v); 
			    boolean resultWill = mWill.matches();
			    if(resultWill != true)
			    	addErrorList(WILLINGNESS + " is not yes, no, or -");
				break;

			default:
				addErrorList(k + " is not a filter key");
			}
		});
		
		if(hasDivision != true) addErrorList(DIVISION + MANDTERRMSG);
		if(hasDepartment != true) addErrorList(DEPARTMENT + MANDTERRMSG);
 
	}
	
	private void setHasDivision(boolean bool) {
		this.hasDivision = bool;
	}
	
	private void setHasDepartment(boolean bool) {
		this.hasDepartment = bool;
	}
}
