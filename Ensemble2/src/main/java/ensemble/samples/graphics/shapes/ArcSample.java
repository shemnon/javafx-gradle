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
package ensemble.samples.graphics.shapes;

import ensemble.Sample;
import ensemble.controls.SimplePropertySheet;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;

/**
 * A sample showing two arcs with controls to change various settings.
 *
 * @see javafx.scene.shape.Arc
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 * @related graphics/shapes/CircleSample
 * @related graphics/shapes/EllipseSample
 */
public class ArcSample extends Sample {

    public ArcSample() {
        super(180,90);
        // Simple red filled arc
        Arc arc1 = new Arc(45,60,45,45,40,100);
        arc1.setFill(Color.RED);
        // Blue stroked arc
        Arc arc2 = new Arc(155,60,45,45,40,100);
        arc2.setStroke(Color.DODGERBLUE);
        arc2.setFill(null);
        // Create a group to show all the arcs);
        getChildren().add(new Group(arc1,arc2));
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Arc 1 Fill", arc1.fillProperty()),
                new SimplePropertySheet.PropDesc("Arc 1 Start Angle", arc1.startAngleProperty(), 0d, 360d),
                new SimplePropertySheet.PropDesc("Arc 1 Length", arc1.lengthProperty(), 0d, 360d),
                new SimplePropertySheet.PropDesc("Arc 2 Stroke", arc2.strokeProperty()),
                new SimplePropertySheet.PropDesc("Arc 2 Stroke Width", arc2.strokeWidthProperty(), 1d, 5d),
                new SimplePropertySheet.PropDesc("Arc 2 Radius X", arc2.radiusXProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Arc 2 Radius Y", arc2.radiusYProperty(), 0d, 50d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Arc arc = new Arc(57,57,45,45,40,100);
        arc.setStroke(Color.web("#b9c0c5"));
        arc.setStrokeWidth(5);
        arc.getStrokeDashArray().addAll(15d,15d);
        arc.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        arc.setEffect(effect);
        return arc;
    }
    // END REMOVE ME
}