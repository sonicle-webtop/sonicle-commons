/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.virtual.InstantLikePropertyDelegate
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
import com.sonicle.commons.qbuilders.properties.virtual.InstantLikeProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("unchecked")
public abstract class InstantLikePropertyDelegate<T extends QBuilder<T>, S>
		extends EquitablePropertyDelegate<T, S> implements InstantLikeProperty<T, S> {

	public InstantLikePropertyDelegate(FieldPath field, T canonical) {
		super(field, canonical);
	}

	@Override
	public final Condition<T> before(S dateTime, boolean exclusive) {
		return condition(getField(), exclusive ? ComparisonOperator.LT : ComparisonOperator.LTE,
				Collections.singletonList(normalize(dateTime)));
	}

	@Override
	public final Condition<T> after(S dateTime, boolean exclusive) {
		return condition(getField(), exclusive ? ComparisonOperator.GT : ComparisonOperator.GTE,
				Collections.singletonList(normalize(dateTime)));
	}

	/*
    @Override
    public final Condition<T> between(S after, boolean exclusiveAfter, S before, boolean exclusiveBefore) {
        Condition<T> afterCondition = new QBuilder().instant(getField().asKey()).after(after, exclusiveAfter);
        Condition<T> beforeCondition = new QBuilder().instant(getField().asKey()).before(before, exclusiveBefore);
        return and(afterCondition, beforeCondition);
    }
	 */
	@Override
	public final Condition<T> btw(S dateTime1, S dateTime2) {
		return condition(getField(), ComparisonOperator.BTW, Collections.unmodifiableList(Arrays.asList(normalize(dateTime1), normalize(dateTime2))));
	}

	@Override
	public final Condition<T> nbtw(S dateTime1, S dateTime2) {
		return condition(getField(), ComparisonOperator.NBTW, Collections.unmodifiableList(Arrays.asList(normalize(dateTime1), normalize(dateTime2))));
	}

	protected abstract Instant normalize(S dateTime);
}
