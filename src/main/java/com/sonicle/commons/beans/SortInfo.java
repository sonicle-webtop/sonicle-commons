/*
 * Copyright (C) 2021 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2021 Sonicle S.r.l.".
 */
package com.sonicle.commons.beans;

import com.google.gson.annotations.SerializedName;
import com.sonicle.commons.EnumUtils;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class is used as an argument for functions that sort a data set.
 * @author malbinola
 */
public class SortInfo {
	private String field;
	private Direction direction;
	
	public SortInfo() {}
	
	public SortInfo(String field, Direction direction) {
		this.field = Check.notNull(field, "field");
		this.direction = Check.notNull(direction, "direction");
	}
	
	public String getField() {
		return field;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public boolean isAscending() {
		return Direction.ASC.equals(direction);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getField())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SortInfo == false) return false;
		if (this == obj) return true;
		final SortInfo otherObject = (SortInfo)obj;
		return new EqualsBuilder()
			.append(getField(), otherObject.getField())
			.isEquals();
	}
	
	public static SortInfo asc(final String field) {
		return new SortInfo(field, Direction.ASC);
	}
	
	public static SortInfo desc(final String field) {
		return new SortInfo(field, Direction.DESC);
	}
	
	public static enum Direction {
		@SerializedName("ASC") ASC,
		@SerializedName("DESC") DESC
	}
	
	public static Set<SortInfo> parseCollection(final Set<String> collection) throws ParseException {
		LinkedHashSet<SortInfo> sortInfo = new LinkedHashSet<>();
		if (collection != null) {
			for (String s : collection) {
				sortInfo.add(SortInfo.parse(s));
			}
		}
		return sortInfo;
	}
	
	public static SortInfo parse(final String sortInfo) throws ParseException {
		String[] tokens = StringUtils.split(sortInfo, " ");
		if (tokens.length == 0 || StringUtils.isBlank(tokens[0])) throw new ParseException(sortInfo, 0);
		return new SortInfo(tokens[0], EnumUtils.forSerializedName(tokens.length == 1 ? null : tokens[1], Direction.ASC, Direction.class));
	}
}
