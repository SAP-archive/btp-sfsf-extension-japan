package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;

public class CandidateQuery {

    private String caseId;
    private final String OPERAND_EQ = "[\\s]*eq[\\s]*";

    public void validateParameters(Map<String, String> parameters) throws ServiceException {
        // For PUT requests, skip validation because there are no parameters.
        if (parameters.isEmpty()) {
            caseId = null;
            return;
        }

        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern caseIdPattern = Pattern.compile("caseID" + OPERAND_EQ + "'[\\S]*'");
        Matcher caseIdMatcher = caseIdPattern.matcher(filterQuery);

        while (caseIdMatcher.find())
            caseId = caseIdMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        if (caseId == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The parameter caseID is not set.");
    }

    public String getCaseId() {
        return caseId;
    }
}
