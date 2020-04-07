/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.virtual.EquitablePropertyDelegate
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package com.sonicle.commons.qbuilders.delegates.virtual;

import com.sonicle.commons.qbuilders.builders.QBuilder;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.qbuilders.properties.virtual.EquitableProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.util.Collections;

public abstract class EquitablePropertyDelegate<T extends QBuilder<T>, S>
        extends ExistentialPropertyDelegate<T> implements EquitableProperty<T,S> {

    protected EquitablePropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

    public final Condition<T> eq(S value) {
        if(value == null) {
            return condition(getField(), ComparisonOperator.EX, Collections.singleton(false));
        }
        return condition(getField(), ComparisonOperator.EQ, Collections.singletonList(value));
    }

    public final Condition<T> ne(S value) {
        if(value == null) {
            return condition(getField(), ComparisonOperator.EX, Collections.singleton(true));
        }
        return condition(getField(), ComparisonOperator.NE, Collections.singletonList(value));
    }

}
