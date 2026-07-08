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
package com.sonicle.commons.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.AbstractListValuedMap;

/**
 * @deprecated replace with ArrayListValuedLinkedHashMap from CommonsCollections 4.5
 * @author malbinola
 * @param <K>
 * @param <V>
 */
public class ArrayListValuedLinkedHashMap<K, V> extends AbstractListValuedMap<K, V> implements Serializable {
	/**
	 * Serialization Version
	 */
	private static final long serialVersionUID = 20241014L;

	/**
	 * The initial map capacity used when none specified in constructor.
	 */
	private static final int DEFAULT_INITIAL_MAP_CAPACITY = 16;

	/**
	 * The initial list capacity when using none specified in constructor.
	 */
	private static final int DEFAULT_INITIAL_LIST_CAPACITY = 3;

	/**
	 * The initial list capacity when creating a new value collection.
	 */
	private final int initialListCapacity;

	/**
	 * @deprecated replace with ArrayListValuedLinkedHashMap from CommonsCollections 4.5
	 * Creates an empty ArrayListValuedHashMap with the default initial map
	 * capacity (16) and the default initial list capacity (3).
	 */
	public ArrayListValuedLinkedHashMap() {
		this(DEFAULT_INITIAL_MAP_CAPACITY, DEFAULT_INITIAL_LIST_CAPACITY);
	}

	/**
	 * @deprecated replace with ArrayListValuedLinkedHashMap from CommonsCollections 4.5
	 * Creates an empty ArrayListValuedHashMap with the default initial map
	 * capacity (16) and the specified initial list capacity.
	 *
	 * @param initialListCapacity the initial capacity used for value
	 * collections
	 */
	public ArrayListValuedLinkedHashMap(final int initialListCapacity) {
		this(DEFAULT_INITIAL_MAP_CAPACITY, initialListCapacity);
	}

	/**
	 * @deprecated replace with ArrayListValuedLinkedHashMap from CommonsCollections 4.5
	 * Creates an empty ArrayListValuedHashMap with the specified initial map
	 * and list capacities.
	 *
	 * @param initialMapCapacity the initial hashmap capacity
	 * @param initialListCapacity the initial capacity used for value
	 * collections
	 */
	public ArrayListValuedLinkedHashMap(final int initialMapCapacity, final int initialListCapacity) {
		super(new LinkedHashMap<>(initialMapCapacity));
		this.initialListCapacity = initialListCapacity;
	}

	/**
	 * @deprecated replace with ArrayListValuedLinkedHashMap from CommonsCollections 4.5
	 * Creates an ArrayListValuedHashMap copying all the mappings of the given
	 * map.
	 *
	 * @param map a {@code Map} to copy into this map
	 */
	public ArrayListValuedLinkedHashMap(final Map<? extends K, ? extends V> map) {
		this(map.size(), DEFAULT_INITIAL_LIST_CAPACITY);
		super.putAll(map);
	}

	/**
	 * Creates an ArrayListValuedHashMap copying all the mappings of the given
	 * map.
	 *
	 * @param map a {@code MultiValuedMap} to copy into this map
	 */
	public ArrayListValuedLinkedHashMap(final MultiValuedMap<? extends K, ? extends V> map) {
		this(map.size(), DEFAULT_INITIAL_LIST_CAPACITY);
		super.putAll(map);
	}

	@Override
	protected ArrayList<V> createCollection() {
		return new ArrayList<>(initialListCapacity);
	}

	/**
	 * Deserializes an instance from an ObjectInputStream.
	 *
	 * @param in The source ObjectInputStream.
	 * @throws IOException Any of the usual Input/Output related exceptions.
	 * @throws ClassNotFoundException A class of a serialized object cannot be
	 * found.
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		setMap(new LinkedHashMap<>());
		doReadObject(in);
	}

	/**
	 * Trims the capacity of all value collections to their current size.
	 */
	public void trimToSize() {
		for (final Collection<V> coll : getMap().values()) {
			final ArrayList<V> list = (ArrayList<V>) coll;
			list.trimToSize();
		}
	}

	/**
	 * Serializes this object to an ObjectOutputStream.
	 *
	 * @param out the target ObjectOutputStream.
	 * @throws IOException thrown when an I/O errors occur writing to the target
	 * stream.
	 */
	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		doWriteObject(out);
	}
}
