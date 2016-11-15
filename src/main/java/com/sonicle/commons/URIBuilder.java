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

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class URIBuilder {
	private String scheme;
	private String host;
	private Integer port;
	private String username;
	private String password;
	private String path;
	private String query;
	private String fragment;

	public URIBuilder() {}

	public URIBuilder setScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	public URIBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	public URIBuilder setPort(Integer port) {
		this.port = port;
		return this;
	}

	public URIBuilder setUsername(String username) {
		this.username = username;
		return this;
	}

	public URIBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public URIBuilder setPath(String path) {
		this.path = path;
		return this;
	}

	public URIBuilder setQuery(String query) {
		this.query = query;
		return this;
	}

	public URIBuilder setFragment(String fragment) {
		this.fragment = fragment;
		return this;
	}

	public URI build() throws URISyntaxException {
		int iport = (port == null) ? -1 : port;
		String userInfo = null;
		if (!StringUtils.isBlank(username)) {
			userInfo = username;
			if (!StringUtils.isBlank(password)) {
				userInfo += ":";
				userInfo += password;
			}
		}
		return new URI(scheme, userInfo, host, iport, path, query, fragment);
	}
}
