package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.time.Instant;
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
import com.sap.sfsf.reshuffle.applicants.backend.util.query.NextPositionQuery;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.reshuffleservice.Configs;
import cds.gen.reshuffleservice.NextPositions;
import cds.gen.reshuffleservice.NextPositions_;

@Component
@ServiceName("ReshuffleService")
public class NextPositionService extends AbstractPositionService {
    private Logger logger = LoggerFactory.getLogger(NextPositionService.class);

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private ConfigService configService;

    @On(entity = NextPositions_.CDS_NAME)
    public void onRead(CdsReadEventContext context)
            throws ServiceException, ODataException, NullPointerException, EmptyConfigException {

        // 1. fetch Positions and EmpJobs
        Map<String, String> parameters = context.getParameterInfo().getQueryParams();

        NextPositionQuery query = new NextPositionQuery();
        query.validateParameters(parameters);

        Configs config = configService.getConfig();
        Instant configuredStartDate = config.getStartDateTime();

        LocalDateTime startDate = DateTimeUtil.toLocalDateTime(configuredStartDate);
        LocalDateTime endDate = DateTimeUtil.getEndDate(startDate, config.getSpan());
        LocalDateTime lastDate = DateTimeUtil.getEndDate();

        // filter = entity_start_date >= startDate && (entity_end_date <= endDate ||
        // entity_end_date == 9999/12/31)
        List<Position> positionList = fetchPosition(query, startDate, endDate, lastDate);
        List<EmpJob> empJobList = fetchEmpJob(query, startDate, endDate, lastDate, positionList);

        // 2. fetch willings
        List<String> userIds = new ArrayList<>();

        for (final EmpJob e : empJobList)
            userIds.add(e.getUserId());

        Map<String, Boolean> willingnessMap = fetchWillingness(userIds);

        // 3. create return entity
        Map<String, List<EmpJob>> empJobListMap = convertEmpJob(empJobList);

        List<NextPositions> nextPositionList = createNextPositionList(query, positionList, empJobListMap,
                willingnessMap);

        final List<Map<String, Object>> result = new ArrayList<>();

        for (final NextPositions p : nextPositionList)
            result.add(p);

        logger.info("Complete creating return entity...");

        context.setResult(result);
    }

    private List<Position> fetchPosition(NextPositionQuery query, LocalDateTime startDate, LocalDateTime endDate,
            LocalDateTime lastDate) throws ODataException {

        ExpressionFluentHelper<Position> dateFilter = Position.EFFECTIVE_START_DATE.ge(startDate)
                .and(Position.EFFECTIVE_END_DATE.le(endDate).or(Position.EFFECTIVE_END_DATE.eq(lastDate)));
        ExpressionFluentHelper<Position> positionFilter = query.createPositionFilter(dateFilter);

        return fetchPosition(positionFilter);
    }

    private List<EmpJob> fetchEmpJob(NextPositionQuery query, LocalDateTime startDate, LocalDateTime endDate,
            LocalDateTime lastDate, List<Position> positionList) throws ODataException {

        ExpressionFluentHelper<EmpJob> dateFilter = EmpJob.START_DATE.ge(startDate)
                .and(EmpJob.END_DATE.le(endDate).or(EmpJob.END_DATE.eq(lastDate)));
        ExpressionFluentHelper<EmpJob> empJobFilter = query.createEmpJobFilter(dateFilter);

        return fetchEmpJob(positionList, empJobFilter);
    }

    private List<NextPositions> createNextPositionList(NextPositionQuery query, List<Position> positionList,
            Map<String, List<EmpJob>> empJobListMap, Map<String, Boolean> willingnessMap) throws NullPointerException {

        List<NextPositions> retList = new ArrayList<>();

        for (final Position position : positionList) {
            List<EmpJob> tmpEmpJobList = empJobListMap.getOrDefault(position.getCode(), null);

            if (tmpEmpJobList == null) {
                if (!query.hasParamForEmpJob())
                    retList.add(createNextPositionEntityWithoutEmpJob(position));
                else
                    continue;
            } else {
                for (final EmpJob empJob : tmpEmpJobList) {
                    final Boolean willingness = willingnessMap.getOrDefault(empJob.getUserId(), null);

                    retList.add(createNextPositionEntity(position, empJob, willingness));
                }
            }
        }

        return retList;
    }

    private NextPositions createNextPositionEntity(Position position, EmpJob empJob, Boolean willingness)
            throws NullPointerException {

        final NextPositions ret = createNextPositionEntityWithoutEmpJob(position);
        Map<String, String> userNav = new HashMap<>();
        Map<String, String> payGradeNav = new HashMap<>();
        Util util = new Util();
        
        try {
            userNav = empJob.getCustomField("userNav");
            payGradeNav = empJob.getCustomField("payGradeNav");
        } catch (NoSuchEntityFieldException ex) {
            logger.warn(ex.getMessage());
        }

        String empName = util.combineName(userNav.getOrDefault("lastName", ""), userNav.getOrDefault("firstName", ""));
        String terminationCode = envConfig.getTerminationCode();

        ret.setEmpID(empJob.getUserId());
        ret.setEmpManagerID(empJob.getManagerId());
        ret.setJobGradeID(empJob.getPayGrade());
        ret.setEmpName(empName);
        ret.setJobGradeName(payGradeNav.getOrDefault("name", ""));

        if (empJob.getEvent() != null && empJob.getEvent().equals(terminationCode))
            ret.setEmpRetire("yes");
        else
            ret.setEmpRetire("no");
            
        if (willingness != null && willingness)
            ret.setEmpRetirementIntension("yes");
        else
            ret.setEmpRetirementIntension("no");

        return ret;
    }

    private NextPositions createNextPositionEntityWithoutEmpJob(Position position) throws NullPointerException {
        final NextPositions ret = Struct.create(NextPositions.class);

        Map<String, String> departmentNav = new HashMap<>();
        Map<String, String> divisionNav = new HashMap<>();

        try {
            departmentNav = position.getCustomField("departmentNav");
            divisionNav = position.getCustomField("divisionNav");
        } catch (NoSuchEntityFieldException e) {
            logger.warn(e.getMessage());
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
