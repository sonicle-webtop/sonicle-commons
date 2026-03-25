/*
 * Copyright (C) 2026 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2026 Sonicle S.r.l.".
 */
package com.sonicle.commons.time;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author malbinola
 */
public class DurationParser {
	private static final Pattern PATTERN = Pattern.compile("^(\\d+)\\s*(ms|s|m|h|d|w)$", Pattern.CASE_INSENSITIVE);
	
	public static Duration parse(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Duration cannot be null");
		}
		
		Matcher matcher = PATTERN.matcher(value.trim());
		if (!matcher.matches()) {	
			throw new IllegalArgumentException("Invalid duration: " + value);
		}
		
		long amount = Long.parseLong(matcher.group(1));
		String unit = matcher.group(2).toLowerCase();	
		
		switch (unit) {
			case "ms": return Duration.ofMillis(amount);
			case "s": return Duration.ofSeconds(amount);
			case "m": return Duration.ofMinutes(amount);
			case "h": return Duration.ofHours(amount);
			case "d": return Duration.ofDays(amount);
			case "w": return Duration.ofDays(amount * 7);
			default: throw new IllegalArgumentException("Unknown unit: " + unit);
		}
		
		/*
		return switch (unit) {
			case "ms" -> Duration.ofMillis(amount);
			case "s" -> Duration.ofSeconds(amount);
			case "m" -> Duration.ofMinutes(amount);
			case "h" -> Duration.ofHours(amount);
			case "d" -> Duration.ofDays(amount);
			case "w"  -> Duration.ofDays(amount * 7);
			default -> throw new IllegalArgumentException("Unknown unit: " + unit);
		}
		*/
	}
	
	public static long parseToMillis(String value) {
		return parse(value).toMillis();
	}
	
	public static int toIntHours(Duration duration) {
		return Long.valueOf(duration.toHours()).intValue();
	}
	
	public static int toIntDays(Duration duration) {
		return Long.valueOf(duration.toDays()).intValue();
	}
}
