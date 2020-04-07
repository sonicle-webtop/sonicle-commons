/*
 *
 *  *  com.sonicle.commons.qbuilders.properties.virtual.NumberProperty
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
 * For numerical fields.
 *
 * @param <T> The final type of the builder.
 * @param <S> The type of the numbers that the property supports.
 */
public interface NumberProperty<T extends QBuilder<T>, S extends Number>
        extends ListableProperty<T, S>, EquitableProperty<T, S> {

    /**
     * Specifies that the field's value must be greater than the provided value.
     * @param number The value that the field's value must be greater than.
     * @return The logically complete condition.
     */
    Condition<T> gt(S number);

    /**
     * Specifies that the field's value must be less than the provided value.
     * @param number The value that the field's value must be less than.
     * @return The logically complete condition.
     */
    Condition<T> lt(S number);

    /**
     * Specifies that the field's value must be greater than or equal to the provided value.
     * @param number The value that the field's value must be greater than or equal to.
     * @return The logically complete condition.
     */
    Condition<T> gte(S number);

    /**
     * Specifies that the field's value must be less than or equal to the provided value.
     * @param number The value that the field's value must be less than or equal to.
     * @return The logically complete condition.
     */
    Condition<T> lte(S number);
	
	/**
	 * Specifies that the field's value must be between the provided range (inclusive).
	 * @param number1 The value that the field's value must be greater than or equal to.
	 * @param number2 The value that the field's value must be less than or equal to.
	 * @return The logically complete condition.
	 */
	Condition<T> btw(S number1, S number2);
	
	/**
	 * Specifies that the field's value must NOT be between the provided range.
	 * @param number1 The value that the field's value must be less than.
	 * @param number2 The value that the field's value must be greater than.
	 * @return The logically complete condition.
	 */
	Condition<T> nbtw(S number1, S number2);
}
