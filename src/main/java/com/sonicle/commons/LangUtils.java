/*
 * Sonicle Commons is a helper library developed by Sonicle S.r.l.
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

package com.sonicle.commons;

import com.sonicle.commons.net.IPUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.CharacterIterator;
import java.text.MessageFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class LangUtils {
	
	final static Logger logger = (Logger) LoggerFactory.getLogger(LangUtils.class);
	public static final String REGEX_FULL_CLASSNAME = "([a-z][a-z_0-9]*\\.)*[A-Z_]($[A-Z_]|[\\w_])*";
	public static final Pattern PATTERN_FULL_CLASSNAME = Pattern.compile("^"+REGEX_FULL_CLASSNAME+"$");
	
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
	 * Convenience method for replacing line-breaks control characters
	 * ('\r\n', '\n' and '\r') from passed string.
	 * @param s The source string.
	 * @return The cleaned string.
	 */
	public static String stripLineBreaks(String s) {
		String str = StringUtils.replace(s, "\r\n", "");
		str = StringUtils.replace(str, "\n", "");
		str = StringUtils.replace(str, "\r", "");
		return str;
	}
	
	/**
	 * Returns the String value of the passed string.
	 * In case of empty value, default value is returned.
	 * 
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
	 * 
	 * @param value The value
	 * @param defaultValue The dafault value
	 * @return The Boolean value
	 */
	public static Boolean value(String value, Boolean defaultValue) {
		if(StringUtils.isEmpty(value)) return defaultValue;
		Boolean valueof = Boolean.valueOf(value);
		return (valueof != null) ? valueof : defaultValue;
	}
	
	/**
	 * Returns the Integer value of the passed string.
	 * In case of empty value, default value is returned.
	 * During conversion Integer.valueOf method is used!
	 * 
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
	 * 
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
	 * 
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
	 * 
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
		ArrayList<T> items = new ArrayList<T>();
		
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
		URL dirURL = clazz.getClassLoader().getResource(path);
		if(dirURL != null && dirURL.getProtocol().equals("file")) {
			// A file path: easy enough
			return new File(dirURL.toURI()).list();
		}
		if(dirURL == null) {
			// In case of a jar file, we can't actually find a directory.
			// Have to assume the same jar as clazz.
			String me = clazz.getName().replace(".", "/")+".class";
			dirURL = clazz.getClassLoader().getResource(me);
		}
		if(dirURL.getProtocol().equals("jar")) {
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); // Strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); // Gives ALL entries in jar
			//HashSet<String> result = new HashSet<String>(); // Avoid duplicates in case it is a subdirectory
			SortedSet<String> result = new TreeSet<String>(); // Avoid duplicates in case it is a subdirectory
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
	
	public static String buildClassName(String packageName, String className) {
		return MessageFormat.format("{0}.{1}", packageName, className);
	}
	
	public static String extractPackageName(String className) {
		int lastDot = StringUtils.lastIndexOf(className, ".");
		if(lastDot < 0) return className;
		return StringUtils.substring(className, 0, lastDot);
	}
	
	public static String packageToPath(String pkg) {
		return StringUtils.lowerCase(StringUtils.replace(pkg, ".", "/"));
	}
	
	public static String pathToPackage(String path) {
		return StringUtils.lowerCase(StringUtils.replace(path, "/", "."));
	}
	
	public static ClassLoader findClassLoader(Class clazz) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if(cl == null) cl = clazz.getClassLoader();
		return cl;
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
}