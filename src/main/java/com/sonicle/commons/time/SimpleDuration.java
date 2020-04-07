/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.commons.time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author malbinola
 */
public class SimpleDuration {
	private final static String DOUBLE_PART = "([0-9]*(.[0-9]+)?)";
	private final static int DOUBLE_GROUP = 1;
	private final static String UNIT_PART = "(|milli(second)?|second(e)?|minute|hour|day)s?";
	private final static int UNIT_GROUP = 3;
	private static final Pattern DURATION_PATTERN = Pattern.compile(DOUBLE_PART + "\\s*" + UNIT_PART, Pattern.CASE_INSENSITIVE);
	static final long SECONDS_COEFFICIENT = 1000;
	static final long MINUTES_COEFFICIENT = 60 * SECONDS_COEFFICIENT;
	static final long HOURS_COEFFICIENT = 60 * MINUTES_COEFFICIENT;
	static final long DAYS_COEFFICIENT = 24 * HOURS_COEFFICIENT;

	final long millis;
	
	public SimpleDuration(long millis) {
		this.millis = millis;
	}
	
	public long getMilliseconds() {
		return millis;
	}
	
	@Override
	public String toString() {
		if (millis < SECONDS_COEFFICIENT) {
			return millis + " milliseconds";
		} else if (millis < MINUTES_COEFFICIENT) {
			return millis / SECONDS_COEFFICIENT + " seconds";
		} else if (millis < HOURS_COEFFICIENT) {
			return millis / MINUTES_COEFFICIENT + " minutes";
		} else {
			return millis / HOURS_COEFFICIENT + " hours";
		}
	}
	
	public static SimpleDuration ofMillis(double millis) {
		return new SimpleDuration((long)millis);
	}
	
	public static SimpleDuration ofSeconds(double seconds) {
		return new SimpleDuration((long)(SECONDS_COEFFICIENT * seconds));
	}
	
	public static SimpleDuration ofMinutes(double minutes) {
		return new SimpleDuration((long)(MINUTES_COEFFICIENT * minutes));
	}
	
	public static SimpleDuration ofHours(double hours) {
		return new SimpleDuration((long)(HOURS_COEFFICIENT * hours));
	}
	
	public static SimpleDuration ofDays(double days) {
		return new SimpleDuration((long)(DAYS_COEFFICIENT * days));
	}
	
	public static SimpleDuration parse(String s) {
		Matcher matcher = DURATION_PATTERN.matcher(s);
		
		if (matcher.matches()) {
			String doubleStr = matcher.group(DOUBLE_GROUP);
			String unitStr = matcher.group(UNIT_GROUP);
			
			double doubleValue = Long.valueOf(doubleStr);
			if (unitStr.equalsIgnoreCase("milli") || unitStr.equalsIgnoreCase("millisecond") || unitStr.length() == 0) {
				return ofMillis(doubleValue);
			} else if (unitStr.equalsIgnoreCase("second") || unitStr.equalsIgnoreCase("seconde")) {
				return ofSeconds(doubleValue);
			} else if (unitStr.equalsIgnoreCase("minute")) {
				return ofMinutes(doubleValue);
			} else if (unitStr.equalsIgnoreCase("hour")) {
				return ofHours(doubleValue);
			} else if (unitStr.equalsIgnoreCase("day")) {
				return ofDays(doubleValue);
			} else {
				throw new IllegalStateException("Unexpected " + unitStr);
			}
		} else {
			throw new IllegalArgumentException("String value [" + s + "] is not in the expected format.");
		}
	}
}
