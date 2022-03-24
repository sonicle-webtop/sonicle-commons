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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.MessageFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 *
 * @author malbinola
 */
public class LangUtils {
	final static Logger logger = (Logger) LoggerFactory.getLogger(LangUtils.class);
	public static final HashMap<Integer, String> htmlEscapeEntities2 = new HashMap<Integer, String>();
	
	static {
		htmlEscapeEntities2.put(169, "copy"); // copyright
		htmlEscapeEntities2.put(174, "reg"); // registered trademark
		htmlEscapeEntities2.put(192, "Agrave"); // uppercase A, grave accent
		htmlEscapeEntities2.put(193, "Aacute"); // uppercase A, acute accent
		htmlEscapeEntities2.put(194, "Acirc"); // uppercase A, circumflex accent
		htmlEscapeEntities2.put(195, "Atilde"); // uppercase A, umlaut
		htmlEscapeEntities2.put(196, "Auml"); // uppercase A, umlaut
		htmlEscapeEntities2.put(197, "Aring"); // uppercase A, ring
		htmlEscapeEntities2.put(198, "AElig"); // uppercase AE
		htmlEscapeEntities2.put(199, "Ccedil"); // uppercase C, cedilla
		htmlEscapeEntities2.put(200, "Egrave"); // uppercase E, grave accent
		htmlEscapeEntities2.put(201, "Eacute"); // uppercase E, acute accent
		htmlEscapeEntities2.put(202, "Ecirc"); // uppercase E, circumflex accent
		htmlEscapeEntities2.put(203, "Euml");  // uppercase E, umlaut
		htmlEscapeEntities2.put(204, "Igrave"); // uppercase I, grave accent
		htmlEscapeEntities2.put(205, "Iacute"); // uppercase I, acute accent
		htmlEscapeEntities2.put(206, "Icirc"); // uppercase I, circumflex accent
		htmlEscapeEntities2.put(207, "Iuml");  // uppercase I, umlaut
		htmlEscapeEntities2.put(208, "ETH");   // uppercase Eth, Icelandic
		htmlEscapeEntities2.put(209, "Ntilde"); // uppercase N, tilde
		htmlEscapeEntities2.put(210, "Ograve"); // uppercase O, grave accent
		htmlEscapeEntities2.put(211, "Oacute"); // uppercase O, acute accent
		htmlEscapeEntities2.put(212, "Ocirc"); // uppercase O, circumflex accent
		htmlEscapeEntities2.put(213, "Otilde"); // uppercase O, tilde
		htmlEscapeEntities2.put(214, "Ouml");  // uppercase O, umlaut
		htmlEscapeEntities2.put(216, "Oslash"); // uppercase O, slash
		htmlEscapeEntities2.put(217, "Ugrave"); // uppercase U, grave accent
		htmlEscapeEntities2.put(218, "Uacute"); // uppercase U, acute accent
		htmlEscapeEntities2.put(219, "Ucirc"); // uppercase U, circumflex accent
		htmlEscapeEntities2.put(220, "Uuml");  // uppercase U, umlaut
		htmlEscapeEntities2.put(221, "Yacute"); // uppercase Y, acute accent
		htmlEscapeEntities2.put(222, "THORN"); // uppercase THORN, Icelandic
		htmlEscapeEntities2.put(223, "szlig"); // lowercase sharps, German
		htmlEscapeEntities2.put(224, "agrave"); // lowercase a, grave accent
		htmlEscapeEntities2.put(225, "aacute"); // lowercase a, acute accent
		htmlEscapeEntities2.put(226, "acirc"); // lowercase a, circumflex accent
		htmlEscapeEntities2.put(227, "atilde"); // lowercase a, tilde
		htmlEscapeEntities2.put(228, "auml");  // lowercase a, umlaut
		htmlEscapeEntities2.put(229, "aring"); // lowercase a, ring
		htmlEscapeEntities2.put(230, "aelig"); // lowercase ae
		htmlEscapeEntities2.put(231, "ccedil"); // lowercase c, cedilla
		htmlEscapeEntities2.put(232, "egrave"); // lowercase e, grave accent
		htmlEscapeEntities2.put(233, "eacute"); // lowercase e, acute accent
		htmlEscapeEntities2.put(234, "ecirc"); // lowercase e, circumflex accent
		htmlEscapeEntities2.put(235, "euml");  // lowercase e, umlaut
		htmlEscapeEntities2.put(236, "igrave"); // lowercase i, grave accent
		htmlEscapeEntities2.put(237, "iacute"); // lowercase i, acute accent
		htmlEscapeEntities2.put(238, "icirc"); // lowercase i, circumflex accent
		htmlEscapeEntities2.put(239, "iuml");  // lowercase i, umlaut
		htmlEscapeEntities2.put(240, "eth");   // lowercase eth, Icelandic
		htmlEscapeEntities2.put(241, "ntilde"); // lowercase n, tilde
		htmlEscapeEntities2.put(242, "ograve"); // lowercase o, grave accent
		htmlEscapeEntities2.put(243, "oacute"); // lowercase o, acute accent
		htmlEscapeEntities2.put(244, "ocirc"); // lowercase o, circumflex accent
		htmlEscapeEntities2.put(245, "otilde"); // lowercase o, tilde
		htmlEscapeEntities2.put(246, "ouml");  // lowercase o, umlaut
		htmlEscapeEntities2.put(248, "oslash"); // lowercase o, slash
		htmlEscapeEntities2.put(249, "ugrave"); // lowercase u, grave accent
		htmlEscapeEntities2.put(250, "uacute"); // lowercase u, acute accent
		htmlEscapeEntities2.put(251, "ucirc"); // lowercase u, circumflex accent
		htmlEscapeEntities2.put(252, "uuml");  // lowercase u, umlaut
		htmlEscapeEntities2.put(253, "yacute"); // lowercase y, acute accent
		htmlEscapeEntities2.put(254, "thorn"); // lowercase thorn, Icelandic
		htmlEscapeEntities2.put(255, "yuml");  // lowercase y, umlaut
		htmlEscapeEntities2.put(8364, "euro"); // Euro symbol
	}
	
	public static String escapeHtmlAccentsAndSymbols(String text) {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator it = new StringCharacterIterator(text);
		
		int ich = -1;
		for(char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			ich = (int) ch;
			if(htmlEscapeEntities2.containsKey(ich)) {
				result.append("&");
				result.append(htmlEscapeEntities2.get(ich));
				result.append(";");
			} else {
				result.append(ch);
			}
		}
		return result.toString();
	}
	
	/**
	 * Escapes the double-quote char in a String using Json String double-quote rules.
	 * Example:
	 * input string: He didn't say, "Stop!"
	 * output string: He didn't say, \\"Stop!\\"
	 * 
	 * @param s String to escape values in, may be null.
	 * @return String with escaped values, null if null string input.
	 */
	public static String escapeJsonDoubleQuote(String s) {
		return (s != null) ? s.replaceAll("\"", "\\\\\"") : null;
	}
	
	/**
	 * Escapes the single-quote char in a String.
	 * Example:
	 * input string: He can't say anything!
	 * output string: He can\'t say anything!
	 * 
	 * @param s String to escape values in, may be null.
	 * @return String with escaped values, null if null string input.
	 */
	public static String escapeSingleQuote(String s) {
		return (s != null) ? s.replaceAll("'", "\\'") : null;
	}
	
	/**
	 * Escapes the single-quote char in a String using MessageFormat rules.
	 * Example:
	 * input string: He can't say anything!
	 * output string: He can''t say anything!
	 * 
	 * @param s String to escape values in, may be null.
	 * @return String with escaped values, null if null string input.
	 */
	public static String escapeMessageFormat(String s) {
		return (s != null) ? s.replaceAll("(?<!')'(?!')", "''") : null;
	}
	
	public static String unescapeUnicodeBackslashes(String s) {
		return (s != null) ? s.replaceAll("\\\\u", "\\u") : null;
	}
	
    /**
     * fills the left side of a number with zeros <br>
     * e.g. zerofill(14, 3) -> "014" <br>
     * e.g. zerofill(187, 6) -> "000014" <br>
     * e.g. zerofill(-33, 4) -> "-033" <br>
     **/
    public static String zerofill(int x, int d) {
        StringBuffer buf = new StringBuffer();
        if (x < 0) {
            buf.append("-");
            d--;
            x = -x;
        }
        while (d>7) {
            buf.append("0");
            d--;
        }
        switch (d) {
        case 7:
            if (x<1000000) buf.append("0");
        case 6:
            if (x<100000) buf.append("0");
        case 5:
            if (x<10000) buf.append("0");
        case 4:
            if (x<1000) buf.append("0");
        case 3:
            if (x<100) buf.append("0");
        case 2:
            if (x<10) buf.append( "0" );
        }
        buf.append(x);
        return buf.toString();
    }
	
	public static String encodeURL(final String url) {
		try {
			if(StringUtils.isBlank(url)) return url;
			String s = URLEncoder.encode(url, "UTF-8");
			return StringUtils.replace(s, " ", "+");
		} catch(UnsupportedEncodingException ex) {
			return null;
		}
	}
	
	/**
	 * Encode data for use in HTML using HTML entity encoding.
	 * For both for content and attributes.
	 * @see
	 * <a href="http://en.wikipedia.org/wiki/Character_encodings_in_HTML">HTML
	 * Encodings [wikipedia.org]</a>
	 * @see <a href="http://www.w3.org/TR/html4/sgml/sgmldecl.html">SGML
	 * Specification [w3.org]</a>
	 * @see <a href="http://www.w3.org/TR/REC-xml/#charsets">XML Specification
	 * [w3.org]</a>
	 * @param str The string to encode.
	 * @return Encoded for use in HTML.
	 */
	public static String encodeForHTML(final String str) {
		if (StringUtils.isBlank(str)) return str;
		return Encode.forHtml(str);
	}
	
	/**
	 * Encode data for use in HTML content.
	 * @param str The string to encode.
	 * @return Encoded for use in HTML attribute.
	 */
	public static String encodeForHTMLContent(final String str) {
		if (StringUtils.isBlank(str)) return str;
		return encodeLineBreaksForHTML(Encode.forHtmlContent(str));
		//return Encode.forHtmlContent(str);
	}
	
	/**
	 * Encode data for use in HTML attributes.
	 * @param str The string to encode.
	 * @return Encoded for use in HTML attribute.
	 */
	public static String encodeForHTMLAttribute(final String str) {
		if (StringUtils.isBlank(str)) return str;
		return Encode.forHtmlAttribute(str);
	}
	
	/**
	 * Encodes line-breaks chars (both '\n' and '\r') into HTML '<br>' element.
	 * @param str The string to encode.
	 * @return Encoded for use in HTML.
	 */
	public static String encodeLineBreaksForHTML(final String str) {
		return replaceLineBreaks(str, "<br>");
	}

	/**
	 * Removes line-breaks control characters ('\r\n', '\n' and '\r') from the passed string.
	 * @param str The source string.
	 * @return The cleaned string.
	 */
	public static String stripLineBreaks(final String str) {
		return replaceLineBreaks(str, "");
	}
	
	/**
	 * Cleanses line-breaks characters ('\r\n', '\n' and '\r') from passed
	 * String, replacing them with the specified replacement. 
	 * Multiple line-breaks will be replaced with a set of replacement String 
	 * of same cardinality.
	 * @param str The source string.
	 * @param replace The replacement string.
	 * @return The cleaned string.
	 */
	public static String replaceLineBreaks(final String str, final String replace) {
		return StringUtils.isBlank(str) ? str : str.replaceAll("\\R", replace);
	}
	
	/**
	 * Flattens line-breaks characters ('\r\n', '\n' and '\r') from passed 
	 * String, replacing them with a white-space. Consecutive line-breaks will 
	 * be replaced only with a single white-space.
	 * @param str The source string.
	 * @return The flattened string.
	 */
	public static String flattenLineBreaks(final String str) {
		return flattenLineBreaks(str, " ");
	}
	
	/**
	 * Flattens line-breaks characters ('\r\n', '\n' and '\r') from passed 
	 * String, replacing them with the specified replacement. Consecutive 
	 * line-breaks will be replaced only with one occurence of replacement String.
	 * @param str The source string.
	 * @param replace The replacement string.
	 * @return The flattened string.
	 */
	public static String flattenLineBreaks(final String str, final String replace) {
		return StringUtils.isBlank(str) ? str : str.replaceAll("\\R+", replace);
	}
	
	/**
	 * Convenience method for building a pattern string (siutable for queries)
	 * from a text containing many words.
	 * @param text The source text.
	 * @return The pattern.
	 */
	public static String patternizeWords(String text) {
		String s = "";
		for(String token : StringUtils.split(text, " ")) {
			s += "%" + token + "% ";
		}
		return s.trim();
	}
	
	/**
	 * Analyzes a source string s and compares it with referenceString by 
	 * building sub-string blocks of specified length and comparing them with 
	 * the reference string. If any token is found, the source string is  
	 * classified as similar returning true.
	 * @param s The source string being compared.
	 * @param referenceString The reference string in which look for tokens.
	 * @param tokenSize The choosen token size.
	 * @return True if string are classified as similar, false otherwise.
	 */
	public static boolean containsSimilarTokens(String s, String referenceString, int tokenSize) {
		if ((s.length() < tokenSize) || (referenceString.length() < tokenSize)) return false;
		for (int i = 0; (i + tokenSize) < s.length(); i++) {
			if (referenceString.contains(s.substring(i, i + tokenSize))) return true;
		}
		return false;
	}
	
	/**
	 * Returns the String value of the passed string.
	 * In case of empty value, default value is returned.
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The String value
	 */
	public static String value(String value, String defaultValue) {
		return StringUtils.defaultString(value, defaultValue);
	}
	
	/**
	 * Returns the Boolean value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Boolean.valueOf method is used!
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Boolean value
	 */
	public static Boolean value(String value, Boolean defaultValue) {
		if (StringUtils.isEmpty(value)) return defaultValue;
		Boolean valueof = Boolean.valueOf(value);
		return (valueof != null) ? valueof : defaultValue;
	}
	
	/**
	 * Returns the Short value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Boolean.valueOf method is used.
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Short value
	 */
	public static Short value(String value, Short defaultValue) {
		if (StringUtils.isEmpty(value)) return defaultValue;
		return Short.valueOf(value);
	}
	
	/**
	 * Returns the Integer value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Integer.valueOf method is used!
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Integer value
	 */
	public static Integer value(String value, Integer defaultValue) {
		if(StringUtils.isEmpty(value)) return defaultValue;
		Integer valueof = Integer.valueOf(value);
		return (valueof != null) ? valueof : defaultValue;
	}
	
	/**
	 * Returns the Long value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Integer.valueOf method is used!
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Long value
	 */
	public static Long value(String value, Long defaultValue) {
		if(StringUtils.isEmpty(value)) return defaultValue;
		Long valueof = Long.valueOf(value);
		return (valueof != null) ? valueof : defaultValue;
	}
	
	/**
	 * Returns the Float value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Float.valueOf method is used!
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Float value
	 */
	public static Float value(String value, Float defaultValue) {
		if(StringUtils.isEmpty(value)) return defaultValue;
		Float valueof = Float.valueOf(value);
		return (valueof != null) ? valueof : defaultValue;
	}
	
	/**
	 * Returns the Type value of the passed string.
	 * During conversion Type.valueOf method is used!
	 * @param <T> The Type
	 * @param value The value
	 * @param type The class Type
	 * @return The value of T Type
	 */
	public static <T>T value(String value, Class<T> type) {
		return value(value, null, type);
	}
	
	/**
	 * Returns the Type value of the passed string.
	 * In case of empty value, if not null default value is returned.
	 * During conversion Type.valueOf method is used!
	 * 
	 * @param <T> The Type
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @param type The class Type
	 * @return The value of T Type
	 */
	/*
	public static <T>T value(String value, T defaultValue, Class<T> type) {
		if(StringUtils.isEmpty(value) && (defaultValue != null)) return defaultValue;
		T valueof = invokeStaticFromJson(type, value);
		return (valueof == null) ? defaultValue : valueof;
	}
	*/
	
	public static <T>T value(String value, T defaultValue, Class<T> type) {
		if(StringUtils.isEmpty(value)) return defaultValue;
		/*
		T valueof = null;
		if(type.isAssignableFrom(String.class) || type.isAssignableFrom(Boolean.class) 
			|| type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class)
			|| type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)) {
			valueof = invokeStaticValueOf(type, value);
		} else {
			valueof = invokeStaticFromJson(type, value);
		}
		*/
		T valueof = deserialize(value, defaultValue, type);
		return (valueof == null) ? defaultValue : valueof;
	}
	
	public static Boolean value(Boolean value, Boolean defaultValue) {
		return (value != null) ? value : defaultValue;
	}
	
	public static Short value(Short value, Short defaultValue) {
		return (value != null) ? value : defaultValue;
	}
	
	public static Integer value(Integer value, Integer defaultValue) {
		return (value != null) ? value : defaultValue;
	}
	
	public static Long value(Long value, Long defaultValue) {
		return (value != null) ? value : defaultValue;
	}
	
	public static <T>T value(T value, T defaultValue, Class<T> type) {
		return (value != null) ? value : defaultValue;
	}
	
	/**
	 * Serializes the passed object into a suitable string according to its type.
	 * If the object is a primitive type toString method will be used, 
	 * otherwise it tries to apply a JSON serialization invoking toJson method 
	 * defined in the type class. 
	 * 
	 * @param <T>
	 * @param value The object value.
	 * @param type The object type.
	 * @return String representation of the passed object.
	 */
	public static <T> String serialize(T value, Class<T> type) {
		if((value instanceof String) || (value instanceof Boolean)
			|| (value instanceof Integer) || (value instanceof Long)
			|| (value instanceof Float) || (value instanceof Double)) {
			logger.debug("Serializing primitive class {}", type.toString());
			if(value == null) return null;
			return value.toString();
		} else {
			logger.debug("Serializing custom class {}", type.toString());
			return invokeStaticToJson(type, value);
		}
	}
	
	/**
	 * Deserializes the passed string into a corresponding object.
	 * If the type is primitive valueOf method of the type class will be used,
	 * otherwise it tries to apply a JSON deserialization invoking fromJson
	 * method defined in type class.
	 * 
	 * @param <T>
	 * @param value Serialized value.
	 * @param type The object type.
	 * @return Object representation of the passed string.
	 */
	public static <T>T deserialize(String value, Class<T> type) {
		return deserialize(value, null, type);
	}
	
	/**
	 * Deserializes the passed string into a corresponding object.
	 * If the type is primitive valueOf method of the type class will be used,
	 * otherwise it tries to apply a JSON deserialization invoking fromJson
	 * method defined in type class.
	 * 
	 * @param <T>
	 * @param value Serialized value.
	 * @param defaultValue The default value 
	 * @param type The object type.
	 * @return Object representation of the passed string.
	 */
	public static <T>T deserialize(String value, T defaultValue, Class<T> type) {
		T valueof = null;
		if(type.isAssignableFrom(String.class) || type.isAssignableFrom(Boolean.class) || 
			type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class) ||
			type.isAssignableFrom(Float.class) || type.isAssignableFrom(Double.class)) {
			logger.debug("Deserializing primitive class {}", type.toString());
			valueof = invokeStaticValueOf(type, value);
		} else {
			logger.debug("Deserializing custom class {}", type.toString());
			valueof = invokeStaticFromJson(type, value);
		}
		return (valueof == null) ? defaultValue : valueof;
	}
	
	private static <T>T invokeStaticFromJson(Class<T> type, String value) {
		try {
			logger.debug("Invoking static fromJson from {}", type.toString());
			Method method = type.getMethod("fromJson", String.class);
			return (T)method.invoke(null, value);
		} catch(NoSuchMethodException ex) {
			logger.error("No fromJson(String value) static method found in class {}", type.toString(), ex);
		} catch(Exception ex) {
			logger.error("Error during fromJson invocation", ex);
		}
		return null;
	}
	
	private static String invokeStaticToJson(Class type, Object value) {
		try {
			logger.debug("Invoking static toJson from {}", type.toString());
			Method method = type.getMethod("toJson", type);
			return (String)method.invoke(null, value);
		} catch(NoSuchMethodException ex) {
			logger.error("No toJson(<T> value) static method found in class {}", type.toString(), ex);
		} catch(Exception ex) {
			logger.error("Error during toJson invocation", ex);
		}
		return null;
	}
	
	private static <T>T invokeStaticValueOf(Class<T> type, String value) {
		try {
			logger.debug("Invoking static valueOf from {}", type.toString());
			Method method = type.getMethod("valueOf", String.class);
			return (T)method.invoke(null, value);
		} catch(NoSuchMethodException ex) {
			logger.error("No valueOf(String value) static method found in class {}", type.toString(), ex);
		} catch(Exception ex) {
			logger.error("Error during valueOf invocation", ex);
		}
		return null;
	}
	/*
	public static String invokeStaticToString(Class type, Object value) {
		try {
			if(value == null) return null;
			Method method = type.getMethod("toString", type);
			return (String)method.invoke(null, value);
		} catch(NoSuchMethodException ex) {
			logger.error("No toString(<T> value) static method found in class {}", type.toString(), ex);
		} catch(Exception ex) {
			logger.error("Error during toString invocation", ex);
		}
		return null;
	}
	*/
	
	public static <T> String to2DString(T[][] x) {
		final String vSep = "\n";
		final String hSep = "|";
		final StringBuilder sb = new StringBuilder();

		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				final T[] a = x[i];
				if (i > 0) {
					sb.append(vSep);
				}
				if (a != null) {
					for (int j = 0; j < a.length; j++) {
						final T b = a[j];
						if (j > 0) {
							sb.append(hSep);
						}
						sb.append((b == null) ? " " : b);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Parses a String into Map entries. Character ',' will be uses as items
	 * separator and '=' as values separator. Key first (index 0), followed by 
	 * its associated value (index 1).
	 * @param str The String value.
	 * @return The resulting Map.
	 */
	public static Map<String, String> parseStringAsKeyValueMap(final String str) {
		return parseStringAsKeyValueMap(str, 0, 1);
	}
	
	/**
	 * Parses a String into Map entries using indexes to select which token is 
	 * the key and which one is the value. Character ',' will be uses as items
	 * separator and '=' as values separator.
	 * @param str The String value.
	 * @param keyIndex The key index.
	 * @param valueIndex The value index.
	 * @return The resulting Map.
	 */
	public static Map<String, String> parseStringAsKeyValueMap(final String str, final int keyIndex, final int valueIndex) {
		return parseStringAsKeyValueMap(str, keyIndex, valueIndex, ",", "=", true);
	}
	
	/**
	 * Parses a String into Map entries using the specified separators to 
	 * split the source value and indexes to select which token is the key and
	 * which one is the value.
	 * @param str The String value.
	 * @param keyIndex The key index.
	 * @param valueIndex The value index.
	 * @param itemsSeparator Separator char between items.
	 * @param valuesSeparator Separator char between key and value.
	 * @param trim Controls whether to trim items before adding to map.
	 * @return The resulting Map.
	 */
	public static Map<String, String> parseStringAsKeyValueMap(final String str, final int keyIndex, final int valueIndex, final String itemsSeparator, final String valuesSeparator, final boolean trim) {
		LinkedHashMap<String, String> map = new LinkedHashMap();
		if (!StringUtils.isBlank(str)) {
			int ki = keyIndex >= 0 && keyIndex <= 1 ? keyIndex : 0;
			int vi = valueIndex >= 0 && valueIndex <= 1 ? valueIndex : 1;
			String[] ltokens = StringUtils.split(str, itemsSeparator);
			for (int i=0; i<ltokens.length; i++) {
				String[] itokens = StringUtils.split(ltokens[i], valuesSeparator, 2);
				if (itokens.length == 2) {
					map.put(trim ? StringUtils.trim(itokens[ki]) : itokens[ki], trim ? StringUtils.trim(itokens[vi]) : itokens[vi]);
				}
			}
		}
		return map;
	}
	
	/**
	 * Parses a String into a List of items of the specified type.
	 * Character ',' will be uses as items separator. Null-items will be ignored.
	 * @param <T>
	 * @param str The String value.
	 * @param type The object type.
	 * @return The resulting List.
	 */
	public static <T> List<T> parseStringAsList(final String str, Class<T> type) {
		return parseStringAsList(str, ",", true, type);
	}
	
	/**
	 * Parses a String into a List of items of the specified type, using the 
	 * provided character as separator between items. Null-items will be ignored.
	 * @param <T>
	 * @param str The String value.
	 * @param separator Separator char between items.
	 * @param trim Controls whether to trim items before converting them.
	 * @param type The object type.
	 * @return The resulting List.
	 */
	public static <T> List<T> parseStringAsList(final String str, final String separator, final boolean trim, Class<T> type) {
		ArrayList<T> list = new ArrayList<>();
		if (!StringUtils.isBlank(str)) {
			String[] ltokens = StringUtils.split(str, separator);
			for (int i=0; i<ltokens.length; i++) {
				String svalue = trim ? StringUtils.trim(ltokens[i]) : ltokens[i];
				if (type.isAssignableFrom(String.class)) {
					if (svalue != null) list.add((T)svalue);
				} else {
					T value = value(svalue, null, type);
					if (value != null) list.add(value);
				}
			}
		}
		return list;
	}
	
	/**
	 * Converts a name:value string of arbitrary elements into
	 * an objects list of specified type T.
	 * A string like this: "name1:value1,name2:value2"
	 * contains two items separated by a comma; each item has 
	 * a name (eg. name1) and a value (eg. value1).
	 * 
	 * @param <T> The Type.
	 * @param str Source string of name:value pairs.
	 * @param type The class Type.
	 * @return A List of type T objects.
	 */
	public static <T> ArrayList<T> fromNameValueString(String str, Class<T> type) {
		ArrayList<T> items = new ArrayList<>();
		
		if(!StringUtils.isEmpty(str)) {
			String[] itemTokens = StringUtils.split(str, ",");
			try {
				Constructor<T> constructor = type.getConstructor(String.class, String.class);
				for(int i=0; i<itemTokens.length; i++) {
					String[] propTokens = StringUtils.split(itemTokens[i], ":", 2);
					if(propTokens.length == 2) items.add((T) constructor.newInstance(propTokens[0], propTokens[1]));
				}
			} catch(Exception ex) { /* Do nothing... */ }
		}
		return items;
	}
	
	public static String[] listPackageFiles(Class clazz, String path) throws URISyntaxException, IOException {
		ClassLoader cl = findClassLoader(clazz);
		URL dirURL = cl.getResource(path);
		if(dirURL != null && dirURL.getProtocol().equals("file")) { // A file path: easy enough
			return new File(dirURL.toURI()).list();
		}
		if(dirURL == null) {
			// In case of a jar file, we can't actually find a directory.
			// Have to assume the same jar as clazz.
			String me = clazz.getName().replace(".", "/") + ".class";
			dirURL = cl.getResource(me);
		}
		if(dirURL.getProtocol().equals("jar")) {
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); // Strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); // Gives ALL entries in jar
			SortedSet<String> result = new TreeSet<>(); // Avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { // Filter according to the path
					String entry = name.substring(path.length());
					if(!StringUtils.isEmpty(entry)) {
						int checkSubdir = entry.indexOf("/");
						if (checkSubdir >= 0) {
							// If it is a subdirectory, we just return the directory name
							entry = entry.substring(0, checkSubdir);
						}
						result.add(entry);
					}
				}
			}
			return result.toArray(new String[result.size()]);
		}
		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
	
	/**
	 * @deprecated Use ClassUtils.classForNameQuietly instead
	 */
	@Deprecated
	public static boolean classForNameQuietly(String className) {
		try {
			Class.forName(className);
			return true;
		} catch(ClassNotFoundException ex) {
			return false;
		}
	}
	
	/**
	 * @deprecated Use ClassUtils.joinClassPackages instead
	 */
	@Deprecated
	public static String joinClassPackages(String packageName1, String packageName2) {
		return StringUtils.removeEnd(packageName1, ".") + "." + StringUtils.removeStart(packageName2, ".");
	}
	
	/**
	 * @deprecated Use ClassUtils.buildClassName instead
	 */
	@Deprecated
	public static String buildClassName(String packageName, String className) {
		return StringUtils.removeEnd(packageName, ".") + "." + StringUtils.removeStart(className, ".");
		//return MessageFormat.format("{0}.{1}", packageName, className);
	}
	
	/**
	 * @deprecated Use ClassUtils.getClassName instead
	 */
	@Deprecated
	public static String getClassSimpleName(String className) {
		int lastDot = StringUtils.lastIndexOf(className, ".");
		if(lastDot < 0) return className;
		return StringUtils.substring(className, lastDot+1);
	}
	
	/**
	 * @deprecated Use ClassUtils.getClassPackageName instead
	 */
	@Deprecated
	public static String getClassPackageName(Class clazz) {
		return getClassPackageName(clazz.getCanonicalName());
	}
	
	/**
	 * @deprecated Use ClassUtils.getClassPackageName instead
	 */
	@Deprecated
	public static String getClassPackageName(String className) {
		int lastDot = StringUtils.lastIndexOf(className, ".");
		if(lastDot < 0) return className;
		return StringUtils.substring(className, 0, lastDot);
	}
	
	/**
	 * @deprecated Use ClassUtils.classPackageAsPath instead
	 */
	@Deprecated
	public static String packageToPath(String pkg) {
		return StringUtils.lowerCase(StringUtils.replace(pkg, ".", "/"));
	}
	
	/**
	 * @deprecated Use ClassUtils.pathAsClassPackage instead
	 */
	@Deprecated
	public static String pathToPackage(String path) {
		return StringUtils.lowerCase(StringUtils.replace(path, "/", "."));
	}
	
	public static String joinPaths(String... paths) {
		ArrayList<String> parts = new ArrayList<>();
		for(String path : paths) {
			parts.add(StringUtils.removeStart(path, "/"));
		}
		return StringUtils.join(parts, "/");
	}
	
	/**
	 * @deprecated Use ClassUtils.getClassLoaderOf instead
	 */
	@Deprecated
	public static ClassLoader findClassLoader(Class clazz) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if(cl == null) cl = clazz.getClassLoader();
		return cl;
	}
	
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[ste.length - 1 - depth].getMethodName();
	}
	
	public static String homogenizeLocaleId(String languageTag) {
		return StringUtils.replace(languageTag, "-", "_");
	}
	
	public static Locale languageTagToLocale(String languageTag) {
		languageTag = StringUtils.replace(languageTag, "_", "-");
		return Locale.forLanguageTag(languageTag);
	}
	
	public static <K,V> V ifValue(Map<K,V> map, K key, V value) {
		return (map.containsKey(key)) ? map.get(key) : value;
	}
	
	public static int charOccurrences(char c, String s) {
		int n=0,ix=0;
		while((ix=s.indexOf(c,ix))>=0) {
			++n;
			++ix;
		}
		return n;
	}
	
	public static String camelize(String str) {
		String value = WordUtils.capitalize(str, '.', '-', '_');
		value = StringUtils.remove(value, '.');
		value = StringUtils.remove(value, '-');
		value = StringUtils.remove(value, '_');
		return WordUtils.uncapitalize(value);
	}
	
	public static String abbreviatePersonalName(boolean initialsOnly, String fullName) {
		if (fullName == null) return null;
		StringBuilder sb = new StringBuilder();
		// https://english.stackexchange.com/questions/412943/initials-in-multiple-surnames
		// We suppose that full-name has the following structure:
		//   First-name Mid-name Last-name
		
		String[] tokens = StringUtils.split(fullName, " ");
		if (initialsOnly) {
			sb.append(tokens[0].substring(0, 1).toUpperCase());
			if (tokens.length > 1) {
				sb.append(tokens[tokens.length-1].substring(0, 1).toUpperCase());
			}
		} else {
			if (tokens.length == 1) {
				sb.append(StringUtils.capitalize(tokens[0]));
				
			} else {
				sb.append(tokens[0].substring(0, 1).toUpperCase());
				sb.append(".");
				if (tokens.length > 2) {
					sb.append(tokens[1].substring(0, 1).toUpperCase());
					sb.append(".");
				}
				sb.append(" ");
				sb.append(StringUtils.capitalize(tokens[tokens.length-1]));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param <T>
	 * @param objects
	 * @return 
	 */
	public static <T> List<T> nonNulls(T... objects) {
		ArrayList<T> items = new ArrayList<>();
		for (T obj : objects) {
			if (obj != null) items.add(obj);
		}
		return items;
	}
	
	/**
	 * Returns first non-null object in arguments list, null otherwise.
	 * @param <T>
	 * @param objects Objects to evaluate.
	 * @return The first object matching criteria
	 */
	public static <T> T coalesce(T... objects) {
		for (T obj : objects) {
			if (obj != null) return obj;
		}
		return null;
	}
	
	/**
	 * Returns first non-null/non-blank string in arguments list, null otherwise.
	 * @param strings Strings to evaluate.
	 * @return The first string matching criteria
	 */
	public static String coalesceStrings(String... strings) {
		if (strings == null) throw new IllegalArgumentException("String varargs must not be null");
		for (String value : strings) {
			if (!StringUtils.isBlank(StringUtils.trim(value))) return value;
		}
		return null;
	}
	
	/**
	 * Joins passed strings using provided separator, string is joined only if it is not blank.
	 * @param separator The string sepatator.
	 * @param strings Strings to evaluate.
	 * @return The joined string
	 */
	public static String joinStrings(final String separator, final String... strings) {
		if (strings == null) throw new IllegalArgumentException("String varargs must not be null");
		final String sanitizedSeparator = StringUtils.defaultString(separator);
		final StringBuilder result = new StringBuilder();
		final Iterator<String> it = Arrays.asList(strings).iterator();
		while (it.hasNext()) {
			final String value = it.next();
			if (!StringUtils.isBlank(StringUtils.trim(value))) {
				result.append(value);
				if (it.hasNext()) {
					result.append(sanitizedSeparator);
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * Formats a message using provided pattern and arguments.
	 * @param pattern The message pattern.
	 * @param arguments The arguments list.
	 * @return The formatted message
	 */
	public static String formatMessage(String pattern, Object... arguments) {
		return formatMessage(false, pattern, arguments);
	}
	
	/**
	 * Formats a message using provided pattern and arguments.
	 * @param autoDetect Wether to activate MessageFormat patter auto-detect, `false` to fallback on smarter MessageFormatter.
	 * @param pattern The message pattern.
	 * @param arguments The arguments list.
	 * @return The formatted message
	 */
	public static String formatMessage(boolean autoDetect, String pattern, Object... arguments) {
		if (autoDetect && StringUtils.contains(pattern, "{0}")) {
			return MessageFormat.format(escapeMessageFormat(pattern), arguments);
		} else {
			return MessageFormatter.arrayFormat(pattern, arguments).getMessage();
		}
	}
	
	/**
	 * Obtains Throwable's message.
	 * @param t The throwable to get the message.
	 * @return 
	 */
	public static String getThrowableMessage(Throwable t) {
		return ExceptionUtils.getMessage(t);
	}
	
	/**
	 * Introspects the Throwable to obtain the deepest (root) cause.
	 * @param t The throwable to get the root cause for, may be null.
	 * @return The deepest cause or throwable itself
	 */
	public static Throwable getDeepestCause(Throwable t) {
		Throwable deepest = ExceptionUtils.getRootCause(t);
		return (deepest == null) ? t : deepest;
	}
	
	/**
	 * Introspects the Throwable to obtain the deepest (root) cause message.
	 * The message returned is of the form {ClassNameWithoutPackage}: {ThrowableMessage}.
	 * @param t The throwable to get the root cause for, may be null.
	 * @return The deepest cause message, or null if the initial input is null
	 */
	public static String getDeepestCauseMessage(Throwable t) {
		String s = ExceptionUtils.getRootCauseMessage(t);
		return StringUtils.isBlank(s) ? null : s;	
	}
	
	public static <T>CollectionChangeSet getCollectionChanges(Collection<T> fromCollection, Collection<T> toCollection) {
		List<T> created = new ArrayList<>(toCollection);
		created.removeAll(fromCollection);
		List<T> updated = new ArrayList<>(toCollection);
		updated.retainAll(fromCollection);
		List<T> deleted = new ArrayList<>(fromCollection);
		deleted.removeAll(toCollection);
		return new CollectionChangeSet<>(created, updated, deleted);
	}
	
	public static class CollectionChangeSet<T> {
		public List<T> inserted;
		public List<T> updated;
		public List<T> deleted;
		
		public CollectionChangeSet(List<T> inserted, List<T> updated, List<T> deleted) {
			this.inserted = inserted;
			this.updated = updated;
			this.deleted = deleted;
		}
	}
}