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
package ensemble.samples.graphics.transforms;

import ensemble.Sample;
import ensemble.controls.SimplePropertySheet;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * An example of horizontal and vertical scaling.
 *
 * @see javafx.scene.transform.Scale
 */
public class ScaleSample extends Sample {

    public ScaleSample() {
        super(180,180);
        // simple rectangle
        Rectangle rect1 = new Rectangle(0, 25, 25, 25);
        rect1.setArcHeight(15);
        rect1.setArcWidth(15);
        rect1.setFill(Color.WHITE);
        rect1.setStroke(Color.DODGERBLUE);
        rect1.setStrokeWidth(3);
        
        Polygon arrow = createArrow();
        arrow.setLayoutX(46);
        arrow.setLayoutY(22);
        arrow.setRotate(90);
        
        // simple rectangle with scale 2 in X axis and 0.5 in Y
        Rectangle rect2 = new Rectangle(95, 25, 25, 25);
        rect2.setArcHeight(15);
        rect2.setArcWidth(15);
        rect2.setFill(Color.WHITE);
        rect2.setStroke(Color.DODGERBLUE);
        rect2.setStrokeWidth(3);
        rect2.setScaleX(2);
        rect2.setScaleY(0.5);
        // rectangle with adjustable scale
        Rectangle rect3 = new Rectangle(40, 130, 25, 25);
        rect3.setArcHeight(15);
        rect3.setArcWidth(15);
        rect3.setFill(Color.WHITE);
        rect3.setStroke(Color.DODGERBLUE);
        rect3.setStrokeWidth(3);
        rect3.setScaleX(6);
        rect3.setScaleY(0.5);
        rect3.setTranslateX(rect3.getTranslateX()+30);
        //getChildren().addAll(rect1, rect2, rect3);
        getChildren().addAll(rect1, arrow, rect2, rect3);
        
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Scale X", rect3.scaleXProperty(), 0.1d, 16d),
                new SimplePropertySheet.PropDesc("Scale Y", rect3.scaleYProperty(), 0.1d, 4d)
        );
        // END REMOVE ME
    }
    
    public static Polygon createArrow() {
        Polygon polygon = new Polygon(new double[]{
            7.5, 0,
            15, 15,
            10, 15,
            10, 30,
            5, 30,
            5, 15,
            0, 15
        });
        polygon.setFill(Color.web("#ff0900"));
        
        return polygon;
    }

    // REMOVE ME
    public static Node createIconContent() {
        final Rectangle r1 = new Rectangle (50, 50, 14, 14);
        r1.setArcHeight(4);
        r1.setArcWidth(4);
        r1.setFill(Color.web("#ed4b00"));

        Polygon polygon = createArrow();
        polygon.setLayoutX(68);
        polygon.setLayoutY(25);
        polygon.setRotate(45);

        Rectangle r3 = new Rectangle (25, 25, 64, 64);
        r3.setArcHeight(15);
        r3.setArcWidth(15);
        r3.setFill(Color.web("#f49b00"));
        javafx.scene.Group g = new javafx.scene.Group(r3,r1, polygon);
        return new javafx.scene.Group(g);
    }
    // END REMOVE ME
}
