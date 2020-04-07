/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.InstantPropertyDelegate
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
import com.sonicle.commons.qbuilders.delegates.virtual.InstantLikePropertyDelegate;
import com.sonicle.commons.qbuilders.properties.concrete.InstantProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.time.Instant;

public final class InstantPropertyDelegate<T extends QBuilder<T>>
        extends InstantLikePropertyDelegate<T, Instant> implements InstantProperty<T> {

    public InstantPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

    @Override
    protected Instant normalize(Instant instant) {
        return instant;
    }

}
