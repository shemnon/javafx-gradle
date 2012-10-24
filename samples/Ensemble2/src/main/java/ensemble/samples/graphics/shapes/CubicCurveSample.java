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
import javafx.scene.shape.CubicCurve;

/**
 * A sample showing how various settings change a cubic Bézier parametric curve.
 *
 * @see javafx.scene.shape.CubicCurve
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 */
public class CubicCurveSample extends Sample {

    public CubicCurveSample() {
        super(180,90);
        // Create cubicCurve shape
        CubicCurve cubicCurve = new CubicCurve();
        cubicCurve.setStartX(0);
        cubicCurve.setStartY(45);
        cubicCurve.setControlX1(30);
        cubicCurve.setControlY1(10);
        cubicCurve.setControlX2(150);
        cubicCurve.setControlY2(80);
        cubicCurve.setEndX(180);
        cubicCurve.setEndY(45);
        cubicCurve.setStroke(Color.RED);
        cubicCurve.setFill(Color.ROSYBROWN);
        cubicCurve.setStrokeWidth(2d);

        // show the cubicCurve shape;
        getChildren().add(cubicCurve);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Cubic Curve Fill", cubicCurve.fillProperty()),
                new SimplePropertySheet.PropDesc("Cubic Curve Stroke", cubicCurve.strokeProperty()),
                new SimplePropertySheet.PropDesc("Cubic Curve Start X", cubicCurve.startXProperty(), 0d, 170d),
                new SimplePropertySheet.PropDesc("Cubic Curve Start Y", cubicCurve.startYProperty(), 10d, 80d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control X1", cubicCurve.controlX1Property(), 0d, 180d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control Y1", cubicCurve.controlY1Property(), 0d, 90d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control X2", cubicCurve.controlX2Property(), 0d, 180d),
                new SimplePropertySheet.PropDesc("Cubic Curve Control Y2", cubicCurve.controlY2Property(), 0d, 90d),
                new SimplePropertySheet.PropDesc("Cubic Curve End X", cubicCurve.endXProperty(), 10d, 180d),
                new SimplePropertySheet.PropDesc("Cubic Curve End Y", cubicCurve.endYProperty(), 10d, 80d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        CubicCurve cubicCurve = new CubicCurve();
        cubicCurve.setStartX(0);
        cubicCurve.setStartY(50);
        cubicCurve.setControlX1(20);
        cubicCurve.setControlY1(0);
        cubicCurve.setControlX2(70);
        cubicCurve.setControlY2(100);
        cubicCurve.setEndX(80);
        cubicCurve.setEndY(50);
        cubicCurve.setStroke(Color.web("#b9c0c5"));
        cubicCurve.setStrokeWidth(5);
        cubicCurve.getStrokeDashArray().addAll(15d,15d);
        cubicCurve.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        cubicCurve.setEffect(effect);
        return cubicCurve;
    }
    // END REMOVE ME
}