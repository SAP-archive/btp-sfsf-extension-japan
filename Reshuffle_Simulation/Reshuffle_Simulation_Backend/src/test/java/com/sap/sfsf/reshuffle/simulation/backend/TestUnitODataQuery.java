package com.sap.sfsf.reshuffle.simulation.backend;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.joda.time.DateTime;
import org.junit.Test;

import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataProperty;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJob;
import com.sap.sfsf.reshuffle.simulation.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.simulation.backend.util.FilterUtil;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJob;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJobFluentHelper;
import com.sap.sfsf.vdm.services.DefaultECEmploymentInformationService;

public class TestUnitODataQuery {

	@Test
	public void testGetCurrentJobByID() {
		LocalDateTime endDate = java.time.OffsetDateTime.of(9999, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
		LocalDateTime today = java.time.OffsetDateTime.now(ZoneId.of("Z")).toLocalDateTime();

		EmpJobFluentHelper empQuery = new DefaultECEmploymentInformationService().withServicePath("odata/v2")
				.getAllEmpJob()
				.select(EmpJob.USER_ID, EmpJob.START_DATE, EmpJob.END_DATE, EmpJob.EVENT, EmpJob.EVENT_REASON)
				.filter(EmpJob.USER_ID.eq("mnadal").and(EmpJob.END_DATE.eq(endDate)).and(EmpJob.START_DATE.le(today))
				// .and(EmpJob.EVENT.eq("3681"))
				).top(1).orderBy(EmpJob.END_DATE, Order.DESC);
		System.out.println("Query:" + empQuery.toQuery().toString());
	}

	@Test
	public void testFilterIn() {
		String[] selects = { "personIdExternal", "relatedPersonIdExternal" };
		List<String> idList = new ArrayList<String>();
		idList.add("100001");
		idList.add("100002");
		FilterExpression filter = FilterUtil.generateFilter("personaIdExternal", idList);
		ODataQuery query = ODataQueryBuilder.withEntity("odata/v2", "PerPersonRelationship").select(selects)
				.filter(filter).build();
		System.out.println(query.toString());
	}

	@Test
	public void testQueryBuilder() {

		LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();

		String[] selects = { "userId", "startDate", "endDate", "event", "eventReason",
				"positionNav/parentPosition/code" };
		CustomFilterExpression filter = new CustomFilterExpression("startDate", "eq",
				DateTimeUtil.getDateTimeFilter(today));
		/*
		 * FilterExpression filter =
		 * ODataProperty.field("startDate").le(ODataType.of(DateTimeUtil.
		 * getDateTimeFilter(today))).and(
		 * ODataProperty.field("endDate").eq(ODataType.of(DateTimeUtil.getDateTimeFilter
		 * (endDate))));
		 */
		List<MyEmpJob> list = null;
		ODataQuery query = ODataQueryBuilder.withEntity("odata/v2", "EmpJob").select(selects)
				.expand("positionNav/parentPosition").filter(filter).build();
		System.out.println("Query:" + query.toString());
	}


}
