/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.LongPropertyDelegate
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
import com.sonicle.commons.qbuilders.properties.concrete.LongProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

public final class LongPropertyDelegate<T extends QBuilder<T>> extends NumberPropertyDelegate<T, Long> implements LongProperty<T> {

    public LongPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

}
