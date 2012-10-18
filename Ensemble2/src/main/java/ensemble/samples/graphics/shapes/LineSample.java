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
import javafx.scene.shape.Line;

/**
 * A sample that demonstrates how various settings affect a line shape.
 *
 * @see javafx.scene.shape.Line
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 */
public class LineSample extends Sample {

    public LineSample() {
        super(180,90);
        // Create line shape
        Line line = new Line(5, 85, 175 , 5);
        line.setFill(null);
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);

        // show the line shape;
        getChildren().add(line);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Line Stroke", line.strokeProperty()),
                new SimplePropertySheet.PropDesc("Line Start X", line.startXProperty(), 0d, 170d),
                new SimplePropertySheet.PropDesc("Line Start Y", line.startYProperty(), 0d, 90d),
                new SimplePropertySheet.PropDesc("Line End X", line.endXProperty(), 10d, 180d),
                new SimplePropertySheet.PropDesc("Line End Y", line.endYProperty(), 0d, 90d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Line line = new Line(0, 0, 70, 70);
        line.setStroke(Color.web("#b9c0c5"));
        line.setStrokeWidth(5);
        line.getStrokeDashArray().addAll(15d,15d);
        line.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        line.setEffect(effect);
        return line;
    }
    // END REMOVE ME
}