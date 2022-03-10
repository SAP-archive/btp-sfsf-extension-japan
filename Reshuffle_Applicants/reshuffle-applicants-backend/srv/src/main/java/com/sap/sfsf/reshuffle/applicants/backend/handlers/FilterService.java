package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.ql.Select;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.services.runtime.CdsRuntime;
import com.sap.cds.services.runtime.RequestContextRunner;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.vdm.namespaces.fobusinessunit.FOBusinessUnit;
import com.sap.sfsf.vdm.namespaces.focompany.FOCompany;
import com.sap.sfsf.vdm.namespaces.fodepartment.FODepartment;
import com.sap.sfsf.vdm.namespaces.fodivision.FODivision;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.CdsModel_;
import cds.gen.FilterReturn;
import cds.gen.reshuffleservice.GetFilterContext;

@Component
@ServiceName("ReshuffleService")
public class FilterService implements EventHandler {
    private Logger logger = LoggerFactory.getLogger(FilterService.class);

    @Autowired
    private ODataRequest request;

    @Autowired
    private PersistenceService db;

    @On(event = "getFilter")
    public void getFilter(GetFilterContext context) throws Exception {
        List<FilterReturn> list = null;

        LocalDateTime today = DateTimeUtil.getToday();

        CdsRuntime runtime = context.getCdsRuntime();
        RequestContextRunner runner = runtime.requestContext();

        switch (context.getArg()) {
            case "companies":
                list = fetchCompanies(today);
                break;
            case "businessunits":
                list = fetchBusinessUnits(today);
                break;
            case "divisions":
                list = fetchDivisions(today);
                break;
            case "departments":
                list = fetchDepartments(today);
                break;
            case "positions":
                list = fetchPositions(today);
                break;
            case "caseids":
                list = fetchCandidates(today, runner);
                break;
            default:
                logger.error("Invalid argment.");
                throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The argument is invalid.");
        }

        context.setResult(list);
    }

    private List<FilterReturn> fetchCompanies(LocalDateTime localDateTime) throws ODataException {
        ExpressionFluentHelper<FOCompany> filter = FOCompany.START_DATE.le(localDateTime)
                .and(FOCompany.END_DATE.gt(localDateTime))
                .and(FOCompany.NAME_JA_JP.neNull());

        List<FOCompany> companyList = request.requestCompany(filter);

        List<FilterReturn> retList = new ArrayList<>();

        for (final FOCompany c : companyList) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(c.getExternalCode());
            fEntity.setName(c.getName_ja_JP());
            retList.add(fEntity);
        }

        return retList;
    }

    private List<FilterReturn> fetchBusinessUnits(LocalDateTime localDateTime) throws ODataException {
        ExpressionFluentHelper<FOBusinessUnit> filter = FOBusinessUnit.START_DATE.le(localDateTime)
                .and(FOBusinessUnit.END_DATE.gt(localDateTime))
                .and(FOBusinessUnit.NAME_JA_JP.neNull());

        List<FOBusinessUnit> businessUnitList = request.requestBusinessUnit(filter);

        List<FilterReturn> retList = new ArrayList<>();

        for (final FOBusinessUnit b : businessUnitList) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(b.getExternalCode());
            fEntity.setName(b.getName_ja_JP());
            retList.add(fEntity);
        }

        return retList;
    }

    private List<FilterReturn> fetchDivisions(LocalDateTime localDateTime) throws Exception {
        ExpressionFluentHelper<FODivision> filter = FODivision.START_DATE.le(localDateTime)
                .and(FODivision.END_DATE.gt(localDateTime))
                .and(FODivision.NAME_JA_JP.neNull());

        List<FODivision> divisionList = request.requestDivision(filter);

        List<FilterReturn> retList = new ArrayList<>();

        for (final FODivision d : divisionList) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(d.getExternalCode());
            fEntity.setName(d.getName_ja_JP());

            try {
                List<Map<String, String>> parent = d.getCustomField("cust_toBusinessUnit");
                fEntity.setParentID(parent.get(0).getOrDefault("externalCode", null));
            } catch (Exception e) {
                logger.warn("Division " + d.getExternalCode() + " does not have parent.");
            }

            retList.add(fEntity);
        }

        return retList;
    }

    private List<FilterReturn> fetchDepartments(LocalDateTime localDateTime)
            throws ODataException {

        ExpressionFluentHelper<FODepartment> filter = FODepartment.START_DATE.le(localDateTime)
                .and(FODepartment.END_DATE.gt(localDateTime))
                .and(FODepartment.NAME_JA_JP.neNull());

        List<FODepartment> departmentList = request.requestDepartment(filter);

        List<FilterReturn> retList = new ArrayList<>();

        for (final FODepartment d : departmentList) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(d.getExternalCode());
            fEntity.setName(d.getName_ja_JP());

            try {
                List<Map<String, String>> parent = d.getCustomField("cust_toDivision");
                fEntity.setParentID(parent.get(0).getOrDefault("externalCode", null));
            } catch (Exception e) {
                logger.warn("Department " + d.getExternalCode() + " does not have parent.");
            }

            retList.add(fEntity);
        }

        return retList;
    }

    private List<FilterReturn> fetchPositions(LocalDateTime localDateTime) throws ODataException {
        ExpressionFluentHelper<Position> filter = Position.EFFECTIVE_START_DATE.le(localDateTime)
                .and(Position.EFFECTIVE_END_DATE.gt(localDateTime))
                .and(Position.EXTERNAL_NAME_JA_JP.neNull());

        List<Position> positionList = request.requestPosition(filter);

        List<FilterReturn> retList = new ArrayList<>();

        for (final Position p : positionList) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(p.getCode());
            fEntity.setName(p.getExternalName_ja_JP());
            fEntity.setParentID(p.getDepartment());
            retList.add(fEntity);
        }

        return retList;
    }

    private List<FilterReturn> fetchCandidates(LocalDateTime localDateTime, RequestContextRunner runner) throws InterruptedException, ExecutionException {

        Future<Result> candidateResult = Executors.newSingleThreadExecutor().submit(() -> {
            return runner.run(threadContext -> {
                return db.run(Select.from(CdsModel_.CANDIDATES).columns(c -> c.caseID()));
            });
        });

        // Result candidateResult = db.run(Select.from(CdsModel_.CANDIDATES).columns(c -> c.caseID()));
        // List<Row> candidateList = candidateResult.list();
        List<Row> candidateList = candidateResult.get().list();

        LinkedHashSet<String> caseIdSet = new LinkedHashSet<>();
        List<FilterReturn> retList = new ArrayList<>();

        // Extract caseIds excluding duplicates.
        for (Row row : candidateList)
            row.forEach((k, v) -> caseIdSet.add(v.toString()));

        for (final String caseId : caseIdSet) {
            FilterReturn fEntity = FilterReturn.create();
            fEntity.setId(caseId);
            retList.add(fEntity);
        }

        return retList;
    }
}
