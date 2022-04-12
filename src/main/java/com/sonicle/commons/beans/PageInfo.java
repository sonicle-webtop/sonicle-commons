/*
 * Copyright (C) 2022 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2022 Sonicle S.r.l.".
 */
package com.sonicle.commons.beans;

import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author malbinola
 */
public class PageInfo {
	private final int pageNumber;
	private final int pageSize;
	private final boolean calculateFullCount;
	
	public PageInfo(final int pageNumber, final int pageSize, final boolean calculateFullCount) {
		this.pageNumber = Check.greaterOrEqualThan(1, pageNumber, "Page number must start from 1");
		this.pageSize = Check.greaterThan(0, pageSize, "The number of rows per page must be greater of 0");
		this.calculateFullCount = calculateFullCount;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public boolean getCalculateFullCount() {
		return calculateFullCount;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPageNumber())
			.append(getPageSize())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PageInfo == false) return false;
		if (this == obj) return true;
		final PageInfo otherObject = (PageInfo)obj;
		return new EqualsBuilder()
			.append(getPageNumber(), otherObject.getPageNumber())
			.append(getPageSize(), otherObject.getPageSize())
			.isEquals();
	}
}
