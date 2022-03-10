package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "CANDIDATE")
@IdClass(CandidateId.class)
public class Candidate {

    @Id
    @Column(name = "CASEID")
    private String caseID;

    @Id
    @Column(name = "CANDIDATEID")
    private String candidateID;

    @Column(name = "CANDIDATENAME")
    private String candidateName;

    @Column(name = "CANDIDATEDIVISIONID")
    private String candidateDivisionID;

    @Column(name = "CANDIDATEDEPARTMENTID")
    private String candidateDepartmentID;

    @Column(name = "CANDIDATEPOSITIONID")
    private String candidatePositionID;

    @Column(name = "CANDIDATEDIVISIONNAME")
    private String candidateDivisionName;

    @Column(name = "CANDIDATEDEPARTMENTNAME")
    private String candidateDepartmentName;
    
    @Column(name = "CANDIDATEPOSITIONNAME")
    private String candidatePositionName;
    
    @Column(name = "CANDIDATEJOBGRADEID")
    private String candidateJobGradeID;
    
    @Column(name = "CANDIDATEJOBGRADENAME")
    private String candidateJobGradeName;
    
    @Column(name = "CANDIDATEJOBTENURE")
    private Integer candidateJobTenure;
    
    @Column(name = "CANDIDATETRANSFERTIMES")
    private Integer candidateTransferTimes;
    
    @Column(name = "CANDIDATELASTRATING1")
    private String candidateLastRating1;
    
    @Column(name = "CANDIDATELASTRATING2")
    private String candidateLastRating2;
    
    @Column(name = "CANDIDATELASTRATING3")
    private String candidateLastRating3;
    
    @Column(name = "CANDIDATETRANSFERREASON")
    private String candidateTransferReason;
    
    @Column(name = "CANDIDATECERTIFICATION")
    private String candidateCertification;
    
    @Column(name = "DIVISIONID")
    private String divisionID;
    
    @Column(name = "DEPARTMENTID")
    private String departmentID;
    
    @Id
    @Column(name = "POSITIONID")
    private String positionID;
    
    @Column(name = "DIVISIONNAME")
    private String divisionName;
    
    @Column(name = "DEPARTMENTNAME")
    private String departmentName;
    
    @Column(name = "POSITIONNAME")
    private String positionName;
    
    @Column(name = "JOBGRADEID")
    private String jobGradeID;
    
    @Column(name = "JOBGRADENAME")
    private String jobGradeName;
    
    @Column(name = "INCUMBENTEMPID")
    private String incumbentEmpID;
    
    @Column(name = "INCUMBENTEMPNAME")
    private String incumbentEmpName;
    
    @Column(name = "INCUMBENTEMPRETIREMENTINTENTION")
    private String incumbentEmpRetirementIntention;
    
    @Column(name = "INCUMBENTEMPMANAGER")
    private String incumbentEmpManager;
    
    @Column(name = "CANDIDATEMANAGERID")
    private String candidateManagerID;
        
    @Column(name = "SIMULATIONCHECKRESULT")
    private String simulationCheckResult;
    
    @Column(name = "SIMULATIONCHECKSTATUS")
    private String simulationCheckStatus;
    
    @Column(name = "SIMULATIONCHECKDATETIME")
    private Date simulationCheckDatetime;
    
    @Column(name = "MAILSENTFLG")
    private String mailSentFlg;
    
    @Column(name = "MAILSENTAT")
    private Date mailSentAt;
    
    @Column(name = "SFUPSERTFLG")
    private String sfUpsertFlg;
    
    @Column(name = "SFUPSERTAT")
    private Date sfUpsertAt;
    
    @Column(name = "CREATEDAT")
    private Date createdAt;
    
    @Column(name = "CREATEDBY")
    private String createdBy;
    
    @Column(name = "MODIFIEDAT")
    private Date modifiedAt;
    
    @Column(name = "MODIFIEDBY")
    private String modifiedBy;
    
    @Column(name = "WFSTATUS")
    private String wfStatus;

    public Candidate() {
    }

    public Candidate(String userId, String nextPosition) {
        this.candidateID = userId;
        this.positionID = nextPosition;
        this.simulationCheckResult = "";
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateDivisionID() {
        return candidateDivisionID;
    }

    public void setCandidateDivisionID(String candidateDivisionID) {
        this.candidateDivisionID = candidateDivisionID;
    }

    public String getCandidateDepartmentID() {
        return candidateDepartmentID;
    }

    public void setCandidateDepartmentID(String candidateDepartmentID) {
        this.candidateDepartmentID = candidateDepartmentID;
    }

    public String getCandidatePositionID() {
        return candidatePositionID;
    }

    public void setCandidatePositionID(String candidatePositionID) {
        this.candidatePositionID = candidatePositionID;
    }

    public String getCandidateDivisionName() {
        return candidateDivisionName;
    }

    public void setCandidateDivisionName(String candidateDivisionName) {
        this.candidateDivisionName = candidateDivisionName;
    }

    public String getCandidateDepartmentName() {
        return candidateDepartmentName;
    }

    public void setCandidateDepartmentName(String candidateDepartmentName) {
        this.candidateDepartmentName = candidateDepartmentName;
    }

    public String getCandidatePositionName() {
        return candidatePositionName;
    }

    public void setCandidatePositionName(String candidatePositionName) {
        this.candidatePositionName = candidatePositionName;
    }

    public String getCandidateJobGradeID() {
        return candidateJobGradeID;
    }

    public void setCandidateJobGradeID(String candidateJobGradeID) {
        this.candidateJobGradeID = candidateJobGradeID;
    }

    public String getCandidateJobGradeName() {
        return candidateJobGradeName;
    }

    public void setCandidateJobGradeName(String candidateJobGradeName) {
        this.candidateJobGradeName = candidateJobGradeName;
    }

    public Integer getCandidateJobTenure() {
        return candidateJobTenure;
    }

    public void setCandidateJobTenure(Integer candidateJobTenure) {
        this.candidateJobTenure = candidateJobTenure;
    }

    public Integer getCandidateTransferTimes() {
        return candidateTransferTimes;
    }

    public void setCandidateTransferTimes(Integer candidateTransferTimes) {
        this.candidateTransferTimes = candidateTransferTimes;
    }

    public String getCandidateLastRating1() {
        return candidateLastRating1;
    }

    public void setCandidateLastRating1(String candidateLastRating1) {
        this.candidateLastRating1 = candidateLastRating1;
    }

    public String getCandidateLastRating2() {
        return candidateLastRating2;
    }

    public void setCandidateLastRating2(String candidateLastRating2) {
        this.candidateLastRating2 = candidateLastRating2;
    }

    public String getCandidateLastRating3() {
        return candidateLastRating3;
    }

    public void setCandidateLastRating3(String candidateLastRating3) {
        this.candidateLastRating3 = candidateLastRating3;
    }

    public String getCandidateTransferReason() {
        return candidateTransferReason;
    }

    public void setCandidateTransferReason(String candidateTransferReason) {
        this.candidateTransferReason = candidateTransferReason;
    }

    public String getCandidateCertification() {
        return candidateCertification;
    }

    public void setCandidateCertification(String candidateCertification) {
        this.candidateCertification = candidateCertification;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getJobGradeID() {
        return jobGradeID;
    }

    public void setJobGradeID(String jobGradeID) {
        this.jobGradeID = jobGradeID;
    }

    public String getJobGradeName() {
        return jobGradeName;
    }

    public void setJobGradeName(String jobGradeName) {
        this.jobGradeName = jobGradeName;
    }

    public String getIncumbentEmpID() {
        return incumbentEmpID;
    }

    public void setIncumbentEmpID(String incumbentEmpID) {
        this.incumbentEmpID = incumbentEmpID;
    }

    public String getIncumbentEmpName() {
        return incumbentEmpName;
    }

    public void setIncumbentEmpName(String incumbentEmpName) {
        this.incumbentEmpName = incumbentEmpName;
    }

    public String getIncumbentEmpRetirementIntention() {
        return incumbentEmpRetirementIntention;
    }

    public void setIncumbentEmpRetirementIntention(String incumbentEmpRetirementIntention) {
        this.incumbentEmpRetirementIntention = incumbentEmpRetirementIntention;
    }

    public String getIncumbentEmpManager() {
        return incumbentEmpManager;
    }

    public void setIncumbentEmpManager(String incumbentEmpManager) {
        this.incumbentEmpManager = incumbentEmpManager;
    }

    public String getCandidateManagerID() {
        return candidateManagerID;
    }

    public void setCandidateManagerID(String candidateManagerID) {
        this.candidateManagerID = candidateManagerID;
    }

    public String getSimulationCheckResult() {
        return simulationCheckResult;
    }

    public void setSimulationCheckResult(String simulationCheckResult) {
        this.simulationCheckResult = simulationCheckResult;
    }

    public String getSimulationCheckStatus() {
        return simulationCheckStatus;
    }

    public void setSimulationCheckStatus(String simulationCheckStatus) {
        this.simulationCheckStatus = simulationCheckStatus;
    }

    public Date getSimulationCheckDatetime() {
        return simulationCheckDatetime;
    }

    public void setSimulationCheckDatetime(Date simulationCheckDatetime) {
        this.simulationCheckDatetime = simulationCheckDatetime;
    }

    public String getMailSentFlg() {
        return mailSentFlg;
    }

    public void setMailSentFlg(String mailSentFlg) {
        this.mailSentFlg = mailSentFlg;
    }

    public Date getMailSentAt() {
        return mailSentAt;
    }

    public void setMailSentAt(Date mailSentAt) {
        this.mailSentAt = mailSentAt;
    }

    public String getSfUpsertFlg() {
        return sfUpsertFlg;
    }

    public void setSfUpsertFlg(String sfUpsertFlg) {
        this.sfUpsertFlg = sfUpsertFlg;
    }

    public Date getSfUpsertAt() {
        return sfUpsertAt;
    }

    public void setSfUpsertAt(Date sfUpsertAt) {
        this.sfUpsertAt = sfUpsertAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }


}
