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
import javafx.scene.shape.Polyline;

/**
 * A sample that demonstrates polyline construction with stroke and fill.
 *
 * @see javafx.scene.shape.Polyline
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 */
public class PolylineSample extends Sample {

    public PolylineSample() {
        super(180,90);
        // Red stroked not closed triangle
        Polyline polyline1 = new Polyline(new double[]{
            45, 10,
            10, 80,
            80, 80,
        });
        polyline1.setFill(Color.TRANSPARENT);
        polyline1.setStroke(Color.RED);

        // Blue stroked closed triangle
        Polyline polyline2 = new Polyline(new double[]{
            135, 10,
            100, 80,
            170, 80,
            135, 10,
        });
        polyline2.setStroke(Color.DODGERBLUE);
        polyline2.setStrokeWidth(2);
        polyline2.setFill(null);

        
        // Create a group to show all the polylines);
        getChildren().add(new Group(polyline1, polyline2));
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Polyline 1 Fill", polyline1.fillProperty()),
                new SimplePropertySheet.PropDesc("Polyline 1 Stroke", polyline1.strokeProperty()),
                new SimplePropertySheet.PropDesc("Polyline 2 Stroke", polyline2.strokeProperty())
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Polyline polyline = new Polyline(new double[]{
            45, 10,
            10, 80,
            80, 80,
        });
        polyline.setStroke(Color.web("#b9c0c5"));
        polyline.setStrokeWidth(5);
        polyline.getStrokeDashArray().addAll(15d,15d);
        polyline.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        polyline.setEffect(effect);
        return polyline;
    }
    // END REMOVE ME
}