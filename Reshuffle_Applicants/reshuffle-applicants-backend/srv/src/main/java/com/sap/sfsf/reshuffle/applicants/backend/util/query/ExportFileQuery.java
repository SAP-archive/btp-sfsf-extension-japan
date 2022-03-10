package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;

import cds.gen.Content;

public class ExportFileQuery {

    private String filename;
    private List<Content> contentList;

    protected final String OPERAND_EQ = "[\\s]*eq[\\s]*";

    public void validateParameter(Map<String, String> parameters) {
        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern filernamePattern = Pattern.compile("filename" + OPERAND_EQ + "'[\\S]*'");
        Matcher filernameMatcher = filernamePattern.matcher(filterQuery);

        while (filernameMatcher.find())
            filename = filernameMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        String contentQuery = parameters.getOrDefault("contents", null);

        if (contentQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no contents.");

        Pattern contentPattern = Pattern.compile("\\(candidateID" + OPERAND_EQ + "'[\\S]*'\\)"
                + "and\\(positionID" + OPERAND_EQ + "'[\\S]*'\\)"
                + "and\\(incumbentEmpID" + OPERAND_EQ + "'[\\S]*'\\)");
        Matcher contentMatcher = contentPattern.matcher(contentQuery);

        contentList = new ArrayList<>();

        while (contentMatcher.find()) {
            String[] contentArray = contentMatcher.group().split(OPERAND_EQ);
            String candidateId = contentArray[1].split("\\)")[0].replaceAll("'", "");
            String positionId = contentArray[2].split("\\)")[0].replaceAll("'", "");
            String incumbentEmpId = contentArray[3].split("\\)")[0].replaceAll("'", "");

            Content content = Content.create();
            content.setCandidateID(candidateId);
            content.setPositionID(positionId);
            content.setIncumbentEmpID(incumbentEmpId);

            contentList.add(content);
        }
    }

    public String getFilename() {
        return filename;
    }

    public List<Content> getContentList() {
        return contentList;
    }
}
