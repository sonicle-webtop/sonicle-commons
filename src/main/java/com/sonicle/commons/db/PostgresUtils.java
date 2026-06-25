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

import com.sonicle.commons.Check;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.postgresql.largeobject.LargeObject;
//import org.postgresql.largeobject.LargeObjectManager;

/**
 *
 * @author malbinola
 */
public class PostgresUtils {
	private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(PostgresUtils.class);
	private static final Pattern GUC_SAFE_NAME = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");
	
	/**
	 * Escapes a value for use inside a single-quoted PostgreSQL string literal,
	 * assuming standard_conforming_strings = on (the default since PostgreSQL 9.1).
	 * Under this setting, backslash has no special meaning inside '...' literals,
	 * so it must NOT be doubled, and double quotes have no special meaning either.
	 * The only character that needs escaping is the single quote itself.
	 * @param s
	 * @return 
	 */
	public static String escapeSQLLiteral(String s) {
		if (s == null) return null;
		// PostgreSQL text values cannot contain an actual NUL byte,
		// so strip it outright rather than trying to escape it
		s = s.replace("\u0000", "");
		s = s.replace("'", "''");
		return s;
	}
	
	/**
	 * Sets a PostgreSQL GUC (Grand Unified Configuration) custom parameter on the given connection.
	 * The parameter name is built as {@code prefix + "." + name} and validated against a strict
	 * whitelist, since PostgreSQL's {@code SET} statement does not support JDBC bind parameters
	 * for the parameter name. The value, on the other hand, is always passed as a quoted string
	 * literal and properly escaped.
	 * @param con The connection on which the parameter is set.
	 * @param transactionScope If {@code true}, issues {@code SET LOCAL} (scoped to the current 
	 * transaction; requires autocommit to be disabled to have any effect);
	 * if {@code false}, issues a plain {@code SET} (scoped to the session).
	 * @param prefix The GUC namespace prefix (e.g. "myapp").
	 * @param name The value to assign to the parameter.
	 * @param value The value to assign to the parameter.
	 * @return {@code true} if the parameter was set successfully, {@code false} otherwise (invalid parameter name or SQL execution failure)
	 */
	public static boolean setGUCParameter(final Connection con, final boolean transactionScope, final String prefix, final String name, final String value) {
		Check.notNull(con, "con");
		Check.notEmpty(prefix, "prefix");
		Check.notEmpty(name, "name");
		
		String setting = prefix + "." + name;
		
		// The parameter name cannot be bound as a JDBC parameter in a SET statement,
		// so it must be validated against a strict whitelist instead
		if (!GUC_SAFE_NAME.matcher(setting).matches()) {
			LOGGER.error("Rejected unsafe GUC parameter name: {}", setting);
			return false;
		}
		
		String sql = "SET";
		if (transactionScope) sql += " LOCAL";
		sql += " " + setting + " = '" + escapeSQLLiteral(value) + "'";
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(sql);
			return true;
			
		} catch (SQLException ex) {
			LOGGER.error("Failed to set GUC parameter '{}' to '{}'", setting, value, ex);
			return false;
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	
	/**
	 * Resets a PostgreSQL GUC (Grand Unified Configuration) custom parameter to its default
	 * value on the given connection, equivalent to {@code SET <parameter> = DEFAULT}.
	 * For custom (non-built-in) parameters such as {@code prefix.name}, this effectively
	 * clears the value, so subsequent calls to {@code current_setting(..., true)} on this
	 * session will return {@code NULL} again.
	 * @param con The connection on which the parameter is set.
	 * @param prefix The GUC namespace prefix (e.g. "myapp").
	 * @param name The value to assign to the parameter.
	 * @return {@code true} if the parameter was reset successfully, {@code false} otherwise (invalid parameter name or SQL execution failure)
	 */
	public static boolean resetGUCParameter(final Connection con, final String prefix, final String name) {
		Check.notNull(con, "con");
		Check.notEmpty(prefix, "prefix");
		Check.notEmpty(name, "name");
		
		String setting = prefix + "." + name;
		
		// The parameter name cannot be bound as a JDBC parameter in a SET statement,
		// so it must be validated against a strict whitelist instead
		if (!GUC_SAFE_NAME.matcher(setting).matches()) {
			LOGGER.error("Rejected unsafe GUC parameter name: {}", setting);
			return false;
		}
		
		String sql = "RESET " + setting;
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(sql);
			return true;
			
		} catch (SQLException ex) {
			LOGGER.error("Failed to reset GUC parameter '{}'", setting, ex);
			return false;
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	
	/*
	public static byte[] readLargeObject(Connection con, long oid) throws SQLException {
		LargeObjectManager lom = (con.unwrap(org.postgresql.PGConnection.class)).getLargeObjectAPI();
		LargeObject lo = lom.open(oid, LargeObjectManager.READ);
		
		byte bytes[] = new byte[lo.size()];
		lo.read(bytes, 0, lo.size());
		lo.close();
		return bytes;
	}
	*/
}
