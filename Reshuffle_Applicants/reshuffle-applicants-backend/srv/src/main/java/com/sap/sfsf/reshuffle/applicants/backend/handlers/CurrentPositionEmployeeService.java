package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cds.Struct;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.exception.NoSuchEntityFieldException;
import com.sap.sfsf.reshuffle.applicants.backend.config.ConfigService;
import com.sap.sfsf.reshuffle.applicants.backend.config.EnvConfig;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.Util;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.query.CurrentPositionEmployeeQuery;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.reshuffleservice.Configs;
import cds.gen.reshuffleservice.CurrentPositionEmployees;
import cds.gen.reshuffleservice.CurrentPositionEmployees_;

@Component
@ServiceName("ReshuffleService")
public class CurrentPositionEmployeeService extends AbstractPositionService {

    private Logger logger = LoggerFactory.getLogger(CurrentPositionEmployeeService.class);

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private ConfigService configService;

    @On(entity = CurrentPositionEmployees_.CDS_NAME)
    public void onRead(CdsReadEventContext context)
            throws ServiceException, ODataException, NullPointerException, EmptyConfigException {

        // 1. fetch Positions and EmpJobs
        Map<String, String> parameters = context.getParameterInfo().getQueryParams();

        CurrentPositionEmployeeQuery query = new CurrentPositionEmployeeQuery();
        query.validateParameters(parameters);

        List<Position> positionList = fetchPosition(query);
        List<EmpJob> empJobList = fetchEmpJob(query, positionList);

        // 2. fetch willings and ratings
        List<String> userIds = new ArrayList<>();
        Configs config = configService.getConfig();

        for (final EmpJob e : empJobList)
            userIds.add(e.getUserId());

        Map<String, Boolean> willingnessMap = fetchWillingness(userIds);
        Map<String, BigDecimal> rate1Map = fetchRating(userIds, config.getRateFormKey1());
        Map<String, BigDecimal> rate2Map = fetchRating(userIds, config.getRateFormKey2());
        Map<String, BigDecimal> rate3Map = fetchRating(userIds, config.getRateFormKey3());

        // 3. create return entity
        Map<String, List<EmpJob>> empJobListMap = convertEmpJob(empJobList);
        
        List<CurrentPositionEmployees> currentPositionEmployeeList = createCurrentPositionEmployeeList(query,
                positionList, empJobListMap, willingnessMap, rate1Map, rate2Map, rate3Map);

        final List<Map<String, Object>> result = new ArrayList<>();

        for (final CurrentPositionEmployees c : currentPositionEmployeeList)
            result.add(c);

        logger.info("Complete creating return entity...");

        context.setResult(result);
    }

    private List<Position> fetchPosition(CurrentPositionEmployeeQuery query)
            throws ODataException {

        LocalDateTime today = DateTimeUtil.getToday();

        ExpressionFluentHelper<Position> dateFilter = Position.EFFECTIVE_START_DATE.le(today)
                .and(Position.EFFECTIVE_END_DATE.gt(today));
        ExpressionFluentHelper<Position> positionFilter = query.createPositionFilter(dateFilter);

        return fetchPosition(positionFilter);
    }

    private List<EmpJob> fetchEmpJob(CurrentPositionEmployeeQuery query, List<Position> positionList)
            throws ODataException {

        LocalDateTime today = DateTimeUtil.getToday();

        ExpressionFluentHelper<EmpJob> dateFilter = EmpJob.START_DATE.le(today)
                .and(EmpJob.END_DATE.gt(today));
        ExpressionFluentHelper<EmpJob> empJobFilter = query.createEmpJobFilter(dateFilter);

        return fetchEmpJob(positionList, empJobFilter);
    }

    private List<CurrentPositionEmployees> createCurrentPositionEmployeeList(CurrentPositionEmployeeQuery query,
            List<Position> positionList, Map<String, List<EmpJob>> empJobListMap, Map<String, Boolean> willingnessMap,
            Map<String, BigDecimal> rate1Map, Map<String, BigDecimal> rate2Map, Map<String, BigDecimal> rate3Map)
            throws NullPointerException {

        List<CurrentPositionEmployees> retList = new ArrayList<>();

        for (final Position position : positionList) {
            List<EmpJob> tmpEmpJobList = empJobListMap.getOrDefault(position.getCode(), null);

            // Exclude positions with no employees
            if (tmpEmpJobList == null)
                continue;

            for (final EmpJob empJob : tmpEmpJobList) {
                final Boolean willingness = willingnessMap.getOrDefault(empJob.getUserId(), null);
                final BigDecimal rating1 = rate1Map.getOrDefault(empJob.getUserId(), null);
                final BigDecimal rating2 = rate2Map.getOrDefault(empJob.getUserId(), null);
                final BigDecimal rating3 = rate3Map.getOrDefault(empJob.getUserId(), null);

                // avoid returning exCandidate if a willingness does not correspond
                // with the filter willingness
                if (query.isWillingness() != null) {
                    if (query.isWillingness() != willingness)
                        continue;
                }

                retList.add(
                        createCurrentPositionEmployeeEntity(position, empJob, willingness, rating1, rating2, rating3));
            }
        }

        return retList;
    }

    private CurrentPositionEmployees createCurrentPositionEmployeeEntity(Position position, EmpJob empJob,
            Boolean willingness, BigDecimal rating1, BigDecimal rating2, BigDecimal rating3)
            throws NullPointerException {

        final CurrentPositionEmployees ret = createCurrentPositionEmployeeEntityWithoutEmpJob(position);
        Util util = new Util();

        Map<String, String> userNav = new HashMap<>();
        Map<String, String> payGradeNav = new HashMap<>();
        try {
            userNav = empJob.getCustomField("userNav");
            payGradeNav = empJob.getCustomField("payGradeNav");
        } catch (NoSuchEntityFieldException e) {
            logger.warn(e.getMessage());
        }

        int jobTenure = util.calculateTenure(empJob.getStartDate());
        String empName = util.combineName(userNav.getOrDefault("lastName", ""), userNav.getOrDefault("firstName", ""));
        String terminationCode = envConfig.getTerminationCode();

        ret.setEmpID(empJob.getUserId());
        ret.setEmpManagerID(empJob.getManagerId());
        ret.setJobGradeID(empJob.getPayGrade());
        ret.setEmpJobTenure(jobTenure);
        ret.setEmpName(empName);
        ret.setJobGradeName(payGradeNav.getOrDefault("name", ""));

        if (empJob.getEvent() != null && empJob.getEvent().equals(terminationCode))
            ret.setEmpRetire("yes");
        else
            ret.setEmpRetire("no");

        if (willingness != null && willingness)
            ret.setWillingness("yes");
        else
            ret.setWillingness("no");
            
        if (rating1 != null)
            ret.setLastRating1(rating1.toPlainString());
        if (rating2 != null)
            ret.setLastRating1(rating2.toPlainString());
        if (rating3 != null)
            ret.setLastRating1(rating3.toPlainString());

        return ret;
    }

    private CurrentPositionEmployees createCurrentPositionEmployeeEntityWithoutEmpJob(Position position)
            throws NullPointerException {

        final CurrentPositionEmployees ret = Struct.create(CurrentPositionEmployees.class);

        Map<String, String> departmentNav = new HashMap<String, String>();
        Map<String, String> divisionNav = new HashMap<String, String>();
        try {
            departmentNav = position.getCustomField("departmentNav");
            divisionNav = position.getCustomField("divisionNav");
        } catch (NoSuchEntityFieldException ex) {
            logger.warn(ex.getMessage());
        }

        ret.setDepartmentID(position.getDepartment());
        ret.setDivisionID(position.getDivision());
        ret.setPositionID(position.getCode());
        ret.setPositionName(position.getExternalName_ja_JP());
        ret.setDepartmentName(departmentNav.getOrDefault("name_ja_JP", ""));
        ret.setDivisionName(divisionNav.getOrDefault("name_ja_JP", ""));

        return ret;
    }
}
