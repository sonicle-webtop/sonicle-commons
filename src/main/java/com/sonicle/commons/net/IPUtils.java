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
package com.sonicle.commons.net;

import com.sonicle.commons.RegexUtils;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class IPUtils {
	final static Logger logger = (Logger) LoggerFactory.getLogger(IPUtils.class);
	public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	public static final Pattern patternIPv4 = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
	private static final Pattern NETMASK4_PATTERN = RegexUtils.match(RegexUtils.MATCH_NETMASK4, true, true);
	
	//private static final String ADDRESS4 = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
	//private static final String CIDR4 = ADDRESS4 + "/(\\d{1,3})";
	//private static final Pattern ADDRESS4_PATTERN = Pattern.compile(ADDRESS4);
	//private static final Pattern CIDR4_PATTERN = Pattern.compile(CIDR4);
	
	/**
	 * Parses a string into an IPAddress object.
	 * @param ip The address String to parse.
	 * @return The IPAddress object or null if string is not parsable.
	 */
	public static IPAddress toIPAddress(final String ip) {
		return new IPAddressString(ip).getAddress();
	}
	
	/**
	 * Checks if passed IPAddress is a public address or not.
	 * @param ip The address to check.
	 * @return `true` if address is public, `false` otherwise.
	 */
	public static boolean isPublicAddress(final IPAddress ip) {
		return ip == null ? false : !ip.isLocal() && !ip.isLoopback();
	}
	
	/**
	 * Checks if passed IPAddress is in range of specified CIDRs.
	 * @param ip The address to check.
	 * @param cidrs The CIDRs to evaluate address against.
	 * @return `true` if address is contained in one of CIDRs, `false` otherwise.
	 */
	public static boolean isAddressInRange(final IPAddress ip, final String[] cidrs) {
		Check.notNull(cidrs, "cidrs");
		if (ip == null) return false;
		
		for (String scidr : cidrs) {
			try {
				IPAddress cidr = new IPAddressString(scidr).toAddress();
				if (cidr.contains(ip)) return true;
			} catch(AddressStringException ex) {
				logger.error("String '{}' is not a valid CIDR address", scidr, ex);
			}
		}
		return false;
	}
	
	/**
	 * Checks if passed IPAddress is in range of specified CIDRs.
	 * @param ip The address to check.
	 * @param cidrs The CIDRs to evaluate address against.
	 * @return `true` if address is contained in one of CIDRs, `false` otherwise.
	 */
	public static boolean isAddressInRange(final IPAddress ip, final IPAddress[] cidrs) {
		Check.notNull(cidrs, "cidrs");
		if (ip == null) return false;
		
		for (IPAddress cidr : cidrs) {
			if (cidr.contains(ip)) return true;
		}
		return false;
	}
	
	/**
	 * Converts a netmask (IPv4), in dotted notation format, into number of bits.
	 * @param netmask Netmask in dotted notation.
	 * @return The corresponding number of bits.
	 */
	public static Integer toIntNetmask4(String netmask) {
		if (netmask == null) return null;
		if (NETMASK4_PATTERN.matcher(netmask).matches()) {
			return packDottedString4(netmask);
		} else {
			throw new IllegalArgumentException("Could not parse [" + netmask + "]");
		}
	}
	
	/**
	 * Converts a netmask (IPv4), in number of bits format, into dotted notation.
	 * Example:
	 *	- 24 -> 255.255.255.0
	 *	-  0 -> 0.0.0.0
	 * @param netmask Netmask in number of bits.
	 * @return The corresponding dotted notation.
	 */
	public static String toStringNetmask4(Integer netmask) {
		if (netmask == null) return null;
		if (netmask == 0) {
			return "0.0.0.0";
		} else {
			long bits = 0xffffffff ^ (1 << 32 - netmask) - 1;
			return String.format("%d.%d.%d.%d", (bits & 0x0000000000ff000000L) >> 24, (bits & 0x0000000000ff0000) >> 16, (bits & 0x0000000000ff00) >> 8, bits & 0xff);
		}
	}
	
	public static boolean isIPInRange(String[] cidrs, String clientIP) throws UnknownHostException {
		InetAddress ia = InetAddress.getByName(clientIP);	
		if(ia instanceof Inet6Address) { // We are not able to validate an IPv6, so skip check!
			logger.warn("Request address ({}) seems an IPv6; check skipped!", clientIP);
			return true;
		} else { // old-style IPv4
			SubnetUtils.SubnetInfo subnet = null;
			String cidr = null;
			for(int i=0; i<cidrs.length; i++) {
				cidr = cidrs[i];
				// Checks for a CIDR that maches all
				if(cidr.equals("0.0.0.0/0") || cidr.equals("::/0")) {
					logger.trace("Special CIDR found! All clients are allowed!");
					return true;
				}
				logger.debug("Checking {} against CIDR {}", clientIP, cidr);
				subnet = (new SubnetUtils(cidr)).getInfo();
				if(subnet.isInRange(clientIP)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Deprecated
	public static boolean isIPv4Private(String ip) {
		long longIp = ipV4ToLong(ip);
		return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
				|| (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
				|| longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
	}

	@Deprecated
	public static boolean isIPv4Valid(String address) {
		return patternIPv4.matcher(address).matches();
	}
	
	@Deprecated
	public static String longToIpV4(long longIp) {
		int octet3 = (int) ((longIp >> 24) % 256);
		int octet2 = (int) ((longIp >> 16) % 256);
		int octet1 = (int) ((longIp >> 8) % 256);
		int octet0 = (int) ((longIp) % 256);
		return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
	}
	
	@Deprecated
	public static long ipV4ToLong(String ip) {
		String[] octets = ip.split("\\.");
		return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
				+ (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
	}
	
	private static int packDottedString4(String dottedString) {
		String tokens[] = StringUtils.split(dottedString, ".", 4);
		if (tokens.length != 4) throw new IllegalArgumentException("Invalid dotted notation [" + dottedString + "]");
		int addr = 0;
		for (int i = 1; i <= 4; ++i) {
			int n = Integer.parseInt(tokens[i]);
			if (n < 0 && n > 255) throw new IllegalArgumentException("Value out of range: [" + n + "]");
			addr |= ((n & 0xff) << 8*(4-i));
		}
		return addr;
	}
}