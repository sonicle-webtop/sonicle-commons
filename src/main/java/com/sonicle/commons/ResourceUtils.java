/*
 * Copyright (C) 2021 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2021 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.annotation.Resources;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author malbinola
 * Inspired by:
 * https://github.com/dropwizard/dropwizard/blob/v2.0.25/dropwizard-util/src/main/java/io/dropwizard/util/Resources.java
 */
public class ResourceUtils {
	
	/**
	 * Returns a {@code URL} pointing to {@code resourceName} if the resource is found using the
	 * {@linkplain Thread#getContextClassLoader() context class loader}. In simple environments, the
	 * context class loader will find resources from the class path. In environments where different
	 * threads can have different class loaders, for example app servers, the context class loader
	 * will typically have been set to an appropriate loader for the current thread.
	 * 
	 * In the unusual case where the context class loader is null, the class loader that loaded this class ({@code Resources}) will be used instead.
	 * 
	 * @param resourceName
	 * @return Resource's URL
	 * @throws IllegalArgumentException if the resource is not found
	 */
	public static URL getResource(String resourceName) {
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		final ClassLoader loader = contextClassLoader == null ? Resources.class.getClassLoader() : contextClassLoader;
		final URL url = loader.getResource(resourceName);
		if (url == null) throw new IllegalArgumentException("resource " + resourceName + " not found.");
		return url;
	}
	
	/**
	 * Reads all bytes from a URL into a byte array.
	 * @param url The URL to read from
	 * @return A byte array containing all the bytes from the URL
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(URL url) throws IOException {
		try (InputStream inputStream = url.openStream()) {
			return IOUtils.toByteArray(inputStream);
		}
	}
}
