package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="HISTORY")
public class History {
	@Column(name="CHECKEDBY")
	private String checkedBy;
	
	@Id
	@Column(name="CHECKEDAT")
	private Date checkedAt;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="TOTALCNT")
	private Long totalCnt;
	
	@Column(name="OKCNT")
	private Long okCnt ;
	
	@Column(name="NGCNT")
	private Long ngCnt ;
	
	@Column(name="WARNCNT")
	private Long warnCnt ;
	
	public Date getCheckedAt() {
		return checkedAt;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public Long getNgCnt() {
		return ngCnt;
	}
	public Long getOkCnt() {
		return okCnt;
	}
	public String getStatus() {
		return status;
	}
	public Long getTotalCnt() {
		return totalCnt;
	}
	public Long getWarnCnt() {
		return warnCnt;
	}
	public void setCheckedAt(Date checkedAt) {
		this.checkedAt = checkedAt;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public void setNgCnt(Long ngCnt) {
		this.ngCnt = ngCnt;
	}
	public void setOkCnt(Long okCnt) {
		this.okCnt = okCnt;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setTotalCnt(Long totalCnt) {
		this.totalCnt = totalCnt;
	}
	public void setWarnCnt(Long warnCnt) {
		this.warnCnt = warnCnt;
	}
	
	
	
	
}
