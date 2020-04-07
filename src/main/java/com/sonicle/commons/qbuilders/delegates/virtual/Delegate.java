/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.virtual.Delegate
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

public abstract class Delegate<T extends QBuilder<T>> extends QBuilder<T> {

    private T canonical;

    protected Delegate(T canonical) {
        this.canonical = canonical;
    }

    @Override
    protected final T self() {
        return canonical;
    }

}
