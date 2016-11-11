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
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle.com
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class DbUtils {
	
	public static void commitQuietly(Connection con) {
		try { if(con != null) con.commit(); } catch(Exception ex) { /* Do nothing... */ }
	}
	
	public static void rollbackQuietly(Connection con) {
		try { if(con != null) con.rollback(); } catch(Exception ex) { /* Do nothing... */ }
	}
	
	public static void closeQuietly(Connection con) {
		try { if(con != null) con.close(); } catch(Exception ex) { /* Do nothing... */ }
	}
	
	/**
	 * Perform basic SQL validation on input string.  This is to allow safe 
	 * encoding of parameters that must contain quotes, while still protecting 
	 * users from SQL injection.
	 * @param str The SQL string
	 * @return Escaped SQL string
	 */
	public static String escapeSQL(String str) {
		if(str != null) {
			// \x00 -> removed
			str = str.replace("\\x00", "");
			// \x1a -> removed
			str = str.replace("\\x1a", "");
			// ' -> ''
			str = str.replace("'", "''");
			// " -> ""
			str = str.replace("\"", "\"\"");
			// \ ->  \\
			str = str.replace("\\", "\\\\");
		}
		return str;
		/*
		fixedConstant = fixedConstant.replaceAll("\\\\x00", "");
42        fixedConstant = fixedConstant.replaceAll("\\\\x1a", "");
43        fixedConstant = fixedConstant.replaceAll("'", "''");
44        fixedConstant = fixedConstant.replaceAll("\\\\", "\\\\\\\\");
45        fixedConstant = fixedConstant.replaceAll("\\\"", "\\\\\"");
		*/
	}
	
	public static Exception proxyException(SQLException ex) {
		String state = ex.getSQLState();
		String class1 = StringUtils.substring(state, 0, 2);
		String class2 = StringUtils.substring(state, 2);
		
		if(class1.equals("23")) {
			return new IntegrityConstraintViolationException(ex);
		} else {
			return ex;
		}
	}
	
	public static boolean existTable(Connection con, String tableName) {
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT * FROM " + tableName + " LIMIT 1";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			return true;
			
		} catch(SQLException ex) {
			return false;
		} finally {
			RSUtils.closeQuietly(rs);
			StatementUtils.closeQuietly(stmt);
		}
	}
	
	/**
	 * Builds temp table name using the source table name and provided suffix.
	 * Eg: buildTempTableName('table1', '1ad4') -> 'table1_tmp1ad4'
	 * 
	 * @param originalTableName Source/Original table name.
	 * @param tempSuffix The suffix for building the temp table name.
	 * @return The temp table name.
	 */
	public static String buildTempTableName(String originalTableName, String tempSuffix) {
		return MessageFormat.format("{0}_tmp{1}", originalTableName, tempSuffix);
	}
	
	/**
	 * Checks if a temp table already exists.
	 * 
	 * @param con The database connection.
	 * @param origTableName Source/Original table name.
	 * @param tempSuffix The suffix for building the temp table name.
	 * @return True if table already exists, false otherwise.
	 */
	public static boolean existTempTable(Connection con, String origTableName, String tempSuffix) {
		return existTable(con, buildTempTableName(origTableName, tempSuffix));
	}
	
	/**
	 * Creates a new temporary table starting from a source/original table.
	 * Temp table name is automatically created using provided suffix.
	 * 
	 * @param con The database connection.
	 * @param originalTableName Source/Original table name.
	 * @param cutomSuffix The suffix appended to original table name.
	 * @return A TempTable object, as results of table creation.
	 * @throws SQLException If a SQL error occurs.
	 */
	public static TempTable createTempTable(Connection con, String originalTableName, String cutomSuffix) throws SQLException {
		Statement stmt = null;
		String sql;
		
		try {
			String name = DbUtils.buildTempTableName(originalTableName, cutomSuffix);
			sql = "CREATE TABLE " + name + " "
				+ "(LIKE " + originalTableName + " INCLUDING ALL)";
			
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			return new TempTable(name, originalTableName, cutomSuffix);
			
		} catch(SQLException ex) {
			throw ex;
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	
	/**
	 * Deletes specified temp table.
	 * 
	 * @param con The database connection.
	 * @param tempTable The TempTable object containing all info.
	 * @throws SQLException If a SQL error occurs.
	 */
	public static void deleteTempTable(Connection con, TempTable tempTable) throws SQLException {
		deleteTempTable(con, tempTable.originaleTableName, tempTable.tableName);
	}
	
	/**
	 * Deletes specified temp table.
	 * 
	 * @param con The database connection.
	 * @param originalTableName Source/Original table name.
	 * @param tempTableName Temporary table name.
	 * @throws SQLException If a SQL error occurs.
	 */
	public static void deleteTempTable(Connection con, String originalTableName, String tempTableName) throws SQLException {
		Statement stmt = null;
		String sql;
		
		try {
			sql = "DROP TABLE " + tempTableName;
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	
	/**
	 * Deletes specified temp table without returning exceptions.
	 * 
	 * @param con The database connection.
	 * @param tempTable The TempTable object containing all info.
	 * @return True for a successful deletion, otherwise false.
	 */
	public static boolean deleteQuietlyTempTable(Connection con, TempTable tempTable) {
		return deleteQuietlyTempTable(con, tempTable.originaleTableName, tempTable.tableName);
	}
	
	/**
	 * Deletes specified temp table without returning exceptions.
	 * 
	 * @param con The database connection.
	 * @param originalTableName Source/Original table name.
	 * @param tempTableName Temporary table name.
	 * @return True for a successful deletion, otherwise false.
	 */
	public static boolean deleteQuietlyTempTable(Connection con, String originalTableName, String tempTableName) {
		try {
			DbUtils.deleteTempTable(con, originalTableName, tempTableName);
			return true;
		} catch(SQLException ex) {
			return false;
		}
	}
	
	/**
	 * Copies back all rows from a temporary table into the original one.
	 * 
	 * @param con The database connection.
	 * @param tempTable The TempTable object containing all info.
	 * @param dropTemp True to drop temporary table after copy operations.
	 * @throws SQLException If a SQL error occurs.
	 */
	public static void copyBackFromTemp(Connection con, TempTable tempTable, boolean dropTemp) throws SQLException {
		copyBackFromTemp(con, tempTable.originaleTableName, tempTable.tableName, dropTemp);
	}
	
	/**
	 * Copies back all rows from a temporary table into the original one.
	 * 
	 * @param con The database connection.
	 * @param originalTableName Source/Original table name.
	 * @param tempTableName Temporary table name.
	 * @param dropTemp True to drop temporary table after copy operations.
	 * @throws SQLException If a SQL error occurs.
	 */
	public static void copyBackFromTemp(Connection con, String originalTableName, String tempTableName, boolean dropTemp) throws SQLException {
		Statement stmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO " + originalTableName + " "
				+ "SELECT * FROM " + tempTableName;
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			if(dropTemp) deleteTempTable(con, originalTableName, tempTableName);
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	
    public static String getSQLString(String content) {
		if(content==null) {
			return null;
		}
		int ix=0;
		while((ix=content.indexOf('\'', ix))>=0) {
			String s1=content.substring(0, ix);
			String s2=content.substring(ix+1);
			content=s1+"''"+s2;
			ix+=2;
		}
		return content;
    }

}
