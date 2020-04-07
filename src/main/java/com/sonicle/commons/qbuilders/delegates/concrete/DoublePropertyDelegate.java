/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.DoublePropertyDelegate
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
import com.sonicle.commons.qbuilders.delegates.virtual.NumberPropertyDelegate;
import com.sonicle.commons.qbuilders.properties.concrete.DoubleProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

public final class DoublePropertyDelegate<T extends QBuilder<T>> extends NumberPropertyDelegate<T, Double> implements DoubleProperty<T> {

    public DoublePropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

}
