/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.commons.mail;

/**
 *
 * @author malbinola
 */
public class StoreHostDefinition {
	private String host;
	private int port;
	private StoreProtocol protocol;
	private String username;
	private String password;
	private boolean trustHost = false;
	
	public StoreHostDefinition() {}
	
	public StoreHostDefinition(String host, int port, StoreProtocol protocol, String username, String password) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public StoreProtocol getProtocol() {
		return protocol;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getTrustHost() {
		return trustHost;
	}
	
	public StoreHostDefinition withHost(String host) {
		this.host = host;
		return this;
	}
	
	public StoreHostDefinition withPort(int port) {
		this.port = port;
		return this;
	}
	
	public StoreHostDefinition withProtocol(StoreProtocol protocol) {
		this.protocol = protocol;
		return this;
	}
	
	public StoreHostDefinition withUsername(String username) {
		this.username = username;
		return this;
	}
	
	public StoreHostDefinition withPassword(String password) {
		this.password = password;
		return this;
	}
	
	public StoreHostDefinition withTrustHost(boolean trustHost) {
		this.trustHost = trustHost;
		return this;
	}
}
