package com.sap.sfsf.reshuffle.applicants.backend.util.query;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.sfsf.reshuffle.applicants.backend.config.EnvConfig;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;

public class NextPositionQuery extends AbstractPositionQuery {

    private String empRetire;

    @Override
    public void validateParameters(Map<String, String> parameters) throws ServiceException {
        super.validateParameters(parameters);

        String filterQuery = parameters.getOrDefault("$filter", null);

        if (filterQuery == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There is no filter.");

        Pattern empRetirePattern = Pattern.compile("empRetire" + OPERAND_EQ + "'[\\w]*'");
        Matcher empRetireMatcher = empRetirePattern.matcher(filterQuery);

        while (empRetireMatcher.find())
            empRetire = empRetireMatcher.group().split(OPERAND_EQ)[1].replaceAll("'", "");

        if (empRetire != null)
            hasParamForEmpJob = true;
        else
            hasParamForEmpJob = false;
    }

    public ExpressionFluentHelper<EmpJob> createEmpJobFilter(ExpressionFluentHelper<EmpJob> filters) {
        EnvConfig envConfig = new EnvConfig();

        if (empRetire != null) {
            if (empRetire.equals("yes"))
                filters = filters.and(EmpJob.EVENT.eq(envConfig.getTerminationCode()));
            else if (empRetire.equals("no"))
                filters = filters.and(EmpJob.EVENT.ne(envConfig.getTerminationCode()));
        }

        return filters;
    }

}
