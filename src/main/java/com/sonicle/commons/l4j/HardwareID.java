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
package com.sonicle.commons.l4j;

import com.license4j.util.Hex;
import com.sonicle.commons.OS;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class HardwareID {
	private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(HardwareID.class);
	
	public static String getHardwareIDFromHostName() {
		try {
			return "1" + UUID.nameUUIDFromBytes(InetAddress.getLocalHost().getHostName().getBytes(StandardCharsets.UTF_8)).toString();
			
		} catch (UnknownHostException ex) {
			LOGGER.error("MSG101", ex);
		} catch (Exception ex) {
			LOGGER.error("MSG147", ex);
		}
		 return null;
	}
	
	public static String getHardwareIDFromEthernetAddress() {
		return getHardwareIDFromEthernetAddress(false);
	}
	
	public static String getHardwareIDFromEthernetAddress(boolean useFirstOnly) {
		try {
			String s = null;
			if (OS.isWindows()) {
				final NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
				if (!byInetAddress.isLoopback() && !byInetAddress.isVirtual() && !byInetAddress.isPointToPoint() && byInetAddress.getHardwareAddress() != null) {
					s = Hex.encodeHexString(byInetAddress.getHardwareAddress());
				}
				if (s == null) s = generateStringFromPhysicalNetInterfaceMACs(useFirstOnly ? 1 : -1);
			} else {
				s = generateStringFromPhysicalNetInterfaceMACs(useFirstOnly ? 1 : -1);
			}
			if (s != null && s.length() > 11) {
				return "2" + UUID.nameUUIDFromBytes(s.getBytes(StandardCharsets.UTF_8)).toString();
			}
			 
		} catch (SocketException ex) {
			LOGGER.error("MSG103", ex);
		} catch (UnknownHostException ex) {
			LOGGER.error("MSG104", ex);
		} catch (Exception ex) {
			LOGGER.error("MSG146", ex);
		}
		return null;
	}
	
	private static String generateStringFromPhysicalNetInterfaceMACs(int limit) throws SocketException {
		final ArrayList<Comparable> list = new ArrayList<>();
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Enumerating interfaces...");
		final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			if (limit != -1 && list.size() >= limit) break;
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			if (LOGGER.isDebugEnabled()) LOGGER.debug("Evaluating interface '{}'", networkInterface.getName());
			if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && !networkInterface.isPointToPoint() && networkInterface.getHardwareAddress() != null) {
				if (OS.isLinux()) {
					final Boolean virtual = isLinuxVirtualInterface(networkInterface.getName());
					if (virtual != null && virtual == true) continue;
				}
				if (LOGGER.isDebugEnabled()) LOGGER.debug("Using '{}' from interface '{}'", networkInterface.getHardwareAddress(), networkInterface.getName());
				list.add(Hex.encodeHexString(networkInterface.getHardwareAddress()));
			}
		}
		if (list.size() > 1) Collections.sort(list);
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Building joined string...");
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); ++i) {
			sb.append(list.get(i));
		}
		return sb.length() > 0 ? sb.toString() : null;
	}
	
	private static Boolean isLinuxVirtualInterface(String name) {
		Path file = Paths.get("/sys/class/net/" + name);
		if (Files.exists(file) && Files.isSymbolicLink(file)) {
			if (LOGGER.isDebugEnabled()) LOGGER.debug("Evaluating target for symlink '{}'", file.toString());
			try {
				return Files.readSymbolicLink(file).toString().contains("virtual/net");
			} catch(IOException ex) {
				LOGGER.error("Unable to read symlink target for '{}'", file.toString(), ex);
			}
		} else {
			if (LOGGER.isDebugEnabled()) LOGGER.debug("File '{}' does not exist or is not a symlink", file.toString());
		}
		return null;
	}
}
