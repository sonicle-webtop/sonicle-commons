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

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.FlagUtils;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is the Class that defines the BitFlags itself, as result set of 
 * possible flag values. Under the hood a long value keeps track of active bits.
 * @author malbinola
 * https://eddmann.com/posts/using-bit-flags-and-enumsets-in-java/
 * https://www.zacsweers.dev/api-design-case-studies-intersection-types/
 * @param <E>
 */
public abstract class BaseBitFlags<E extends Enum<E> & BitFlagsEnum<E>> {
	private final Class<E> enumType;
	private long value;
	
	protected BaseBitFlags(final Class<E> enumType) {
		this(enumType, 0);
	}
	
	protected BaseBitFlags(final Class<E> enumType, final E... flags) {
		this(enumType, 0);
		for (E flag : flags) set(flag);
	}
	
	protected BaseBitFlags(final Class<E> enumType, final long value) {
		this.enumType = enumType != null ? enumType : findTypeArgument();
		if (value == 0) {
			this.value = value;
		} else {
			long newValue = 0;
			for (E e : allAvailableFlags()) {
				if (FlagUtils.has(value, e.mask())) {
					newValue = FlagUtils.set(newValue, e.mask());
				}
			}
			this.value = newValue;
		}
	}
	
	protected BaseBitFlags(final BaseBitFlags<E> from) {
		this(from.enumType, from.value);
	}
	
	/**
	 * Returns the underlying numeric value representing the current set of flags.
	 * Each bit corresponds to a specific enum flag.
	 * @return the raw bitmask value
	 */
	public final long getValue() {
		return value;
	}
	
	/**
	 * Checks if NO flag is currently set.
	 * @return true if at least a flag is set, false otherwise
	 */
	public final boolean isEmpty() {
		return value == 0;
	}
	
	/**
	 * Checks whether the specified flag is currently set.
	 * @param flag the flag to check
	 * @return true if the flag is set, false otherwise
	 */
	public final boolean has(E flag) {
		return FlagUtils.has(this.value, flag.mask());
	}
	
	/**
	 * Checks whether only the specified flag is set and no other flags are active.
	 * @param flag the flag to check
	 * @return true if this instance contains only the specified flag
	 */
	public final boolean hasOnly(E flag) {
		return this.value == flag.mask();
		//return has(flag) && this.value == flag.mask();
	}
	
	/**
	 * Checks whether only the specified flags are set.
	 * @param flags the expected flags
	 * @return true if exactly those flags are set and no others
	 */
	public final boolean hasOnly(E... flags) {
		long expected = 0;
		for (E f : flags) {
			expected = FlagUtils.set(expected, f.mask());
		}
		return this.value == expected;
	}
	
	/**
	 * Checks whether at least one of the specified flags is set.
	 * @param flags the flags to check
	 * @return true if at least one flag is set, false otherwise
	 */
	public final boolean hasAny(E... flags) {
		if (flags == null) return false;
		
		for (E flag : flags) {
			if (has(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Checks whether all the specified flags are set.
	 * @param flags the flags to check
	 * @return true if all flags are set, false otherwise
	 */
	public final boolean hasAll(E... flags) {
		if (flags == null) return false;
		
		for (E flag : flags) {
			if (!has(flag)) return false;
		}
		return true;
	}
	
	/**
	 * Sets all flags that are active in the provided BitFlags instance.
	 * @param flags the flags to copy from
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> set(BaseBitFlags<E> flags) {
		for (E e : allAvailableFlags()) {
			if (flags.has(e)) set(e);
		}
		return this;
	}
	
	/**
	 * Sets the specified flag.
	 * @param flag the flag to enable
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> set(E flag) {
		this.value = FlagUtils.set(this.value, flag.mask());
		return this;
	}
	
	/**
	 * Enables all available flags.
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> setAll() {
		for (E e : allAvailableFlags()) set(e);
		return this;
	}
	
	/**
	 * Sets multiple flags at once.
	 * @param flags the flags to enable
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> set(E... flags) {
		for (E flag : flags) set(flag);
		return this;
	}
	
	/**
	 * Unsets (clears) the specified flag.
	 * @param flag flag the flag to disable
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> unset(E flag) {
		this.value = FlagUtils.unset(this.value, flag.mask());
		return this;
	}
	
	/**
	 * Unsets (clears) multiple flags.
	 * @param flags the flags to disable
	 * @return this instance for chaining
	 */
	public BaseBitFlags<E> unset(E... flags) {
		for (E flag : flags) unset(flag);
		return this;
	}
	
	/**
	 * Returns all flags defined in the enum type.
	 * @return list of all available flags
	 */
	public final List<E> allAvailableFlags() {
		return EnumUtils.allTypesOf(enumType);
	}
	
	/**
	 * Returns the names of all available flags.
	 * @return set of flag names
	 */
	public final Set<String> allAvailableFlagNames() {
		return EnumUtils.allNamesOf(enumType);
	}
	
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this);
		for (E e : allAvailableFlags()) if (has(e)) sb.append(e.name());
		return sb.toString();
	}
	
	private Class<E> findTypeArgument() {
		// https://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
		// https://stackoverflow.com/questions/15084670/how-to-get-the-class-of-type-variable-in-java-generics
		final ParameterizedType parameterizedType = (ParameterizedType)(getClass().getGenericSuperclass());
		return (Class<E>)parameterizedType.getActualTypeArguments()[0];
	}
	
	/*
	private Class<E> findEnumClass() {
		java.lang.reflect.Type genericSuperClass = getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = null;
		while (parameterizedType == null) {
			if ((genericSuperClass instanceof ParameterizedType)) {
				parameterizedType = (ParameterizedType) genericSuperClass;
			} else {
				genericSuperClass = ((Class<?>) genericSuperClass).getGenericSuperclass();
			}
		}
		return (Class<E>) parameterizedType.getActualTypeArguments()[0];
		//final ParameterizedType parameterizedType = (ParameterizedType)(getClass().getGenericSuperclass());
		//return (Class<E>)parameterizedType.getActualTypeArguments()[0];
	}
	*/
}
