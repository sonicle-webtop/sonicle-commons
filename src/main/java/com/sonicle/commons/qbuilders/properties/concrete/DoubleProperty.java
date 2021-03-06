/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.concrete.DoubleProperty
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
 * A property view for fields with {@link Double} values.
 *
 * @param <T> The type of the final builder.
 */
public interface DoubleProperty<T extends QBuilder<T>> extends NumberProperty<T, Double> {}
