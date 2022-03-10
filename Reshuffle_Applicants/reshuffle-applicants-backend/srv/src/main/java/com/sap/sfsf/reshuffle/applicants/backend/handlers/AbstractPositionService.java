package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.util.Util;
import com.sap.sfsf.vdm.namespaces.custwillingness.Cust_transfer_willingness;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.formheader.FormHeader;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractPositionService implements EventHandler {

    private Logger logger = LoggerFactory.getLogger(AbstractPositionService.class);

    @Autowired
    private ODataRequest request;

    protected List<Position> fetchPosition(ExpressionFluentHelper<Position> filter) throws ODataException {
        return request.requestPosition(filter);
    }

    protected List<EmpJob> fetchEmpJob(List<Position> positionList, ExpressionFluentHelper<EmpJob> empJobFilter)
            throws ODataException {

        List<String> positionCodes = new ArrayList<String>();
        List<EmpJob> retList = new ArrayList<>();
        Util util = new Util();

        for (final Position p : positionList)
            positionCodes.add(p.getCode());

        Map<Integer, ExpressionFluentHelper<EmpJob>> positionFilters = util.createFilterMap(EmpJob.POSITION, positionCodes);

        for (final ExpressionFluentHelper<EmpJob> f : positionFilters.values())
            retList.addAll(request.requestEmpJob(f.and(empJobFilter)));

        return retList;
    }

    protected Map<String, Boolean> fetchWillingness(List<String> userIds) throws ODataException {
        List<Cust_transfer_willingness> willingnessList = new ArrayList<>();
        Map<String, Boolean> retMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<Cust_transfer_willingness>> willingnessFilters = util.createFilterMap(
                Cust_transfer_willingness.USER, userIds);

        for (final ExpressionFluentHelper<Cust_transfer_willingness> f : willingnessFilters.values())
            willingnessList.addAll(request.requestWillingness(f));

        for (final Cust_transfer_willingness w : willingnessList)
            retMap.put(w.getUser(), w.getCust_willingness());

        return retMap;
    }

    protected Map<String, BigDecimal> fetchRating(List<String> userIds, String formTemplateId) throws ODataException {
        List<FormHeader> formHeaderList = new ArrayList<>();
        Map<String, BigDecimal> retMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<FormHeader>> formHeaderFilters = util.createFilterMap(FormHeader.FORM_SUBJECT_ID,
                userIds);

        ExpressionFluentHelper<FormHeader> additionalFilter = FormHeader.FORM_TEMPLATE_ID
                .eq(Long.parseLong(formTemplateId)).and(FormHeader.IS_RATED.eq(true));

        for (final ExpressionFluentHelper<FormHeader> f : formHeaderFilters.values())
            formHeaderList.addAll(request.requestFormHeader(f.and(additionalFilter)));

        for (final FormHeader f : formHeaderList)
            retMap.put(f.getFormSubjectId(), f.getRating());

        return retMap;
    }

    protected Map<String, List<EmpJob>> convertEmpJob(List<EmpJob> empJobList) {
        Map<String, List<EmpJob>> retLists = new HashMap<>();

        for (final EmpJob e : empJobList) {
            final String key = e.getPosition();
            final List<EmpJob> tmpList = retLists.getOrDefault(key, new ArrayList<EmpJob>());
            tmpList.add(e);

            retLists.put(key, tmpList);
        }

        return retLists;
    }
}
