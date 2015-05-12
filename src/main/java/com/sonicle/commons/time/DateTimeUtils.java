/*
 * sonicle-commons is a helper library developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
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

import org.joda.time.DateTimeZone;
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
}
