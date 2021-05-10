package com.sap.sfsf.reshuffle.simulation.backend;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Test;

import com.sap.cloud.sdk.datamodel.odata.helper.Order;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJob;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJobFluentHelper;
import com.sap.sfsf.vdm.services.DefaultECEmploymentInformationService;

public class TestUnitODataQuery {

	@Test
	public void testGetCurrentJobByID() {
		LocalDateTime endDate = java.time.OffsetDateTime.of(9999, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
		LocalDateTime today = java.time.OffsetDateTime.now(ZoneId.of("Z")).toLocalDateTime();
		
		EmpJobFluentHelper empQuery = new DefaultECEmploymentInformationService()
				.withServicePath("odata/v2")
				.getAllEmpJob()
				.select(
						EmpJob.USER_ID,
						EmpJob.START_DATE,
						EmpJob.END_DATE,
						EmpJob.EVENT,
						EmpJob.EVENT_REASON
						)
				.filter(EmpJob.USER_ID.eq("mnadal")
						.and(EmpJob.END_DATE.eq(endDate))
						.and(EmpJob.START_DATE.le(today))
						//.and(EmpJob.EVENT.eq("3681"))
						)
				.top(1)
				.orderBy(EmpJob.END_DATE, Order.DESC);
		System.out.println("Query:"+empQuery.toQuery().toString());
	}
}
