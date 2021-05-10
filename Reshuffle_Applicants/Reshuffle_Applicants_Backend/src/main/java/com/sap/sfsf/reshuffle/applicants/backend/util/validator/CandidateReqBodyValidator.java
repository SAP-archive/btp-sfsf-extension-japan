package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;

public class CandidateReqBodyValidator extends Validator {

	private static final String CANDIDATEID = "candidateID";
	private static final String NEXTPOSITION = "nextPosition";
	
	private static int STRUL = 50;
	private static int STRLL = 1;
	
	public CandidateReqBodyValidator(Candidate candidate) {
		String candidateId = candidate.getCandidateID();
		String nextPosition = candidate.getNextPosition();
	
		
		if(candidateId == null || candidateId.equals("")) {
			addErrorList(CANDIDATEID + " is mandatory");
		} else {
			int length = candidateId.length();
			if(length < STRLL || length > STRUL)
				addErrorList(CANDIDATEID + " length is not between " + STRLL + " to " + STRUL);
		}
		
		if(nextPosition == null || nextPosition.equals("")) {
			addErrorList(NEXTPOSITION + " is mandatory");
		} else {
			int length = nextPosition.length();
			if(length < STRLL || length > STRUL)
				addErrorList(NEXTPOSITION + " length is not between " + STRLL + " to " + STRUL);
		}
	}
}
