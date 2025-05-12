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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author malbinola
 */
public class DateTimeWindow {
	private static final String NULL = "00000000-000000000";
	private static final DateTimeFormatter FMT = DateTimeUtils.createFormatter("yyyyMMdd-HHmmssSSS", DateTimeZone.UTC);
	private final DateTime start;
	private final DateTime end;
	
	protected DateTimeWindow(DateTime start, DateTime end) {
		this.start = start;
		this.end = end;
	}
	
	private DateTimeWindow(Builder builder) {
		this(builder.start, builder.end);
	}
	
	public DateTime getStart() {
		return start;
	}
	
	public DateTime getStartOrDefault(DateTime defaultTime) {
		return (start != null) ? start : defaultTime;
	}
	
	public DateTime getEnd() {
		return end;
	}
	
	public DateTime getEndOrDefault(DateTime defaultTime) {
		return (end != null) ? end : defaultTime;
	}
	
	public boolean isEmpty() {
		return (start == null) || (end == null);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(DateTimeUtils.print(FMT, start, NULL))
			.append(DateTimeUtils.print(FMT, end, NULL))
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateTimeWindow == false) return false;
		if (this == obj) return true;
		final DateTimeWindow otherObject = (DateTimeWindow)obj;
		return new EqualsBuilder()
			.append(DateTimeUtils.print(FMT, start, NULL), DateTimeUtils.print(FMT, otherObject.start, NULL))
			.append(DateTimeUtils.print(FMT, end, NULL), DateTimeUtils.print(FMT, otherObject.end, NULL))
			.isEquals();
	}

	@Override
	public String toString() {
		return toString(DateTimeUtils.createFormatter("yyyy-MM-dd HH:mm:ss", DateTimeZone.getDefault()), "0000-00-00 00:00:00");
	}
	
	private String toString(DateTimeFormatter formatter, String nullValue) {
		return "(" + DateTimeUtils.print(formatter, start, nullValue) + " -> " + DateTimeUtils.print(formatter, end, nullValue) + ")";
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private DateTime start = null;
		private DateTime end = null;
		
		public Builder with(DateTime start, DateTime end) {
			withStart(start);
			withEnd(end);
			return this;
		}
		
		public Builder withStart(int year, int month, int day, int hours, int minutes, int seconds) {
			return withStart(new DateTime(year, month, day, hours, minutes, seconds));
		}
		
		public Builder withStart(DateTime start) {
			this.start = start;
			if ((start != null) && (end != null) && end.isBefore(start)) {
				end = start;
			}
			return this;
		}
		
		public Builder withEnd(int year, int month, int day, int hours, int minutes, int seconds) {
			return withEnd(new DateTime(year, month, day, hours, minutes, seconds));
		}
		
		public Builder withEnd(DateTime end) {
			this.end = end;
			if ((start != null) && (end != null) && start.isAfter(end)) {
				start = end;
			}
			return this;
		}
		
		public DateTimeWindow build() {
			return new DateTimeWindow(this);
		}
	}
}
