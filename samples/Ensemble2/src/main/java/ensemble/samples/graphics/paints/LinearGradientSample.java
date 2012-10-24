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
package ensemble.samples.graphics.paints;

import ensemble.Sample;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * A sample with two rectangles, one filled with a simple linear gradient and one filled with a more complex gradient using the reflection cycle method.
 *
 * @related graphics/paints/Color
 * @see javafx.scene.paint.LinearGradient
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Paint
 * @see javafx.scene.paint.Color
 */
public class LinearGradientSample extends Sample {

    public LinearGradientSample() {
        //First rectangle
        Rectangle rect1 = new Rectangle(0,0,80,80);

        //create simple linear gradient
        LinearGradient gradient1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[] {
            new Stop(0, Color.DODGERBLUE),
            new Stop(1, Color.BLACK)
        });

        //set rectangle fill
        rect1.setFill(gradient1);

        // Second rectangle
        Rectangle rect2 = new Rectangle(0,0,80,80);

        //create complex linear gradient
        LinearGradient gradient2 = new LinearGradient(0, 0, 0, 0.5,  true, CycleMethod.REFLECT, new Stop[] {
            new Stop(0, Color.DODGERBLUE),
            new Stop(0.1, Color.BLACK),
            new Stop(1, Color.DODGERBLUE)
        });

        //set rectangle fill
        rect2.setFill(gradient2);

        // show the rectangles
        HBox hb = new HBox(10);
        hb.getChildren().addAll(rect1, rect2);
        getChildren().add(hb);
    }

    // REMOVE ME
    public static Node createIconContent() {
        Rectangle rect = new Rectangle(80,80,new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop[] {
            new Stop(0, Color.rgb(156,216,255)),
            new Stop(0.5, Color.DODGERBLUE),
            new Stop(1, Color.rgb(0,70,140))
        }));
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        return rect;
    }
    // END REMOVE ME
}