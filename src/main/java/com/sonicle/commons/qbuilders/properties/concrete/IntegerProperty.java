/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.concrete.IntegerProperty
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package com.sonicle.commons.qbuilders.properties.concrete;

import com.sonicle.commons.qbuilders.builders.QBuilder;
import com.sonicle.commons.qbuilders.properties.virtual.NumberProperty;

/**
 * A property view for fields with {@link Integer} values.
 *
 * @param <T> The type of the final builder.
 */
public interface IntegerProperty<T extends QBuilder<T>> extends NumberProperty<T, Integer> {}
