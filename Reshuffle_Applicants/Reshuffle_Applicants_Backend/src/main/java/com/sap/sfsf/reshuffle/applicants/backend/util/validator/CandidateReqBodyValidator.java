package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.List;

import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;

public class CandidateReqBodyValidator extends Validator {

	private static final String CASEID = "caseID";
	private static final String CANDIDATEID = "candidateID";
	private static final String DEPARTMENT = "nextPosition";
	
	private static int STRUL = 50;
	private static int STRLL = 1;

	private boolean isCaseIdDuplicated = false;
	
	public CandidateReqBodyValidator(Candidate candidate, List<String> caseIds) {
		String caseID = candidate.getCaseID();
		String candidateId = candidate.getCandidateID();
		String nextPosition = candidate.getNextPosition();
	
		int caseIdLength = caseID.length();
		if(caseID == null || caseID.equals("")) {
			addErrorList(CASEID + " is mandatory");
		} else if(caseIdLength < STRLL || caseIdLength > STRUL ) {
				addErrorList(CASEID + " length is not between " + STRLL + " to " + STRUL);
		} else {
			if (caseIds != null) {
				caseIds.forEach(existingCaseId -> {
					if (existingCaseId.equals(caseID)) {
						addErrorList(caseID.toString() + " is deplicated");
						isCaseIdDuplicated = true;
					}
				});
			}
		}
		
		int candidateIdLength = candidateId.length();
		if(candidateId == null || candidateId.equals("")) {
			addErrorList(CANDIDATEID + " is mandatory");
		} else if(candidateIdLength < STRLL || candidateIdLength > STRUL ) {
			addErrorList(CANDIDATEID + " length is not between " + STRLL + " to " + STRUL);
		}
		
		int nextPositionLength = nextPosition.length();
		if(nextPosition == null || nextPosition.equals("")) {
			addErrorList(DEPARTMENT + " is mandatory");
		} else if(nextPositionLength < STRLL || nextPositionLength > STRUL) {
			addErrorList(DEPARTMENT + " length is not between " + STRLL + " to " + STRUL);
		}
	}

	public boolean isCaseIdDuplicated() {
		return this.isCaseIdDuplicated;
	}
}
