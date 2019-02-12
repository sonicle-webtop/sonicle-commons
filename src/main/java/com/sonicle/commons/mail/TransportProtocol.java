/*
 * Copyright (C) 2019 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2019 Sonicle S.r.l.".
 */
package com.sonicle.commons.mail;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public enum TransportProtocol {
	SMTP {
		@Override
		public String getProtocol() {
			return "smtp";
		}
		
		@Override
		public String getPropertyName(String remaining) {
			return "mail." + getProtocol() + "." + remaining;
		}
		
		@Override
		public void applyProperties(Properties props) {
			props.setProperty("mail.transport.protocol", getProtocol());
			props.setProperty(getPropertyName("starttls.enable"), "true");
			props.setProperty(getPropertyName("starttls.required"), "false");
			props.setProperty(getPropertyName("ssl.trust"), "*");
			props.setProperty(getPropertyName("ssl.checkserveridentity"), "false");
		}
	},
	
	SMTPS {
		@Override
		public String getProtocol() {
			return "smtps";
		}
		
		@Override
		public String getPropertyName(String remaining) {
			return "mail." + getProtocol() + "." + remaining;
		}
		
		@Override
		public void applyProperties(Properties props) {
			props.setProperty("mail.transport.protocol", getProtocol());
			props.setProperty(getPropertyName("ssl.checkserveridentity"), "false");
			//props.setProperty(getPropertyName("ssl.quitwait"), "false");
		}
	},
	
	SMTP_TLS {
		@Override
		public String getProtocol() {
			return "smtp";
		}
		
		@Override
		public String getPropertyName(String remaining) {
			return "mail." + getProtocol() + "." + remaining;
		}
		
		@Override
		public void applyProperties(Properties props) {
			props.setProperty("mail.transport.protocol", getProtocol());
			props.setProperty(getPropertyName("starttls.enable"), "true");
			props.setProperty(getPropertyName("starttls.required"), "true");
			props.setProperty(getPropertyName("ssl.checkserveridentity"), "true");
		}
	};
	
	public abstract String getProtocol();
	public abstract String getPropertyName(String remaining);
	public abstract void applyProperties(Properties props);
	
	public static TransportProtocol parse(String proto, Boolean ssl, Boolean startTls) {
		if (StringUtils.isBlank(proto)) return null;
		if (!StringUtils.containsIgnoreCase(proto, "smtp")) return null;
		if (Boolean.TRUE.equals(ssl)) {
			return TransportProtocol.SMTPS;
		} else {
			if (Boolean.TRUE.equals(startTls)) {
				return TransportProtocol.SMTP_TLS;
			} else {
				return TransportProtocol.SMTP;
			}
		}
	}
}
