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
     * 
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
     * 
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
     * 
     * @param years
     * @return
     */
    public static LocalDateTime getYearsAgo(int years) {
        return getToday().plusYears(0 - years);
    }

    public static LocalDateTime getEndDate(LocalDateTime startDate, int span) {
        return startDate.plusMonths((long) span);
    }

    public static String formatDateTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static String formatDateTime(Instant instant) {
        return toLocalDateTime(instant).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static String getDateTimeFilter(LocalDateTime localDT) {
        return String.format("datetime'%s'", formatDateTime(localDT));
    }

    public static String getDateTimeFilter(Instant instant) {
        return String.format("datetime'%s'", formatDateTime(toLocalDateTime(instant)));
    }

    public static Date getDate(LocalDateTime localDT) {
        return new Date(localDT.toEpochSecond(ZoneOffset.of("Z")));
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(ZonedDateTime.of(localDateTime, ZoneId.of("UTC")).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneId.of("UTC")).toInstant();
    }
}
