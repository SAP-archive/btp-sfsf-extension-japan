package com.sap.sfsf.reshuffle.simulation.backend;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

public class TestUnitDateTime {

	@Test
	public void testDateTime() {

		LocalDateTime endDate = LocalDateTime.of(9999, 12, 31, 0, 0);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime ldt = java.time.OffsetDateTime.of(9999, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = java.time.OffsetDateTime.now(ZoneId.of("Z")).toLocalDateTime();
		
		formatter.withZone(ZoneId.of("Asia/Tokyo"));
		System.out.println(ldt.format(formatter));
		System.out.println(ldt.toInstant(ZoneOffset.UTC).toEpochMilli());
		System.out.println(now.toInstant(ZoneOffset.UTC).toEpochMilli());
		;
		System.out.println(now.format(formatter));
		System.out.println("=== End Date:"+endDate.format(formatter ));
		System.out.println("=== Today:"+today.format(formatter));
		
		endDate =  LocalDateTime.ofInstant(Instant.ofEpochMilli(new org.joda.time.DateTime(9999,12,31,00,00).getMillis()), ZoneId.of("UTC"));
		today = LocalDateTime.ofInstant(Instant.ofEpochMilli(new org.joda.time.DateTime().getMillis()), ZoneId.of("UTC"));
		
		System.out.println("=== End Date:"+endDate.format(formatter ));
		System.out.println("=== Today:"+today.format(formatter));
	}
}
