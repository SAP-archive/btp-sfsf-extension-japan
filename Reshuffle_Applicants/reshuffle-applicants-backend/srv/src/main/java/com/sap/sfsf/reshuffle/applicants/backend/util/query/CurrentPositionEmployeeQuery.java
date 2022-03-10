package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CurrentPositionEmployeeQuery extends AbstractPositionQuery {

    private String empJobTenureLL;
    private String empJobTenureUL;
    private String willingness;

    private final String OPERAND_GE = "[\\s]*ge[\\s]*";
    private final String OPERAND_LE = "[\\s]*le[\\s]*";

    @Override
    public void validateParameters(Map<String, String> parameters) throws ServiceException {
        super.validateParameters(parameters);

        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern empJobTenureLLPattern = Pattern.compile("empJobTenure" + OPERAND_GE + "[\\d]*");
        Pattern empJobTenureULPattern = Pattern.compile("empJobTenure" + OPERAND_LE + "[\\d]*");
        Pattern willingnessPattern = Pattern.compile("willingness" + OPERAND_EQ + "'[\\S]*'");

        Matcher empJobTenureLLMatcher = empJobTenureLLPattern.matcher(filterQuery);
        Matcher empJobTenureULMatcher = empJobTenureULPattern.matcher(filterQuery);
        Matcher willingnessMatcher = willingnessPattern.matcher(filterQuery);

        while (empJobTenureLLMatcher.find())
            empJobTenureLL = empJobTenureLLMatcher.group().split(OPERAND_GE)[1];
        while (empJobTenureULMatcher.find())
            empJobTenureUL = empJobTenureULMatcher.group().split(OPERAND_LE)[1];
        while (willingnessMatcher.find())
            willingness = willingnessMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        if (this.empJobTenureLL != null || this.empJobTenureUL != null || this.willingness != null)
            hasParamForEmpJob = true;
        else
            hasParamForEmpJob = false;
    }

    public ExpressionFluentHelper<EmpJob> createEmpJobFilter(ExpressionFluentHelper<EmpJob> filters) {
        if (empJobTenureLL != null) {
            int tenureLL = Integer.parseInt(empJobTenureLL);
            LocalDateTime tenureLLDate = DateTimeUtil.getYearsAgo(tenureLL);
            filters = filters.and(EmpJob.START_DATE.le(tenureLLDate));
        }
        if (empJobTenureUL != null) {
            int tenureUL = Integer.parseInt(empJobTenureUL);
            LocalDateTime tenureULDate = DateTimeUtil.getYearsAgo(tenureUL);
            filters = filters.and(EmpJob.START_DATE.ge(tenureULDate));
        }

        return filters;
    }

    public Boolean isWillingness() throws ResponseStatusException {
        if (willingness == null)
            return null;
        else if (willingness.equals("yes"))
            return true;
        else if (willingness.equals("no"))
            return false;

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

}
