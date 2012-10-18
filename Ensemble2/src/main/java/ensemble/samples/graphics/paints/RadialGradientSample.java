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
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

// REMOVE ME
// END REMOVE ME

/**
 * A sample that demonstrates two circles, one filled with a simple radial gradient and one filled with a more complex radial gradient.
 *
 * @related graphics/paints/Color
 * @see javafx.scene.paint.RadialGradient
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Paint
 * @see javafx.scene.paint.Color
 */
public class RadialGradientSample extends Sample {

    public RadialGradientSample() {
        //create simple radial gradient
        RadialGradient gradient1 = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
            new Stop(0, Color.DODGERBLUE),
            new Stop(1, Color.BLACK)
        });
        Circle circle1 = new Circle(45, 45, 40, gradient1);

        //create complex radial gradient
        RadialGradient gradient2 = new RadialGradient(20, 1, 0.5, 0.5, 0.6, true, CycleMethod.NO_CYCLE, new Stop[] {
            new Stop(0,  Color.TRANSPARENT),
            new Stop(0.5,  Color.DARKGRAY),
            new Stop(0.64, Color.WHITESMOKE),
            new Stop(0.65, Color.YELLOW),
            new Stop(1, Color.GOLD)
        });
        Circle circle2 = new Circle(145, 45, 40, gradient2);

        HBox hb = new HBox(10);
        hb.getChildren().addAll(circle1, circle2);
        
        // show the circles
        getChildren().addAll(hb);
    }

    // REMOVE ME
    public static Node createIconContent() {
        Rectangle rect = new Rectangle(80,80,new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
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