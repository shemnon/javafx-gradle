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
import javafx.scene.shape.Circle;

/**
 * A sample showing two circles with controls to change fill, stroke, and radius.
 *
 * @see javafx.scene.shape.Circle
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 * @related graphics/shapes/ArcSample
 * @related graphics/shapes/EllipseSample
 */
public class CircleSample extends Sample {

    public CircleSample() {
        super(180,90);
        // Simple red filled circle
        Circle circle1 = new Circle(45,45,40, Color.RED);
        // Blue stroked circle
        Circle circle2 = new Circle(135,45,40);
        circle2.setStroke(Color.DODGERBLUE);
        circle2.setFill(null);
        // Create a group to show all the circles);
        getChildren().add(new Group(circle1,circle2));
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Circle 1 Fill", circle1.fillProperty()),
                new SimplePropertySheet.PropDesc("Circle 1 Radius", circle1.radiusProperty(), 10d, 40d),
                new SimplePropertySheet.PropDesc("Circle 2 Stroke", circle2.strokeProperty()),
                new SimplePropertySheet.PropDesc("Circle 2 Stroke Width", circle2.strokeWidthProperty(), 1d, 5d),
                new SimplePropertySheet.PropDesc("Circle 2 Radius", circle2.radiusProperty(), 10d, 40d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Circle circle = new Circle(57,57,40);
        circle.setStroke(Color.web("#b9c0c5"));
        circle.setStrokeWidth(5);
        circle.getStrokeDashArray().addAll(15d,15d);
        circle.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        circle.setEffect(effect);
        return circle;
    }
    // END REMOVE ME
}