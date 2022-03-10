package com.sap.sfsf.reshuffle.simulation.backend.services;

import com.sap.sfsf.reshuffle.simulation.backend.config.PlaceholderConfig;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailBodyService {
	private Logger LOG = LoggerFactory.getLogger(MailBodyService.class);
	
	// default placeholder values when corresponding column values aren't defined.
	// would like to define them at placeholder.properties. but couldn't use japanese letters there.
	private static String[] PLACEHOLDER_DEFAULT_VALUES_WHEN_NOT_DEFINED = {
			"(候補者ID 設定なし)",
			"(候補者名 設定なし)",
			"(現在の事業部 設定なし)",
			"(現在の部門 設定なし)",
			"(現在のポジション 設定なし)",
			"(現在の等級 設定なし)",
			"(次の事業部 設定なし)",
			"(次の部門 設定なし)",
			"(次のポジション 設定なし)",
			"(次の等級 設定なし)"
	};

	@Autowired
	ConfigService configService;
	
	@Autowired
	private CandidateService candidateService;
	
	@Autowired
	private PlaceholderConfig placeholderConfig;
	
	public String generateMailBody(Candidate candidate) {
		String mailTemplate = configService.getMailTemplate();
		String[] placeholderNames = placeholderConfig.fetchPlaceholderNames();
		String[] candidateParamValues = this.fetchCandidateParamValues(candidate);
		
		String mailBody = this.replacePlaceholderWithActualValue(mailTemplate, placeholderNames, candidateParamValues);
		LOG.debug("mail body for " + candidateParamValues[1] + " is: " + mailBody);
		
		return mailBody;
	}

	private String[] fetchCandidateParamValues(Candidate candidate) {
		String caseID = candidate.getCaseID();
		String candidateID = candidate.getCandidateID();
		Candidate candidateFromDB = candidateService.findByCaseidAndCandidateidIn(caseID, candidateID);

		String[] candidateParamValues = {
				candidateFromDB.getCandidateID(),
				candidateFromDB.getCandidateName(),
				candidateFromDB.getCandidateDivisionID(),
				candidateFromDB.getCandidateDepartmentName(),
				candidateFromDB.getCandidatePositionName(),
				candidateFromDB.getCandidateJobGradeName(),
				candidateFromDB.getDivisionName(),
				candidateFromDB.getDepartmentName(),
				candidateFromDB.getPositionName(),
				candidateFromDB.getJobGradeName()
		};
	
		return candidateParamValues;
	}
	
	private String replacePlaceholderWithActualValue(String mailTemplate, String[] placeholderNames, String[] candidateParamValues) {
		String mailBody = mailTemplate;
		for(int i=0; i<placeholderNames.length; i++) {
			if(candidateParamValues[i] == null) {
				candidateParamValues[i] = this.PLACEHOLDER_DEFAULT_VALUES_WHEN_NOT_DEFINED[i];
			}
			mailBody = mailBody.replace(placeholderNames[i], candidateParamValues[i]);
		}
		
		return mailBody;
	}
}