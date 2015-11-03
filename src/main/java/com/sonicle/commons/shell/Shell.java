/*
 * WebTop Services is a Web Application framework developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
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
package com.sonicle.commons.shell;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class Shell {
	private final ParsedUri parsedUri;
	private JSch jsch = null;
	private Session session = null;
	
	public Shell(URI uri) {
		this.parsedUri = new ParsedUri(uri);
		if(parsedUri.scheme.equals(ParsedUri.SCHEME_SSH)) {
			this.jsch = new JSch();
			JSch.setConfig("StrictHostKeyChecking", "no");
		}	
	}
	
	private synchronized ChannelExec createExecutor() throws Exception {
		if(parsedUri.scheme.equals(ParsedUri.SCHEME_SSH)) {
			ensureSession();
			return (ChannelExec)session.openChannel("exec");
		} else {
			return null;
		}
	}
	
	public ShellChannel createShellChannel(String cmd) throws Exception {
		if(parsedUri.scheme.equals(ParsedUri.SCHEME_SSH)) {
			ChannelExec exec = createExecutor();
			ExecShellChannel channel = new ExecShellChannel(exec);
			exec.setCommand(cmd);
			exec.connect();
			return channel;
		} else {
			Process process = Runtime.getRuntime().exec(cmd);
			return new ProcessShellChannel(process);
		}
	}
	
	public synchronized List<String> execute(String cmd) throws Exception {
		ShellChannel channel = createShellChannel(cmd);
		ArrayList<String> lines = new ArrayList<>();
		
		// Reads standard output
		String line = null;
		while ((line = channel.getOut().readLine()) != null) {
			lines.add(line);
		}
		// Reads standard error
		line = null;
		while ((line = channel.getErr().readLine()) != null) {
			//TODO logger
		}
		
		channel.close();
		return lines;
	}
	
	public void close() {
		if(parsedUri.scheme.equals(ParsedUri.SCHEME_SSH)) {
			if((session != null) && session.isConnected()) session.disconnect();
			session = null;
		} else {
			// Do nothing...
		}
	}
	
	private void ensureSession() throws JSchException {
		if(session == null) {
			session = jsch.getSession(parsedUri.username, parsedUri.host, parsedUri.port);
			session.setUserInfo(new SshUserInfo(parsedUri.password));
			session.connect();
		} else if(!session.isConnected()) {
			session.connect();
		}
	}
	
	private static class ParsedUri {
		public static final String SCHEME_SH = "sh";
		public static final String SCHEME_SSH = "ssh";
		public String scheme;
		public String host;
		public int port;
		public String username;
		public String password;
		
		public ParsedUri(URI uri) {
			scheme = uri.getScheme();
			if(StringUtils.equals(scheme, SCHEME_SH)) {
				host = null;
				port = -1;
				
				String[] tokens = StringUtils.split(uri.getUserInfo(), ":");
				if(tokens.length == 1) {
					username = tokens[0];
				} else if(tokens.length == 2) {
					username = tokens[0];
					password = tokens[1];
				}
				
			} else if(StringUtils.equals(scheme, SCHEME_SSH)) {
				host = uri.getHost();
				if(StringUtils.isEmpty(host)) throw new RuntimeException("Invalid host");

				port = uri.getPort();
				if(port == -1) port = 22;

				String[] tokens = StringUtils.split(uri.getUserInfo(), ":");
				if(tokens.length == 1) {
					username = tokens[0];
				} else if(tokens.length == 2) {
					username = tokens[0];
					password = tokens[1];
				}
			} else {
				throw new RuntimeException("Invalid URI");
			}	
		}
	}
}
