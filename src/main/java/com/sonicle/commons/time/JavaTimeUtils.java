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

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 * 
 * https://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html
 */
public class JavaTimeUtils {
	public static final String ISO_LOCALDATE_PATTERN = "yyyy-MM-dd";
	public static final String ISO_LOCALTIME_PATTERN = "HH:mm:ss";
	public static final String ISO_LOCALTIME_SHORT_PATTERN = "HH:mm";
	public static final DateTimeFormatter ISO_LOCALDATE_FMT = createFormatter(ISO_LOCALDATE_PATTERN);
	public static final DateTimeFormatter ISO_LOCALTIME_FMT = createFormatter(ISO_LOCALTIME_PATTERN);
	//public static final DateTimeFormatter ISO_DATEDIME_FMT = createFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'", ZoneId.UTC);
	
	public static String[] getDayNamesShort(final Locale locale) {
		return new DateFormatSymbols(locale).getShortWeekdays();
	}
	
	public static String getDayNameShort(final int day, final Locale locale) {
		return getDayNamesShort(locale)[day];
	}
	
	public static String[] getDayNamesLong(final Locale locale) {
		return new DateFormatSymbols(locale).getWeekdays();
	}
	
	public static String getDayNameLong(final int day, final Locale locale) {
		return getDayNamesLong(locale)[day];
	}
	
	public static String[] getMonthNamesShort(final Locale locale) {
		return new DateFormatSymbols(locale).getShortMonths();
	}
	
	public static String getMonthNameShort(final int month, final Locale locale) {
		return getMonthNamesShort(locale)[month-1];
	}
	
	public static String[] getMonthNamesLong(final Locale locale) {
		return new DateFormatSymbols(locale).getMonths();
	}
	
	public static String getMonthNameLong(final int month, final Locale locale) {
		return getMonthNamesLong(locale)[month-1];
	}
	
	public static ZonedDateTime withTimeAtStartOfDay(final ZonedDateTime dt) {
		if (dt == null) return null;
		return dt.withHour(0).withMinute(0).withSecond(0).withNano(0);
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
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final String pattern, final ZoneId zone) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		return (zone != null) ? dtf.withZone(zone) : dtf;
	}
	
	/**
	 * Returns a copy for the passed formatter applying the specified timezone.
	 * @param formatter The base formatter to copy.
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final DateTimeFormatter formatter, final ZoneId zone) {
		return (zone != null) ? formatter.withZone(zone) : formatter;
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
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHMS(final ZoneId zone) {
		return createFormatter(ISO_LOCALDATE_PATTERN + " " + ISO_LOCALTIME_PATTERN, zone);
	}
	
	/**
	 * Instantiates a ISO date-time ("yyyy-MM-dd HH:mm") formatter using specified timezone.
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHM(final ZoneId zone) {
		return createFormatter(ISO_LOCALDATE_PATTERN + " " + ISO_LOCALTIME_SHORT_PATTERN, zone);
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
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMD(final ZoneId zone) {
		return createFormatter(ISO_LOCALDATE_PATTERN, zone);
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
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHMS(final ZoneId zone) {
		return createFormatter(ISO_LOCALTIME_PATTERN, zone);
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
	 * @param zone Desired formatter timezone.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHM(final ZoneId zone) {
		return createFormatter(ISO_LOCALTIME_SHORT_PATTERN, zone);
	}
	
	// ---------- Parse
	
	public static LocalDate parseLocalDateYMD(final String date) {
		return parseLocalDate(DateTimeFormatter.ISO_LOCAL_DATE, date);
	}
	
	public static LocalDate parseLocalDate(final DateTimeFormatter formatter, final String date) {
		if (StringUtils.isBlank(date)) return null;
		return LocalDate.parse(date, formatter);
	}
	
	public static LocalTime parseLocalTimeHMS(final String time) {
		return parseLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, time);
	}
	
	public static LocalTime parseLocalTime(final DateTimeFormatter formatter, final String time) {
		if (StringUtils.isBlank(time)) return null;
		return LocalTime.parse(time, formatter);
	}
	
	public static ZonedDateTime parseDateTimeISO(final String dateTime, final ZoneId zone) {
		return parseDateTime(DateTimeFormatter.ISO_DATE_TIME.withZone(zone), dateTime);
	}
	
	public static ZonedDateTime parseDateTime(final DateTimeFormatter formatter, final String dateTime) {
		if (StringUtils.isBlank(dateTime)) return null;
		return ZonedDateTime.parse(dateTime, formatter);
	}
	
	// ---------- Prints
	
	/**
	 * Converts the passed temporal value into a String using specified formatter.
	 * A null string is returned in case of null input value.
	 * @param formatter The formatter to use for printing.
	 * @param temporal The value to convert.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final TemporalAccessor temporal) {
		return print(formatter, temporal, null);
	}
	
	/**
	 * Converts the passed temporal value into a String using specified formatter.
	 * @param formatter The formatter to use for printing. 
	 * @param temporal The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String print(final DateTimeFormatter formatter, final TemporalAccessor temporal, final String defaultString) {
		return (temporal == null) ? defaultString : formatter.format(temporal);
	}
	
	// ---------- Transforms
	
	public static Instant toInstant(final LocalDate date, final ZoneId zone) {
		if (date == null) return null;
		return date.atStartOfDay((zone == null) ? java.time.ZoneId.systemDefault() : zone).toInstant();
	}
	
	public static Instant toInstant(final LocalTime time, final ZoneId zone) {
		if (time == null) return null;
		return time.atDate(LocalDate.ofEpochDay(0)).atZone((zone == null) ? ZoneId.systemDefault() : zone).toInstant();
	}
	
	public static Instant toInstant(final ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.toInstant();
	}
	
	public static ZonedDateTime toZonedDateTime(final Instant instant, final ZoneId zone) {
		if (instant == null) return null;
		return instant.atZone((zone == null) ? ZoneId.systemDefault() : zone);
	}
	
	// ----------  JodaTime -> java.time
	
	public static ZoneId toZoneId(final org.joda.time.DateTimeZone timezone) {
		if (timezone == null) return null;
		return ZoneId.of(timezone.getID());
	}
	
	public static LocalDate toLocalDate(final org.joda.time.LocalDate localDate) {
		if (localDate == null) return null;
		return LocalDate.of(
			localDate.getYear(),
			localDate.getMonthOfYear(),
			localDate.getDayOfMonth()
		);
	}
	
	public static LocalTime toLocalDate(final org.joda.time.LocalTime localTime) {
		if (localTime == null) return null;
		return LocalTime.of(
			localTime.getHourOfDay(),
			localTime.getMinuteOfHour(),
			localTime.getSecondOfMinute(),
			localTime.getMillisOfSecond() * 1_000_000 // nanos
		);
	}
	
	public static ZonedDateTime toZonedDateTime(final org.joda.time.DateTime dateTime) {
		// https://stackoverflow.com/questions/28877981/how-to-convert-from-org-joda-time-datetime-to-java-time-zoneddatetime
		if (dateTime == null) return null;
		return ZonedDateTime.ofLocal(
			LocalDateTime.of(
				dateTime.getYear(),
				dateTime.getMonthOfYear(),
				dateTime.getDayOfMonth(),
				dateTime.getHourOfDay(),
				dateTime.getMinuteOfHour(),
				dateTime.getSecondOfMinute(),
				dateTime.getMillisOfSecond() * 1_000_000
			),
			toZoneId(dateTime.getZone()),
			ZoneOffset.ofTotalSeconds(dateTime.getZone().getOffset(dateTime) / 1000)
		);
	}
}
