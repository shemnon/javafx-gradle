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
import javafx.scene.shape.Rectangle;

/**
 * A sample showing how various settings effect two rectangles.
 *
 * @see javafx.scene.shape.Rectangle
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 */
public class RectangleSample extends Sample {

    public RectangleSample() {
        super(180,90);
        // Simple red filled rectangle
        Rectangle rect1 = new Rectangle(25,25,40,40);
        rect1.setFill(Color.RED);
        // Blue stroked rectangle
        Rectangle rect2 = new Rectangle(135,25,40,40);
        rect2.setStroke(Color.DODGERBLUE);
        rect2.setFill(null);
        // Create a group to show all the rectangles);
        getChildren().add(new Group(rect1,rect2));
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Rectangle 1 Fill", rect1.fillProperty()),
                new SimplePropertySheet.PropDesc("Rectangle 1 Width", rect1.widthProperty(), 10d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 1 Height", rect1.heightProperty(), 10d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 1 Arc Width", rect1.arcWidthProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 1 Arc Height", rect1.arcHeightProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 2 Stroke", rect2.strokeProperty()),
                new SimplePropertySheet.PropDesc("Rectangle 2 Stroke Width", rect2.strokeWidthProperty(), 1d, 5d),
                new SimplePropertySheet.PropDesc("Rectangle 2 Width", rect2.widthProperty(), 10d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 2 Height", rect2.heightProperty(), 10d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 2 Arc Width", rect2.arcWidthProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle 2 Arc Height", rect2.arcHeightProperty(), 0d, 50d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Rectangle rectangle = new Rectangle(40,40);
        rectangle.setStroke(Color.web("#b9c0c5"));
        rectangle.setStrokeWidth(5);
        rectangle.getStrokeDashArray().addAll(15d,15d);
        rectangle.setFill(null);
        javafx.scene.effect.InnerShadow effect = new javafx.scene.effect.InnerShadow();
        effect.setOffsetX(1);
        effect.setOffsetY(1);
        effect.setRadius(3);
        effect.setColor(Color.rgb(0,0,0,0.6));
        rectangle.setEffect(effect);
        return rectangle;
    }
    // END REMOVE ME
}