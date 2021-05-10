package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyBusinessUnit;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyCompany;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDepartment;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDivision;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyPosition;
import com.sap.sfsf.reshuffle.simulation.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;

@Service
public class FilterService {
	private Logger LOG = LoggerFactory.getLogger(FilterService.class);
	
	public List<MyCompany> getCompanyByQuery(){

		LOG.debug("=== Get company start ===");
		
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"externalCode","startDate","endDate","name_ja_JP"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP","ne", "null");
		
		List<MyCompany> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FOCompany")
				.select(selects)
				.filter(filter1.and(filter2).and(filter3))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyCompany.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get company end ===");
		return list;
	}
	
	public List<MyBusinessUnit> getBusinessUnitByQuery(){

		LOG.debug("=== Get MyBusinessUnit start ===");
		
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"externalCode","startDate","endDate","name_ja_JP"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP","ne", "null");
		
		List<MyBusinessUnit> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FOBusinessUnit")
				.select(selects)
				.filter(filter1.and(filter2).and(filter3))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyBusinessUnit.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get MyBusinessUnit end ===");
		return list;
	}
	
	/**
	 * Divisionを取得
	 * @return
	 */
	public List<MyDivision> getDivisionByQuery() {
		LOG.debug("=== Get division start ===");
		
		LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"externalCode","startDate","endDate","name_ja_JP","cust_toBusinessUnit/externalCode"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP","ne", "null");
		
		List<MyDivision> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FODivision")
				.select(selects)
				.expand("cust_toBusinessUnit")
				.filter(filter1.and(filter2).and(filter3))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyDivision.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get division end ===");
		return list;
	}
	
	
	/**
	 * Departmentを取得
	 * @return
	 */
	public List<MyDepartment> getDepartmentByQuery() {
		LOG.debug("=== Get department start ===");
		
		LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"externalCode","startDate","endDate","name_ja_JP","cust_toDivision/externalCode"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("name_ja_JP","ne", "null");
		
		List<MyDepartment> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FODepartment")
				.select(selects)
				.expand("cust_toDivision")
				.filter(filter1.and(filter2).and(filter3))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyDepartment.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get department end ===");
		return list;
	}
	
	
	/**
	 * Departmentを取得
	 * @return
	 */
	public List<MyPosition> getPositionByQuery() {
		LOG.debug("=== Get position start ===");
		
		//LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"code","effectiveStartDate","effectiveEndDate","externalName_ja_JP",
				"company","businessUnit","division","department"};
		CustomFilterExpression filter1 = new CustomFilterExpression("effectiveStartDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("effectiveEndDate","gt",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter3 = new CustomFilterExpression("externalName_ja_JP","ne", "null");
		
		//FilterExpression countryFilter = new FilterExpression("companyNav/country", "eq", ODataType.of("JPN"));
		
		List<MyPosition> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "Position")
				.select(selects)
				.expand("companyNav,businessUnitNav,departmentNav")
				.filter(filter1.and(filter2).and(filter3))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyPosition.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get position end ===");
		return list;
	}
}
