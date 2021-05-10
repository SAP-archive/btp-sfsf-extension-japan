package com.sap.sfsf.reshuffle.applicants.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CANDIDATE")
public class Candidate {
    @Id
    @Column(name = "CANDIDATEID")
    private String candidateID;
    
    @Column(name = "CANDIDATENAME")
    private String candidateName;
    
    @Column(name = "NEXTDIVISION")
    private String nextDivision;
    
    @Column(name = "NEXTDIVISIONNAME")
    private String nextDivisionName;
    
    @Column(name = "NEXTDEPARTMENT")
    private String nextDepartment;
    
    @Column(name = "NEXTDEPARTMENTNAME")
    private String nextDepartmentName;

    @Column(name = "NEXTMANAGER")
    private String nextManager;   

    @Column(name = "NEXTPOSITION")
    private String nextPosition;

    @Column(name = "NEXTPOSITIONNAME")
    private String nextPositionName;

    @Column(name = "NEXTJOBGRADE")
    private String nextJobGrade;

    @Column(name = "NEXTJOBGRADENAME")
    private String nextJobGradeName;
    
    @Column(name = "CURRENTEMPID")
    private String currentEmpId;
    
    @Column(name = "CURRENTEMPNAME")
    private String currentEmpName;
    
    @Column(name = "CURRENTDIVISION")
    private String currentDivision;
    
    @Column(name = "CURRENTDIVISIONNAME")
    private String currentDivisionName;

    @Column(name = "CURRENTDEPARTMENT")
    private String currentDepartment;

    @Column(name = "CURRENTDEPARTMENTNAME")
    private String currentDepartmentName;

    @Column(name = "CURRENTMANAGER")
    private String currentManager;   

    @Column(name = "CURRENTPOSITION")
    private String currentPosition;   

    @Column(name = "CURRENTPOSITIONNAME")
    private String currentPositionName;
    
    @Column(name = "CURRENTJOBGRADE")
    private String currentJobGrade;
    
    @Column(name = "CURRENTJOBGRADENAME")
    private String currentJobGradeName;

    @Column(name = "CURRENTEMPRETIRE")
    private String currentEmpRetire;
    
    @Column(name = "RATING1")
    private String rating1;

    @Column(name = "RATING2")
    private String rating2;
    
    @Column(name = "RATING3")
    private String rating3;
    
    @Column(name = "JOBTENURE")
    private int jobTenure;

    @Column(name = "TRANSFERTIMES")
    private int transferTimes;

    @Column(name = "LASTTRANSREASON")
    private String lastTransReason;

    @Column(name = "CERTIFICATION")
    private String certification;

    @Column(name = "CHECKRESULT")
    private String checkResult;

    @Column(name = "CHECKSTATUS")
    private String checkStatus;
    
    @Column(name = "CHECKDATETIME")
    private Date checkDateTime;    
}
