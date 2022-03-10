package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.exception.NoSuchEntityFieldException;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cds.gen.reshuffleservice.Candidates;

public class CandidateUtil {
    private Logger logger = LoggerFactory.getLogger(CandidateUtil.class);

    public void createCandidateEntity(Candidates candidate, String caseId, EmpJob candidateEmpJob,
            EmpJob incumbentEmpJob, Position position, BigDecimal rating1, BigDecimal rating2, BigDecimal rating3,
            String willingness) {

        Util util = new Util();

        String candidateId = null;
        String candidateName = null;
        String candidateDivisionId = null;
        String candidateDivisionName = null;
        String candidateDepartmentId = null;
        String candidateDepartmentName = null;
        String candidatePositionId = null;
        String candidatePositionName = null;
        String candidateJobGradeId = null;
        String candidateJobGradeName = null;
        Integer candidateJobTenure = null;
        String candidateManagerID = null;
        String rating1Str = null;
        String rating2Str = null;
        String rating3Str = null;

        if (candidateEmpJob != null) {
            Map<String, String> candidateUserNav = new HashMap<>();
            Map<String, String> candidateDivisionNav = new HashMap<>();
            Map<String, String> candidateDepartmentNav = new HashMap<>();
            Map<String, String> candidatePositionNav = new HashMap<>();
            Map<String, String> candidatePayGradeNav = new HashMap<>();

            try {
                candidateUserNav = candidateEmpJob.getCustomField("userNav");
                candidateDivisionNav = candidateEmpJob.getCustomField("divisionNav");
                candidateDepartmentNav = candidateEmpJob.getCustomField("departmentNav");
                candidatePositionNav = candidateEmpJob.getCustomField("positionNav");
                candidatePayGradeNav = candidateEmpJob.getCustomField("payGradeNav");
            } catch (NoSuchEntityFieldException e) {
                logger.warn(e.getMessage());
            }

            candidateId = candidateEmpJob.getUserId();
            candidateName = util.combineName(candidateUserNav.getOrDefault("lastName", ""),
                    candidateUserNav.getOrDefault("firstName", ""));
            candidateDivisionId = candidateEmpJob.getDivision();
            candidateDivisionName = candidateDivisionNav.getOrDefault("name_ja_JP", "");
            candidateDepartmentId = candidateEmpJob.getDepartment();
            candidateDepartmentName = candidateDepartmentNav.getOrDefault("name_ja_JP", "");
            candidatePositionId = candidateEmpJob.getPosition();
            candidatePositionName = candidatePositionNav.getOrDefault("externalName_ja_JP", "");
            candidateJobGradeId = candidateEmpJob.getPayGrade();
            candidateJobGradeName = candidatePayGradeNav.getOrDefault("name", "");
            candidateJobTenure = util.calculateTenure(candidateEmpJob.getStartDate());
            candidateManagerID = candidateEmpJob.getManagerId();

            if (rating1 != null)
                rating1Str = rating1.toPlainString();
            if (rating2 != null)
                rating2Str = rating2.toPlainString();
            if (rating3 != null)
                rating3Str = rating3.toPlainString();
        }

        String incumbentEmpId = null;
        String incumbentEmpName = null;
        String divisionId = null;
        String divisionName = null;
        String departmentId = null;
        String departmentName = null;
        String positionId = null;
        String positionName = null;
        String jobGradeId = null;
        String jobGradeName = null;
        String incumbentEmpManagerId = null;

        if (incumbentEmpJob != null) {
            Map<String, String> incumbentUserNav = new HashMap<>();
            Map<String, String> incumbentDivisionNav = new HashMap<>();
            Map<String, String> incumbentDepartmentNav = new HashMap<>();
            Map<String, String> incumbentPositionNav = new HashMap<>();
            Map<String, String> incumbentPayGradeNav = new HashMap<>();

            try {
                incumbentUserNav = incumbentEmpJob.getCustomField("userNav");
                incumbentDivisionNav = incumbentEmpJob.getCustomField("divisionNav");
                incumbentDepartmentNav = incumbentEmpJob.getCustomField("departmentNav");
                incumbentPositionNav = incumbentEmpJob.getCustomField("positionNav");
                incumbentPayGradeNav = incumbentEmpJob.getCustomField("payGradeNav");
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }

            incumbentEmpId = incumbentEmpJob.getUserId();
            incumbentEmpName = util.combineName(incumbentUserNav.getOrDefault("lastName", ""),
                    incumbentUserNav.getOrDefault("firstName", ""));
            divisionId = incumbentEmpJob.getDivision();
            divisionName = incumbentDivisionNav.getOrDefault("name_ja_JP", "");
            departmentId = incumbentEmpJob.getDepartment();
            departmentName = incumbentDepartmentNav.getOrDefault("name_ja_JP", "");
            positionId = incumbentEmpJob.getPosition();
            positionName = incumbentPositionNav.getOrDefault("externalName_ja_JP", "");
            jobGradeId = incumbentEmpJob.getPayGrade();
            jobGradeName = incumbentPayGradeNav.getOrDefault("name", "");
            incumbentEmpManagerId = incumbentEmpJob.getManagerId();

        } else if (position != null) {
            Map<String, String> positionDivisionNav = new HashMap<>();
            Map<String, String> positionDepartmentNav = new HashMap<>();

            try {
                positionDivisionNav = position.getCustomField("departmentNav");
                positionDepartmentNav = position.getCustomField("divisionNav");
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }

            divisionId = position.getDivision();
            divisionName = positionDivisionNav.getOrDefault("name_ja_JP", "");
            departmentId = position.getDepartment();
            departmentName = positionDepartmentNav.getOrDefault("name_ja_JP", "");
            positionId = position.getCode();
            positionName = position.getExternalName_ja_JP();
        }

        candidate.setCaseID(caseId);
        candidate.setCandidateID(candidateId);
        candidate.setCandidateName(candidateName);
        candidate.setCandidateDivisionID(candidateDivisionId);
        candidate.setCandidateDivisionName(candidateDivisionName);
        candidate.setCandidateDepartmentID(candidateDepartmentId);
        candidate.setCandidateDepartmentName(candidateDepartmentName);
        candidate.setCandidatePositionID(candidatePositionId);
        candidate.setCandidatePositionName(candidatePositionName);
        candidate.setCandidateJobGradeID(candidateJobGradeId);
        candidate.setCandidateJobGradeName(candidateJobGradeName);
        candidate.setCandidateJobTenure(candidateJobTenure);
        candidate.setCandidateManagerID(candidateManagerID);
        candidate.setIncumbentEmpRetirementIntention(willingness);
        candidate.setCandidateLastRating1(rating1Str);
        candidate.setCandidateLastRating2(rating2Str);
        candidate.setCandidateLastRating3(rating3Str);

        candidate.setIncumbentEmpID(incumbentEmpId);
        candidate.setIncumbentEmpName(incumbentEmpName);
        candidate.setDivisionID(divisionId);
        candidate.setDivisionName(divisionName);
        candidate.setDepartmentID(departmentId);
        candidate.setDepartmentName(departmentName);
        candidate.setPositionID(positionId);
        candidate.setPositionName(positionName);
        candidate.setJobGradeID(jobGradeId);
        candidate.setJobGradeName(jobGradeName);
        candidate.setIncumbentEmpManagerID(incumbentEmpManagerId);
    }

    public void validateCandidate(Candidates candidate, String positionIdOnParam) throws ServiceException {
        String caseId = candidate.getCaseID();
        String candidateId = candidate.getCandidateID();
        String positionId = candidate.getPositionID();

        if (caseId == null || caseId.equals(""))
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "CaseID is mandatory.");
        if (candidateId == null || candidateId.equals(""))
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "candidateID is mandatory.");
        if (positionId == null || positionId.equals(""))
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "positionID is mandatory.");

        // Error if there is a conflict between the positionID given as a parameter and
        // the ID obtained from EmpJob.
        if (!positionId.equals(positionIdOnParam))
            throw new ServiceException(ErrorStatuses.BAD_REQUEST,
                    "There were somthing wrong between positionID and incumbentID.");
    }
}
