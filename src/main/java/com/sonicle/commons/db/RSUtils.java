/*
 * Sonicle Commons is a helper library developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
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

package com.sonicle.commons.db;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author malbinola
 */
public class RSUtils {
	
	public static void closeQuietly(ResultSet rs) {
		try { if(rs != null) rs.close(); } catch(Exception ex) { /* Do nothing... */ }
	}
	
	public static Boolean getBoolean(ResultSet rs, int columnIndex) throws SQLException {
		Boolean value = rs.getBoolean(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static Boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
		Boolean value = rs.getBoolean(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static String getString(ResultSet rs, int columnIndex) throws SQLException {
		String value = rs.getString(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static String getString(ResultSet rs, String columnLabel) throws SQLException {
		String value = rs.getString(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
		int value = rs.getInt(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static Long getLong(ResultSet rs, int columnIndex) throws SQLException {
		long value = rs.getLong(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
		long value = rs.getLong(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static Float getFloat(ResultSet rs, int columnIndex) throws SQLException {
		float value = rs.getFloat(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static Float getFloat(ResultSet rs, String columnLabel) throws SQLException {
		float value = rs.getFloat(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static Date getTimestamp(ResultSet rs, int columnIndex) throws SQLException {
		Date value = rs.getTimestamp(columnIndex);
		return rs.wasNull() ? null : value;
	}
	
	public static Date getTimestamp(ResultSet rs, String columnLabel) throws SQLException {
		Date value = rs.getTimestamp(columnLabel);
		return rs.wasNull() ? null : value;
	}
	
	public static String toStringBean(ResultSet rs) throws SQLException {
		try {
			return (rs.next()) ? RSUtils.getString(rs, 1) : null;
		} catch(SQLException ex) {
			throw ex;
		} finally {
			if(rs != null) try { rs.close(); } catch(SQLException ex2) {}
		}
	}
	
	public static ArrayList<String> toStringBeanList(ResultSet rs) throws SQLException {
		ArrayList<String> items = new ArrayList<String>();
		try {
			while(rs.next()) items.add(RSUtils.getString(rs, 1));
			return items;
		} catch(SQLException ex) {
			throw ex;
		} finally {
			if(rs != null) try { rs.close(); } catch(SQLException ex2) {}
		}
	}
	
	public static Integer toIntegerBean(ResultSet rs) throws SQLException {
		try {
			return (rs.next()) ? RSUtils.getInteger(rs, 1) : null;
		} catch(SQLException ex) {
			throw ex;
		} finally {
			if(rs != null) try { rs.close(); } catch(SQLException ex2) {}
		}
	}
	
	public static <T> T toBean(ResultSet rs, Class<T> type) throws SQLException, Exception {
		ArrayList<T> items = RSUtils.toBeanList(rs, type);
		return (items.size() == 1) ? items.get(0) : null;
	}
	
	public static <T> ArrayList<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException, Exception {
		return toBeanList(rs, type, true);
	}
	
	public static <T> ArrayList<T> toBeanList(ResultSet rs, Class<T> type, boolean close) throws SQLException, Exception {
		ArrayList<T> items = new ArrayList<T>();
		
		try {
			Constructor<T> constructor = type.getConstructor(ResultSet.class);
			while(rs.next()) items.add((T) constructor.newInstance(rs));
			return items;
		} catch(SQLException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new BeanException(ex);
		} finally {
			if(close & (rs != null)) try { rs.close(); } catch(SQLException ex2) {}
		}
	}
}