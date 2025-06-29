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
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author malbinola
 */
public class DateWindow {
	private static final String NULL = "00000000";
	private static final DateTimeFormatter FMT = JodaTimeUtils.createFormatter("yyyyMMdd", DateTimeZone.UTC);
	private final LocalDate start;
	private final LocalDate end;
	
	protected DateWindow(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}
	
	private DateWindow(Builder builder) {
		this(builder.start, builder.end);
	}
	
	public LocalDate getStart() {
		return start;
	}
	
	public LocalDate getStartOrDefault(LocalDate defaultTime) {
		return (start != null) ? start : defaultTime;
	}
	
	public LocalDate getEnd() {
		return end;
	}
	
	public LocalDate getEndOrDefault(LocalDate defaultTime) {
		return (end != null) ? end : defaultTime;
	}
	
	public boolean isEmpty() {
		return (start == null) || (end == null);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(JodaTimeUtils.print(FMT, start, NULL))
			.append(JodaTimeUtils.print(FMT, end, NULL))
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateWindow == false) return false;
		if (this == obj) return true;
		final DateWindow otherObject = (DateWindow)obj;
		return new EqualsBuilder()
			.append(JodaTimeUtils.print(FMT, start, NULL), JodaTimeUtils.print(FMT, otherObject.start, NULL))
			.append(JodaTimeUtils.print(FMT, end, NULL), JodaTimeUtils.print(FMT, otherObject.end, NULL))
			.isEquals();
	}
	
	@Override
	public String toString() {
		return toString(JodaTimeUtils.createFormatter("yyyy-MM-dd", DateTimeZone.getDefault()), "0000-00-00");
	}
	
	private String toString(DateTimeFormatter formatter, String nullValue) {
		return "(" + JodaTimeUtils.print(formatter, start, nullValue) + " -> " + JodaTimeUtils.print(formatter, end, nullValue) + ")";
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private LocalDate start = null;
		private LocalDate end = null;
		
		public Builder with(LocalDate start, LocalDate end) {
			withStart(start);
			withEnd(end);
			return this;
		}
		
		public Builder withStart(int year, int month, int day) {
			return withStart(new LocalDate(year, month, day));
		}
		
		public Builder withStart(LocalDate start) {
			this.start = start;
			if ((start != null) && (end != null) && end.isBefore(start)) {
				end = start;
			}
			return this;
		}
		
		public Builder withEnd(int year, int month, int day) {
			return withEnd(new LocalDate(year, month, day));
		}
		
		public Builder withEnd(LocalDate end) {
			this.end = end;
			if ((start != null) && (end != null) && start.isAfter(end)) {
				start = end;
			}
			return this;
		}
		
		public DateWindow build() {
			return new DateWindow(this);
		}
	}
}
