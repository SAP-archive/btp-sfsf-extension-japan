package com.sap.sfsf.reshuffle.comparison.backend.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@Entity
@Table(name = "CANDIDATE")
@IdClass(CandidateId.class)
public class Candidate {
    @Id
    @Column(name = "CASEID")
    private String caseID;

    @Id
    @Column(name = "POSITIONID")
    private String positionID;

    @Column(name = "POSITIONNAME")
    private String positionName;
    
    @Id
    @Column(name = "CANDIDATEID")
    private String candidateID;

    @Column(name = "DEPARTMENTID")
    private String departmentID;
    
    @Column(name = "DEPARTMENTNAME")
    private String departmentName;

    @Column(name = "DIVISIONID")
    private String divisionID;

    @Column(name = "DIVISIONNAME")
    private String divisionName;

    @Column(name = "JOBGRADEID")
    private String jobGradeID;

    @Column(name = "JOBGRADENAME")
    private String jobGradeName;
    
    @Column(name = "CANDIDATEDEPARTMENTID")
    private String candidateDepartmentID;

    @Column(name = "CANDIDATEDEPARTMENTNAME")
    private String candidateDepartmentName;

    @Column(name = "CANDIDATEDIVISIONID")
    private String candidateDivisionID;

    @Column(name = "CANDIDATEDIVISIONNAME")
    private String candidateDivisionName;

    @Column(name = "CANDIDATEPOSITIONID")
    private String candidatePositionID;

    @Column(name = "CANDIDATEPOSITIONNAME")
    private String candidatePositionName;

    @Column(name = "CANDIDATEJOBGRADEID")
    private String candidateJobGradeID;

    @Column(name = "CANDIDATEJOBGRADENAME")
    private String candidateJobGradeName;

    @Column(name = "CANDIDATEJOBTENURE")
    private Integer candidateJobTenure;

    @Column(name = "CANDIDATELASTRATING1")
    private String candidateLastRating1;

    @Column(name = "CANDIDATELASTRATING2")
    private String candidateLastRating2;
    
    @Column(name = "CANDIDATELASTRATING3")
    private String candidateLastRating3;

    @Column(name = "CANDIDATETRANSFERTIMES")
    private Integer candidateTransferTimes;

    @Column(name = "CANDIDATETRANSFERREASON")
    private String candidateTransferReason;

    @Column(name = "CANDIDATECERTIFICATION")
    private String candidateCertification;

    @Column(name = "CANDIDATERESHUFFLECOST")
    private BigInteger candidateReshuffleCost;

    @Column(name = "CANDIDATEMANAGERID")
    private String candidateManagerID;

    @Column(name = "INCUMBENTEMPID")
    private String incumbentEmpID;

    @Column(name = "INCUMBENTEMPNAME")
    private String incumbentEmpName;

    @Column(name = "INCUMBENTEMPRETIREMENTINTENTION")
    private String incumbentEmpRetirementInvention;

    @Column(name = "INCUMBENTEMPMANAGER")
    private String incumbentEmpManager;

    @Column(name = "SIMULATIONCHECKRESULT")
    private String simulationCheckResult;

    @Column(name = "SIMULATIONCHECKSTATUS")
    private String simulationCheckStatus;

    @Column(name = "SIMULATIONCHECKDATETIME")
    private Date simulationCheckDatetime;

    @Column(name = "WFSTATUS")
    private String wfStatus;

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
    private Date modifiledAt;

    @Column(name = "MODIFIEDBY")
    private String modifiledBy;
}