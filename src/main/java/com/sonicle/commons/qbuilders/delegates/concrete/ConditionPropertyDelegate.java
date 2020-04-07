/*
 *
 *  *  com.sonicle.commons.qbuilders.delegates.concrete.ConditionPropertyDelegate
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package com.sonicle.commons.qbuilders.delegates.concrete;

import com.sonicle.commons.qbuilders.nodes.OrNode;
import com.sonicle.commons.qbuilders.nodes.AndNode;
import com.sonicle.commons.qbuilders.nodes.LogicalNode;
import com.sonicle.commons.qbuilders.nodes.ComparisonNode;
import com.sonicle.commons.qbuilders.builders.QBuilder;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.qbuilders.delegates.virtual.PropertyDelegate;
import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.qbuilders.properties.concrete.ConditionProperty;
import com.sonicle.commons.qbuilders.structures.FieldPath;
import com.sonicle.commons.qbuilders.visitors.AbstractVoidContextNodeVisitor;

import java.util.Collections;

public final class ConditionPropertyDelegate<T extends QBuilder<T>, S extends QBuilder<S>>
        extends PropertyDelegate<T> implements ConditionProperty<T, S> {

    public ConditionPropertyDelegate(FieldPath field, T canonical) {
        super(field, canonical);
    }

    @Override
    public Condition<T> any(Condition<S> condition) {

        final LogicalNode root = ((ConditionDelegate) condition).getRootNode();

        // prepend this field to all of the fields in the subtree
        root.visit(new NamespacingVisitor(getField()));

        return condition(getField(), ComparisonOperator.SUB_CONDITION_ANY, Collections.singleton(condition));
    }

    private class NamespacingVisitor extends AbstractVoidContextNodeVisitor<Void> {

        private FieldPath parent;

        public NamespacingVisitor(FieldPath parent) {
            this.parent = parent;
        }

        @Override
        protected Void visit(AndNode node) {
            node.getChildren().stream().forEach(this::visitAny);
            return null;
        }

        @Override
        protected Void visit(OrNode node) {
            node.getChildren().stream().forEach(this::visitAny);
            return null;
        }

        @Override
        protected Void visit(ComparisonNode node) {
            node.setField(node.getField().prepend(parent));
            return null;
        }
    }

}
