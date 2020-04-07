/*
 *
 *  *  com.sonicle.commons.qbuilders.nodes.ComparisonNode
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package com.sonicle.commons.qbuilders.nodes;

import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.qbuilders.structures.FieldPath;

import java.util.Collection;

public final class ComparisonNode extends AbstractNode {

    private FieldPath path;
    private ComparisonOperator operator;
    private Collection<?> values;

    public ComparisonNode(LogicalNode parent) {
        super(parent);
    }

    public FieldPath getField() {
        return path;
    }

    public void setField(FieldPath field) {
        this.path = field;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }

    public Collection<?> getValues() {
        return values;
    }

    public void setValues(Collection<?> values) {
        this.values = values;
    }
}
