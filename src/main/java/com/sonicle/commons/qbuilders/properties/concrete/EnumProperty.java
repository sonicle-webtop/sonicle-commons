/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.concrete.EnumProperty
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
import com.sonicle.commons.qbuilders.properties.virtual.EquitableProperty;
import com.sonicle.commons.qbuilders.properties.virtual.ListableProperty;

/**
 * A property view for fields with {@link Enum} values.
 *
 * @param <T> The type of the final builder.
 */
public interface EnumProperty<T extends QBuilder<T>, S extends Enum<S>>
        extends ListableProperty<T,S>, EquitableProperty<T, S> {}
