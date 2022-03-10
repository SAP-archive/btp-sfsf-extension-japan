package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.math.BigDecimal;
import java.util.List;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.config.ConfigService;
import com.sap.sfsf.reshuffle.applicants.backend.util.CandidateUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.vdm.namespaces.custwillingness.Cust_transfer_willingness;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.formheader.FormHeader;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.springframework.beans.factory.annotation.Autowired;

import cds.gen.reshuffleservice.Candidates;
import cds.gen.reshuffleservice.Configs;

public abstract class AbstractCandidateService {

    @Autowired
    private ODataRequest request;

    @Autowired
    private ConfigService configService;

    protected void createCandidateEntity(Candidates candidate)
            throws ODataException, EmptyConfigException {

        CandidateUtil util = new CandidateUtil();

        String caseId = candidate.getCaseID();
        String positionId = candidate.getPositionID();
        String candidateId = candidate.getCandidateID();
        String incumbentEmpId = candidate.getIncumbentEmpID();

        EmpJob candidateEmpJob = fetchEmpJob(candidateId);
        EmpJob incumbentEmpJob = fetchEmpJob(incumbentEmpId);
        Position position = fetchPosition(positionId);

        if (candidateEmpJob == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST,
                    "candidateID: " + candidateId + " doesn't exist.");
        if (incumbentEmpJob == null && position == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST,
                    "incumbentEmpID: " + incumbentEmpId + " and positionID: " + positionId + " doesn't exist.");

        Configs config = configService.getConfig();
        BigDecimal rating1 = fetchRating(candidateId, config.getRateFormKey1());
        BigDecimal rating2 = fetchRating(candidateId, config.getRateFormKey2());
        BigDecimal rating3 = fetchRating(candidateId, config.getRateFormKey3());
        String willingness = fetchWillingness(candidateId);

        util.createCandidateEntity(candidate, caseId, candidateEmpJob, incumbentEmpJob, position, rating1, rating2,
                rating3, willingness);
    }

    protected EmpJob fetchEmpJob(String userId) throws ODataException {
        List<EmpJob> empJobList = request.requestEmpJob(EmpJob.USER_ID.eq(userId));

        if (empJobList.size() == 0)
            return null;

        return empJobList.get(0);
    }

    protected Position fetchPosition(String positionId) throws ODataException {
        List<Position> positionList = request.requestPosition(Position.CODE.eq(positionId));

        if (positionList.size() == 0)
            return null;

        return positionList.get(0);
    }

    protected BigDecimal fetchRating(String userId, String formTemplateId) throws ODataException {
        ExpressionFluentHelper<FormHeader> filter = FormHeader.FORM_SUBJECT_ID.eq(userId)
                .and(FormHeader.FORM_TEMPLATE_ID.eq(Long.parseLong(formTemplateId)));

        List<FormHeader> formHeaderList = request.requestFormHeader(filter);

        if (formHeaderList.size() == 0)
            return null;

        return formHeaderList.get(0).getRating();
    }

    protected String fetchWillingness(String userId) throws ODataException {
        List<Cust_transfer_willingness> willingnessList = request
                .requestWillingness(Cust_transfer_willingness.USER.eq(userId));

        if (willingnessList.size() == 0)
            return null;

        return willingnessList.get(0).getCust_willingness() ? "yes" : "no";
    }
}
