package com.sap.sfsf.reshuffle.simulation.backend.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

	/**
	 * '9999-12-31'のLocalDateTimeを取得
	 * @return
	 */
	public static LocalDateTime getEndDate() {
		LocalDateTime endDate = java.time.OffsetDateTime.of(9999, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDateTime();
		return endDate;
	}
	
	/**
	 * 今日のLocalDateTimeを取得、時刻は00:00:00 000
	 * @return
	 */
	public static LocalDateTime getToday() {
		LocalDateTime today = java.time.OffsetDateTime.now(ZoneId.of("Z")).toLocalDateTime();
		return today.withHour(0).withMinute(0).withSecond(0).withNano(0);
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
	
	public static String formatDateTime(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return date.format(formatter);
	}
	
	public static String formatDateTokyo(Date date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		return LocalDateTime.ofInstant(date.toInstant(),ZoneId.of("Asia/Tokyo")).format(formatter);
	}
	
	public static String formatJobDateTokyo(Date date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		return LocalDateTime.ofInstant(date.toInstant(),ZoneId.of("Asia/Tokyo")).format(formatter);
	}
	
	public static String formatDateTokyoZip(Date date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return LocalDateTime.ofInstant(date.toInstant(),ZoneId.of("Asia/Tokyo")).format(formatter);
	}
	
	public static String getDateTimeFilter(LocalDateTime localDT) {
		return String.format("datetime'%s'", formatDateTime(localDT));
	}
	
	public static Date getDate(LocalDateTime localDT) {
		long epoch = localDT.toEpochSecond(ZoneOffset.of("Z"));
		return new Date(epoch);
	}
}
