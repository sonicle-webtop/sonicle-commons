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
package com.sonicle.commons.flags;

/**
 *
 * @author malbinola
 * @param <E>
 */
public final class BitFlags<E extends Enum<E> & BitFlagsEnum<E>> extends BaseBitFlags<E> {
	
	public BitFlags(final Class<E> enumType) { super(enumType); }
	public BitFlags(final Class<E> enumType, final E... flags) { super(enumType, flags); }
	public BitFlags(final Class<E> enumType, final long value) { super(enumType, value); }
	public BitFlags(final BitFlags<E> from) { super(from); }
	
	@Override
	public BitFlags<E> set(BaseBitFlags<E> flags) {
		return (BitFlags<E>)super.set(flags);
	}
	
	@Override
	public BitFlags<E> set(E flag) {
		return (BitFlags<E>)super.set(flag);
	}
	
	@Override
	public BitFlags<E> setAll() {
		return (BitFlags<E>)super.setAll();
	}
	
	@Override
	public BitFlags<E> set(E... flags) {
		return (BitFlags<E>)super.set(flags);
	}
	
	@Override
	public BitFlags<E> unset(E flag) {
		return (BitFlags<E>)super.unset(flag);
	}
	
	@Override
	public BitFlags<E> unset(E... flags) {
		return (BitFlags<E>)super.unset(flags);
	}
	
	public BitFlags<E> copy() {
		return new BitFlags<>(this);
	}
	
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> BitFlags<E> newFrom(final Class<E> enumType, BaseBitFlags<?> valueFrom) {
		return new BitFlags<>(enumType, valueFrom.getValue());
	}
	
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> BitFlags<E> newFrom(final Class<E> enumType, long value) {
		return new BitFlags<>(enumType, value);
	}
	
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> BitFlags<E> allOf(final Class<E> enumType) {
		return BitFlagsUtils.allOf(BitFlags.class, enumType);
	}
	
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> BitFlags<E> noneOf(final Class<E> enumType) {
		return BitFlagsUtils.noneOf(BitFlags.class, enumType);
	}
	
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> BitFlags<E> with(final E flag, final E... moreFlags) {
		return BitFlagsUtils.with(BitFlags.class, flag, moreFlags);
	}
}
