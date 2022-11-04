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

import com.sonicle.commons.FlagUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import net.sf.qualitycheck.Check;

/**
 *
 * @author malbinola
 */
public class BitFlagsUtils {
	
	/**
	 * Creates new instance of specified BitFlags with ALL flags set.
	 * This can be used only in BaseBitFlags subclasses with NO parametric enum-type argument.
	 * @param <E>
	 * @param <T>
	 * @param returnType Return object type, that is extending BaseBitFlags<?>.
	 * @return BitFlags instance with ALL flags set
	 */
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T allOf(final Class<T> returnType) {
		return allOf(returnType, null);
	}
	
	/**
	 * Creates new instance of specified BitFlags with ALL flags set.
	 * This can be used only in BaseBitFlags subclasses with parametric enum-type argument.
	 * @param <E>
	 * @param <T>
	 * @param returnType Return object type, that is extending BaseBitFlags<?>.
	 * @param enumType Enum type, only necessary when using generic parametric enum-type.
	 * @return BitFlags instance with ALL flags set
	 */
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T allOf(final Class<T> returnType, final Class<E> enumType) {
		Check.notNull(returnType, "returnType");
		final T newInstance = newEmptyInstance(returnType, enumType);
		newInstance.setAll();
		return newInstance;
		//return newInstance(returnType, enumType, computeValueOf(EnumUtils.allTypesOf(enumType)));
	}
	
	/**
	 * Creates new instance of specified BitFlags with NO flags set.
	 * This can be used only in BaseBitFlags subclasses with NO parametric enum-type argument.
	 * @param <E>
	 * @param <T>
	 * @param returnType Return object type, that is extending BaseBitFlags<?>.
	 * @return BitFlags instance with NO flags set
	 */
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T noneOf(final Class<T> returnType) {
		return noneOf(returnType, null);
	}
	
	/**
	 * Creates new instance of specified BitFlags with NO flags set.
	 * This can be used only in BaseBitFlags subclasses with parametric enum-type argument.
	 * @param <E>
	 * @param <T>
	 * @param returnType Return object type, that is extending BaseBitFlags<?>.
	 * @param enumType Enum type, only necessary when using generic parametric enum-type.
	 * @return BitFlags instance with NO flags set
	 */
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T noneOf(final Class<T> returnType, final Class<E> enumType) {
		Check.notNull(returnType, "returnType");
		return newEmptyInstance(returnType, enumType);
	}
	
	/**
	 * Creates new instance of specified BitFlags with passed flags set.
	 * @param <E>
	 * @param <T>
	 * @param returnType Return object type, that is extending BaseBitFlags<?>.
	 * @param flag The Enum flag to set.
	 * @param moreFlags More Enum flags to set, can be null.
	 * @return 
	 */
	public static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T with(final Class<T> returnType, final E flag, final E... moreFlags) {
		Check.notNull(returnType, "returnType");
		Check.notNull(flag, "flag");
		final T newInstance = newEmptyInstance(returnType, flag.getDeclaringClass());
		newInstance.set(flag);
		if (moreFlags != null) newInstance.set(moreFlags);
		return newInstance;
	}
	
	private static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> T newEmptyInstance(final Class<T> returnType, final Class<E> enumType) {
		Check.notNull(returnType, "returnType");
		try {
			Constructor<T> contructor = null;
			if (enumType != null) {
				// Due to Java erasure, enumType cannot be inferred at runtime 
				// getting the actual argument or using reflection, so classes 
				// with generic enum-type argument MUST pass the enum-type and 
				// use this to instantiate the new BitFlag.
				try {
					contructor = returnType.getConstructor(Class.class);
				} catch (NoSuchMethodException ex1) {
					System.out.println(ex1.getMessage());
				}
			}
			if (contructor != null) {
				return contructor.newInstance(enumType);
			} else {
				// If no enum type was provided, it's assumed that this method was
				// called from a class that extends BaseBitFlags<?> (subclass) that 
				// do not have a parametric type; here enumType is discovered 
				// evaluating the current type argument at runtime. This kind 
				// of classes do NOT offer a constructor with enumType parameter.
				return (T)returnType.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
		
		/*
		try {
			if (enumType != null) {
				// Due to Java erasure, enumType cannot be inferred at runtime 
				// getting the actual argument or using reflection, so classes 
				// with generic enum-type argument MUST pass the enum-type and 
				// use this to instantiate the new BitFlag.
				return (T)returnType.getConstructor(enumType).newInstance(enumType);
			} else {
				// If no enum type was provided, it's assumed that this method was
				// called from a class that extends BaseBitFlags<?> (subclass) that 
				// do not have a parametric type; here enumType is discovered 
				// evaluating the current type argument at runtime. This kind 
				// of classes do NOT offer a constructor with enumType parameter.
				return (T)returnType.newInstance();
			}
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
		*/
	}
	
	/*
	private static <E extends Enum<E> & BitFlagsEnum<E>, T extends BaseBitFlags<E>> long computeValueOf(final Collection<E> flags) {
		long value = 0;
		for (E e : flags) value = FlagUtils.set(value, e.mask());
		return value;
	}
	*/
}
