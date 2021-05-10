package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
	 private static LocalDateTime today = java.time.OffsetDateTime.now(ZoneId.of("Z")).toLocalDateTime();
	 private static int nextYear = today.getYear() + 1;
	/**
	 * '9999-12-31'のLocalDateTimeを取得
	 * @return
	 */
	public static LocalDateTime getEndDate() {
		LocalDateTime endDate = java.time.OffsetDateTime.of(9999, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
		return endDate;
	}
	
	public static LocalDateTime getNextNewYearDate() {
		return java.time.OffsetDateTime.of(nextYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
	}
	
	public static LocalDateTime getNextEndYearDate() {
		return java.time.OffsetDateTime.of(nextYear, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
	}
	
	/**
	 * 今日のLocalDateTimeを取得、時刻は00:00:00 000
	 * @return
	 */
	public static LocalDateTime getToday() {
		return today.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
    public static String toStr(LocalDateTime dateTime, String format) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(dateTimeFormatter);
    }
    
    public static String toStr(OffsetDateTime dateTime, String format) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(dateTimeFormatter);
    }
	
	
	/**
	 * N年前の今日を取得
	 * @param years
	 * @return
	 */
	public static LocalDateTime getYearsAgo(int years) {
		LocalDateTime today = getToday();
		LocalDateTime yearsAgo = today.plusYears(0-years);
		return yearsAgo;
	}
	
	public static LocalDateTime getEndDate(LocalDateTime startDate, int span) {
		LocalDateTime monthsAgo = startDate.plusMonths((long)span);
		return monthsAgo;
	}
	
	public static String formatDateTime(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return date.format(formatter);
	}
	
	public static String formatDateTime(Date orgdate) {
		LocalDateTime date = toLocalDateTime(orgdate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return date.format(formatter);
	}
	
	public static String getDateTimeFilter(LocalDateTime localDT) {
		return String.format("datetime'%s'", formatDateTime(localDT));
	}
	
	public static String getDateTimeFilter(Date date) {
		LocalDateTime localDT = toLocalDateTime(date);
		return String.format("datetime'%s'", formatDateTime(localDT));
	}
	public static Date getDate(LocalDateTime localDT) {
		long epoch = localDT.toEpochSecond(ZoneOffset.of("Z"));
		return new Date(epoch);
	}
	
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
    
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }
}