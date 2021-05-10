package com.sap.sfsf.reshuffle.applicants.backend.services;

import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.BusinessUnitFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CompanyFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DepartmentFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.DivisionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.PositionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;

@Service
public class FilterService {
	private Logger Logger = LoggerFactory.getLogger(FilterService.class);

	public List<CompanyFilter> getCompanyFilter() throws Exception, ODataException {
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = {"externalCode", "startDate", "endDate", "name_ja_JP", "country"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP", "ne", "null");
		FilterExpression filters = filter1.and(filter2).and(filter3);

		List<CompanyFilter> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FOCompany")
				.select(selects)
				.filter(filters)
				.build();

		Logger.debug("Business Unit Filter Query: " + query.toString());

		list = query.execute("SFSF_2nd")
				.asList(CompanyFilter.class);

		return list;
	}

	public List<BusinessUnitFilter> getBUFilter() throws Exception, ODataException {
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = {"externalCode", "startDate", "endDate", "name_ja_JP"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP", "ne", "null");
		FilterExpression filters = filter1.and(filter2).and(filter3);

		List<BusinessUnitFilter> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FOBusinessUnit")
				.select(selects)
				.filter(filters)
				.build();

		Logger.debug("Business Unit Filter Query: " + query.toString());

		list = query.execute("SFSF_2nd")
				.asList(BusinessUnitFilter.class);

		return list;
	}

	public List<DivisionFilter> getDivisionFilter() throws Exception, ODataException {
		//LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = {"externalCode", "startDate", "endDate", "name_ja_JP", "cust_toBusinessUnit/externalCode"};
		String expand = "cust_toBusinessUnit";
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP", "ne", "null");

		List<DivisionFilter> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FODivision")
				.select(selects)
				.filter(filter1.and(filter2).and(filter3))
				.expand(expand)
				.build();

		Logger.debug("Division Filter Query: " + query.toString());

		list = query.execute("SFSF_2nd")
				.asList(DivisionFilter.class);

		return list;
	}

	public List<DepartmentFilter> getDepartmentFilter() throws Exception, ODataException {
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = {"externalCode", "startDate", "endDate", "name_ja_JP", "cust_toDivision/externalCode"};
		String expand = "cust_toDivision";
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP", "ne", "null");

		List<DepartmentFilter> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FODepartment")
				.select(selects)
				.expand(expand)
				.filter(filter1.and(filter2).and(filter3))
				.build();

		Logger.debug("Department Filter Query: " + query.toString());

		list = query.execute("SFSF_2nd")
				.asList(DepartmentFilter.class);

		return list;
	}

	public List<PositionFilter> getPositionFilter() throws Exception, ODataException {
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = {"code", "effectiveStartDate", "effectiveEndDate", "externalName_ja_JP", "company", "businessUnit", "division", "department"};
		String expand = "companyNav";
		CustomFilterExpression filter1 = new CustomFilterExpression("effectiveStartDate", "le", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("effectiveEndDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("externalName_ja_JP", "ne", "null");
		FilterExpression countryFilter = new FilterExpression("companyNav/country", "eq", ODataType.of("JPN"));

		List<PositionFilter> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "Position")
				.select(selects)
				.expand(expand)
				.filter(filter1.and(filter2).and(filter3).and(countryFilter))
				.build();

		Logger.info("Position Filter Query: " + query.toString());

		list = query.execute("SFSF_2nd")
				.asList(PositionFilter.class);

		return list;
	}

}
