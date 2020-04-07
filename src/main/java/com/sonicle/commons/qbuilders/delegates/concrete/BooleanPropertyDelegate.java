/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.BooleanPropertyDelegate
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package com.sonicle.commons.qbuilders.delegates.concrete;

import com.sonicle.commons.qbuilders.builders.QBuilder;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.qbuilders.delegates.virtual.ExistentialPropertyDelegate;
import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.qbuilders.properties.concrete.BooleanProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.util.Collections;

public final class BooleanPropertyDelegate<T extends QBuilder<T>> extends ExistentialPropertyDelegate<T> implements BooleanProperty<T> {

    public BooleanPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

    public final Condition<T> isTrue() {
        return condition(getField(), ComparisonOperator.EQ, Collections.singletonList(true));
    }

    public final Condition<T> isFalse() {
        return condition(getField(), ComparisonOperator.EQ, Collections.singletonList(false));
    }

}
