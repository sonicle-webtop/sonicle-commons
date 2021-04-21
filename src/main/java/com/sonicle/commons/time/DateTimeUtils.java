/* 
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.commons.time;

import java.text.DateFormatSymbols;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author malbinola
 */
public class DateTimeUtils {
	public static final LocalTime TIME_AT_STARTOFDAY = new LocalTime(0, 0, 0, 0);
	public static final LocalTime TIME_AT_ENDOFDAY = new LocalTime(23, 59, 59, 0);
	
	public static String[] getDayNamesShort(Locale locale) {
		return new DateFormatSymbols(locale).getShortWeekdays();
	}
	
	public static String getDayNameShort(int day, Locale locale) {
		return getDayNamesShort(locale)[day];
	}
	
	public static String[] getDayNamesLong(Locale locale) {
		return new DateFormatSymbols(locale).getWeekdays();
	}
	
	public static String getDayNameLong(int day, Locale locale) {
		return getDayNamesLong(locale)[day];
	}
	
	public static String[] getMonthNamesShort(Locale locale) {
		return new DateFormatSymbols(locale).getShortMonths();
	}
	
	public static String getMonthNameShort(int month, Locale locale) {
		return getMonthNamesShort(locale)[month-1];
	}
	
	public static String[] getMonthNamesLong(Locale locale) {
		return new DateFormatSymbols(locale).getMonths();
	}
	
	public static String getMonthNameLong(int month, Locale locale) {
		return getMonthNamesLong(locale)[month-1];
	}
	
	public static DateTime now() {
		return now(false);
	}
	
	public static DateTime now(boolean exact) {
		if(exact) {
			return DateTime.now(DateTimeZone.UTC);
		} else {
			return DateTime.now(DateTimeZone.UTC).withMillisOfSecond(0);
		}
	}
	
	public static boolean isMidnight(DateTime dt) {
		return (dt.compareTo(dt.withTimeAtStartOfDay()) == 0);
	}
	
	public static boolean isEndOfDay(DateTime dt) {
		return isEndOfDay(dt, false);
	}
	
	public static boolean isEndOfDay(DateTime dt, boolean relaxed) {
		if (relaxed) {
			return ((dt.getHourOfDay() == 23) && (dt.getMinuteOfHour() == 59));
		} else {
			return (dt.compareTo(withTimeAtEndOfDay(dt)) == 0);
		}	
	}
	
	public static boolean isDayBefore(DateTime dt1, DateTime dt2) {
		return isDayBefore(dt1.toLocalDate(), dt2.toLocalDate());
	}
	
	public static boolean isDayBefore(LocalDate l1, LocalDate l2) {
		return l1.isBefore(l2) && (Days.daysBetween(l1, l2).getDays() == 1);
	}
	
	public static DateTime withTimeAtStartOfDay(DateTime dt) {
		if (dt == null) return null;
		return dt.withTimeAtStartOfDay();
	}
	
	public static DateTime withTimeAtStartOfDay(LocalDate ld, DateTimeZone tz) {
		if (ld == null) return null;
		return ld.toDateTimeAtStartOfDay(tz);
	}
	
	public static DateTime withTimeAtMidday(DateTime dt) {
		if (dt == null) return null;
		return dt.withTime(12, 0, 0, 0);
	}
	
	public static DateTime withTimeAtEndOfDay(LocalDate ld, DateTimeZone tz) {
		if (ld == null) return null;
		return withTimeAtEndOfDay(withTimeAtStartOfDay(ld, tz));
	}
	
	public static DateTime withTimeAtEndOfDay(DateTime dt) {
		if (dt == null) return null;
		return dt.withTime(23, 59, 59, 0);
	}
	
	public static DateTime ceilTimeAtEndOfDay(DateTime dt) {
		return (isEndOfDay(dt, true)) ? withTimeAtEndOfDay(dt) : dt;
	}
	
	
	public static boolean startsInDay(DateTime day, ReadableInstant instant) {
		DateTime dayFrom = day.withTimeAtStartOfDay();
		DateTime dayTo = dayFrom.plusDays(1);
		return (instant.compareTo(dayFrom) >= 0) && instant.isBefore(dayTo);
	}
	
	public static boolean endsInDay(DateTime day, ReadableInstant instant) {
		DateTime dayFrom = day.withTimeAtStartOfDay();
		DateTime dayTo = dayFrom.plusDays(1);
		return instant.isAfter(dayFrom) && instant.isBefore(dayTo); // We need to exclude midnight!
	}
	
	public static Days daysBetween(ReadableInstant instant1, ReadableInstant instant2) {
		return (instant1 != null && instant2 != null) ? Days.daysBetween(instant1, instant2) : null; 
	}
	
	/**
	 * Returns the difference in days (or dates) not keeping into account the
	 * real amount of time passed between the two dates.
	 * To avoid wrong calculations, the two dates must be in same TimeZone.
	 * @param dt1 The first dateTime
	 * @param dt2 The second dateTime.
	 * @return 
	 */
	public static int datesBetween(DateTime dt1, DateTime dt2) {
		return datesBetween(dt1, dt2, false);
	}
	
	/**
	 * Returns the difference in days (or dates) not keeping into account the
	 * real amount of time passed between the two dates.
	 * To avoid wrong calculations, the two dates must be in same TimeZone.
	 * @param dt1 The first dateTime
	 * @param dt2 The second dateTime.
	 * @param midnightAsDayBefore If 'true' and dt2 is at midnight, dt2 will be set at the day before.
	 * @return 
	 */
	public static int datesBetween(DateTime dt1, DateTime dt2, boolean midnightAsDayBefore) {
		LocalDate ld1 = dt1.toLocalDate();
		LocalDate ld2 = (midnightAsDayBefore && DateTimeUtils.isMidnight(dt2)) ? dt2.minusDays(1).toLocalDate() : dt2.toLocalDate();
		return Days.daysBetween(ld1, ld2).getDays();
	}
	
	/**
	 * Compares two times and returns the youngest one.
	 * @param time1 Time instance 1.
	 * @param time2 Time instance 2.
	 * @return Youngest time instance.
	 */
	public static LocalTime min(LocalTime time1, LocalTime time2) {
		return (time1.compareTo(time2) < 0) ? time1 : time2;
	}
	
	/**
	 * Compares two times and returns the older one.
	 * @param time1 Time instance 1.
	 * @param time2 Time instance 2.
	 * @return Oldest time instance.
	 */
	public static LocalTime max(LocalTime time1, LocalTime time2) {
		return (time1.compareTo(time2) > 0) ? time1 : time2;
	}
	
	public static boolean between(ReadablePartial value, ReadablePartial rp1, ReadablePartial rp2) {
		return ((value.compareTo(rp1) >= 0) && (value.compareTo(rp2) <= 0));
	}
	
	public static boolean between(ReadableInstant value, ReadableInstant ri1, ReadableInstant ri2) {
		return ((value.compareTo(ri1) >= 0) && (value.compareTo(ri2) <= 0));
	}
	
	/**
	 * Compares the standard offset (regard to UTC) of the two passed timezones.
	 * If the offset is the same, the two timezones are considered compatible.
	 * @param tz1 TimeZone instance 1.
	 * @param tz2 TimeZone instance 2.
	 * @param dt The reference date for checking the offset.
	 * @return True if compatible, false otherwise.
	 */
	public static boolean isTimeZoneCompatible(DateTimeZone tz1, DateTimeZone tz2, DateTime dt) {
		long instant = dt.withZone(DateTimeZone.UTC).getMillis();
		int utcOff1 = tz1.getStandardOffset(instant);
		int utcOff2 = tz2.getStandardOffset(instant);
		return utcOff1 == utcOff2;
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd HH:mm" and specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdHmFormatter(DateTimeZone tz) {
		return createFormatter("yyyy-MM-dd HH:mm", tz);
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd HH:mm" and default timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdHmFormatter() {
		return createYmdHmFormatter(null);
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd HH:mm:ss" and specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdHmsFormatter(DateTimeZone tz) {
		return createFormatter("yyyy-MM-dd HH:mm:ss", tz);
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd HH:mm:ss" and default timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdHmsFormatter() {
		return createYmdHmsFormatter(null);
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd" and specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdFormatter(DateTimeZone tz) {
		return createFormatter("yyyy-MM-dd", tz);
	}
	
	/**
	 * Instantiates the formatter using pattern "yyyy-MM-dd" and default timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createYmdFormatter() {
		return createYmdFormatter(null);
	}
	
	/**
	 * Instantiates the formatter using pattern "HH:mm:ss" and specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createHmsFormatter(DateTimeZone tz) {
		return createFormatter("HH:mm:ss", tz);
	}
	
	/**
	 * Instantiates the formatter using pattern "HH:mm:ss" and default timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createHmsFormatter() {
		return createHmsFormatter(null);
	}
	
	/**
	 * Instantiates the formatter using pattern "HH:mm" and specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createHmFormatter(DateTimeZone tz) {
		return createFormatter("HH:mm", tz);
	}
	
	/**
	 * Instantiates the formatter using pattern "HH:mm" and default timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createHmFormatter() {
		return createHmFormatter(null);
	}
	
	/**
	 * Instantiates the formatter using specified pattern and default timezone.
	 * @param pattern
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(String pattern) {
		return createFormatter(pattern, null);
	}
	
	/**
	 * Instantiates the formatter using specified pattern and timezone.
	 * @param pattern Desired pattern.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(String pattern, DateTimeZone tz) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
		return (tz != null) ? dtf.withZone(tz) : dtf;
	}
	
	public static DateTimeZone parseDateTimeZone(String tzid) {
		return !StringUtils.isBlank(tzid) ? DateTimeZone.forID(tzid) : null;
	}

	public static DateTime parseYmdHmsWithZone(String date, String time, DateTimeZone tz) {
		return parseYmdHmsWithZone(date + " " + time, tz);
	}

	public static DateTime parseYmdHmsWithZone(String dateTime, DateTimeZone tz) {
		if(StringUtils.isBlank(dateTime)) return null;
		String s = StringUtils.replace(dateTime, "T", " ");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(tz);
		return fmt.parseDateTime(s);
	}
	
	/**
	 * @deprecated Use printYmdHms instead
	 */
	@Deprecated
	public static String printYmdHmsWithZone(DateTime dateTime, DateTimeZone tz) {
		return printYmdHms(dateTime, tz);
	}
	
	public static String printYmdHms(DateTime dateTime, DateTimeZone tz) {
		if (dateTime == null) return null;
		return DateTimeUtils.createYmdHmsFormatter(tz).print(dateTime);
	}
	
	public static String printYmd(DateTime dateTime, DateTimeZone tz) {
		if (dateTime == null) return null;
		return DateTimeUtils.createYmdFormatter(tz).print(dateTime);
	}
	
	public static String print(DateTimeFormatter formatter, ReadablePartial rp) {
		return (rp == null) ? null : formatter.print(rp);
	}
	
	public static String print(DateTimeFormatter formatter, ReadableInstant ri) {
		return (ri == null) ? null : formatter.print(ri);
	}
	
	//event.setRecurrence(orec.getRule(), orec.getLocalStartDate(event.getDateTimeZone()));
	
	public static DateTime parseDateTime(DateTimeFormatter formatter, String s) {
		return StringUtils.isBlank(s) ? null : formatter.parseDateTime(s);
	}
	
	public static LocalDate parseLocalDate(DateTimeFormatter formatter, String s) {
		return StringUtils.isBlank(s) ? null : formatter.parseLocalDate(s);
	}
	
	/**
	 * @param formatter
	 * @param ri
	 * @return
	 * @deprecated Use print instead
	 */
	@Deprecated
	public static String printWithFormatter(DateTimeFormatter formatter, ReadableInstant ri) {
		return print(formatter, ri);
	}
	
	
	// ---------- Conversions java.time <-> JodaTime
	
	public static java.time.ZoneId toZoneId(DateTimeZone timezone) {
		if (timezone == null) return null;
		return java.time.ZoneId.of(timezone.getID());
	}
	
	public static DateTimeZone toDateTimeZone(java.time.ZoneId zone) {
		if (zone == null) return null;
		if (java.time.ZoneOffset.UTC.getId().equals(zone.getId())) {
			return DateTimeZone.UTC;
		} else {
			// This might crash for any unsupported or unrecognized id
			return DateTimeZone.forID(zone.getId());
		}
	}
	
	public static DateTime toDateTime(java.time.ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		DateTimeZone timezone = toDateTimeZone(dateTime.getZone());
		return new DateTime(dateTime.toInstant().toEpochMilli(), timezone);
	}
	
	// ---------- java.time utilities
	
	public static java.time.LocalDate parseLocalDate(String isoLocalDate) {
		return parseLocalDate(isoLocalDate, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
	public static java.time.LocalDate parseLocalDate(String date, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(date)) return null;
		return java.time.LocalDate.parse(date, formatter);
	}
	
	public static java.time.LocalTime parseLocalTime(String isoLocalTime) {
		return parseLocalTime(isoLocalTime, java.time.format.DateTimeFormatter.ISO_LOCAL_TIME);
	}
	
	public static java.time.LocalTime parseLocalTime(String time, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(time)) return null;
		return java.time.LocalTime.parse(time, formatter);
	}
	
	public static java.time.ZonedDateTime parseDateTime(String isoDateTime, java.time.ZoneId zone) {
		return parseDateTime(isoDateTime, java.time.format.DateTimeFormatter.ISO_DATE_TIME.withZone(zone));
	}
	
	public static java.time.ZonedDateTime parseDateTime(String dateTime, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(dateTime)) return null;
		return java.time.ZonedDateTime.parse(dateTime, formatter);
	}
	
	public static java.time.Instant toInstant(java.time.LocalDate date, java.time.ZoneId zone) {
		if (date == null) return null;
		return date.atStartOfDay((zone == null) ? java.time.ZoneId.systemDefault() : zone).toInstant();
	}
	
	public static java.time.Instant toInstant(java.time.LocalTime time, java.time.ZoneId zone) {
		if (time == null) return null;
		return time.atDate(java.time.LocalDate.ofEpochDay(0)).atZone((zone == null) ? java.time.ZoneId.systemDefault() : zone).toInstant();
	}
	
	public static java.time.Instant toInstant(java.time.ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.toInstant();
	}
	
	public static java.time.ZonedDateTime toZonedDateTime(java.time.Instant instant, java.time.ZoneId zone) {
		if (instant == null) return null;
		return instant.atZone((zone == null) ? java.time.ZoneId.systemDefault() : zone);
	}
	
	
	
	public static java.time.ZonedDateTime toZonedDateTime(LocalDate localDate, DateTimeZone timezone) {
		if (localDate == null) return null;
		if (timezone == null) {
			return toZonedDateTime(localDate.toDateTimeAtStartOfDay());
		} else {
			return toZonedDateTime(localDate.toDateTimeAtStartOfDay(timezone));
		}
	}
	
	public static java.time.ZonedDateTime toZonedDateTime(DateTime dateTime) {
		if (dateTime == null) return null;
		return java.time.ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(dateTime.getMillis()), java.time.ZoneId.of(dateTime.getZone().getID()));
	}
}
