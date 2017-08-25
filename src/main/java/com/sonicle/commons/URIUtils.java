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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author malbinola
 */
public class URIUtils {
	
	/**
	 * Constructs a URI by parsing the given string. This method is null-safe.
	 * @param s The string to be parsed into a URI
	 * @return The URI
	 * @throws URISyntaxException If the given string violates RFC&nbsp;2396
	 */
	public static URI createURI(String s) throws URISyntaxException {
		return new URI(s);
	}
	
	/**
	 * Constructs a URI by parsing the given string. This method is null-safe.
	 * @param s The string to be parsed into a URI
	 * @return The URI
	 */
	public static URI createURIQuietly(String s) {
		try {
			return createURI(s);
		} catch(URISyntaxException ex) {
			return null;
		}
	}
	
	public static String encodeQuietly(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			return s;
		}
	}
	
	public static Integer getPort(String uri) throws URISyntaxException {
		return getPort(new URI(uri));
	}
	
	public static Integer getPort(URI uri) {
		int port = uri.getPort();
		return port == -1 ? null : port;
	}
	
	public static String getScheme(String uri) throws URISyntaxException {
		return new URI(uri).getScheme();
	}
	
	public static String[] getUserInfo(URI uri) {
		String[] tokens = StringUtils.split(uri.getUserInfo(), ":", 2);
		if(tokens == null || tokens.length == 0) return null;
		return (tokens.length == 1) ? new String[]{tokens[0], null} : tokens;
	}
	
	public static URI appendPath(URI uri, String path) throws URISyntaxException {
		URIBuilder ub = new URIBuilder(uri);
		appendPath(ub, path);
		return ub.build();
	}
	
	public static void appendPath(URIBuilder builder, String path) {
		if (!StringUtils.isBlank(path)) {
			final String p1 = StringUtils.defaultString(StringUtils.removeEnd(builder.getPath(), "/"));
			final String p2 = StringUtils.defaultString(StringUtils.removeStart(path, "/"));
			builder.setPath(p1 + "/" + p2);
		}
	}
	
	public static String asUserInfo(String username, String password) {
		String userInfo = null;
		if (!StringUtils.isBlank(username)) {
			userInfo = username;
			if (!StringUtils.isBlank(password)) {
				userInfo += ":";
				userInfo += password;
			}
		}
		return userInfo;
	}
	
	public static String[] toURIStrings(URI[] uris, boolean ascii) {
		String[] ss = new String[uris.length];
		for(int i=0; i<uris.length; i++) {
			ss[i] = (ascii) ? toASCIIString(uris[i]) : toString(uris[i]);
		}
		return ss;
	}
	
	public static String toASCIIString(URI uri) {
		return (uri == null) ? null : uri.toASCIIString();
	}
	
	public static String toString(URI uri) {
		return (uri == null) ? null : uri.toString();
	}
}
