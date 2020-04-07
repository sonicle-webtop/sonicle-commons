/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.virtual.NumberPropertyDelegate
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
import com.sonicle.commons.qbuilders.properties.virtual.NumberProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;
import java.util.Arrays;

import java.util.Collections;

public abstract class NumberPropertyDelegate<T extends QBuilder<T>, S extends Number>
        extends ListablePropertyDelegate<T, S> implements NumberProperty<T, S> {

    protected NumberPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

	@Override
    public final Condition<T> gt(S number) {
        return condition(getField(), ComparisonOperator.GT, Collections.singletonList(number));
    }

	@Override
    public final Condition<T> lt(S number) {
        return condition(getField(), ComparisonOperator.LT, Collections.singletonList(number));
    }

	@Override
    public final Condition<T> gte(S number) {
        return condition(getField(), ComparisonOperator.GTE, Collections.singletonList(number));
    }

	@Override
    public final Condition<T> lte(S number) {
        return condition(getField(), ComparisonOperator.LTE, Collections.singletonList(number));
    }
	
	@Override
	public final Condition<T> btw(S number1, S number2) {
        return condition(getField(), ComparisonOperator.BTW, Collections.unmodifiableList(Arrays.asList(number1, number2)));
    }
	
	@Override
	public final Condition<T> nbtw(S number1, S number2) {
        return condition(getField(), ComparisonOperator.NBTW, Collections.unmodifiableList(Arrays.asList(number1, number2)));
    }
}
