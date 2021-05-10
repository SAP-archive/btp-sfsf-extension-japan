package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.Map;

public class ExportReqParamsValidator extends Validator {

	private static final String FILENAMEPREFIX = "filename";
	
	private static int STRUL = 50;
	private static int STRLL = 1;
	
	private static final String LENGTHERRMSG = " length is not between " + STRLL + "to " + STRUL;
	
	public ExportReqParamsValidator(Map<String, String> params) {

		params.forEach((k, v) ->{
			int length =  v.length();
			
			switch (k) {
			case FILENAMEPREFIX:
				if(length < STRLL || length > STRUL)
					addErrorList(FILENAMEPREFIX + LENGTHERRMSG);
				break;
			
			default:
				addErrorList(k + " is not a filter key");
			}
		});
	}
}
