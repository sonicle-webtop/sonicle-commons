/*
 * sonicle-commons is a helper library developed by Sonicle S.r.l.
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class PathUtils {
	
	public static boolean isFolder(String path) {
		return StringUtils.endsWith(path, "/");
	}
	
	public static boolean isRootFolder(String path) {
		return StringUtils.equals(path, "/");
	}
	
	/**
	 * Converts all separators to the Unix separator of forward slash.
	 * @param path The path to be changed, null ignored.
	 * @return The updated path
	*/
	public static String homogenizeSeparator(String path) {
		if(path == null) return null;
		return FilenameUtils.separatorsToUnix(path);
	}
	
	/**
	 * Ensures that passed path starts by separator (Unix forward slash).
	 * @param path The path to be changed, null ignored.
	 * @return The updated path
	 */
	public static String ensureBeginningSeparator(String path) {
		return ensureBeginningSeparator(path, false);
	}
	
	/**
	 * Ensures that passed path starts by separator (Unix forward slash).
	 * @param path The path to be changed, null ignored.
	 * @param homogenize True to homogenize separatore to the Unix style before checking.
	 * @return The updated path
	 */
	public static String ensureBeginningSeparator(String path, boolean homogenize) {
		if(path == null) return null;
		if(homogenize) path = homogenizeSeparator(path);
		return StringUtils.startsWith(path, "/") ? path : "/" + path;
	}
	
	/**
	 * Ensures that passed path is followed by separator (Unix forward slash).
	 * This method does not check if passed path is a file path; you
	 * should use this only within directory paths.
	 * @param path The path to be changed, null ignored.
	 * @return The updated path
	 */
	public static String ensureTrailingSeparator(String path) {
		return ensureTrailingSeparator(path, false);
	}
	
	/**
	 * Ensures that passed path is followed by separator (Unix forward slash).
	 * This method does not check if passed path is a file path; you
	 * should use this only within directory paths.
	 * @param path The path to be changed, null ignored.
	 * @param homogenize True to homogenize separatore to the Unix style before checking.
	 * @return The updated path
	 */
	public static String ensureTrailingSeparator(String path, boolean homogenize) {
		if(path == null) return null;
		if(homogenize) path = homogenizeSeparator(path);
		return StringUtils.endsWith(path, "/") ? path : path + "/";
	}
	
	public static String concatPaths(String path1, String path2) {
		return concatPaths(path1, path2, false);
	}
	
	public static String concatPaths(String path1, String path2, boolean homogenize) {
		if(path1 == null && path2 == null) return null;
		if(path1 == null) return path2;
		if(path2 == null) return path1;
		if(homogenize) {
			path1 = homogenizeSeparator(path1);
			path2 = homogenizeSeparator(path2);
		}
		path1 = ensureTrailingSeparator(path1, false);
		path2 = StringUtils.removeStart(path2, "/");
		return path1 + path2;
	}
	
	public static String concatPathParts(String... pathParts) {
		String path = pathParts[0];
		int i = 1;
		while(i < pathParts.length) {
			path = concatPaths(path, pathParts[i], false);
			i++;
		}
		return path;
	}
	
	public static String getFullParentPath(String path) {
		if(StringUtils.endsWith(path, "/")) {
			return FilenameUtils.getFullPath(StringUtils.removeEnd(path, "/"));
		} else {
			return FilenameUtils.getFullPath(path);
		}
	}
	
	public static String getFileName(String path) {
		if(isFolder(path)) {
			return FilenameUtils.getName(StringUtils.removeEnd(path, "/"));
		} else {
			return FilenameUtils.getName(path);
		}
	}
	
	public static String getFileExtension(String path) {
		if(isFolder(path)) {
			return "";
		} else {
			return FilenameUtils.getExtension(path);
		}
	}
	
	
}
