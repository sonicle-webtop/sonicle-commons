/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.EnumPropertyDelegate
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
import com.sonicle.commons.qbuilders.delegates.virtual.ListablePropertyDelegate;
import com.sonicle.commons.qbuilders.properties.concrete.EnumProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;

public final class EnumPropertyDelegate<T extends QBuilder<T>, S extends Enum<S>>
        extends ListablePropertyDelegate<T,S> implements EnumProperty<T,S> {

    public EnumPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

}
