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

import java.util.function.Supplier;

/**
 * Utility methods for implementing identity-based {@code equals} and
 * {@code hashCode} semantics on persistent entities.
 * <p>
 * This helper is designed for entities whose logical identity is determined
 * by a persistent identifier (typically a database primary key).
 * </p>
 *
 * <h3>Behavior</h3>
 * <ul>
 *   <li>If the identifier is non-null, equality and hash code are based on it.</li>
 *   <li>If the identifier is null (transient entity), instances are considered
 *       different unless they are the same object reference.</li>
 *   <li>Transient entities use {@link System#identityHashCode(Object)} in order
 *       to avoid collisions inside hash-based collections.</li>
 * </ul>
 *
 * <h3>Important note</h3>
 * <p>
 * Objects inserted into hash-based collections ({@link java.util.HashSet},
 * {@link java.util.HashMap}, etc.) should not change identifier while they
 * are contained in the collection, otherwise lookup semantics may break.
 * </p>
 * @author malbinola
 */
public final class IdentityEquality {
	
	private IdentityEquality() {}
	
	/**
	 * Computes the hash code for an identity-based entity.
	 * <p>
	 * Persistent entities (non-null identifier) use the identifier hash code,
	 * while transient entities use the object identity hash code.
	 * </p>
	 *
	 * @param self the current object instance
	 * @param id the persistent identifier
	 * @return the computed hash code
	 */
	public static int hashCode(final Object self, final Object id) {
		return (id == null) ? System.identityHashCode(self) : id.hashCode();
	}
	
	/**
	 * Evaluates equality between two identity-based entities.
	 * <p>
	 * Two entities are considered equal only if:
	 * </p>
	 * <ul>
	 *	<li>they are the same instance, or</li>
	 *	<li>they are of the same concrete class and both have a non-null
	 * identifier with the same value.</li>
	 * </ul>
	 *
	 * <p>
	 * Transient entities (null identifier) are never considered equal unless
	 * they are the exact same object reference.
	 * </p>
	 *
	 * @param self the current object instance
	 * @param other the object to compare against
	 * @param id the identifier of the current instance
	 * @param otherIdSupplier supplier used to lazily retrieve the identifier of
	 * the other instance
	 * @return {@code true} if the two entities are equal, otherwise
	 * {@code false}
	 */
	public static boolean equals(final Object self, final Object other, final Object id, final Supplier<Object> otherIdSupplier) {
		if (self == other) return true;
		if (other == null) return false;
		if (self.getClass() != other.getClass()) return false;
		if (id == null) return false;
		
		Object otherId = otherIdSupplier.get();
		if (otherId == null) return false;
		
		return id.equals(otherId);
	}
}
