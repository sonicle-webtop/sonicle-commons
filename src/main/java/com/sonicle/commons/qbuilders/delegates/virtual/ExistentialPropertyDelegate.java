/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.virtual.ExistentialPropertyDelegate
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
import com.sonicle.commons.qbuilders.properties.virtual.ExistentialProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.util.Collections;

public abstract class ExistentialPropertyDelegate<T extends QBuilder<T>> extends PropertyDelegate<T>
        implements ExistentialProperty<T> {

    protected ExistentialPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

    public final Condition<T> exists() {
        return condition(getField(), ComparisonOperator.EX, Collections.singletonList(true));
    }

    public final Condition<T> doesNotExist() {
        return condition(getField(), ComparisonOperator.EX, Collections.singletonList(false));
    }

}
