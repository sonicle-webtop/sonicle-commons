/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.ShortPropertyDelegate
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
import com.sonicle.commons.qbuilders.properties.concrete.ShortProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

public final class ShortPropertyDelegate<T extends QBuilder<T>>
        extends NumberPropertyDelegate<T, Short> implements ShortProperty<T> {

    public ShortPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

}
