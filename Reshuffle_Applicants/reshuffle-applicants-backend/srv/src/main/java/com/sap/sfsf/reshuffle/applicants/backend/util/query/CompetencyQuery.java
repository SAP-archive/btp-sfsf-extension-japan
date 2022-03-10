package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.sfsf.vdm.namespaces.positionentity.PositionEntity;
import com.sap.sfsf.vdm.namespaces.positionentity.field.PositionEntityField;

public class CompetencyQuery {

    private String currentPositionID;
    private final String OPERAND_EQ = "[\\s]*eq[\\s]*";

    public void validateParameters(Map<String, String> parameters) throws ServiceException {
        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern currentPositionIDPattern = Pattern.compile("currentPositionID" + OPERAND_EQ + "'[\\S]*'");
        Matcher currentPositionIDMatcher = currentPositionIDPattern.matcher(filterQuery);

        while (currentPositionIDMatcher.find())
            currentPositionID = currentPositionIDMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        if (currentPositionID == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The parameter currentPositionID is not set.");
    }

    public ExpressionFluentHelper<PositionEntity> createPositionEntityFilter() {
        return new PositionEntityField<String>("positionNav/code").eq(currentPositionID);
    }

    public String getCurrentPosition() {
        return currentPositionID;
    }
}
