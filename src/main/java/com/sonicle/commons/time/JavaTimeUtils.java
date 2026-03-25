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
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Locale;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 * 
 * https://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html
 */
public class JavaTimeUtils {
	public static final String ISO_LOCALTIME_SHORT_PATTERN = "HH:mm";
	public static final String ISO_LOCALDATE_PATTERN = "yyyy-MM-dd"; // Do NOT modify!
	public static final String ISO_LOCALTIME_PATTERN = "HH:mm:ss"; // Do NOT modify!
	public static final DateTimeFormatter ISO_LOCALDATE_FMT = createFormatter(ISO_LOCALDATE_PATTERN);
	public static final DateTimeFormatter ISO_LOCALTIME_FMT = createFormatter(ISO_LOCALTIME_PATTERN); // This is more strict respect the built-in DateTimeFormatter.ISO_LOCAL_DATE
	public static final DateTimeFormatter ISO_DATEDIME_FMT = createFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'", ZoneOffset.UTC); // This is more strict respect the built-in DateTimeFormatter.ISO_INSTANT
	public static final String SYNCTOKEN_PATTERN = "yyyyMMddHHmmssSSS";
	public static final DateTimeFormatter SYNCTOKEN_FMT = createFormatter(SYNCTOKEN_PATTERN, ZoneOffset.UTC);
	
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
	
	/**
	 * Returns now (approximated - without nanos) dateTime in UTC.
	 * @return A dateTime of now in UTC timezone.
	 */
	public static ZonedDateTime now() {
		return now(ZoneOffset.UTC, false);
	}
	
	/**
	 * Returns now dateTime in UTC.
	 * @param exactNanos Set to `false` to ignore nanos resetting them to zero.
	 * @return A dateTime of now in UTC timezone.
	 */
	public static ZonedDateTime now(final boolean exactNanos) {
		return now(ZoneOffset.UTC, exactNanos);
	}
	
	/**
	 * Returns now dateTime.
	 * @param tz Desired Zone ID.
	 * @param exactNanos Set to `false` to ignore nanos resetting them to zero.
	 * @return A dateTime of now in desired timezone.
	 */
	public static ZonedDateTime now(final ZoneId tz, final boolean exactNanos) {
		if (exactNanos) {
			return ZonedDateTime.now(tz);
		} else {
			return ZonedDateTime.now(tz).withNano(0);
		}
	}
	
	public static boolean isAtMidnight(final ZonedDateTime dateTime) {
		return (dateTime.compareTo(withTimeAtStartOfDay(dateTime)) == 0);
	}
	
	public static boolean isEndOfDay(final ZonedDateTime dateTime, final boolean relaxed) {
		if (relaxed) {
			return (dateTime.compareTo(withTimeAtEndOfDay(dateTime)) == 0) || ((dateTime.getHour() == 23) && (dateTime.getMinute()== 59));
		} else {
			return (dateTime.compareTo(withTimeAtEndOfDay(dateTime)) == 0);
		}	
	}
	
	/**
	 * Set time at midnight (start-of-day).
	 * @param dateTime The dateTime to set.
	 * @return The dateTime at midnight
	 */
	public static ZonedDateTime withTimeAtStartOfDay(final ZonedDateTime dateTime) {
		// https://stackoverflow.com/questions/30293748/java-time-equivalent-of-joda-time-withtimeatstartofday-get-first-moment-of-t
		if (dateTime == null) return null;
		return dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
	/**
	 * Creates a new dateTime at midnight (start-of-day) using passed localDate as template.
	 * @param localDate The target localDate.
	 * @param zone Desired Zone ID.
	 * @return New dateTime at midnight
	 */
	public static ZonedDateTime withTimeAtStartOfDay(final LocalDate localDate, final ZoneId zone) {
		if (localDate == null) return null;
		return localDate.atStartOfDay(Check.notNull(zone, "zone"));
	}
	
	/**
	 * Set time at midday.
	 * @param dateTime The dateTime to set.
	 * @return The dateTime at midday
	 */
	public static ZonedDateTime withTimeAtMidday(final ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.withHour(12).withMinute(0).withSecond(0).withNano(0);
	}
	
	/**
	 * Set time at end-of-day.
	 * @param dateTime The dateTime to set.
	 * @return The dateTime at end-of-day
	 */
	public static ZonedDateTime withTimeAtEndOfDay(final ZonedDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
	}
	
	/**
	 * Creates a new dateTime at midnight (start-of-day) using passed localDate as template.
	 * @param localDate The target localDate.
	 * @param zone Desired Zone ID.
	 * @return New dateTime at midnight
	 */
	public static ZonedDateTime withTimeAtEndOfDay(final LocalDate localDate, final ZoneId zone) {
		if (localDate == null) return null;
		return withTimeAtEndOfDay(withTimeAtStartOfDay(localDate, zone));
	}
	
	/**
	 * Compares two times and returns the youngest one.
	 * @param time1 Time instance 1.
	 * @param time2 Time instance 2.
	 * @return Youngest time instance.
	 */
	public static LocalTime min(final LocalTime time1, final LocalTime time2) {
		return (time1.compareTo(time2) < 0) ? time1 : time2;
	}
	
	/**
	 * Compares two times and returns the older one.
	 * @param time1 Time instance 1.
	 * @param time2 Time instance 2.
	 * @return Oldest time instance.
	 */
	public static LocalTime max(final LocalTime time1, final LocalTime time2) {
		return (time1.compareTo(time2) > 0) ? time1 : time2;
	}
	
	//public static boolean between(final ReadablePartial value, final ReadablePartial partial1, final ReadablePartial partial2) {
	
	//public static boolean between(final ReadablePartial value, final ReadablePartial partial1, final ReadablePartial partial2, final boolean inclusiveStart, final boolean inclusiveEnd) {
	
	//public static boolean between(ReadableInstant value, ReadableInstant instant1, ReadableInstant instant2) {
	
	/**
	 * Returns the difference in minutest between two temporal objects.
	 * @param temporal1 The start temporal object, must not be null.
	 * @param temporal2 The end temporal object, must not be null.
	 * @param absolute Set to `true` to return the absolute difference, instants position does not matter.
	 * @return 
	 */
	public static int minutesBetween(final Temporal temporal1, final Temporal temporal2, final boolean absolute) {
		int minutes = (int) ChronoUnit.MINUTES.between(temporal1, temporal2);
		return absolute ? Math.abs(minutes) : minutes;
	}
	
	/**
	 * Returns the difference in days between two temporal objects.
	 * @param temporal1 The start temporal object, must not be null.
	 * @param temporal2 The end temporal object, must not be null.
	 * @param absolute Set to `true` to return the absolute difference, instants position does not matter.
	 * @return 
	 */
	public static int daysBetween(final Temporal temporal1, final Temporal temporal2, final boolean absolute) {
		int days = (int) ChronoUnit.DAYS.between(temporal1, temporal2);
		return absolute ? Math.abs(days) : days;
	}
	
	/**
	 * Returns the difference in days not keeping into account the real amount of time passed between the two dates.
	 * @param dateTime1 The first dateTime
	 * @param dateTime2 The second dateTime.
	 * @param midnightAsDayBefore If `true` and dateTime2 is at midnight, dateTime2 will be set at the day before.
	 * @return 
	 */
	public static int calendarDaysBetween(final ZonedDateTime dateTime1, final ZonedDateTime dateTime2, final boolean midnightAsDayBefore) {
		final LocalDate date2 = (midnightAsDayBefore && isAtMidnight(dateTime2)) ? dateTime2.minusDays(1).toLocalDate() : dateTime2.toLocalDate();
		return daysBetween(dateTime1.toLocalDate(), date2, true);
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
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final String pattern, final ZoneId tz) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		return (tz != null) ? dtf.withZone(tz) : dtf;
	}
	
	/**
	 * Returns a copy for the passed formatter applying the specified timezone.
	 * @param formatter The base formatter to copy.
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatter(final DateTimeFormatter formatter, final ZoneId tz) {
		return (tz != null) ? formatter.withZone(tz) : formatter;
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
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHMS(final ZoneId tz) {
		return createFormatter(ISO_LOCALDATE_PATTERN + " " + ISO_LOCALTIME_PATTERN, tz);
	}
	
	/**
	 * Instantiates a ISO date-time ("yyyy-MM-dd HH:mm") formatter using specified timezone.
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMDHM(final ZoneId tz) {
		return createFormatter(ISO_LOCALDATE_PATTERN + " " + ISO_LOCALTIME_SHORT_PATTERN, tz);
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
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterYMD(final ZoneId tz) {
		return createFormatter(ISO_LOCALDATE_PATTERN, tz);
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
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHMS(final ZoneId tz) {
		return createFormatter(ISO_LOCALTIME_PATTERN, tz);
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
	 * @param tz Desired Zone ID to apply to the formatter.
	 * @return Formatter instance.
	 */
	public static DateTimeFormatter createFormatterHM(final ZoneId tz) {
		return createFormatter(ISO_LOCALTIME_SHORT_PATTERN, tz);
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
		return parseLocalTimeHMS(true, time);
	}
	
	public static LocalTime parseLocalTimeHMS(final boolean strict, final String time) {
		if (strict) {
			return parseLocalTime(ISO_LOCALTIME_FMT, time);
		} else {
			return parseLocalTime(DateTimeFormatter.ISO_LOCAL_TIME, time);
		}
	}
	
	public static LocalTime parseLocalTime(final DateTimeFormatter formatter, final String time) {
		if (StringUtils.isBlank(time)) return null;
		return LocalTime.parse(time, formatter);
	}
	
	public static ZonedDateTime parseDateTimeYMDHMS(final ZoneId tz, final String date, final String time) {
		return parseDateTime(createFormatterYMDHMS(tz), StringUtils.join(date, " ", time));
	}
	
	public static ZonedDateTime parseDateTimeYMDHMS(final ZoneId tz, final String dateTime) {
		String s = StringUtils.replace(dateTime, "T", " "); // Ported from DateTimeUtils: why is necessary? Use case?
		return parseDateTime(createFormatterYMDHMS(tz), dateTime);
	}
	
	public static ZonedDateTime parseDateTimeISO(final String isoDateTime) {
		return parseDateTimeISO(true, isoDateTime);
	}
	
	public static ZonedDateTime parseDateTimeISO(final boolean strict, final String isoDateTime) {
		if (strict) {
			return parseDateTime(ISO_DATEDIME_FMT, isoDateTime);
		} else {
			return parseDateTime(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC), isoDateTime);
		}
	}
	
	public static ZonedDateTime parseDateTime(final DateTimeFormatter formatter, final String dateTime) {
		if (StringUtils.isBlank(dateTime)) return null;
		return ZonedDateTime.parse(dateTime, formatter);
	}
	
	// ---------- Prints
	
	public static String printYMDHMS() {
		return null;
	}
	
	/**
	 * Converts the passed date-time into a String in ISO format, applying UTC timezone before printing.
	 * @param dateTime The value to convert.
	 * @return The resulting String
	 */
	public static String printISO(final ZonedDateTime dateTime) {
		return printISO(dateTime, null);
	}
	
	/**
	 * Converts the passed date-time into a String in ISO format, applying UTC timezone before printing.
	 * @param dateTime The value to convert.
	 * @param defaultString The String to return in case of null input.
	 * @return The resulting String
	 */
	public static String printISO(final ZonedDateTime dateTime, final String defaultString) {
		return print(ISO_DATEDIME_FMT, dateTime, defaultString);
	}
	
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
	
	public static ZonedDateTime toZonedDateTime(final LocalDate localDate, final LocalTime localTime, final ZoneId zone) {
		return toZonedDateTime(localDate, localTime, zone, true);
	}
	
	public static ZonedDateTime toZonedDateTime(final LocalDate localDate, final LocalTime localTime, final ZoneId zone, final boolean pushForwardAtGap) {
		LocalDateTime ldt = localDate.atTime(localTime);
		ZoneRules rules = zone.getRules();
		
		if (rules.isValidOffset(ldt, rules.getOffset(ldt))) {
			// Instant is valid, no gap
			return ldt.atZone(zone);
		}
		
		// ldt falls in a gap
		if (!pushForwardAtGap) {
			throw new DateTimeException("Invalid local date-time in zone " + zone + ": " + ldt);
		}
		
		// Find the transition and use its instant (first valid moment after the gap)
		ZoneOffsetTransition transition = rules.getTransition(ldt);
		return transition.getDateTimeAfter().atZone(zone);
	}
	
	// ----------  JodaTime -> java.time
	
	public static ZoneId toZoneId(final org.joda.time.DateTimeZone tz) {
		if (tz == null) return null;
		return ZoneId.of(tz.getID());
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
