package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import java.time.LocalDateTime;
import java.util.Map;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.applicants.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;

import lombok.Data;

@Data
public class CurrentPositionFilter {
	private String company;
	private String businessUnit;
	private String division;
	private String department;
	private String position;
	private String tenurePositionLL;
	private String tenurePositionUL;
	private String willingness;
	private String ratingType;
	private String retirement;
	
	private final static String TERMINATIONCODE = "3680";//PropertyUtil.getProperty("sfsf_termination_code");
	
	public CurrentPositionFilter(Map<String, String> params) {
		this.company = params.get("company");
		this.businessUnit = params.get("businessUnit");
		this.division = params.get("division");
		this.department = params.get("department");
		this.position = params.get("position");
		this.tenurePositionLL = params.get("tenureLL");
		this.tenurePositionUL = params.get("tenureUL");
		this.willingness = params.get("willingness");
		this.ratingType = params.get("ratingType");
		this.retirement = params.get("retirement");
	}
	
	public FilterExpression addArgsFilter(FilterExpression filter) {
		if(company != null) filter = filter.and(new FilterExpression("company", "eq", ODataType.of(company)));
		if(businessUnit != null) filter = filter.and(new FilterExpression("businessUnit", "eq", ODataType.of(businessUnit)));
		if(department != null) filter = filter.and(new FilterExpression("department", "eq", ODataType.of(department)));
		if(division != null) filter = filter.and(new FilterExpression("division", "eq", ODataType.of(division)));
		if(position != null) filter = filter.and(new FilterExpression("position", "eq", ODataType.of(position)));
		if(tenurePositionLL != null) {
			int tenureLL = Integer.parseInt(tenurePositionLL);
			LocalDateTime tenureLLDate = DateTimeUtil.getYearsAgo(tenureLL);
			filter = filter.and(new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(tenureLLDate)));
		}
		if(tenurePositionUL != null) {
			int tenureUL = Integer.parseInt(tenurePositionUL);
			LocalDateTime tenureULDate = DateTimeUtil.getYearsAgo(tenureUL);
			filter = filter.and(new CustomFilterExpression("startDate", "ge", DateTimeUtil.getDateTimeFilter(tenureULDate.plusYears(-1))));
		}
		if(retirement != null) {
			String operator = null;
			if(retirement.equals("yes")) {
				operator = "eq";
			} else if(retirement.equals("no")) {
				operator = "ne";
			}
			if(operator != null) {
				filter = filter.and(new FilterExpression("event", operator, ODataType.of(TERMINATIONCODE)));
			}
		}
		
		return filter;
	}
	
	public String getRatingType() {
		return ratingType;
	}
	
	public Boolean isWillingness() {
		Boolean isWillingness = null;
		if(willingness != null) {
			if(willingness.equals("yes")) {
				isWillingness = true;
			} else if(willingness.equals("no")) {
				isWillingness = false;
			}			
		}
		return isWillingness;
	}
}
