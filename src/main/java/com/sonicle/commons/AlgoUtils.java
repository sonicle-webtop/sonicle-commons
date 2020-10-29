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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;

/**
 *
 * @author malbinola
 */
public class AlgoUtils {
	
	public static String md5Hex(final String s) {
		return DigestUtils.md5Hex(s);
	}
	
	public static String md5Hex(final byte[] bytes) {
		return DigestUtils.md5Hex(bytes);
	}
	
	public static String crc32Hex(final String s) {
		return crc32Hex(s.getBytes(Charsets.UTF_8));
	}
	
	public static String crc32Hex(final byte[] bytes) {
		CRC32 crc32 = new CRC32();
		crc32.update(bytes);
		return Long.toHexString(crc32.getValue());
	}
	
	public static String adler32Hex(final String s) {
		return adler32Hex(s.getBytes(Charsets.UTF_8));
	}
	
	public static String adler32Hex(final byte[] bytes) {
		Adler32 adler = new Adler32();
		adler.update(bytes);
		return Long.toHexString(adler.getValue());
	}
	
	/**
	 * Encodes a string in a way that makes it harder to read it "as is" this makes 
	 * it possible for Strings to be "encoded" within the app and thus harder to 
	 * discover by a casual search.
	 * https://github.com/codenameone/CodenameOne/blob/c3322f5f50c56c4abedfd4b1ff78ecae7b006b48/CodenameOne/src/com/codename1/io/Util.java
	 * @param s The string to decode
	 * @return The decoded string
	 */
	public static String xorDecode(final String s) {
		byte[] dat = Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8));
		for (int i = 0 ; i < dat.length ; i++) {
			dat[i] = (byte)(dat[i] ^ (i % 254 + 1));
		}
		return new String(dat, StandardCharsets.UTF_8);
	}
	
	/**
	 * The inverse method of xorDecode, this is normally unnecessary and is here 
	 * mostly for completeness.
	 * @param s The string to encode
	 * @return The encoded string
	 */
	public static String xorEncode(final String s) {
		byte[] dat = s.getBytes(StandardCharsets.UTF_8);
		for (int i = 0 ; i < dat.length ; i++) {
			dat[i] = (byte)(dat[i] ^ (i % 254 + 1));
		}
		return Base64.getEncoder().encodeToString(dat);
	}
}
