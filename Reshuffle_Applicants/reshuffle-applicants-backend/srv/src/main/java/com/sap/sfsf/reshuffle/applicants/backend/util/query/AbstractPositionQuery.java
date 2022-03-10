package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.sfsf.vdm.namespaces.position.Position;

public abstract class AbstractPositionQuery {

    protected String companyId;
    protected String businessUnitId;
    protected String divisionId;
    protected String departmentId;
    protected String positionId;

    protected Boolean hasParamForEmpJob;

    protected final String OPERAND_EQ = "[\\s]*eq[\\s]*";

    public void validateParameters(Map<String, String> parameters) throws ServiceException {
        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern companyPattern = Pattern.compile("companyID" + OPERAND_EQ + "'[\\S]*'");
        Pattern businessUnitPattern = Pattern.compile("businessUnitID" + OPERAND_EQ + "'[\\S]*'");
        Pattern divisionPattern = Pattern.compile("divisionID" + OPERAND_EQ + "'[\\S]*'");
        Pattern departmentPattern = Pattern.compile("departmentID" + OPERAND_EQ + "'[\\S]*'");
        Pattern positionPattern = Pattern.compile("positionID" + OPERAND_EQ + "'[\\S]*'");

        Matcher companyMatcher = companyPattern.matcher(filterQuery);
        Matcher businessUnitMatcher = businessUnitPattern.matcher(filterQuery);
        Matcher divisionMatcher = divisionPattern.matcher(filterQuery);
        Matcher departmentMatcher = departmentPattern.matcher(filterQuery);
        Matcher positionMatcher = positionPattern.matcher(filterQuery);

        while (companyMatcher.find())
            companyId = companyMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");
        while (businessUnitMatcher.find())
            businessUnitId = businessUnitMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");
        while (divisionMatcher.find())
            divisionId = divisionMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");
        while (departmentMatcher.find())
            departmentId = departmentMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");
        while (positionMatcher.find())
            positionId = positionMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        if (divisionId == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The parameter division is not set.");
        if (departmentId == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The parameter department is not set.");
    }

    public ExpressionFluentHelper<Position> createPositionFilter(ExpressionFluentHelper<Position> filters) {
        if (divisionId != null)
            filters = filters.and(Position.DIVISION.eq(divisionId));
        if (departmentId != null)
            filters = filters.and(Position.DEPARTMENT.eq(departmentId));
        if (companyId != null)
            filters = filters.and(Position.COMPANY.eq(companyId));
        if (businessUnitId != null)
            filters = filters.and(Position.BUSINESS_UNIT.eq(businessUnitId));
        if (positionId != null)
            filters = filters.and(Position.CODE.eq(positionId));

        return filters;
    }

    public Boolean hasParamForEmpJob() {
        return hasParamForEmpJob;
    }
}
