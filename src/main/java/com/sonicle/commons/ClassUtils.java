/*
 * Copyright (C) 2022 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2022 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class ClassUtils {
	
	/**
	 * Converts a Package name to a path simply replacing dots with slashes.
	 * @param packageName The Package name to process.
	 * @return A relative path String
	 */
	public static String classPackageAsPath(final String packageName) {
		return StringUtils.lowerCase(StringUtils.replace(packageName, ".", "/"));
	}
	
	/**
	 * Converts a relative path into a Package name simply replacing slashes 
	 * with dots. Any beginning and trailing dots and slashed will be removed.
	 * @param relativePath A path.
	 * @return A Package String
	 */
	public static String pathAsClassPackage(final String relativePath) {
		String s = StringUtils.removeStart(relativePath, "./");
		s = StringUtils.removeStart(s, "/");
		s = StringUtils.removeEnd(s, "/");
		return StringUtils.lowerCase(StringUtils.replace(s, "/", "."));
	}
	
	/**
	 * Joins two package name parts into a String.
	 * @param packageName1 1st Package name.
	 * @param packageName2 2nd Package name.
	 * @return The joined Package name.
	 */
	public static String joinClassPackages(String packageName1, String packageName2) {
		return StringUtils.removeEnd(packageName1, ".") + "." + StringUtils.removeStart(packageName2, ".");
	}
	
	/**
	 * Build a className joining passed Package and Class name.
	 * @param packageName Class Package name.
	 * @param classSimpleName Class simple name.
	 * @return A fully-qualified className
	 */
	public static String buildClassName(final String packageName, final String classSimpleName) {
		return StringUtils.removeEnd(packageName, ".") + "." + StringUtils.removeStart(classSimpleName, ".");
	}
	
	/**
	 * Gets the Class name of the specified Class. This will not work for
	 * anonymous classes due to getCanonicalName is null for that kind of Objects.
	 * @param clazz The Class for which get the name.
	 * @return The Class name
	 */
	public static String getSimpleClassName(final Class clazz) {
		Check.notNull(clazz, "clazz");
		return getSimpleClassName(clazz.getCanonicalName());
	}
	
	/**
	 * Extracts the Class name from a fully-qualified className: the part 
	 * after the last dot in the passed String. This will return the passed 
	 * String itself if no dot is found.
	 * @param fullyQualifiedClassName
	 * @return The Class name
	 */
	public static String getSimpleClassName(final String fullyQualifiedClassName) {
		int lastDot = StringUtils.lastIndexOf(fullyQualifiedClassName, ".");
		if (lastDot < 0) return fullyQualifiedClassName;
		return StringUtils.substring(fullyQualifiedClassName, lastDot+1);
	}
	
	/**
	 * Gets the Package name of the specified Class. This will not work for
	 * anonymous classes due to getCanonicalName is null for that kind of Objects.
	 * @param clazz The Class for which get the package.
	 * @return The package name
	 */
	public static String getClassPackageName(final Class clazz) {
		Check.notNull(clazz, "clazz");
		return getClassPackageName(clazz.getCanonicalName());
	}
	
	/**
	 * Extracts the Package name from a fully-qualified className: the part 
	 * before the last dot in the passed String. This will return the passed 
	 * String itself if no dot is found.
	 * @param fullyQualifiedClassName A fully-qualified className.
	 * @return The Package name
	 */
	public static String getClassPackageName(final String fullyQualifiedClassName) {
		int lastDot = StringUtils.lastIndexOf(fullyQualifiedClassName, ".");
		if (lastDot < 0) return fullyQualifiedClassName;
		return StringUtils.substring(fullyQualifiedClassName, 0, lastDot);
	}
	
	/**
	 * Return the class loader which loaded the class passed as argument; 
	 * otherwise it will return the system one.
	 * @param clazz The Class in which look into.
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoaderOf(final Class<?> clazz) {
		Check.notNull(clazz, "clazz");
		ClassLoader cl = clazz.getClassLoader();
		return (cl == null) ? ClassLoader.getSystemClassLoader() : clazz.getClassLoader();
	}
	
	/**
	 * Checks if a Class is already loaded in specified ClassLoader instance.
	 * @param classLoader The ClassLoader instance to use.
	 * @param className The Class name to check.
	 * @return `true` if Class is already loaded, `false` otherwise
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException 
	 */
	public static boolean isClassLoaded(final ClassLoader classLoader, final String className) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[]{String.class});
		method.setAccessible(true);
		return method.invoke(classLoader, className) != null;
	}
	
	public static Class<?> classForNameQuietly(final ClassLoader classLoader, final String className) {
		try {
			return Class.forName(className, true, classLoader);
		} catch(ClassNotFoundException ex) {
			return null;
		}
	}
	
	/**
	 * Returns `true` if a Class inherits from specified parent.
	 * @param clazz The Class under investigations.
	 * @param parentClass Parent Class to check. 
	 * @return 
	 */
	public static boolean isClassInheritingFromParent(final Class clazz, final Class parentClass) {
		if (clazz == null) return false;
		if (parentClass == null) return false;
		return parentClass.isAssignableFrom(clazz);
	}
	
	/**
	 * Returns `true` if a Class implements the specified interface Class.
	 * @param clazz The Class under investigations.
	 * @param interfaceClass Interface Class to check. 
	 * @return 
	 */
	public static boolean isClassImplementingInterface(final Class clazz, final Class interfaceClass) {
		if (clazz == null) return false;
		if (interfaceClass == null) return false;
		return interfaceClass.isAssignableFrom(clazz);
	}
	
	/**
	 * Returns `true` if the Object is an instance of the specified Class.
	 * @param object The instance under investigations.
	 * @param clazz The Class under investigations.
	 * @return 
	 */
	public static boolean isInstanceOf(final Object object, final Class clazz) {
		if (object == null) return false;
		if (clazz == null) return false;
		return clazz.getClass().isInstance(object);
	}
	
	/**
	 * Returns `true` if the Object has the specified type.
	 * @param object The instance under investigations.
	 * @param type The Class type required.
	 * @return 
	 */
	public static boolean hasStrictlyType(final Object object, final Class type) {
		if (object == null) return false;
		return hasStrictlyType(object.getClass(), type);
	}
	
	/**
	 * Returns `true` if the Class has the specified type.
	 * @param clazz The Class under investigations.
	 * @param type The Class type required.
	 * @return 
	 */
	public static boolean hasStrictlyType(final Class clazz, final Class type) {
		if (clazz == null) return false;
		if (type == null) return false;
		// The checks described here (https://stackoverflow.com/questions/16688655/check-if-an-object-is-an-instance-of-a-class-but-not-an-instance-of-its-subclas)
		// are applicable: comparing classes with equals only work if equals() method is overridden in both classes.
		// So, for now we simply check class names if the strict comparison in requested
		return isClassInheritingFromParent(clazz, type) && getSimpleClassName(clazz).equals(getSimpleClassName(type));
	}
}
