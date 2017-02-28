/* 
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
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.commons.beans;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author malbinola
 */
public class VirtualAddress {
	private String local;
	private String domain;
	
	public VirtualAddress() {}
	
	public VirtualAddress(String fullAddress) {
		int at = StringUtils.lastIndexOf(fullAddress, "@");
		if (at == -1) {
			this.local = fullAddress;
		} else {
			this.local = StringUtils.substring(fullAddress, 0, at);
			this.domain = StringUtils.substring(fullAddress, at+1);
		}
	}
	
	public VirtualAddress(String local, String domain) {
		this.local = local;
		this.domain = domain;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Override
	public String toString() {
		if (this.domain != null) {
			return StringUtils.defaultString(local) + "@" + StringUtils.defaultString(domain);
		} else {
			return StringUtils.defaultString(local);
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(local)
			.append(domain)
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof VirtualAddress == false) return false;
		if(this == obj) return true;
		final VirtualAddress otherObject = (VirtualAddress) obj;
		return new EqualsBuilder()
			.append(local, otherObject.local)
			.append(domain, otherObject.domain)
			.isEquals();
	}
}
