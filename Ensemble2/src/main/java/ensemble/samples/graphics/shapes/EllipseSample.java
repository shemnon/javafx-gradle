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
import javafx.scene.shape.Ellipse;

/**
 * A sample that demonstrates how various settings affect two elliptical shapes.
 *
 * @see javafx.scene.shape.Ellipse
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 * @related graphics/shapes/CircleSample
 * @related graphics/shapes/ArcSample
 */
public class EllipseSample extends Sample {

    public EllipseSample() {
        super(180,90);
        // Simple red filled ellipse
        Ellipse ellipse1 = new Ellipse(45,45,30,45);
        ellipse1.setFill(Color.RED);
        // Blue stroked ellipse
        Ellipse ellipse2 = new Ellipse(135,45,30,45);
        ellipse2.setStroke(Color.DODGERBLUE);
        ellipse2.setFill(null);
        // Create a group to show all the ellipses);
        getChildren().add(new Group(ellipse1,ellipse2));
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Ellipse 1 Fill", ellipse1.fillProperty()),
                new SimplePropertySheet.PropDesc("Ellipse 1 Width", ellipse1.radiusXProperty(), 10d, 40d),
                new SimplePropertySheet.PropDesc("Ellipse 1 Height", ellipse1.radiusYProperty(), 10d, 45d),
                new SimplePropertySheet.PropDesc("Ellipse 2 Stroke", ellipse2.strokeProperty()),
                new SimplePropertySheet.PropDesc("Ellipse 2 Stroke Width", ellipse2.strokeWidthProperty(), 1d, 5d),
                new SimplePropertySheet.PropDesc("Ellipse 2 Width", ellipse2.radiusXProperty(), 10d, 40d),
                new SimplePropertySheet.PropDesc("Ellipse 2 Height", ellipse2.radiusYProperty(), 10d, 45d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Ellipse ellipse = new Ellipse(57,57, 20,40);
        ellipse.setStroke(Color.web("#b9c0c5"));
        ellipse.setStrokeWidth(5);
        ellipse.getStrokeDashArray().addAll(15d,15d);
        ellipse.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        ellipse.setEffect(effect);
        return ellipse;
    }
    // END REMOVE ME
}