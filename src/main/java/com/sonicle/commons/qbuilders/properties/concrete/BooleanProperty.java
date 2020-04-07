/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.concrete.BooleanProperty
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
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.qbuilders.properties.virtual.ExistentialProperty;

/**
 * A property view for fields with {@link Boolean} values.
 *
 * @param <T> The type of the final builder.
 */
public interface BooleanProperty<T extends QBuilder<T>> extends ExistentialProperty<T> {

    /**
     * Mandates that the boolean field must be true to match the query.
     * @return The logically complete condition.
     */
    Condition<T> isTrue();

    /**
     * Mandates that the boolean field must be false to match the query.
     * @return The logically complete condition.
     */
    Condition<T> isFalse();

}
