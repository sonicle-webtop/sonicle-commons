/*
 * Copyright (C) 2026 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2026 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class providing runtime argument checks as a drop-in replacement for
 * {@code net.sf.qualitycheck.Check}.
 *
 * <p>
 * All methods mirror the original QualityCheck API:
 * <ul>
 * <li>They return the validated value, enabling fluent assignment:
 * {@code this.field = Check.notNull(value, "value")}</li>
 * <li>They throw the same exception types (or equivalents) on failure</li>
 * <li>They accept an optional {@code name}/{@code message} parameter used in
 * the error message</li>
 * </ul>
 *
 * <p>
 * Compatible with Java 8+.
 *
 * @author malbinola
 */
public final class Check {

	private Check() {
		// Utility class — not instantiable
	}

	// =========================================================================
	// notNull
	// =========================================================================
	/**
	 * Ensures that the given reference is not null.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNull(T reference)}
	 *
	 * @param <T> the type of the reference
	 * @param reference the object reference to validate
	 * @return {@code reference}, never null
	 * @throws IllegalNullArgumentException if {@code reference} is null
	 */
	@Nonnull
	public static <T> T notNull(@Nullable final T reference) {
		return notNull(reference, null);
	}

	/**
	 * Ensures that the given reference is not null.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNull(T reference, String name)}
	 *
	 * @param <T> the type of the reference
	 * @param reference the object reference to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code reference}, never null
	 * @throws IllegalNullArgumentException if {@code reference} is null
	 */
	@Nonnull
	public static <T> T notNull(@Nullable final T reference, @Nullable final String name) {
		if (reference == null) {
			throw new IllegalNullArgumentException(name);
		}
		return reference;
	}

	// =========================================================================
	// notEmpty — CharSequence
	// =========================================================================
	/**
	 * Ensures that the given {@link CharSequence} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T chars)}
	 *
	 * @param <T> a type extending {@link CharSequence}
	 * @param chars the character sequence to validate
	 * @return {@code chars}, never null or empty
	 * @throws IllegalNullArgumentException if {@code chars} is null
	 * @throws IllegalEmptyArgumentException if {@code chars} has length 0
	 */
	@Nonnull
	public static <T extends CharSequence> T notEmpty(@Nullable final T chars) {
		return notEmpty(chars, null);
	}

	/**
	 * Ensures that the given {@link CharSequence} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T chars, String name)}
	 *
	 * @param <T> a type extending {@link CharSequence}
	 * @param chars the character sequence to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code chars}, never null or empty
	 * @throws IllegalNullArgumentException if {@code chars} is null
	 * @throws IllegalEmptyArgumentException if {@code chars} has length 0
	 */
	@Nonnull
	public static <T extends CharSequence> T notEmpty(@Nullable final T chars, @Nullable final String name) {
		notNull(chars, name);
		if (chars.length() == 0) {
			throw new IllegalEmptyArgumentException(name);
		}
		return chars;
	}

	// =========================================================================
	// notEmpty — Collection
	// =========================================================================
	/**
	 * Ensures that the given {@link Collection} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T collection)}
	 *
	 * @param <T> a type extending {@link Collection}
	 * @param collection the collection to validate
	 * @return {@code collection}, never null or empty
	 * @throws IllegalNullArgumentException if {@code collection} is null
	 * @throws IllegalEmptyArgumentException if {@code collection} is empty
	 */
	@Nonnull
	public static <T extends Collection<?>> T notEmpty(@Nullable final T collection) {
		return notEmpty(collection, null);
	}

	/**
	 * Ensures that the given {@link Collection} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T collection, String name)}
	 *
	 * @param <T> a type extending {@link Collection}
	 * @param collection the collection to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code collection}, never null or empty
	 * @throws IllegalNullArgumentException if {@code collection} is null
	 * @throws IllegalEmptyArgumentException if {@code collection} is empty
	 */
	@Nonnull
	public static <T extends Collection<?>> T notEmpty(@Nullable final T collection, @Nullable final String name) {
		notNull(collection, name);
		if (collection.isEmpty()) {
			throw new IllegalEmptyArgumentException(name);
		}
		return collection;
	}

	// =========================================================================
	// notEmpty — Map
	// =========================================================================
	/**
	 * Ensures that the given {@link Map} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T map)}
	 *
	 * @param <T> a type extending {@link Map}
	 * @param map the map to validate
	 * @return {@code map}, never null or empty
	 * @throws IllegalNullArgumentException if {@code map} is null
	 * @throws IllegalEmptyArgumentException if {@code map} is empty
	 */
	@Nonnull
	public static <T extends Map<?, ?>> T notEmpty(@Nullable final T map) {
		return notEmpty(map, null);
	}

	/**
	 * Ensures that the given {@link Map} is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T map, String name)}
	 *
	 * @param <T> a type extending {@link Map}
	 * @param map the map to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code map}, never null or empty
	 * @throws IllegalNullArgumentException if {@code map} is null
	 * @throws IllegalEmptyArgumentException if {@code map} is empty
	 */
	@Nonnull
	public static <T extends Map<?, ?>> T notEmpty(@Nullable final T map, @Nullable final String name) {
		notNull(map, name);
		if (map.isEmpty()) {
			throw new IllegalEmptyArgumentException(name);
		}
		return map;
	}

	// =========================================================================
	// notEmpty — Array
	// =========================================================================
	/**
	 * Ensures that the given array is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T[] array)}
	 *
	 * @param <T> the component type of the array
	 * @param array the array to validate
	 * @return {@code array}, never null or empty
	 * @throws IllegalNullArgumentException if {@code array} is null
	 * @throws IllegalEmptyArgumentException if {@code array} has length 0
	 */
	@Nonnull
	public static <T> T[] notEmpty(@Nullable final T[] array) {
		return notEmpty(array, null);
	}

	/**
	 * Ensures that the given array is neither null nor empty.
	 *
	 * <p>
	 * Mirrors: {@code Check.notEmpty(T[] array, String name)}
	 *
	 * @param <T> the component type of the array
	 * @param array the array to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code array}, never null or empty
	 * @throws IllegalNullArgumentException if {@code array} is null
	 * @throws IllegalEmptyArgumentException if {@code array} has length 0
	 */
	@Nonnull
	public static <T> T[] notEmpty(@Nullable final T[] array, @Nullable final String name) {
		notNull(array, name);
		if (array.length == 0) {
			throw new IllegalEmptyArgumentException(name);
		}
		return array;
	}

	// =========================================================================
	// noNullElements — Iterable
	// =========================================================================
	/**
	 * Ensures that the given iterable is not null and contains no null
	 * elements.
	 *
	 * <p>
	 * Mirrors: {@code Check.noNullElements(T iterable)}
	 *
	 * @param <T> a type extending {@link Iterable}
	 * @param iterable the iterable to validate
	 * @return {@code iterable}, guaranteed non-null with no null elements
	 * @throws IllegalNullArgumentException if {@code iterable} itself is null
	 * @throws IllegalNullElementsException if any element inside is null
	 */
	@Nonnull
	public static <T extends Iterable<?>> T noNullElements(@Nullable final T iterable) {
		return noNullElements(iterable, null);
	}

	/**
	 * Ensures that the given iterable is not null and contains no null
	 * elements.
	 *
	 * <p>
	 * Mirrors: {@code Check.noNullElements(T iterable, String name)}
	 *
	 * @param <T> a type extending {@link Iterable}
	 * @param iterable the iterable to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code iterable}, guaranteed non-null with no null elements
	 * @throws IllegalNullArgumentException if {@code iterable} itself is null
	 * @throws IllegalNullElementsException if any element inside is null
	 */
	@Nonnull
	public static <T extends Iterable<?>> T noNullElements(@Nullable final T iterable, @Nullable final String name) {
		notNull(iterable, name);
		for (final Object element : iterable) {
			if (element == null) {
				throw new IllegalNullElementsException(name);
			}
		}
		return iterable;
	}

	// =========================================================================
	// noNullElements — Array
	// =========================================================================
	/**
	 * Ensures that the given array is not null and contains no null elements.
	 *
	 * <p>
	 * Mirrors: {@code Check.noNullElements(T[] array)}
	 *
	 * @param <T> the component type of the array
	 * @param array the array to validate
	 * @return {@code array}, guaranteed non-null with no null elements
	 * @throws IllegalNullArgumentException if {@code array} itself is null
	 * @throws IllegalNullElementsException if any element inside is null
	 */
	@Nonnull
	public static <T> T[] noNullElements(@Nullable final T[] array) {
		return noNullElements(array, null);
	}

	/**
	 * Ensures that the given array is not null and contains no null elements.
	 *
	 * <p>
	 * Mirrors: {@code Check.noNullElements(T[] array, String name)}
	 *
	 * @param <T> the component type of the array
	 * @param array the array to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code array}, guaranteed non-null with no null elements
	 * @throws IllegalNullArgumentException if {@code array} itself is null
	 * @throws IllegalNullElementsException if any element inside is null
	 */
	@Nonnull
	public static <T> T[] noNullElements(@Nullable final T[] array, @Nullable final String name) {
		notNull(array, name);
		for (final T element : array) {
			if (element == null) {
				throw new IllegalNullElementsException(name);
			}
		}
		return array;
	}

	// =========================================================================
	// greaterThan — Comparable
	// =========================================================================
	/**
	 * Ensures that {@code check} is strictly greater than {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) >= 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors: {@code Check.greaterThan(T expected, T check)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the lower-bound value (exclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotGreaterThanException if {@code check} is not greater
	 * than {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T greaterThan(@Nullable final T expected, @Nullable final T check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that {@code check} is strictly greater than {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) >= 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors: {@code Check.greaterThan(T expected, T check, String message)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the lower-bound value (exclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotGreaterThanException if {@code check} is not greater
	 * than {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T greaterThan(@Nullable final T expected, @Nullable final T check, @Nullable final String message) {
		notNull(expected, "expected");
		notNull(check, "check");
		if (expected.compareTo(check) >= 0) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	// =========================================================================
	// greaterThan — primitives (int, long, double, float, short, byte)
	// =========================================================================
	/**
	 * Ensures that the {@code int} value {@code check} is strictly greater than
	 * {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static int greaterThan(final int expected, final int check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code int} value {@code check} is strictly greater than
	 * {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static int greaterThan(final int expected, final int check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code long} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static long greaterThan(final long expected, final long check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code long} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static long greaterThan(final long expected, final long check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code double} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static double greaterThan(final double expected, final double check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code double} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static double greaterThan(final double expected, final double check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code float} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static float greaterThan(final float expected, final float check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code float} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static float greaterThan(final float expected, final float check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code short} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static short greaterThan(final short expected, final short check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code short} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static short greaterThan(final short expected, final short check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code byte} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static byte greaterThan(final byte expected, final byte check) {
		return greaterThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code byte} value {@code check} is strictly greater
	 * than {@code expected}.
	 *
	 * @param expected the lower-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly greater than {@code expected}
	 * @throws IllegalNotGreaterThanException if {@code check <= expected}
	 */
	public static byte greaterThan(final byte expected, final byte check, @Nullable final String message) {
		if (check <= expected) {
			throw new IllegalNotGreaterThanException(expected, check, message);
		}
		return check;
	}

	// =========================================================================
	// greaterOrEqualThan — Comparable
	// =========================================================================
	/**
	 * Ensures that {@code check} is greater than or equal to {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) > 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors: {@code Check.greaterOrEqualThan(T expected, T check)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the lower-bound value (inclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @return {@code check} if it is greater than or equal to {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotGreaterOrEqualThanException if {@code check} is less
	 * than {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T greaterOrEqualThan(@Nullable final T expected, @Nullable final T check) {
		return greaterOrEqualThan(expected, check, null);
	}

	/**
	 * Ensures that {@code check} is greater than or equal to {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) > 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors:
	 * {@code Check.greaterOrEqualThan(T expected, T check, String message)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the lower-bound value (inclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is greater than or equal to {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotGreaterOrEqualThanException if {@code check} is less
	 * than {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T greaterOrEqualThan(@Nullable final T expected, @Nullable final T check, @Nullable final String message) {
		notNull(expected, "expected");
		notNull(check, "check");
		if (expected.compareTo(check) > 0) {
			throw new IllegalNotGreaterOrEqualThanException(expected, check, message);
		}
		return check;
	}

	// =========================================================================
	// lesserThan — Comparable
	// =========================================================================
	/**
	 * Ensures that {@code check} is strictly less than {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) <= 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors: {@code Check.lesserThan(T expected, T check)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the upper-bound value (exclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotLesserThanException if {@code check} is not less than
	 * {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T lesserThan(@Nullable final T expected, @Nullable final T check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that {@code check} is strictly less than {@code expected}.
	 *
	 * <p>
	 * The comparison uses {@code expected.compareTo(check) <= 0}, mirroring the
	 * original QualityCheck semantics exactly.
	 *
	 * <p>
	 * Mirrors: {@code Check.lesserThan(T expected, T check, String message)}
	 *
	 * @param <T> a {@link Comparable} type
	 * @param expected the upper-bound value (exclusive); must not be null
	 * @param check the value to validate; must not be null
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalNotLesserThanException if {@code check} is not less than
	 * {@code expected}
	 */
	@Nonnull
	public static <T extends Comparable<T>> T lesserThan(@Nullable final T expected, @Nullable final T check, @Nullable final String message) {
		notNull(expected, "expected");
		notNull(check, "check");
		if (expected.compareTo(check) <= 0) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	// =========================================================================
	// lesserThan — primitives (int, long, double, float, short, byte)
	// =========================================================================
	/**
	 * Ensures that the {@code int} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static int lesserThan(final int expected, final int check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code int} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static int lesserThan(final int expected, final int check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code long} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static long lesserThan(final long expected, final long check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code long} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static long lesserThan(final long expected, final long check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code double} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static double lesserThan(final double expected, final double check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code double} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static double lesserThan(final double expected, final double check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code float} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static float lesserThan(final float expected, final float check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code float} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static float lesserThan(final float expected, final float check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code short} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static short lesserThan(final short expected, final short check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code short} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static short lesserThan(final short expected, final short check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	/**
	 * Ensures that the {@code byte} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static byte lesserThan(final byte expected, final byte check) {
		return lesserThan(expected, check, null);
	}

	/**
	 * Ensures that the {@code byte} value {@code check} is strictly less than
	 * {@code expected}.
	 *
	 * @param expected the upper-bound value (exclusive)
	 * @param check the value to validate
	 * @param message descriptive message passed to the exception if the check
	 * fails; may be null
	 * @return {@code check} if it is strictly less than {@code expected}
	 * @throws IllegalNotLesserThanException if {@code check >= expected}
	 */
	public static byte lesserThan(final byte expected, final byte check, @Nullable final String message) {
		if (check >= expected) {
			throw new IllegalNotLesserThanException(expected, check, message);
		}
		return check;
	}

	// =========================================================================
	// notNegative — guards value >= 0
	// =========================================================================
	/**
	 * Ensures that the given {@code int} value is not negative (i.e., >= 0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(int value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is >= 0
	 * @throws IllegalNegativeArgumentException if {@code value < 0}
	 */
	public static int notNegative(final int value) {
		return notNegative(value, null);
	}

	/**
	 * Ensures that the given {@code int} value is not negative (i.e., >= 0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(int value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is >= 0
	 * @throws IllegalNegativeArgumentException if {@code value < 0}
	 */
	public static int notNegative(final int value, @Nullable final String name) {
		if (value < 0) {
			throw new IllegalNegativeArgumentException(value, name);
		}
		return value;
	}

	/**
	 * Ensures that the given {@code long} value is not negative (i.e., >= 0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(long value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is >= 0
	 * @throws IllegalNegativeArgumentException if {@code value < 0}
	 */
	public static long notNegative(final long value) {
		return notNegative(value, null);
	}

	/**
	 * Ensures that the given {@code long} value is not negative (i.e., >= 0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(long value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is >= 0
	 * @throws IllegalNegativeArgumentException if {@code value < 0}
	 */
	public static long notNegative(final long value, @Nullable final String name) {
		if (value < 0L) {
			throw new IllegalNegativeArgumentException(value, name);
		}
		return value;
	}

	/**
	 * Ensures that the given {@code double} value is not negative (i.e., >=
	 * 0.0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(double value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is >= 0.0
	 * @throws IllegalNegativeArgumentException if {@code value < 0.0}
	 */
	public static double notNegative(final double value) {
		return notNegative(value, null);
	}

	/**
	 * Ensures that the given {@code double} value is not negative (i.e., >=
	 * 0.0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(double value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is >= 0.0
	 * @throws IllegalNegativeArgumentException if {@code value < 0.0}
	 */
	public static double notNegative(final double value, @Nullable final String name) {
		if (value < 0.0) {
			throw new IllegalNegativeArgumentException(value, name);
		}
		return value;
	}

	/**
	 * Ensures that the given {@code float} value is not negative (i.e., >=
	 * 0.0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(float value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is >= 0.0
	 * @throws IllegalNegativeArgumentException if {@code value < 0.0}
	 */
	public static float notNegative(final float value) {
		return notNegative(value, null);
	}

	/**
	 * Ensures that the given {@code float} value is not negative (i.e., >=
	 * 0.0).
	 *
	 * <p>
	 * Mirrors: {@code Check.notNegative(float value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is >= 0.0
	 * @throws IllegalNegativeArgumentException if {@code value < 0.0}
	 */
	public static float notNegative(final float value, @Nullable final String name) {
		if (value < 0.0f) {
			throw new IllegalNegativeArgumentException(value, name);
		}
		return value;
	}

	// =========================================================================
	// notNaN — guards against Double.NaN / Float.NaN
	// =========================================================================
	/**
	 * Ensures that the given {@code double} value is not NaN.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNaN(double value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is not {@link Double#NaN}
	 * @throws IllegalNaNArgumentException if {@code value} is
	 * {@link Double#NaN}
	 */
	public static double notNaN(final double value) {
		return notNaN(value, null);
	}

	/**
	 * Ensures that the given {@code double} value is not NaN.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNaN(double value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is not {@link Double#NaN}
	 * @throws IllegalNaNArgumentException if {@code value} is
	 * {@link Double#NaN}
	 */
	public static double notNaN(final double value, @Nullable final String name) {
		if (Double.isNaN(value)) {
			throw new IllegalNaNArgumentException(name);
		}
		return value;
	}

	/**
	 * Ensures that the given {@code float} value is not NaN.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNaN(float value)}
	 *
	 * @param value the value to validate
	 * @return {@code value} if it is not {@link Float#NaN}
	 * @throws IllegalNaNArgumentException if {@code value} is {@link Float#NaN}
	 */
	public static float notNaN(final float value) {
		return notNaN(value, null);
	}

	/**
	 * Ensures that the given {@code float} value is not NaN.
	 *
	 * <p>
	 * Mirrors: {@code Check.notNaN(float value, String name)}
	 *
	 * @param value the value to validate
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code value} if it is not {@link Float#NaN}
	 * @throws IllegalNaNArgumentException if {@code value} is {@link Float#NaN}
	 */
	public static float notNaN(final float value, @Nullable final String name) {
		if (Float.isNaN(value)) {
			throw new IllegalNaNArgumentException(name);
		}
		return value;
	}

	// =========================================================================
	// stateIsTrue — arbitrary boolean condition
	// =========================================================================
	/**
	 * Ensures that the given boolean expression is {@code true}.
	 *
	 * <p>
	 * Mirrors: {@code Check.stateIsTrue(boolean expression)}
	 *
	 * @param expression the condition that must be true to indicate a valid
	 * state
	 * @throws IllegalStateOfArgumentException if {@code expression} is false
	 */
	public static void stateIsTrue(final boolean expression) {
		stateIsTrue(expression, (String) null);
	}

	/**
	 * Ensures that the given boolean expression is {@code true}.
	 *
	 * <p>
	 * Mirrors:
	 * {@code Check.stateIsTrue(boolean expression, String description)}
	 *
	 * @param expression the condition that must be true to indicate a valid
	 * state
	 * @param description human-readable explanation of why the state must hold;
	 * may be null
	 * @throws IllegalStateOfArgumentException if {@code expression} is false
	 */
	public static void stateIsTrue(final boolean expression, @Nullable final String description) {
		if (!expression) {
			throw new IllegalStateOfArgumentException(description);
		}
	}

	/**
	 * Ensures that the given boolean expression is {@code true}, using a
	 * formatted message.
	 *
	 * <p>
	 * The message is built via {@link String#format(String, Object...)} and is
	 * only evaluated when the check actually fails.
	 *
	 * <p>
	 * Mirrors:
	 * {@code Check.stateIsTrue(boolean expression, String template, Object... args)}
	 *
	 * @param expression the condition that must be true to indicate a valid
	 * state
	 * @param descriptionTemplate a {@link String#format} format string
	 * explaining the expected state
	 * @param args arguments substituted into {@code descriptionTemplate}
	 * @throws IllegalStateOfArgumentException if {@code expression} is false
	 */
	public static void stateIsTrue(final boolean expression, @Nonnull final String descriptionTemplate, final Object... args) {
		if (!expression) {
			throw new IllegalStateOfArgumentException(String.format(descriptionTemplate, args));
		}
	}

	// =========================================================================
	// matchesPattern
	// =========================================================================
	/**
	 * Ensures that the given {@link CharSequence} fully matches the given regex
	 * {@link Pattern}.
	 *
	 * <p>
	 * Mirrors: {@code Check.matchesPattern(Pattern pattern, T chars)}
	 *
	 * @param <T> a type extending {@link CharSequence}
	 * @param pattern the compiled regex pattern the value must fully match
	 * @param chars the character sequence to validate; must not be null
	 * @return {@code chars} if it matches {@code pattern}
	 * @throws IllegalNullArgumentException if {@code chars} is null
	 * @throws IllegalPatternArgumentException if {@code chars} does not match
	 * {@code pattern}
	 */
	@Nonnull
	public static <T extends CharSequence> T matchesPattern(@Nonnull final Pattern pattern, @Nullable final T chars) {
		return matchesPattern(pattern, chars, null);
	}

	/**
	 * Ensures that the given {@link CharSequence} fully matches the given regex
	 * {@link Pattern}.
	 *
	 * <p>
	 * Mirrors:
	 * {@code Check.matchesPattern(Pattern pattern, T chars, String name)}
	 *
	 * @param <T> a type extending {@link CharSequence}
	 * @param pattern the compiled regex pattern the value must fully match
	 * @param chars the character sequence to validate; must not be null
	 * @param name the name of the argument used in the exception message; may
	 * be null
	 * @return {@code chars} if it matches {@code pattern}
	 * @throws IllegalNullArgumentException if {@code chars} is null
	 * @throws IllegalPatternArgumentException if {@code chars} does not match
	 * {@code pattern}
	 */
	@Nonnull
	public static <T extends CharSequence> T matchesPattern(@Nonnull final Pattern pattern, @Nullable final T chars, @Nullable final String name) {
		notNull(chars, name);
		if (!pattern.matcher(chars).matches()) {
			throw new IllegalPatternArgumentException(pattern, chars, name);
		}
		return chars;
	}

	// =========================================================================
	// positionIndex
	// =========================================================================
	/**
	 * Ensures that {@code index} is a valid position index within a sequence of
	 * length {@code size}, i.e., {@code 0 <= index <= size}.
	 *
	 * <p>
	 * Mirrors: {@code Check.positionIndex(int index, int size)}
	 *
	 * @param index the index to validate
	 * @param size the size of the array, list, or string the index refers to
	 * @return {@code index} if it is within the valid range [0, size]
	 * @throws IllegalPositionIndexException if {@code index < 0} or
	 * {@code index > size}
	 */
	public static int positionIndex(final int index, final int size) {
		if (index < 0 || index > size) {
			throw new IllegalPositionIndexException(index, size);
		}
		return index;
	}

	// =========================================================================
	// range
	// =========================================================================
	/**
	 * Ensures that {@code (start, end, size)} form a valid range.
	 *
	 * <p>
	 * A range is valid when all of the following hold:
	 * <ul>
	 * <li>{@code start >= 0}</li>
	 * <li>{@code end >= 0}</li>
	 * <li>{@code size >= 0}</li>
	 * <li>{@code start <= end}</li>
	 * <li>{@code start <= size}</li>
	 * <li>{@code end <= size}</li>
	 * </ul>
	 *
	 * <p>
	 * Mirrors: {@code Check.range(int start, int end, int size)}
	 *
	 * @param start the inclusive start of the range; must be >= 0
	 * @param end the inclusive end of the range; must be >= start
	 * @param size the total size of the sequence; must be >= end
	 * @throws IllegalRangeException if any of the conditions above is violated
	 */
	public static void range(final int start, final int end, final int size) {
		if (start < 0 || end < 0 || size < 0 || start > end || start > size || end > size) {
			throw new IllegalRangeException(start, end, size);
		}
	}

	// =========================================================================
	// contains
	// =========================================================================
	/**
	 * Ensures that {@code needle} is contained in {@code haystack}.
	 *
	 * <p>
	 * The check is implemented via {@link Collection#contains(Object)}.
	 * Particularly useful to verify that an enum value belongs to an
	 * {@code EnumSet}.
	 *
	 * <p>
	 * Mirrors: {@code Check.contains(Collection haystack, T needle)}
	 *
	 * @param <T> the element type
	 * @param haystack the collection that must contain {@code needle}; must not
	 * be null
	 * @param needle the value whose presence must be verified; must not be null
	 * @return {@code needle} if it is found in {@code haystack}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalArgumentNotContainedException if {@code needle} is not in
	 * {@code haystack}
	 */
	@Nonnull
	public static <T> T contains(@Nullable final Collection<T> haystack, @Nullable final T needle) {
		return contains(haystack, needle, null);
	}

	/**
	 * Ensures that {@code needle} is contained in {@code haystack}.
	 *
	 * <p>
	 * The check is implemented via {@link Collection#contains(Object)}.
	 * Particularly useful to verify that an enum value belongs to an
	 * {@code EnumSet}.
	 *
	 * <p>
	 * Mirrors:
	 * {@code Check.contains(Collection haystack, T needle, String name)}
	 *
	 * @param <T> the element type
	 * @param haystack the collection that must contain {@code needle}; must not
	 * be null
	 * @param needle the value whose presence must be verified; must not be null
	 * @param name the name of the {@code needle} argument used in the exception
	 * message; may be null
	 * @return {@code needle} if it is found in {@code haystack}
	 * @throws IllegalNullArgumentException if either argument is null
	 * @throws IllegalArgumentNotContainedException if {@code needle} is not in
	 * {@code haystack}
	 */
	@Nonnull
	public static <T> T contains(@Nullable final Collection<T> haystack, @Nullable final T needle, @Nullable final String name) {
		notNull(haystack, "haystack");
		notNull(needle, name);
		if (!haystack.contains(needle)) {
			throw new IllegalArgumentNotContainedException(needle, name);
		}
		return needle;
	}

	// =========================================================================
	// Exception types — mirroring QualityCheck's exception hierarchy
	// =========================================================================
	
	/**
	 * Base Exception class.
	 */
	public static class BaseIllegalArgumentException extends RuntimeException {

		public BaseIllegalArgumentException(final String message) {
			super(message);
		}
	}

	/**
	 * Thrown when a null argument is passed where a non-null value is required.
	 */
	public static final class IllegalNullArgumentException extends BaseIllegalArgumentException {

		public IllegalNullArgumentException(@Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' must not be null."
				: "An argument must not be null.");
		}
	}

	/**
	 * Thrown when an empty value (string, collection, map, array) is passed
	 * where a non-empty one is required.
	 */
	public static final class IllegalEmptyArgumentException extends BaseIllegalArgumentException {

		public IllegalEmptyArgumentException(@Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' must not be empty."
				: "An argument must not be empty.");
		}
	}

	/**
	 * Thrown when an iterable or array contains at least one null element.
	 */
	public static final class IllegalNullElementsException extends BaseIllegalArgumentException {

		public IllegalNullElementsException(@Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' must not contain null elements."
				: "An argument must not contain null elements.");
		}
	}

	/**
	 * Thrown when a value is not strictly greater than the expected lower
	 * bound.
	 */
	public static final class IllegalNotGreaterThanException extends BaseIllegalArgumentException {

		public IllegalNotGreaterThanException(final Object expected, final Object check, @Nullable final String message) {
			super(message != null
				? message
				: "The value '" + check + "' must be greater than '" + expected + "'.");
		}
	}

	/**
	 * Thrown when a value is not greater than or equal to the expected lower
	 * bound.
	 */
	public static final class IllegalNotGreaterOrEqualThanException extends BaseIllegalArgumentException {

		public IllegalNotGreaterOrEqualThanException(final Object expected, final Object check, @Nullable final String message) {
			super(message != null
				? message
				: "The value '" + check + "' must be greater than or equal to '" + expected + "'.");
		}
	}

	/**
	 * Thrown when a value is not strictly less than the expected upper bound.
	 */
	public static final class IllegalNotLesserThanException extends BaseIllegalArgumentException {

		public IllegalNotLesserThanException(final Object expected, final Object check, @Nullable final String message) {
			super(message != null
				? message
				: "The value '" + check + "' must be less than '" + expected + "'.");
		}
	}

	/**
	 * Thrown when a numeric value is negative where a value >= 0 is required.
	 */
	public static final class IllegalNegativeArgumentException extends BaseIllegalArgumentException {

		public IllegalNegativeArgumentException(final Number value, @Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' must not be negative, but was: " + value + "."
				: "An argument must not be negative, but was: " + value + ".");
		}
	}

	/**
	 * Thrown when a floating-point value is NaN where a real number is
	 * required.
	 */
	public static final class IllegalNaNArgumentException extends BaseIllegalArgumentException {

		public IllegalNaNArgumentException(@Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' must not be NaN."
				: "An argument must not be NaN.");
		}
	}

	/**
	 * Thrown when a boolean condition required to be true is false.
	 */
	public static final class IllegalStateOfArgumentException extends BaseIllegalArgumentException {

		public IllegalStateOfArgumentException(@Nullable final String description) {
			super(description != null
				? "An invalid state was detected: " + description
				: "An invalid state was detected.");
		}
	}

	/**
	 * Thrown when a {@link CharSequence} does not fully match the required
	 * regex pattern.
	 */
	public static final class IllegalPatternArgumentException extends BaseIllegalArgumentException {

		public IllegalPatternArgumentException(@Nonnull final Pattern pattern, @Nonnull final CharSequence value, @Nullable final String name) {
			super(name != null
				? "The argument '" + name + "' with value '" + value + "' does not match the pattern: " + pattern.pattern()
				: "The value '" + value + "' does not match the pattern: " + pattern.pattern());
		}
	}

	/**
	 * Thrown when a position index is outside the valid range [0, size].
	 */
	public static final class IllegalPositionIndexException extends BaseIllegalArgumentException {

		public IllegalPositionIndexException(final int index, final int size) {
			super("Position index " + index + " is out of range [0, " + size + "].");
		}
	}

	/**
	 * Thrown when start, end, and size do not satisfy
	 * {@code 0 <= start <= end <= size}.
	 */
	public static final class IllegalRangeException extends BaseIllegalArgumentException {

		public IllegalRangeException(final int start, final int end, final int size) {
			super("Invalid range: start=" + start + ", end=" + end + ", size=" + size + ". Must satisfy: 0 <= start <= end <= size.");
		}
	}

	/**
	 * Thrown when an element is expected to be present in a collection but is
	 * not.
	 */
	public static final class IllegalArgumentNotContainedException extends BaseIllegalArgumentException {

		public IllegalArgumentNotContainedException(@Nonnull final Object needle, @Nullable final String name) {
			super(name != null
					? "The argument '" + name + "' with value '" + needle + "' was not found in the collection."
					: "The value '" + needle + "' was not found in the collection.");
		}
	}
}
