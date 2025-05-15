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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class JavaTimeUtils {
	
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
	
	// ---------- Formatting
	
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
}
