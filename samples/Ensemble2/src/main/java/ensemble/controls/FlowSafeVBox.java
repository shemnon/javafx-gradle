/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ensemble.controls;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * FlowSafeVBox, simple vbox that supports flow style children
 *
 */
public class FlowSafeVBox extends Pane {
    private double vgap = 8;
    @Override protected double computeMinWidth(double height) {
        double minWidth = 0;
        for(Node child:getChildren()) minWidth = Math.max(minWidth,child.minWidth(-1));
        return getPadding().getLeft() + minWidth + getPadding().getRight();
    }
    @Override protected double computeMinHeight(double width) {
        double minHeight = 0;
        for(Node child:getChildren()) minHeight += child.minWidth(-1) + vgap;
        if (!getChildren().isEmpty()) minHeight -= vgap;
        return getPadding().getTop() + minHeight + getPadding().getBottom();
    }
    @Override protected double computePrefWidth(double height) {
        double prefWidth = 0;
        for(Node child:getChildren())  prefWidth = Math.max(prefWidth,child.prefWidth(-1));
        return prefWidth;
    }
    @Override protected double computePrefHeight(double width) {
        double height = 0;
        for(Node child:getChildren()) height += vgap + child.prefHeight(width);
        if (!getChildren().isEmpty()) height -= vgap;
        return getPadding().getTop() + height + getPadding().getBottom();
    }
    @Override protected double computeMaxWidth(double height) { return Double.MAX_VALUE; }
    @Override protected double computeMaxHeight(double width) { return Double.MAX_VALUE; }
    @Override protected void layoutChildren() {
        double top = getPadding().getTop();
        double left = getPadding().getLeft();
        double right = getPadding().getRight();
        double w = getWidth() - left - right;
        double y = top;
        for(Node child:getChildren()) {
            double prefH = child.prefHeight(w);
            child.resizeRelocate(
                snapPosition(left),
                snapPosition(y),
                snapSize(w),
                snapSize(prefH)
            );
            y += vgap + prefH;
        }
    }
}
