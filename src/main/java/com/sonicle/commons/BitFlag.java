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
package com.sonicle.commons;

import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author malbinola
 * @param <E>
 */
public class BitFlag<E extends BitFlagEnum> {
	private int value;
	
	public BitFlag() {
		this(0);
	}
	
	public BitFlag(int value) {
		this.value = value;
	}
	
	public boolean has(E flag) {
		return FlagUtils.has(this.value, flag.value());
	}
	
	public BitFlag set(E... flags) {
		for (E bfe : flags) {
			this.value = FlagUtils.set(this.value, bfe.value());
		}
		return this;
	}
	
	public BitFlag unset(E... flags) {
		for (E bfe : flags) {
			this.value = FlagUtils.unset(this.value, bfe.value());
		}
		return this;
	}
	
	public BitFlag copy() {
		return new BitFlag(this.value);
	}
	
	public <E extends Enum> String toString(Class<E> clazz) {
		ToStringBuilder tsb = new ToStringBuilder(this);
		for (Map.Entry<Integer, E> entry : BitFlagEnum.getValues(clazz).entrySet()) {
			if (FlagUtils.has(this.value, entry.getKey())) tsb.append(entry.getValue().name());
		}
		return tsb.toString();
	}
	
	public static <E extends BitFlagEnum> BitFlag of(E... flags) {
		int v = 0;
		for (E f : flags) {
			v = FlagUtils.set(v, f.value());
		}
		return new BitFlag(v);
	}
	
	public static BitFlag none() {
		return new BitFlag();
	}
}
