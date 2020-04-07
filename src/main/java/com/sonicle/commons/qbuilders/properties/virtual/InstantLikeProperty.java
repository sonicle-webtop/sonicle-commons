/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.virtual.InstantLikeProperty
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */
package com.sonicle.commons.qbuilders.properties.virtual;

import com.sonicle.commons.qbuilders.builders.QBuilder;
import com.sonicle.commons.qbuilders.conditions.Condition;

/**
 * For date-like properties
 *
 * @param <T> The final type of the builder.
 * @param <S> The type of the date that the property supports.
 */
public interface InstantLikeProperty<T extends QBuilder<T>, S> extends EquitableProperty<T, S> {

	/**
	 * Mandates that the date-like field must be before the provided date
	 *
	 * @param dateTime The date-like representation that it should occur before.
	 * @param exclusive True if the query should not match the provided date,
	 * false if it should.
	 * @return The logically complete condition.
	 */
	Condition<T> before(S dateTime, boolean exclusive);

	/**
	 * Mandates that the date-like field must be after the provided date
	 *
	 * @param dateTime The date-like representation that it should occur after.
	 * @param exclusive True if the query should not match the provided date,
	 * false if it should.
	 * @return The logically complete condition.
	 */
	Condition<T> after(S dateTime, boolean exclusive);

	/**
	 * Mandates that the date-like field must be within the provided range.
	 *
	 * @param after The date-like representation that it should occur after.
	 * @param exclusiveAfter True if the query should not match the provided
	 * date, false if it should.
	 * @param before The date-like representation that it should occur before.
	 * @param exclusiveBefore True if the query should not match the provided
	 * date, false if it should.
	 * @return The logically complete condition.
	 */
	//Condition<T> between(S after, boolean exclusiveAfter, S before, boolean exclusiveBefore);
	
	/**
	 * Specifies that the date-like field must be between the provided range (inclusive).
	 * @param dateTime1 The date-like (inclusive) representation that it should
	 * occur after.
	 * @param dateTime2 The date-like (inclusive) representation that it should
	 * occur before.
	 * @return The logically complete condition.
	 */
	Condition<T> btw(S dateTime1, S dateTime2);

	/**
	 * Specifies that the date-like field must NOT be between the provided range.
	 * @param dateTime1 The date-like representation that it should
	 * occur before.
	 * @param dateTime2 The date-like representation that it should
	 * occur after.
	 * @return The logically complete condition.
	 */
	Condition<T> nbtw(S dateTime1, S dateTime2);
}
