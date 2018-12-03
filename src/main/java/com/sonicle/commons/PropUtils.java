/*
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import java.util.Properties;

/**
 *
 * @author malbinola
 */
public class PropUtils {
	
	public static boolean isDefined(Properties props, String key) {
		return props.containsKey(key);
	}
	
	public static boolean copy(Properties sourceProps, String sourceKey, Properties targetProps, String targetKey) {
		if (isDefined(sourceProps, sourceKey)) {
			targetProps.setProperty(targetKey, sourceProps.getProperty(sourceKey));
			return true;
		} else {
			return false;
		}
	}
	
	public static String getStringProperty(Properties props, String name, String defaultValue) {
		return getString(getProp(props, name), defaultValue);
	}
	
	public static int getIntProperty(Properties props, String name, int defaultValue) {
		return getInt(getProp(props, name), defaultValue);
	}
	
	public static boolean getBooleanProperty(Properties props, String name, boolean defaultValue) {
		return getBoolean(getProp(props, name), defaultValue);
	}
	
	private static String getString(Object value, String defaultValue) {
		if (value == null) return defaultValue;
		if (value instanceof String) return LangUtils.value((String)value, defaultValue);
		return defaultValue;
	}
	
	private static boolean getBoolean(Object value, boolean defaultValue) {
		if (value == null) return defaultValue;
		if (value instanceof String) return LangUtils.value((String)value, defaultValue);
		if (value instanceof Boolean) return (Boolean)value;
		return defaultValue;
	}
	
	private static int getInt(Object value, int defaultValue) {
		if (value == null) return defaultValue;
		if (value instanceof String) return LangUtils.value((String)value, defaultValue);
		if (value instanceof Integer) return (Integer)value;
		return defaultValue;
	}
	
	private static Object getProp(Properties props, String name) {
		Object val = props.get(name);
		return (val != null) ? val : props.getProperty(name);
	}
}
