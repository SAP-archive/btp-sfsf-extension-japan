package com.sap.sfsf.reshuffle.comparison.backend.model;

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

    @Column(name = "MODIFIEDAT")
    private Date modifiedAt;

    @Column(name = "MODIFIEDBY")
    private String modifiedBy;

    @Column(name = "CREATEDAT")
    private Date createdAt;

    @Column(name = "CREATEDBY")
    private String createdBy;

    public String getCaseID() {
		return caseID;
    }
    public String getCandidateID() {
		return candidateID;
    }
    public String getNextPosition() {
		return nextPosition;
    }
    public Date getCreatedAt() {
		return createdAt;
    }
    public Date getModifiedAt() {
		return modifiedAt;
    }
    public String getModifiedBy() {
		return modifiedBy;
    }
    public String getCreatedBy() {
		return createdBy;
    }
    public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
    }
    public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
    }
    public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
    }
    public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}