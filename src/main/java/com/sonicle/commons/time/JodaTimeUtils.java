/*
 * Copyright (C) 2025 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2025 Sonicle S.r.l.".
 */
package com.sonicle.commons.time;

import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.IllegalInstantException;
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
public class JodaTimeUtils {
	public static final String HHMM_PATTERN = "HH:mm";
	public static final String ISO_DATE_PATTERN = "yyyy-MM-dd"; // Do NOT modify!
	public static final String ISO_TIME_PATTERN = "HH:mm:ss"; // Do NOT modify!
	public static final DateTimeFormatter ISO_LOCALDATE_FMT = createFormatter(ISO_DATE_PATTERN);
	public static final DateTimeFormatter ISO_LOCALTIME_FMT = createFormatter(ISO_TIME_PATTERN);
	public static final DateTimeFormatter ISO_DATETIME_FMT = createFormatter(ISO_DATE_PATTERN + "'T'" + ISO_TIME_PATTERN + "'Z'", DateTimeZone.UTC);
	public static final String SYNCTOKEN_PATTERN = "yyyyMMddHHmmssSSS";
	public static final DateTimeFormatter SYNCTOKEN_FMT = createFormatter(SYNCTOKEN_PATTERN, DateTimeZone.UTC);
	public static final LocalTime TIME_AT_STARTOFDAY = new LocalTime(0, 0, 0, 0);
	public static final LocalTime TIME_AT_ENDOFDAY = new LocalTime(23, 59, 59, 0);
	
	/**
	 * Compares the standard offset (regard to UTC) of the two passed timezones.
	 * If the offset is the same, the two timezones are considered compatible.
	 * @param timezone1 TimeZone instance 1.
	 * @param timezone2 TimeZone instance 2.
	 * @param reference The reference date for checking the offset.
	 * @return True if compatible, false otherwise.
	 */
	public static boolean isTimeZoneCompatible(final DateTimeZone timezone1, final DateTimeZone timezone2, final DateTime reference) {
		long instant = reference.withZone(DateTimeZone.UTC).getMillis();
		int utcOff1 = timezone1.getStandardOffset(instant);
		int utcOff2 = timezone2.getStandardOffset(instant);
		return utcOff1 == utcOff2;
	}
	
	public static DateTime now() {
		return now(false);
	}
	
	public static DateTime now(final boolean exact) {
		if (exact) {
			return DateTime.now(DateTimeZone.UTC);
		} else {
			return DateTime.now(DateTimeZone.UTC).withMillisOfSecond(0);
		}
	}
	
	public static boolean isMidnight(final DateTime dt) {
		return (dt.compareTo(dt.withTimeAtStartOfDay()) == 0);
	}
	
	public static boolean isEndOfDay(final DateTime dt) {
		return isEndOfDay(dt, false);
	}
	
	public static boolean isEndOfDay(final DateTime dt, final boolean relaxed) {
		if (relaxed) {
			return ((dt.getHourOfDay() == 23) && (dt.getMinuteOfHour() == 59));
		} else {
			return (dt.compareTo(withTimeAtEndOfDay(dt)) == 0);
		}	
	}
	
	public static boolean isDayBefore(final DateTime dt1, final DateTime dt2) {
		return isDayBefore(dt1.toLocalDate(), dt2.toLocalDate());
	}
	
	public static boolean isDayBefore(final LocalDate l1, final LocalDate l2) {
		return l1.isBefore(l2) && (Days.daysBetween(l1, l2).getDays() == 1);
	}
	
	public static boolean startsInDay(final DateTime day, final ReadableInstant instant) {
		DateTime dayFrom = day.withTimeAtStartOfDay();
		DateTime dayTo = dayFrom.plusDays(1);
		return (instant.compareTo(dayFrom) >= 0) && instant.isBefore(dayTo);
	}
	
	public static boolean endsInDay(final DateTime day, final ReadableInstant instant) {
		DateTime dayFrom = day.withTimeAtStartOfDay();
		DateTime dayTo = dayFrom.plusDays(1);
		return instant.isAfter(dayFrom) && instant.isBefore(dayTo); // We need to exclude midnight!
	}
	
	public static DateTime withTimeAtStartOfDay(final DateTime dt) {
		if (dt == null) return null;
		return dt.withTimeAtStartOfDay();
	}
	
	public static DateTime withTimeAtStartOfDay(final LocalDate ld, final DateTimeZone tz) {
		if (ld == null) return null;
		return ld.toDateTimeAtStartOfDay(tz);
	}
	
	public static DateTime withTimeAtMidday(final DateTime dt) {
		if (dt == null) return null;
		return dt.withTime(12, 0, 0, 0);
	}
	
	public static DateTime withTimeAtEndOfDay(final LocalDate ld, final DateTimeZone tz) {
		if (ld == null) return null;
		return withTimeAtEndOfDay(withTimeAtStartOfDay(ld, tz));
	}
	
	public static DateTime withTimeAtEndOfDay(final DateTime dt) {
		if (dt == null) return null;
		return dt.withTime(23, 59, 59, 0);
	}
	
	public static DateTime ceilTimeAtEndOfDay(final DateTime dt) {
		return (isEndOfDay(dt, true)) ? withTimeAtEndOfDay(dt) : dt;
	}
	
	/**
	 * Returns a new DateTime properly rounded according to choosen nearestMinute.
	 * @param dateTime The DateTime object to round.
	 * @param nearestMinutes The nearest minute to round value to.
	 * @return 
	 */
	public static DateTime roundToNearestMinute(final DateTime dateTime, final int nearestMinutes) {
		Check.notNull(dateTime, "dateTime");
		Check.stateIsTrue(!(nearestMinutes < 1 || 60 % nearestMinutes != 0), "nearestMinutes must be a divider of 60");
		final DateTime hour = dateTime.hourOfDay().roundFloorCopy();
		final long millisSinceHour = new Duration(hour, dateTime).getMillis();
		final int roundedMinutes = ((int)Math.round(millisSinceHour / 60000.0 / nearestMinutes)) * nearestMinutes;
		return hour.plusMinutes(roundedMinutes);
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
	
	/**
	 * Evaluates if an Instant is between start/end bounds, with start bound included and end excluded.
	 * @param value The Partial value to check.
	 * @param partial1 The start bound.
	 * @param partial2 The end bound.
	 * @return `true` if Instant is contained in bounds, `false` otherwise or if any parameter is null.
	 */
	public static boolean between(final ReadablePartial value, final ReadablePartial partial1, final ReadablePartial partial2) {
		return between(value, partial1, partial2, true, false);
	}
	
	/**
	 * Evaluates if an Instant is between start/end bounds.
	 * @param value The Partial value to check.
	 * @param partial1 The start bound.
	 * @param partial2 The end bound.
	 * @param inclusiveStart Set to `true` to make start check inclusive, `false` otherwise.
	 * @param inclusiveEnd Set to `true` to make end check inclusive, `false` otherwise.
	 * @return `true` if Instant is contained in bounds, `false` otherwise or if any parameter is null.
	 */
	public static boolean between(final ReadablePartial value, final ReadablePartial partial1, final ReadablePartial partial2, final boolean inclusiveStart, final boolean inclusiveEnd) {
		if (value == null || partial1 == null || partial2 == null) return false;
		return (inclusiveStart ? value.compareTo(partial1) >= 0 : value.compareTo(partial1) > 0) && (inclusiveEnd ? value.compareTo(partial2) <= 0 : value.compareTo(partial2) < 0);
	}
	
	public static boolean between(ReadableInstant value, ReadableInstant instant1, ReadableInstant instant2) {
		return ((value.compareTo(instant1) >= 0) && (value.compareTo(instant2) <= 0));
	}
	
	public static Days daysBetween(ReadableInstant instant1, ReadableInstant instant2) {
		return (instant1 != null && instant2 != null) ? Days.daysBetween(instant1, instant2) : null; 
	}
	
	/**
	 * Returns the difference in days not keeping into account the
	 * real amount of time passed between the two dates.
	 * To avoid wrong calculations, the two dates must be in same TimeZone.
	 * @param dateTime1 The first dateTime
	 * @param dateTime2 The second dateTime.
	 * @return 
	 */
	public static int calendarDaysBetween(DateTime dateTime1, DateTime dateTime2) {
		return calendarDaysBetween(dateTime1, dateTime2, false);
	}
	
	/**
	 * Returns the difference in days not keeping into account the
	 * real amount of time passed between the two dates.
	 * To avoid wrong calculations, the two dates must be in same TimeZone.
	 * @param dateTime1 The first dateTime
	 * @param dateTime2 The second dateTime.
	 * @param midnightAsDayBefore If 'true' and dt2 is at midnight, dt2 will be set at the day before.
	 * @return 
	 */
	public static int calendarDaysBetween(DateTime dateTime1, DateTime dateTime2, boolean midnightAsDayBefore) {
		LocalDate ld1 = dateTime1.toLocalDate();
		LocalDate ld2 = (midnightAsDayBefore && JodaTimeUtils.isMidnight(dateTime2)) ? dateTime2.minusDays(1).toLocalDate() : dateTime2.toLocalDate();
		return Days.daysBetween(ld1, ld2).getDays();
	}
	
	// ---------- Formatting
	
	/**
	 * Instantiates the formatter using specified pattern and default timezone.
	 * @param pattern
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final String pattern) {
		return createFormatter(pattern, null);
	}
	
	/**
	 * Instantiates the formatter using specified pattern and timezone.
	 * @param pattern Desired pattern.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final String pattern, final DateTimeZone tz) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
		return (tz != null) ? dtf.withZone(tz) : dtf;
	}
	
	/**
	 * Instantiates a ISO date-time ("yyyy-MM-dd HH:mm:ss") formatter using in the local (Java default) timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHMS() {
		return createFormatterYMDHMS(null);
	}
	
	/**
	 * Instantiates a ISO date-time ("yyyy-MM-dd HH:mm:ss") formatter using specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHMS(final DateTimeZone tz) {
		return createFormatter(ISO_DATE_PATTERN + " " + ISO_TIME_PATTERN, tz);
	}
	
	/**
	 * Instantiates a ISO date-time ("yyyy-MM-dd HH:mm") formatter using specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHM(final DateTimeZone tz) {
		return createFormatter(ISO_DATE_PATTERN + " " + HHMM_PATTERN, tz);
	}
	
	/**
	 * Instantiates "yyyy-MM-dd" formatter (ISO date) using in the local (Java default) timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMD() {
		return createFormatterYMD(null);
	}
	
	/**
	 * Instantiates "yyyy-MM-dd" formatter (ISO date) using specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMD(final DateTimeZone tz) {
		return createFormatter(ISO_DATE_PATTERN, tz);
	}
	
	/**
	 * Instantiates a ISO time ("HH:mm:ss") formatter using in the local (Java default) timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHMS() {
		return createFormatterHMS(null);
	}
	
	/**
	 * Instantiates a ISO time ("HH:mm:ss") formatter using specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHMS(final DateTimeZone tz) {
		return createFormatter(ISO_TIME_PATTERN, tz);
	}
	
	/**
	 * Instantiates a "HH:mm" formatter using in the local (Java default) timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHM() {
		return createFormatterHM(null);
	}
	
	/**
	 * Instantiates a "HH:mm" formatter using specified timezone.
	 * @param tz Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHM(final DateTimeZone tz) {
		return createFormatter(HHMM_PATTERN, tz);
	}
	
	// ---------- Parse
	
	/**
	 * Parses passes String as Timezone ID. A null value is returned in case
	 * of empty input String and IllegalArgumentException is thrown in case of no-match.
	 * @param tzid The Timezone ID to be parsed.
	 * @return 
	 */
	public static DateTimeZone parseTimezone(final String tzid) {
		return parseTimezone(tzid, null, false);
	}
	
	/**
	 * Parses passes String as Timezone ID.
	 * This may throw a IllegalArgumentException according to silent parameter, 
	 * otherwise specified default value is returned in case of no-match.
	 * @param tzid The Timezone ID to be parsed.
	 * @param defaultTimezone The Timezone to return in case of missing match.
	 * @param silent Specifies whether to suppress Exceptions or not.
	 * @return 
	 */
	public static DateTimeZone parseTimezone(final String tzid, final DateTimeZone defaultTimezone, final boolean silent) {
		if (StringUtils.isBlank(tzid)) return defaultTimezone;
		if (silent) {
			try {
				return DateTimeZone.forID(tzid);
			} catch (IllegalArgumentException ex) {
				return defaultTimezone;
			}
		} else {
			return DateTimeZone.forID(tzid);
		}
	}
	
	public static LocalDate parseLocalDateYMD(final String date) {
		return parseLocalDate(ISO_LOCALDATE_FMT, date);
	}
	
	public static LocalDate parseLocalDate(final DateTimeFormatter formatter, final String date) {
		if (StringUtils.isBlank(date)) return null;
		return formatter.parseLocalDate(date);
	}
	
	public static LocalTime parseLocalTimeHMS(final String time) {
		return parseLocalTime(ISO_LOCALTIME_FMT, time);
	}
	
	public static LocalTime parseLocalTime(final DateTimeFormatter formatter, final String time) {
		if (StringUtils.isBlank(time)) return null;
		return formatter.parseLocalTime(time);
	}
	
	public static DateTime parseDateTimeISO(final String datetime) {
		return parseDateTime(ISO_DATETIME_FMT, datetime);
	}
	
	public static DateTime parseDateTimeYMDHMS(final DateTimeZone tz, final String datetime) {
		return parseDateTime(createFormatterYMDHMS(tz), datetime);
	}
	
	public static DateTime parseDateTime(final DateTimeFormatter formatter, final String datetime) {
		if (StringUtils.isBlank(datetime)) return null;
		return formatter.parseDateTime(datetime);
	}
	
	// ---------- Prints
	
	/**
	 * Converts the passed date-time into a String in ISO format, applying UTC timezone before printing.
	 * @param dateTime The value to convert.
	 * @return The resulting String
	 */
	public static String printISO(final DateTime dateTime) {
		return printISO( dateTime, null);
	}
	
	/**
	 * Converts the passed date-time into a String in ISO format, applying UTC timezone before printing.
	 * @param dateTime The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String printISO(final DateTime dateTime, final String defaultString) {
		return print(ISO_DATETIME_FMT, dateTime, defaultString);
	}
	
	/**
	 * Converts the passed date-time into a String in format "yyyy-MM-dd HH:mm:ss", 
	 * applying the specified timezone before printing. A null string is returned 
	 * in case of null input value.
	 * @param tz Desired formatter timezone.
	 * @param dateTime The value to convert.
	 * @return The resulting String
	 */
	public static String printYMDHMS(final DateTimeZone tz, final DateTime dateTime) {
		return printYMDHMS(tz, dateTime, null);
	}
	
	/**
	 * Converts the passed date-time into a String in format "yyyy-MM-dd HH:mm:ss", 
	 * applying the specified timezone before printing.
	 * @param tz Desired formatter timezone.
	 * @param dateTime The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String printYMDHMS(final DateTimeZone tz, final DateTime dateTime, final String defaultString) {
		return print(createFormatterYMDHMS(tz), dateTime, defaultString);
	}
	
	/**
	 * Converts the passed partial value into a String using specified formatter.
	 * A null string is returned in case of null input value.
	 * @param formatter The formatter to use for printing. 
	 * @param partial The value to convert.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final ReadablePartial partial) {
		return print(formatter, partial, null);
	}
	
	/**
	 * Converts the passed partial value into a String using specified formatter.
	 * @param formatter The formatter to use for printing. 
	 * @param partial The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final ReadablePartial partial, final String defaultString) {
		return (partial == null) ? defaultString : formatter.print(partial);
	}
	
	/**
	 * Converts the passed instant value into a String using specified formatter.
	 * A null string is returned in case of null input value.
	 * @param formatter The formatter to use for printing. 
	 * @param instant The value to convert.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final ReadableInstant instant) {
		return print(formatter, instant, null);
	}
	
	/**
	 * Converts the passed instant value into a String using specified formatter.
	 * @param formatter The formatter to use for printing. 
	 * @param instant The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final ReadableInstant instant, final String defaultString) {
		return (instant == null) ? defaultString : formatter.print(instant);
	}
	
	// ---------- Transforms
	
	public static DateTime toDateTime(final LocalDate ld, final LocalTime lt, final DateTimeZone dtz) {
		return toDateTime(ld, lt, dtz, true);
	}
	
	public static DateTime toDateTime(final LocalDate ld, final LocalTime lt, final DateTimeZone dtz, final boolean pushForwardAtGap) {
		// https://stackoverflow.com/questions/34617172/handling-time-zone-offset-transition-and-daylight-savings-time-with-joda
		try {
			return ld.toDateTime(lt, dtz);
		} catch (IllegalInstantException ex) {
			if (!pushForwardAtGap) throw ex;
			return ld.toDateTime(lt.plusHours(1), dtz);
		}
	}
	
	// ----------  java.time -> JodaTime
	
	public static DateTimeZone toDateTimeZone(final java.time.ZoneId zone) {
		if (zone == null) return null;
		if (java.time.ZoneOffset.UTC.getId().equals(zone.getId())) {
			return DateTimeZone.UTC;
		} else {
			// This might crash for any unsupported or unrecognized id
			return DateTimeZone.forID(zone.getId());
		}
	}
	
	public static DateTime toDateTime(final java.time.ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		DateTimeZone timezone = toDateTimeZone(dateTime.getZone());
		return new DateTime(dateTime.toInstant().toEpochMilli(), timezone);
	}
}
