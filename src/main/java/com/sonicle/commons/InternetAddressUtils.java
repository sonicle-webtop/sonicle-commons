/*
 * Copyright (C) 2017 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2017 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class InternetAddressUtils {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(RegexUtils.MATCH_EMAIL_ADDRESS);
	
	public static boolean isAddressValid(String address) {
		if(StringUtils.isBlank(address)) return false;
		return EMAIL_PATTERN.matcher(address).matches();
	}
	
	public static boolean isAddressValid(InternetAddress ia) {
		try {
			if(ia == null) return false;
			ia.validate();
			return true;
		} catch(AddressException ex) {
			return false;
		}
	}
	
	/**
	 * Builds a suitable personal string simply joining first and last name.
	 * @param firstName First name.
	 * @param lastName Last name.
	 * @return Personal string
	 */
	public static String toPersonal(String firstName, String lastName) {
		return LangUtils.joinStrings(" ", firstName, lastName);
	}
	
	/**
	 * Returns a full address string in RFC822 style.
	 * Address and personal will be used as is without doing any validity
	 * check and applying any characters encoding.
	 * @param address The address
	 * @param personal The personal name
	 * @return Unicode address string
	 */
	public static String toFullAddress(String address, String personal) {
		if (StringUtils.isBlank(address)) return null;
		if (StringUtils.isBlank(personal)) {
			return address;
		} else {
			return personal + " <" + address + ">";
		}
	}
	
	/**
	 * Converts specified address into a full address string in RFC822 style.
	 * Personal is not outputted in quoted-style format.
	 * @param address The source address.
	 * @return Unicode address string
	 */
	public static String toFullAddress(InternetAddress address) {
		if (address == null) return null;
		return toFullAddress(address.getAddress(), address.getPersonal());
	}
	
	public static InternetAddress toInternetAddress(String local, String domain, String personal) {
		return InternetAddressUtils.toInternetAddress(local + "@" + domain, personal);
	}
	
	public static InternetAddress toInternetAddress(String address, String personal) {
		try {
			if (StringUtils.isBlank(address)) return null;
			InternetAddress ia = new InternetAddress(address);
			if (!StringUtils.isBlank(personal)) ia.setPersonal(personal, StandardCharsets.UTF_8.name());
			return ia;
		} catch(AddressException | UnsupportedEncodingException ex) {
			return null;
		}
	}
	
	public static InternetAddress toInternetAddress(String fullAddress) {
		if (StringUtils.isBlank(fullAddress)) return null;
		try {
			String address = null;
			String personal = null;
			int ix = fullAddress.lastIndexOf('<');
			if (ix>=0) {
				int ix2 = fullAddress.lastIndexOf('>');
				if (ix2>0 && ix2>ix) {
					personal = fullAddress.substring(0,ix).trim();
					address = fullAddress.substring(ix+1, ix2);
					return InternetAddressUtils.toInternetAddress(address, personal);
				}
			}
			return new InternetAddress(fullAddress);
		} catch(AddressException ex) {
			return null;
		}
	}
}
