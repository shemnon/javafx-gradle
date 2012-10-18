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
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/**
 * An example of how various settings affect a quadratic Bézier parametric curve.
 *
 * @see javafx.scene.shape.QuadCurve
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 */
public class QuadCurveSample extends Sample {

    public QuadCurveSample() {
        super(180,90);
        // Create quadCurve shape
        QuadCurve quadCurve = new QuadCurve();
        quadCurve.setStartX(0);
        quadCurve.setStartY(45);
        quadCurve.setControlX(50);
        quadCurve.setControlY(10);
        quadCurve.setEndX(180);
        quadCurve.setEndY(45);
        quadCurve.setStroke(Color.RED);
        quadCurve.setFill(Color.ROSYBROWN);
        quadCurve.setStrokeWidth(2d);

        // show the quadCurve shape;
        getChildren().add(quadCurve);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Cubic Curve Fill", quadCurve.fillProperty()),
                new SimplePropertySheet.PropDesc("Cubic Curve Stroke", quadCurve.strokeProperty()),
                new SimplePropertySheet.PropDesc("Cubic Curve Start X", quadCurve.startXProperty(), 0d, 170d),
                new SimplePropertySheet.PropDesc("Cubic Curve Start Y", quadCurve.startYProperty(), 10d, 80d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control X1", quadCurve.controlXProperty(), 0d, 180d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control Y1", quadCurve.controlYProperty(), 0d, 90d),
                new SimplePropertySheet.PropDesc("Cubic Curve End X", quadCurve.endXProperty(), 10d, 180d),
                new SimplePropertySheet.PropDesc("Cubic Curve End Y", quadCurve.endYProperty(), 10d, 80d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        QuadCurve quadCurve = new QuadCurve();
        quadCurve.setStartX(0);
        quadCurve.setStartY(50);
        quadCurve.setControlX(20);
        quadCurve.setControlY(0);
        quadCurve.setEndX(80);
        quadCurve.setEndY(50);
        quadCurve.setStroke(Color.web("#b9c0c5"));
        quadCurve.setStrokeWidth(5);
        quadCurve.getStrokeDashArray().addAll(15d,15d);
        quadCurve.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        quadCurve.setEffect(effect);
        return quadCurve;
    }
    // END REMOVE ME
}