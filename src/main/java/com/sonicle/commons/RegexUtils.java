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

import java.util.regex.Pattern;

/**
 *
 * @author malbinola
 */
public class RegexUtils {
	
	public static final String MATCH_ANY = ".*";
	public static final String MATCH_URL_SEPARATOR = "\\/";
	
	public static final String MATCH_JAVA_PACKAGE = "(?:[a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";
	public static final String MATCH_JAVA_FULLCLASSNAME = "([a-z][a-z_0-9]*\\.)*[A-Z_]($[A-Z_]|[\\w_])*";
	public static final String MATCH_SW_VERSION = "[0-9]+(?:\\.[0-9]+){1,2}";
	
	/**
	 * Matches a db-field name
	 * Examples:
	 * Fld1
	 * _Fld1
	 * 0Fld
	 */
	public static final String MATCH_DBFIELD_NAME = "[_A-Za-z0-9\\-]+";
	
	/**
	 * Matches a variable name
	 * Examples:
	 * Var1
	 * _Var1
	 */
	public static final String MATCH_VARIABLE_NAME = "[_A-Za-z0-9][_A-Za-z0-9\\-]*";
	
	public static final String MATCH_SCHEME = "[A-Za-z][A-Za-z0-9+.-]*"; //Also called 'protocol'
	public static final String MATCH_AUTHORITY = "\\/{2}";
	public static final String MATCH_USERINFO = "(?:[A-Za-z0-9-._~]|%[A-Fa-f0-9]{2})+(?::(?:[A-Za-z0-9-._~]|%[A-Fa-f0-9]{2})+)?";
	public static final String MATCH_HOST = "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.){1,126}[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?";
	public static final String MATCH_PORT = "\\d+";
	public static final String MATCH_PATH = "\\/(?:[A-Za-z0-9-._~]|%[A-Fa-f0-9]{2})*";
	public static final String MATCH_QUERY = "\\?(?:[A-Za-z0-9-._~]+(?:=(?:[A-Za-z0-9-._~+]|%[A-Fa-f0-9]{2})+)?)(?:[&|;][A-Za-z0-9-._~]+(?:=(?:[A-Za-z0-9-._~+]|%[A-Fa-f0-9]{2})+)?)*";
	
	public static final String MATCH_USERNAME_CHARS = "[A-Za-z0-9\\.\\-\\_]+";
	public static final String MATCH_USERNAME_CHARS_LWRONLY = "[a-z0-9\\.\\-\\_]+";
	public static final String MATCH_USERNAME = "[A-Za-z0-9\\.\\-\\_]+"; // Deprecated!!
	public static final String MATCH_EMAIL_ADDRESS_RFC2822 = "^((?>[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+\\x20*|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\"\\x20*)*<((?!\\.)(?>\\.?[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+)+|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\")@(((?!-)[a-zA-Z\\d\\-]+(?<!-)\\.)+[a-zA-Z]{2,}|\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3}|[a-zA-Z\\d\\-]*[a-zA-Z\\d]:((?=[\\x01-\\x7f])[^\\\\[\\]]|\\[\\x01-\\x7f])+)\\]))|((?!\\.)(?>\\.?[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+)+|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\")@(((?!-)[a-zA-Z\\d\\-]+(?<!-)\\.)+[a-zA-Z]{2,}|\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3}|[a-zA-Z\\d\\-]*[a-zA-Z\\d]:((?=[\\x01-\\x7f])[^\\\\[\\]]|\\[\\x01-\\x7f])+)\\])$";
	//public static final String MATCH_MAIL_ADDRESS = "^(\\w(([_\\.\\-]?\\w+)*)@(\\w+)(([\\.\\-]?\\w+)*)\\.([A-Za-z]{2,}))$";
	public static final String MATCH_EMAIL_ADDRESS = "[_A-Za-z0-9-\\+]+(?:\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(?:\\.[_A-Za-z0-9-]+)*(?:\\.[A-Za-z]{2,})";
    
    public static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("([^a-zA-z0-9])");
	
	public static final String MATCH_URL = "(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,20}(/\\S*)?";
	public static final String MATCH_WWW_URL = "(^|\\s)(www\\.[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,20})(\\S*)?";

	/**
	 * Matches a URI
	 * Examples:
	 * ssh://user@host.example.com
	 */
	public static final String MATCH_URI = "(" + MATCH_SCHEME + "):" + MATCH_AUTHORITY + "(?:(" + MATCH_USERINFO + ")@)?(" + MATCH_HOST + ")(?::(" + MATCH_PORT + "))?";
	
	/**
	 * Pattern that matches URLs in non trivial situations.
	 * There are numerous examples online but not all pass rigoruous test: 
	 *		https://mathiasbynens.be/demo/url-regex
	 * The only one that looks to be comprehensive and bulletproof is this one:
	 *		https://gist.github.com/dperini/729294
	 *		(which requires inclusion of a copyright header )
	 */
	public static final String MATCH_SIMPLE_URL = "(?:http:\\/\\/|https:\\/\\/|ftp:\\/\\/|\\/\\/)(?:[-a-zA-Z0-9@:%_\\+.~#?&\\/=])+";
	
	/**
	 * @deprecated the above one seems simplest!
	 * Matches any URIs inside a block of text.
	 * https://stackoverflow.com/questions/30847/regex-to-validate-uris
	 * https://snipplr.com/view/6889/regular-expressions-for-uri-validationparsing
	 * 
	 * Examples:
	 * ________________________________________________________________________________
	 * Riunione di Microsoft Teams
	 * Partecipa sul computer o con l'app per dispositivi mobili
	 * Fai clic qui per partecipare alla riunione<https://teams.microsoft.com/l/meetup-join/19%3ameeting_ZGFiZjFhMWItMDRkMC00ZDE0LWIzNjMtYjI4OGE0NDQ2Y2Jm%40thread.v2/0?context=%7b%22Tid%22%3a%229252ed8b-dffc-401c-86ca-6237da9991fa%22%2c%22Oid%22%3a%221027e6c1-bd43-449d-a5df-476dc68bbbaa%22%7d>
	 * Altre informazioni<https://aka.ms/JoinTeamsMeeting> | Opzioni riunione<https://teams.microsoft.com/meetingOptions/?organizerId=1027e6c1-bd43-449d-a5df-476dc68bbbaa&tenantId=9252ed8b-dffc-401c-86ca-6237da9991fa&threadId=19_meeting_ZGFiZjFhMWItMDRkMC00ZDE0LWIzNjMtYjI4OGE0NDQ2Y2Jm@thread.v2&messageId=0&language=it-IT>
	 * ________________________________________________________________________________
	 * 
	 */
	//public static final String MATCH_URI_IN_TEXT = "([a-z0-9+.-]+):(?:\\/\\/(?:((?:[a-z0-9-._~!$&'()*+,;=:]|%[0-9A-F]{2})*)@)?((?:[a-z0-9-._~!$&'()*+,;=]|%[0-9A-F]{2})*)(?::(\\d*))?(\\/(?:[a-z0-9-._~!$&'()*+,;=:@\\/]|%[0-9A-F]{2})*)?|(\\/?(?:[a-z0-9-._~!$&'()*+,;=:@]|%[0-9A-F]{2})+(?:[a-z0-9-._~!$&'()*+,;=:@\\/]|%[0-9A-F]{2})*)?)(?:\\?((?:[a-z0-9-._~!$&'()*+,;=:\\/?@]|%[0-9A-F]{2})*))?(?:#((?:[a-z0-9-._~!$&'()*+,;=:\\/?@]|%[0-9A-F]{2})*))?";
	
	public static final String MATCH_NETMASK4 = "(((255\\.){3}(255|254|252|248|240|224|192|128|0+))|((255\\.){2}(255|254|252|248|240|224|192|128|0+)\\.0)|((255\\.)(255|254|252|248|240|224|192|128|0+)(\\.0+){2})|((255|254|252|248|240|224|192|128|0+)(\\.0+){3}))";
	
	@Deprecated
	public static Pattern matchStartEnd(String pattern) {
		return match(pattern);
	}
	
	public static Pattern match(String pattern) {
		return match(pattern, true, true);
	}
	
	public static Pattern match(String pattern, boolean start, boolean end) {
		return Pattern.compile((start ? "^" : "") + pattern + (end ? "$" : ""));
	}
	
	public static String capture(String pattern) {
		return "(" + pattern + ")";
	}
	
	public static String ignoreCapture(String pattern) {
		return "(?:" + pattern + ")";
	}
	
	public static String escapeRegexSpecialChars(String str) {
		return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");//.replaceAll("\\\\", "\\\\\\\\");   
	}
}
