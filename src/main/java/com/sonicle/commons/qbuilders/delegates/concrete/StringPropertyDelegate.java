/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.StringPropertyDelegate
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
import com.sonicle.commons.qbuilders.delegates.virtual.ListablePropertyDelegate;
import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.qbuilders.properties.concrete.StringProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.util.Collections;

public final class StringPropertyDelegate<T extends QBuilder<T>>
        extends ListablePropertyDelegate<T, String> implements StringProperty<T> {

    public StringPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

	@Override
    public final Condition<T> lexicallyAfter(String value) {
        return condition(getField(), ComparisonOperator.GT, Collections.singletonList(value));
    }

	@Override
    public final Condition<T> lexicallyBefore(String value) {
        return condition(getField(), ComparisonOperator.LT, Collections.singletonList(value));
    }

	@Override
    public final Condition<T> lexicallyNotAfter(String value) {
        return condition(getField(), ComparisonOperator.LTE, Collections.singletonList(value));
    }

	@Override
    public final Condition<T> lexicallyNotBefore(String value) {
        return condition(getField(), ComparisonOperator.GTE, Collections.singletonList(value));
    }

	@Override
    public Condition<T> pattern(String pattern) {
        return condition(getField(), ComparisonOperator.RE, Collections.singletonList(pattern));
    }

	@Override
	public Condition<T> like(String value) {
		return condition(getField(), ComparisonOperator.LIKE, Collections.singletonList(value));
	}

	@Override
	public Condition<T> notLike(String value) {
		return condition(getField(), ComparisonOperator.NLIKE, Collections.singletonList(value));
	}
}
