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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.IllegalInstantException;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author malbinola
 */
public class JodaTimeUtils {
	public static final DateTimeFormatter ISO_LOCALDATE_FMT = createFormatter("yyyy-MM-dd");
	public static final DateTimeFormatter ISO_LOCALTIME_FMT = createFormatter("HH:mm:ss");
	public static final DateTimeFormatter ISO_DATEDIME_FMT = createFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'", DateTimeZone.UTC);
	public static final LocalTime TIME_AT_STARTOFDAY = new LocalTime(0, 0, 0, 0);
	public static final LocalTime TIME_AT_ENDOFDAY = new LocalTime(23, 59, 59, 0);
	
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
	
	// ---------- Parse
	
	public static LocalDate parseISOLocalDate(final String isoLocalDate) {
		return parseLocalDate(isoLocalDate, ISO_LOCALDATE_FMT);
	}
	
	public static LocalDate parseLocalDate(final String date, final DateTimeFormatter formatter) {
		if (StringUtils.isBlank(date)) return null;
		return formatter.parseLocalDate(date);
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
