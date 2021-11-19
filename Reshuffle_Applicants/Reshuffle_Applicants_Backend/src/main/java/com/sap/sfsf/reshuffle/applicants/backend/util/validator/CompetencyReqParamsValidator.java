package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.Map;

public class CompetencyReqParamsValidator extends Validator {

    private boolean hasPosition = false;

    private static final String POSITION = "currentPosition";

	private static int STRUL = 50;
    private static int STRLL = 1;
    
	private static final String LENGTHERRMSG = " length is not between " + STRLL + "to " + STRUL;
	private static final String MANDTERRMSG = " is mandatory";
    
    public CompetencyReqParamsValidator(Map<String, String> params) {
        
        params.forEach((k, v) -> {
            int length = v.length();

            switch (k) {
                case POSITION:
                    if (length < STRLL || length > STRUL)
                        addErrorList(POSITION + LENGTHERRMSG);
                    setHasPosition(true);
                    break;
                default:
                    addErrorList(k + " is not a filter key");
                
            }
        });

        if(hasPosition != true) addErrorList(POSITION + MANDTERRMSG);
    }

    private void setHasPosition(boolean bool) {
        this.hasPosition = bool;
    }
}