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

	@Column(name = "CURRENTDEPARTMENT")
	private String currentDepartment;

	@Column(name = "CURRENTDIVISION")
	private String currentDivision;

    @Column(name = "CURRENTMANAGER")
    private String currentManager;   

	@Column(name = "CURRENTPOSITION")
	private String currentPosition;

	@Column(name = "NEXTDEPARTMENT")
	private String nextDepartment;

	@Column(name = "NEXTDIVISION")
	private String nextDivision;

    @Column(name = "NEXTMANAGER")
    private String nextManager;   

	@Column(name = "NEXTPOSITION")
	private String nextPosition;
	
	@Column(name = "CURRENTDEPARTMENTNAME")
	private String currentDepartmentName;

	@Column(name = "CURRENTDIVISIONNAME")
	private String currentDivisionName;

	@Column(name = "CURRENTPOSITIONNAME")
	private String currentPositionName;

	@Column(name = "NEXTDEPARTMENTNAME")
	private String nextDepartmentName;

	@Column(name = "NEXTDIVISIONNAME")
	private String nextDivisionName;

	@Column(name = "NEXTPOSITIONNAME")
	private String nextPositionName;

	@Column(name = "TRANSFERTIMES")
	private Integer transferTimes;

	@Column(name = "LASTTRANSREASON")
	private String lastTransReason;

	@Column(name = "CERTIFICATION")
	private String certification;
	
	@Column(name = "NEXTJOBGRADE")
	private String nextJobGrade;
	@Column(name = "NEXTJOBGRADENAME")
	private String nextJobGradeName;
	
	@Column(name = "CURRENTJOBGRADE")
	private String currentJobGrade;
	@Column(name = "CURRENTJOBGRADENAME")
	private String currentJobGradeName;
	
	@Column(name = "JOBTENURE")
	private Integer jobTenure;
	
	@Column(name = "CURRENTEMPRETIRE")
	private String currentEmpRetire;

	@Column(name = "CHECKSTATUS")
	private String checkStatus;

	@Column(name = "CHECKRESULT")
	private String checkResult;
	
	@Column(name = "CHECKDATETIME")
	private Date checkDateTime;

	@Column(name = "MAILSENTFLG")
	private String mailSentFlg;

	@Column(name = "MAILSENTAT")
	private Date mailSentAt;

	@Column(name = "upsertflg")
	private String upsertFlg;
	
	@Column(name = "upsertat")
	private Date upsertAt;
	
	@Column(name = "RATING1")
	private String rating1;
	@Column(name = "RATING2")
	private String rating2;
	@Column(name = "RATING3")
	private String rating3;

	public Candidate(){
	}

	public Candidate(String userId, String nextPosition){
		this.candidateID = userId;
		this.nextPosition = nextPosition;
		this.checkResult = "";
	}

	public String getCaseID() {
		return caseID;
	}
	
	public String getCandidateID() {
		return candidateID;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public String getCertification() {
		return certification;
	}

	public Date getCheckDateTime() {
		return checkDateTime;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public String getCurrentDepartment() {
		return currentDepartment;
	}

	public String getCurrentDepartmentName() {
		return currentDepartmentName;
	}

	public String getCurrentDivision() {
		return currentDivision;
	}

	public String getCurrentDivisionName() {
		return currentDivisionName;
	}

	public String getCurrentEmpRetire() {
		return currentEmpRetire;
	}

	public String getCurrentJobGrade() {
		return currentJobGrade;
	}

	public String getCurrentJobGradeName() {
		return currentJobGradeName;
	}

	public String getCurrentManager() {
		return currentManager;
	}

	public String getCurrentPosition() {
		return currentPosition;
	}

	public String getCurrentPositionName() {
		return currentPositionName;
	}

	public Integer getJobTenure() {
		return jobTenure;
	}

	public String getLastTransReason() {
		return lastTransReason;
	}

	public String getNextDepartment() {
		return nextDepartment;
	}

	public String getNextDepartmentName() {
		return nextDepartmentName;
	}

	public String getNextDivision() {
		return nextDivision;
	}

	public String getNextDivisionName() {
		return nextDivisionName;
	}

	public String getNextJobGrade() {
		return nextJobGrade;
	}

	public String getNextJobGradeName() {
		return nextJobGradeName;
	}

	public String getNextPosition() {
		return nextPosition;
	}

	public String getNextManager() {
		return nextManager;
	}

	public String getNextPositionName() {
		return nextPositionName;
	}

	public Integer getTransferTimes() {
		return transferTimes;
	}

	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public void setCheckDateTime(Date checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public void setCurrentDepartment(String currentDepartment) {
		this.currentDepartment = currentDepartment;
	}

	public void setCurrentDepartmentName(String currentDepartmentName) {
		this.currentDepartmentName = currentDepartmentName;
	}

	public void setCurrentDivision(String currentDivision) {
		this.currentDivision = currentDivision;
	}

	public void setCurrentDivisionName(String currentDivisionName) {
		this.currentDivisionName = currentDivisionName;
	}

	public void setCurrentEmpRetire(String currentEmpRetire) {
		this.currentEmpRetire = currentEmpRetire;
	}

	public void setCurrentJobGrade(String currentJobGrade) {
		this.currentJobGrade = currentJobGrade;
	}

	public void setCurrentJobGradeName(String currentJobGradeName) {
		this.currentJobGradeName = currentJobGradeName;
	}

	public void setCurrentManager(String currentManager) {
		this.currentManager = currentManager;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}

	public void setCurrentPositionName(String currentPositionName) {
		this.currentPositionName = currentPositionName;
	}

	public void setJobTenure(Integer jobTenure) {
		this.jobTenure = jobTenure;
	}

	public void setLastTransReason(String lastTransReason) {
		this.lastTransReason = lastTransReason;
	}

	public void setNextDepartment(String nextDepartment) {
		this.nextDepartment = nextDepartment;
	}

	public void setNextDepartmentName(String nextDepartmentName) {
		this.nextDepartmentName = nextDepartmentName;
	}

	public void setNextDivision(String nextDivision) {
		this.nextDivision = nextDivision;
	}

	public void setNextDivisionName(String nextDivisionName) {
		this.nextDivisionName = nextDivisionName;
	}

	public void setNextJobGrade(String nextJobGrade) {
		this.nextJobGrade = nextJobGrade;
	}

	public void setNextJobGradeName(String nextJobGradeName) {
		this.nextJobGradeName = nextJobGradeName;
	}

	public void setNextManager(String nextManager) {
		this.nextManager = nextManager;
	}

	public void setNextPosition(String nextPosition) {
		this.nextPosition = nextPosition;
	}

	public void setNextPositionName(String nextPositionName) {
		this.nextPositionName = nextPositionName;
	}

	public void setTransferTimes(Integer transferTimes) {
		this.transferTimes = transferTimes;
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

	public String getRating1() {
		return rating1;
	}

	public void setRating1(String rating1) {
		this.rating1 = rating1;
	}

	public String getRating2() {
		return rating2;
	}

	public void setRating2(String rating2) {
		this.rating2 = rating2;
	}

	public String getRating3() {
		return rating3;
	}

	public void setRating3(String rating3) {
		this.rating3 = rating3;
	}

	public String getUpsertFlg() {
		return upsertFlg;
	}

	public void setUpsertFlg(String upsertFlg) {
		this.upsertFlg = upsertFlg;
	}

	public Date getUpsertAt() {
		return upsertAt;
	}

	public void setUpsertAt(Date upsertAt) {
		this.upsertAt = upsertAt;
	}


}
