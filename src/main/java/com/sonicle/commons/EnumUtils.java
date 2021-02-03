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
package com.sonicle.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class EnumUtils {
	
	public static final Gson GSON = new GsonBuilder()
		.serializeNulls()
		.create();
	
	public static <E extends Enum<E>> E getEnum(String enumName, Class<E> enumClass) {
		if(enumName == null) return null;
		try {
			return Enum.valueOf(enumClass, enumName);
		} catch(IllegalArgumentException ex) {
			return null;
		}
	}
	
	public static <E extends Enum<E>> String getName(Enum<E> en) {
		return (en == null) ? null : en.name();
	}
	
	public static <E extends Enum<E>> boolean equals(Enum<E> en1, Enum<E> en2) {
		if(en1 == null) return false;
		return en1.equals(en2);
	}
	
	
	
	
	/**
	 * Convenience method for getting the enum from its string value representation.
	 * The enum's toString() method will be used for getting the value, so if you
	 * need to change the default return you need to override properly toString() 
	 * method in the enum class.
	 * 
	 * Example:
	 * 
	 * public enum MyEnum {
	 *		VAL_1("val1"), VAL_2("val2");
	 *		private final String value;
	 *		private MyEnum(String value) { this.value = value; }
	 *		@Override
	 *		public String toString() { return value; }
	 * }
	 * 
	 * @param <E>
	 * @param value The String value
	 * @param enumClass The enum class
	 * @return The matching enum if found, null otherwise
	 */
	public static <E extends Enum<E>> E forValue(String value, Class<E> enumClass) {
		if (value == null) return null;
		for (E e : EnumSet.allOf(enumClass)) {
			if (value.equals(e.toString())) return e;
		}
		return null;
	}
	
	public static <E extends Enum<E>> String getValue(E en) {
		return (en == null) ? null : en.toString();
	}
	
	/**
	 * Transforms a string name to enum value equivalent representation.
	 * JSON library annotation will be used for getting the serializedName, so you
	 * need to annotate the enum class properly before using this method.
	 * 
	 * Example:
	 * 
	 * public enum MyEnum {
	 *		@SerializedName("val1") VAL_1, @SerializedName("val2") VAL_2;
	 * }
	 * 
	 * @param <E> Enum type
	 * @param serializedName The name as string
	 * @param enumClass The enum class The Enum class type
	 * @return The matching enum if found, null otherwise
	 */
	public static <E extends Enum<E>> E forSerializedName(final String serializedName, final Class<E> enumClass) {
		return forSerializedName(serializedName, null, enumClass);
	}
	
	/**
	 * Transforms a string name to enum value equivalent representation.
	 * JSON library annotation will be used for getting the serializedName, so you
	 * need to annotate the enum class properly before using this method.
	 * 
	 * @param <E> Enum type
	 * @param serializedName The name as string
	 * @param defaultValue Default value to return if no match is found
	 * @param enumClass The enum class The Enum class type
	 * @return The matching enum if found, the default value otherwise
	 */
	public static <E extends Enum<E>> E forSerializedName(final String serializedName, final E defaultValue, final Class<E> enumClass) {
		if (serializedName == null) return defaultValue;
		final E value = GSON.fromJson(serializedName, enumClass);
		return (value == null) ? defaultValue : value;
	}
	
	/**
	 * Transforms an enum to its string value equivalent.
	 * @param <E> Enum type
	 * @param en The enum value
	 * @return The name of enum value as string
	 */
	public static <E extends Enum<E>> String toSerializedName(final E en) {
		if (en == null) {
			return null;
		} else {
			final String s = GSON.toJson(en, en.getClass());
			return StringUtils.substring(s, 1, s.length()-1);
		}
	}
	
	/**
	 * Transforms a set of enums to their string values equivalent.
	 * Resulting nulls during conversion will be skipped.
	 * @param <E> Enum type
	 * @param ens The enum values
	 * @return The names of enum values as a collection of strings
	 */
	public static <E extends Enum<E>> List<String> toSerializedNames(final Collection<E> ens) {
		return toSerializedNames(ens, true);
	}
	
	/**
	 * Transforms a set of enums to their string values equivalent.
	 * @param <E> Enum type
	 * @param ens The enum values
	 * @param skipNulls False to keep null elements in resulting collection as result of invalid enum conversion.
	 * @return The names of enum values as a collection of strings
	 */
	public static <E extends Enum<E>> List<String> toSerializedNames(final Collection<E> ens, boolean skipNulls) {
		if (ens == null) {
			return null;
		} else {
			List<String> strings = new ArrayList<>();
			for (E en : ens) {
				final String s = toSerializedName(en);
				if (!skipNulls || s != null) strings.add(s);
			}
			return strings;
		}
	}
	
	
	
	/**
	 * @deprecated use getEnum instead
	 */
	@Deprecated
	public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
		if(enumName == null) return null;
		try {
			return Enum.valueOf(enumClass, enumName);
		} catch(IllegalArgumentException ex) {
			return null;
		}
	}
	
	/**
	 * @deprecated use forValue instead
	 */
	@Deprecated
	public static <E extends Enum<E>> E forValue(Class<E> enumClass, String value) {
		return forValue(value, enumClass);
	}
}
