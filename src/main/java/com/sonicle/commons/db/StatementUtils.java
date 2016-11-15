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
package com.sonicle.commons.db;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author malbinola
 */
public class StatementUtils {
	
	public static void closeQuietly(Statement stmt) {
		try { if(stmt != null) stmt.close(); } catch(Exception ex) { /* Do nothing... */ }
	}
	
	/**
	 * Sets the designated parameter to the given Java String value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index to use.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setString(PreparedStatement stmt, int parameterIndex, String value) throws SQLException {
		return setString(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java String value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setString(PreparedStatement stmt, int parameterIndex, String value, String defaultValue) throws SQLException {
		if(value != null) {
			stmt.setString(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setString(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.VARCHAR);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets a list of String values starting from the designated parameter.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param values The list of values.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setStrings(PreparedStatement stmt, int parameterIndex, ArrayList<String> values) throws SQLException {
		int index = parameterIndex;
		for(String value: values) {
			index = setString(stmt, index, value);
		}
		return index;
	}
	
	/**
	 * Sets the designated parameter to the given Java Boolean value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setBoolean(PreparedStatement stmt, int parameterIndex, Boolean value) throws SQLException {
		return setBoolean(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Boolean value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setBoolean(PreparedStatement stmt, int parameterIndex, Boolean value, Boolean defaultValue) throws SQLException {
		if(value != null) {
			stmt.setBoolean(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setBoolean(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.BOOLEAN);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Integer value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setInt(PreparedStatement stmt, int parameterIndex, Integer value) throws SQLException {
		return setInt(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Integer value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setInt(PreparedStatement stmt, int parameterIndex, Integer value, Integer defaultValue) throws SQLException {
		if(value != null) {
			stmt.setInt(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setInt(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.INTEGER);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets a list of Integer values starting from the designated parameter.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param values The list of values.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setInts(PreparedStatement stmt, int parameterIndex, List<Integer> values) throws SQLException {
		int index = parameterIndex;
		for(Integer value: values) {
			index = setInt(stmt, index, value);
		}
		return index;
	}
	
	/**
	 * Sets the designated parameter to the given Java Long value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setLong(PreparedStatement stmt, int parameterIndex, Long value) throws SQLException {
		return setLong(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Long value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setLong(PreparedStatement stmt, int parameterIndex, Long value, Long defaultValue) throws SQLException {
		if(value != null) {
			stmt.setLong(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setLong(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.BIGINT);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Float value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setFloat(PreparedStatement stmt, int parameterIndex, Float value) throws SQLException {
		return setFloat(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Float value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setFloat(PreparedStatement stmt, int parameterIndex, Float value, Float defaultValue) throws SQLException {
		if(value != null) {
			stmt.setFloat(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setFloat(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.FLOAT);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Double value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setDouble(PreparedStatement stmt, int parameterIndex, Double value) throws SQLException {
		return setDouble(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Double value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setDouble(PreparedStatement stmt, int parameterIndex, Double value, Double defaultValue) throws SQLException {
		if(value != null) {
			stmt.setDouble(parameterIndex, value);
		} else {
			if(defaultValue != null) {
				stmt.setDouble(parameterIndex, defaultValue);
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.DOUBLE);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Date value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setDate(PreparedStatement stmt, int parameterIndex, Date value) throws SQLException {
		return setDate(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Date value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setDate(PreparedStatement stmt, int parameterIndex, Date value, Date defaultValue) throws SQLException {
		if(value != null) {
			stmt.setDate(parameterIndex, new java.sql.Date(value.getTime()));
		} else {
			if(defaultValue != null) {
				stmt.setDate(parameterIndex, new java.sql.Date(defaultValue.getTime()));
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.DATE);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Timestamp value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setTimestamp(PreparedStatement stmt, int parameterIndex, Date value) throws SQLException {
		return setTimestamp(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Timestamp value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setTimestamp(PreparedStatement stmt, int parameterIndex, Date value, Date defaultValue) throws SQLException {
		if(value != null) {
			stmt.setTimestamp(parameterIndex, new java.sql.Timestamp(value.getTime()));
		} else {
			if(defaultValue != null) {
				stmt.setTimestamp(parameterIndex, new java.sql.Timestamp(defaultValue.getTime()));
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.TIMESTAMP);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java Time value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setTime(PreparedStatement stmt, int parameterIndex, Date value) throws SQLException {
		return setTime(stmt, parameterIndex, value, null);
	}
	
	/**
	 * Sets the designated parameter to the given Java Time value.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param value The parameter value.
	 * @param defaultValue A default value to use in case of null parameter.
	 * @return Next column index.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setTime(PreparedStatement stmt, int parameterIndex, Date value, Date defaultValue) throws SQLException {
		if(value != null) {
			stmt.setTime(parameterIndex, new java.sql.Time(value.getTime()));
		} else {
			if(defaultValue != null) {
				stmt.setTime(parameterIndex, new java.sql.Time(defaultValue.getTime()));
			} else {
				stmt.setNull(parameterIndex, java.sql.Types.TIME);
			}
		}
		return parameterIndex+1;
	}
	
	/**
	 * Sets the designated parameter to the given Java ByteArrayInputStream.
	 * 
	 * @param stmt The statement object.
	 * @param parameterIndex The first parameter is 1, the second is 2, ...
	 * @param is The stream.
	 * @param length The length of the stream.
	 * @return Next column index to use.
	 * @throws SQLException Rethrow of a inner exception.
	 */
	public static int setBinaryStream(PreparedStatement stmt, int parameterIndex, InputStream is, long length) throws SQLException {
		stmt.setBinaryStream(parameterIndex, is, length);
		return parameterIndex+1;
	}
	
	public static int getIntQueryValue(PreparedStatement stmt) throws SQLException {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (rs != null) try { rs.close(); } catch(SQLException ex2) {}
		}
	}
	
	/**
	 * Generates a string of value placeholders (? symbol) to use into a prepared 
	 * statement sql query. Each symbol will be separated by a comma.
	 * 
	 * @param howMany The number of ? to generate.
	 * @return The builded string.
	 */
	public static String buildInClausePlaceholder(int howMany) {
		StringBuilder sb = new StringBuilder();
		if(howMany < 1) return null;
		for(int i=0; i<howMany; i++) {
			sb.append("?,");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	public static String joinInClauseValues(List<?> values, String valueSeparator) {
		StringBuilder sb = new StringBuilder();
		if(values.isEmpty()) return "";
		for(int i=0; i<values.size(); i++) {
			sb.append(valueSeparator);
			sb.append(String.valueOf(values.get(i)));
			sb.append(valueSeparator);
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length() -1).toString();
	}
	
	public static String buildPlaceholderList(List<?> values) {
		return buildPlaceholderList(values.size());
	}
	
	/**
	 * Builds a sequence of ? (question-mark) placeholders used in sql statements.
	 * Each ? (question-mark) is followed by a comma, exept the last one.
	 * 
	 * @param howMany Numer of laceholders to insert
	 * @return A string of placeholders
	 */
	public static String buildPlaceholderList(int howMany) {
		StringBuilder sb = new StringBuilder();
		for(int i=1; i<=howMany; i++) {
			sb.append("?");
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length() -1).toString();
	}
}