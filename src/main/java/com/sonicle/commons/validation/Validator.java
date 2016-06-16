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

package com.sonicle.commons.validation;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @deprecated
 * @author malbinola
 */
public class Validator {
	
	public static final String REGEX_MAIL_ADDRESS_RFC2822 = "^((?>[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+\\x20*|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\"\\x20*)*<((?!\\.)(?>\\.?[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+)+|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\")@(((?!-)[a-zA-Z\\d\\-]+(?<!-)\\.)+[a-zA-Z]{2,}|\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3}|[a-zA-Z\\d\\-]*[a-zA-Z\\d]:((?=[\\x01-\\x7f])[^\\\\[\\]]|\\[\\x01-\\x7f])+)\\]))|((?!\\.)(?>\\.?[a-zA-Z\\d!#$%&'*+\\-/=?^_`{|}~]+)+|\"((?=[\\x01-\\x7f])[^\"\\]|\\[\\x01-\\x7f])*\")@(((?!-)[a-zA-Z\\d\\-]+(?<!-)\\.)+[a-zA-Z]{2,}|\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)){3}|[a-zA-Z\\d\\-]*[a-zA-Z\\d]:((?=[\\x01-\\x7f])[^\\\\[\\]]|\\[\\x01-\\x7f])+)\\])$";
	public static final String REGEX_MAIL_ADDRESS = "^(\\w(([_\\.\\-]?\\w+)*)@(\\w+)(([\\.\\-]?\\w+)*)\\.([A-Za-z]{2,}))$";

	public static final String REGEX_HTTP_URL = "http\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?";
	public static final String REGEX_URL = "^(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\'/\\\\+&amp;%\\$#\\=~])*$";

	public static final String REGEX_LFS_PATH = "^[A-Za-z]:\\([^\"*/:?|<>\\.\\x00-\\x20]([^\"*/:?|<>\\\\x00-\\x1F]*[^\"*/:?|<>\\.\\x00-\\x20])?\\)*$";
	public static final String REGEX_UNC_PATH = "^\\{2}[-\\w]+\\(([^\"*/:?|<>\\,;[\\]+=.\\x00-\\x20]|\\.[.\\x20]*[^\"*/:?|<>\\,;[\\]+=.\\x00-\\x20])([^\"*/:?|<>\\,;[\\]+=\\x00-\\x1F]*[^\"*/:?|<>\\,;[\\]+=\\x00-\\x20])?)\\([^\"*/:?|<>\\.\\x00-\\x20]([^\"*/:?|<>\\\\x00-\\x1F]*[^\"*/:?|<>\\.\\x00-\\x20])?\\)*$";
	public static final String REGEX_LFS_UNC_PATH = "^([A-Za-z]:|\\{2}([-\\w]+|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\(([^\"*/:?|<>\\,;[\\]+=.\\x00-\\x20]|\\.[.\\x20]*[^\"*/:?|<>\\,;[\\]+=.\\x00-\\x20])([^\"*/:?|<>\\,;[\\]+=\\x00-\\x1F]*[^\"*/:?|<>\\,;[\\]+=\\x00-\\x20])?))\\([^\"*/:?|<>\\.\\x00-\\x20]([^\"*/:?|<>\\\\x00-\\x1F]*[^\"*/:?|<>\\.\\x00-\\x20])?\\)*$";

	public static final String REGEX_GUID = "^(\\{){0,1}[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}(\\}){0,1}$";
	
	public static boolean isNull(Object value) {
		return value == null;
	}
	
	public static boolean isNullString(Object value) {
		return Validator.isNullString(value, true, true);
	}
	
	public static boolean isNullString(Object value, boolean emptyAsNull) {
		return Validator.isNullString(value, emptyAsNull, true);
	}
	
	public static boolean isNullString(Object value, boolean emptyAsNull, boolean applyTrim) {
		if (Validator.isNull(value)) return true;
		if (!emptyAsNull) return false;
		return Validator.isStringNullOrEmpty((applyTrim) ? value.toString().trim() : value.toString());
	}
	
	public static boolean isStringNullOrEmpty(String value) {
		return (value == null)||(value.isEmpty());
	}
	
	public static String validateString(Object value) throws ValidatorException {
		return Validator.validateString(true, value, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}
	
	public static String validateString(boolean required, Object value) throws ValidatorException {
		return Validator.validateString(required, value, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}
	
	public static String validateString(Object value, boolean emptyAsNull) throws ValidatorException {
		return Validator.validateString(true, value, Integer.MIN_VALUE, Integer.MAX_VALUE, emptyAsNull);
	}
	
	public static String validateString(boolean required, Object value, boolean emptyAsNull) throws ValidatorException {
		return Validator.validateString(required, value, Integer.MIN_VALUE, Integer.MAX_VALUE, emptyAsNull);
	}
	
	public static String validateString(boolean required, Object value, int maxLength, boolean emptyAsNull) throws ValidatorException {
		return Validator.validateString(required, value, Integer.MIN_VALUE, maxLength, emptyAsNull);
	}
	
	public static String validateString(boolean required, Object value, int minLength, int maxLength, boolean emptyAsNull) throws ValidatorException {
		String value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(required && emptyAsNull && Validator.isNullString(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		value1 = String.valueOf(value);
		if((value1.trim().length() < minLength)||(value1.trim().length() > maxLength)) throw new ValidatorException("Value length mishmatch.");
		return value1;
	}
	
	public static Boolean validateBoolean(Object value) throws ValidatorException	{
		return Validator.validateBoolean(true, value);
	}
	
	public static Boolean validateBoolean(boolean required, Object value) throws ValidatorException {
		Boolean value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		value1 = Boolean.valueOf(String.valueOf(value));
		return value1;
	}
	
	public static boolean isInteger(Object value) {
		try {
			validateInteger(value);
			return true;
		} catch(ValidatorException ex) {
			return false;
		}
	}
	
	public static Integer validateInteger(Object value) throws ValidatorException	{
		return Validator.validateInteger(true, value, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
	}
	
	public static Integer validateInteger(boolean required, Object value) throws ValidatorException {
		return Validator.validateInteger(required, value, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
	}
		
	public static Integer validateInteger(boolean required, Object value, int minValue, int maxValue) throws ValidatorException {
		return Validator.validateInteger(required, value, minValue, maxValue, false);
	}
	
	public static Integer validateInteger(boolean required, Object value, int minLength, int maxLength, boolean rangeExclusive) throws ValidatorException {
		Integer value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		try {
			value1 = Integer.valueOf(String.valueOf(value));
		} catch(Exception ex) {
			throw new ValidatorException("Unable to parse value as an integer value.", ex);
		}
		if(rangeExclusive) {
			if((value1 < minLength)||(value1 > maxLength)) throw new ValidatorException("Value range mishmatch.");
		} else {
			if((value1 <= minLength)||(value1 >= maxLength)) throw new ValidatorException("Value range mishmatch.");
		}
		return value1;
	}
	

	public static Long validateLong(Object value) throws ValidatorException	{
		return Validator.validateLong(true, value, Long.MIN_VALUE, Long.MAX_VALUE, false);
	}
	
	public static Long validateLong(boolean required, Object value) throws ValidatorException {
		return Validator.validateLong(required, value, Long.MIN_VALUE, Long.MAX_VALUE, false);
	}
		
	public static Long validateLong(boolean required, Object value, long minValue, long maxValue) throws ValidatorException {
		return Validator.validateLong(required, value, minValue, maxValue, false);
	}
	
	public static Long validateLong(boolean required, Object value, long minLength, long maxLength, boolean rangeExclusive) throws ValidatorException {
		Long value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		try {
			value1 = Long.valueOf(String.valueOf(value));
		} catch(Exception ex) {
			throw new ValidatorException("Unable to parse value as long value.", ex);
		}
		if(rangeExclusive) {
			if((value1 < minLength)||(value1 > maxLength)) throw new ValidatorException("Value range mishmatch.");
		} else {
			if((value1 <= minLength)||(value1 >= maxLength)) throw new ValidatorException("Value range mishmatch.");
		}
		return value1;
	}
	
    
    
    public static Float validateFloat(Object value, Locale locale) throws ValidatorException	{
		return Validator.validateFloat(true, value, locale, -Float.MAX_VALUE, Float.MAX_VALUE, false);
	}
	
	public static Float validateFloat(boolean required, Object value, Locale locale) throws ValidatorException {
		return Validator.validateFloat(required, value, locale, -Float.MAX_VALUE, Float.MAX_VALUE, false);
	}
		
	public static Float validateFloat(boolean required, Object value, Locale locale, float minValue, float maxValue) throws ValidatorException {
		return Validator.validateFloat(required, value, locale, minValue, maxValue, false);
	}
	
	public static Float validateFloat(boolean required, Object value, Locale locale, float minLength, float maxLength, boolean rangeExclusive) throws ValidatorException {
		Float value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		try {
			DecimalFormat df = (locale == null) ? (DecimalFormat)DecimalFormat.getInstance() : (DecimalFormat)DecimalFormat.getInstance(locale);
			value1 = df.parse(String.valueOf(value)).floatValue();
		} catch(Exception ex) {
			throw new ValidatorException("Unable to parse value as an float value.", ex);
		}
		if(rangeExclusive) {
			if((value1 < minLength)||(value1 > maxLength)) throw new ValidatorException("Value range mishmatch.");
		} else {
			if((value1 <= minLength)||(value1 >= maxLength)) throw new ValidatorException("Value range mishmatch.");
		}
		return value1;
	}
	
	public static Double validateDouble(Object value, Locale locale) throws ValidatorException	{
		return Validator.validateDouble(true, value, locale, -Double.MAX_VALUE, Double.MAX_VALUE, false);
	}
	
	public static Double validateDouble(boolean required, Object value, Locale locale) throws ValidatorException {
		return Validator.validateDouble(required, value, locale, -Double.MAX_VALUE, Double.MAX_VALUE, false);
	}
		
	public static Double validateDouble(boolean required, Object value, Locale locale, double minValue, double maxValue) throws ValidatorException {
		return Validator.validateDouble(required, value, locale, minValue, maxValue, false);
	}
	
	public static Double validateDouble(boolean required, Object value, Locale locale, double minLength, double maxLength, boolean rangeExclusive) throws ValidatorException {
		Double value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		try {
			DecimalFormat df = (locale == null) ? (DecimalFormat)DecimalFormat.getInstance() : (DecimalFormat)DecimalFormat.getInstance(locale);
			value1 = df.parse(String.valueOf(value)).doubleValue();
		} catch(Exception ex) {
			throw new ValidatorException("Unable to parse value as a double value.", ex);
		}
		if(rangeExclusive) {
			if((value1 < minLength)||(value1 > maxLength)) throw new ValidatorException("Value range mishmatch.");
		} else {
			if((value1 <= minLength)||(value1 >= maxLength)) throw new ValidatorException("Value range mishmatch.");
		}
		return value1;
	}
	
	public static Date validateDate(Object value, String pattern) throws ValidatorException	{
		return Validator.validateDate(true, value, pattern);
	}
	
	public static Date validateDate(boolean required, Object value, String pattern) throws ValidatorException {
		Date value1 = null;
		if(required && Validator.isNull(value)) throw new ValidatorException("Value is null but marked as required.");
		if(Validator.isNull(value)) return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			value1 = sdf.parse(String.valueOf(value));
		} catch(Exception ex) {
			throw new ValidatorException("Unable to parse value as a date value.", ex);
		}
		return value1;
	}
}