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
public class DateTimeUtils {
	public static final DateTimeFormatter ISO_LOCALDATE_FMT = createFormatter("yyyy-MM-dd");
	public static final DateTimeFormatter ISO_LOCALTIME_FMT = createFormatter("HH:mm:ss");
	public static final DateTimeFormatter ISO_DATEDIME_FMT = createFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'", DateTimeZone.UTC);
	public static final LocalTime TIME_AT_STARTOFDAY = new LocalTime(0, 0, 0, 0);
	public static final LocalTime TIME_AT_ENDOFDAY = new LocalTime(23, 59, 59, 0);
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getDayNamesShort
	 */
	@Deprecated public static String[] getDayNamesShort(Locale locale) {
		return JavaTimeUtils.getDayNamesShort(locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getDayNameShort
	 */
	@Deprecated public static String getDayNameShort(int day, Locale locale) {
		return JavaTimeUtils.getDayNameShort(day, locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getDayNamesLong
	 */
	@Deprecated public static String[] getDayNamesLong(Locale locale) {
		return JavaTimeUtils.getDayNamesLong(locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getDayNameLong
	 */
	@Deprecated public static String getDayNameLong(int day, Locale locale) {
		return JavaTimeUtils.getDayNameLong(day, locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getMonthNamesShort
	 */
	@Deprecated public static String[] getMonthNamesShort(Locale locale) {
		return JavaTimeUtils.getMonthNamesShort(locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getMonthNameShort
	 */
	@Deprecated public static String getMonthNameShort(int month, Locale locale) {
		return JavaTimeUtils.getMonthNameShort(month, locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getMonthNamesLong
	 */
	@Deprecated public static String[] getMonthNamesLong(Locale locale) {
		return JavaTimeUtils.getMonthNamesLong(locale);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.getMonthNameLong
	 */
	@Deprecated public static String getMonthNameLong(int month, Locale locale) {
		return JavaTimeUtils.getMonthNameLong(month, locale);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.now
	 */
	@Deprecated public static DateTime now() {
		return JodaTimeUtils.now();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.now
	 */
	@Deprecated public static DateTime now(boolean exact) {
		return JodaTimeUtils.now(exact);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isMidnight
	 */
	@Deprecated public static boolean isMidnight(DateTime dt) {
		return JodaTimeUtils.isMidnight(dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isEndOfDay
	 */
	@Deprecated public static boolean isEndOfDay(DateTime dt) {
		return JodaTimeUtils.isEndOfDay(dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isEndOfDay
	 */
	@Deprecated public static boolean isEndOfDay(DateTime dt, boolean relaxed) {
		return JodaTimeUtils.isEndOfDay(dt, relaxed);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isDayBefore
	 */
	@Deprecated public static boolean isDayBefore(DateTime dt1, DateTime dt2) {
		return JodaTimeUtils.isDayBefore(dt1, dt2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isDayBefore
	 */
	@Deprecated public static boolean isDayBefore(LocalDate l1, LocalDate l2) {
		return JodaTimeUtils.isDayBefore(l1, l2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.toDateTime
	 */
	@Deprecated public static DateTime toDateTime(LocalDate ld, LocalTime lt, DateTimeZone dtz) {
		return JodaTimeUtils.toDateTime(ld, lt, dtz, true);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.toDateTime
	 */
	@Deprecated public static DateTime toDateTime(LocalDate ld, LocalTime lt, DateTimeZone dtz, boolean pushForwardAtGap) {
		return JodaTimeUtils.toDateTime(ld, lt, dtz, pushForwardAtGap);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.toDateTime
	 */
	@Deprecated public static DateTime withTimeAtStartOfDay(DateTime dt) {
		if (dt == null) return null;
		return dt.withTimeAtStartOfDay();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.withTimeAtStartOfDay
	 */
	@Deprecated public static DateTime withTimeAtStartOfDay(LocalDate ld, DateTimeZone tz) {
		return JodaTimeUtils.withTimeAtStartOfDay(ld, tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.withTimeAtMidday
	 */
	@Deprecated public static DateTime withTimeAtMidday(DateTime dt) {
		return JodaTimeUtils.withTimeAtMidday(dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.withTimeAtEndOfDay
	 */
	@Deprecated public static DateTime withTimeAtEndOfDay(LocalDate ld, DateTimeZone tz) {
		return JodaTimeUtils.withTimeAtEndOfDay(ld, tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.withTimeAtEndOfDay
	 */
	@Deprecated public static DateTime withTimeAtEndOfDay(DateTime dt) {
		return JodaTimeUtils.withTimeAtEndOfDay(dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.ceilTimeAtEndOfDay
	 */
	@Deprecated public static DateTime ceilTimeAtEndOfDay(DateTime dt) {
		return JodaTimeUtils.ceilTimeAtEndOfDay(dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.roundToNearestMinute
	 */
	@Deprecated public static DateTime roundToNearestMinute(DateTime dateTime, final int nearestMinutes) {
		return JodaTimeUtils.roundToNearestMinute(dateTime, nearestMinutes);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.startsInDay
	 */
	@Deprecated public static boolean startsInDay(DateTime day, ReadableInstant instant) {
		return JodaTimeUtils.startsInDay(day, instant);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.endsInDay
	 */
	@Deprecated public static boolean endsInDay(DateTime day, ReadableInstant instant) {
		return JodaTimeUtils.endsInDay(day, instant);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.daysBetween
	 */
	@Deprecated public static Days daysBetween(ReadableInstant instant1, ReadableInstant instant2) {
		return JodaTimeUtils.daysBetween(instant1, instant2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.datesBetween
	 */
	@Deprecated public static int datesBetween(DateTime dt1, DateTime dt2) {
		return JodaTimeUtils.calendarDaysBetween(dt1, dt2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.datesBetween
	 */
	@Deprecated public static int datesBetween(DateTime dt1, DateTime dt2, boolean midnightAsDayBefore) {
		return JodaTimeUtils.calendarDaysBetween(dt1, dt2, midnightAsDayBefore);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.min
	 */
	@Deprecated public static LocalTime min(LocalTime time1, LocalTime time2) {
		return JodaTimeUtils.min(time1, time2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.max
	 */
	@Deprecated public static LocalTime max(LocalTime time1, LocalTime time2) {
		return JodaTimeUtils.max(time1, time2);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.between
	 */
	@Deprecated public static boolean between(final ReadablePartial partial, final ReadablePartial start, final ReadablePartial end) {
		return JodaTimeUtils.between(partial, start, end, true, false);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.between
	 */
	@Deprecated public static boolean between(final ReadablePartial partial, final ReadablePartial start, final ReadablePartial end, final boolean inclusiveStart, final boolean inclusiveEnd) {
		return JodaTimeUtils.between(partial, start, end, inclusiveStart, inclusiveEnd);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.between
	 */
	@Deprecated public static boolean between(ReadableInstant value, ReadableInstant ri1, ReadableInstant ri2) {
		return JodaTimeUtils.between(value, ri1, ri2);
	}
	
	/**
	 * Evaluates if an Instant is between start/end bounds, with start bound included and end excluded.
	 * @param instant The Instant value to check.
	 * @param start The start bound.
	 * @param end The end bound.
	 * @return `true` if Instant is contained in bounds, `false` otherwise or if any parameter is null.
	 */
	//public static boolean between(final ReadableInstant instant, final ReadableInstant start, final ReadableInstant end) {
	//	return between(instant, start, end, true, false);
	//}
	
	/**
	 * Evaluates if an Instant is between start/end bounds.
	 * @param instant The Instant value to check.
	 * @param start The start bound.
	 * @param end The end bound.
	 * @param inclusiveStart Set to `true` to make start check inclusive, `false` otherwise.
	 * @param inclusiveEnd Set to `true` to make end check inclusive, `false` otherwise.
	 * @return `true` if Instant is contained in bounds, `false` otherwise or if any parameter is null.
	 */
	//public static boolean between(final ReadableInstant instant, final ReadableInstant start, final ReadableInstant end, final boolean inclusiveStart, final boolean inclusiveEnd) {
	//	if (instant == null || start == null || end == null) return false;
	//	return (inclusiveStart ? instant.compareTo(start) >= 0 : instant.compareTo(start) > 0) && (inclusiveEnd ? instant.compareTo(end) <= 0 : instant.compareTo(end) < 0);
	//}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.isTimeZoneCompatible
	 */
	@Deprecated public static boolean isTimeZoneCompatible(DateTimeZone tz1, DateTimeZone tz2, DateTime dt) {
		return JodaTimeUtils.isTimeZoneCompatible(tz1, tz2, dt);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterYMDHM
	 */
	@Deprecated public static DateTimeFormatter createYmdHmFormatter(DateTimeZone tz) {
		return JodaTimeUtils.createFormatterYMDHM(tz);
	}
	
	/**
	 * @deprecated Use JodaTimeUtils.createFormatterYMDHM
	 */
	@Deprecated public static DateTimeFormatter createYmdHmFormatter() {
		return JodaTimeUtils.createFormatterYMDHM(null);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterYMDHMS
	 */
	@Deprecated public static DateTimeFormatter createYmdHmsFormatter(DateTimeZone tz) {
		return JodaTimeUtils.createFormatterYMDHMS(tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterYMDHMS
	 */
	@Deprecated public static DateTimeFormatter createYmdHmsFormatter() {
		return JodaTimeUtils.createFormatterYMDHMS();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterYMD
	 */
	@Deprecated public static DateTimeFormatter createYmdFormatter(DateTimeZone tz) {
		return JodaTimeUtils.createFormatterYMD(tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterYMD
	 */
	@Deprecated public static DateTimeFormatter createYmdFormatter() {
		return JodaTimeUtils.createFormatterYMD();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterHMS
	 */
	@Deprecated public static DateTimeFormatter createHmsFormatter(DateTimeZone tz) {
		return JodaTimeUtils.createFormatterHMS(tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterHMS
	 */
	@Deprecated public static DateTimeFormatter createHmsFormatter() {
		return JodaTimeUtils.createFormatterHMS();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterHM
	 */
	@Deprecated public static DateTimeFormatter createHmFormatter(DateTimeZone tz) {
		return JodaTimeUtils.createFormatterHM(tz);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatterHM
	 */
	@Deprecated public static DateTimeFormatter createHmFormatter() {
		return JodaTimeUtils.createFormatterHM();
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatter
	 */
	@Deprecated public static DateTimeFormatter createFormatter(String pattern) {
		return JodaTimeUtils.createFormatter(pattern);
	}
	
	/**
	 * @deprecated Moved! Use JodaTimeUtils.createFormatter
	 */
	@Deprecated public static DateTimeFormatter createFormatter(String pattern, DateTimeZone tz) {
		return JodaTimeUtils.createFormatter(pattern, tz);
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
	
	@Deprecated public static String print(final DateTimeFormatter formatter, final ReadablePartial rp) {
		return print(formatter, rp, null);
	}
	
	@Deprecated public static String print(final DateTimeFormatter formatter, final ReadablePartial rp, final String defaultString) {
		return (rp == null) ? defaultString : formatter.print(rp);
	}
	
	@Deprecated public static String print(final DateTimeFormatter formatter, final ReadableInstant ri) {
		return print(formatter, ri, null);
	}
	
	@Deprecated public static String print(final DateTimeFormatter formatter, final ReadableInstant ri, final String defaultString) {
		return (ri == null) ? defaultString : formatter.print(ri);
	}
	
	//event.setRecurrence(orec.getRule(), orec.getLocalStartDate(event.getDateTimeZone()));
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseDateTime
	 */
	@Deprecated public static DateTime parseDateTime(DateTimeFormatter formatter, String s) {
		return StringUtils.isBlank(s) ? null : formatter.parseDateTime(s);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseLocalDate
	 */
	@Deprecated public static LocalDate parseLocalDate(DateTimeFormatter formatter, String s) {
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
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toZoneId
	 */
	@Deprecated public static java.time.ZoneId toZoneId(DateTimeZone timezone) {
		if (timezone == null) return null;
		return java.time.ZoneId.of(timezone.getID());
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toDateTimeZone
	 */
	@Deprecated public static DateTimeZone toDateTimeZone(java.time.ZoneId zone) {
		if (zone == null) return null;
		if (java.time.ZoneOffset.UTC.getId().equals(zone.getId())) {
			return DateTimeZone.UTC;
		} else {
			// This might crash for any unsupported or unrecognized id
			return DateTimeZone.forID(zone.getId());
		}
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toDateTime
	 */
	@Deprecated public static DateTime toDateTime(java.time.ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		DateTimeZone timezone = toDateTimeZone(dateTime.getZone());
		return new DateTime(dateTime.toInstant().toEpochMilli(), timezone);
	}
	
	// ---------- java.time utilities
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseLocalDateYMD
	 */
	@Deprecated public static java.time.LocalDate parseLocalDate(String isoLocalDate) {
		return parseLocalDate(isoLocalDate, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseLocalDate
	 */
	@Deprecated public static java.time.LocalDate parseLocalDate(String date, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(date)) return null;
		return java.time.LocalDate.parse(date, formatter);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseISOLocalTime
	 */
	@Deprecated public static java.time.LocalTime parseLocalTime(String isoLocalTime) {
		return parseLocalTime(isoLocalTime, java.time.format.DateTimeFormatter.ISO_LOCAL_TIME);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseLocalTime
	 */
	@Deprecated public static java.time.LocalTime parseLocalTime(String time, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(time)) return null;
		return java.time.LocalTime.parse(time, formatter);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseISODateTime
	 */
	@Deprecated public static java.time.ZonedDateTime parseDateTime(String isoDateTime, java.time.ZoneId zone) {
		return parseDateTime(isoDateTime, java.time.format.DateTimeFormatter.ISO_DATE_TIME.withZone(zone));
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.parseDateTime
	 */
	@Deprecated public static java.time.ZonedDateTime parseDateTime(String dateTime, java.time.format.DateTimeFormatter formatter) {
		if (StringUtils.isBlank(dateTime)) return null;
		return java.time.ZonedDateTime.parse(dateTime, formatter);
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toInstant
	 */
	@Deprecated public static java.time.Instant toInstant(java.time.LocalDate date, java.time.ZoneId zone) {
		if (date == null) return null;
		return date.atStartOfDay((zone == null) ? java.time.ZoneId.systemDefault() : zone).toInstant();
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toInstant
	 */
	@Deprecated public static java.time.Instant toInstant(java.time.LocalTime time, java.time.ZoneId zone) {
		if (time == null) return null;
		return time.atDate(java.time.LocalDate.ofEpochDay(0)).atZone((zone == null) ? java.time.ZoneId.systemDefault() : zone).toInstant();
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toInstant
	 */
	@Deprecated public static java.time.Instant toInstant(java.time.ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.toInstant();
	}
	
	/**
	 * @deprecated Moved! Use JavaTimeUtils.toZonedDateTime
	 */
	@Deprecated public static java.time.ZonedDateTime toZonedDateTime(java.time.Instant instant, java.time.ZoneId zone) {
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
