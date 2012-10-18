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
import javafx.scene.transform.Rotate;

/**
 * A sample showing rotation of a node around a specified origin.
 *
 * @related animation/transitions/RotateTransition
 * @see javafx.scene.transform.Rotate
 * @see javafx.scene.transform.Transform
 */
public class RotateSample extends Sample {

    public RotateSample() {
        super(220, 270);

        //create 2 rectangles
        Rectangle rect1 = new Rectangle(90, 90, Color.web("#ed4b00", 0.75));
        Rectangle rect2 = new Rectangle(90, 90, Color.web("#ed4b00", 0.5));

        //rotate the second one
        rect2.getTransforms().add(new Rotate(135, 90, 90)); // parameters are angle, pivotX and pivotY

        // rectangle with adjustable rotate
        Rectangle rect3 = new Rectangle(40, 180, 60, 60);
        rect3.setFill(Color.DODGERBLUE);
        rect3.setArcWidth(10);
        rect3.setArcHeight(10);
        rect3.setRotate(45);

        //show the rectangles
        getChildren().addAll(rect2, rect1, rect3);

        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Rotate", rect3.rotateProperty(), 0d, 360d)
        );
        // END REMOVE ME
        
        //create arrow
        Polygon polygon = createArrow();
        polygon.setLayoutX(110);
        polygon.setLayoutY(15);
        polygon.setRotate(135);

        getChildren().addAll(polygon);

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
        final Rectangle r1 = new Rectangle (0, 0, 64, 64);
        r1.setArcHeight(4);
        r1.setArcWidth(4);
        r1.setFill(Color.web("#ed4b00"));

        Polygon polygon = createArrow();
        polygon.setLayoutX(65);
        polygon.setLayoutY(5);
        polygon.setRotate(165);
        

        Rectangle r2 = new Rectangle (0, 0, 64, 64);
        r2.setArcHeight(15);
        r2.setArcWidth(15);
        r2.setFill(Color.web("#ed4b00"));
        r2.setRotate(60);
        r2.setOpacity(0.5);
        javafx.scene.Group g = new javafx.scene.Group(r2,r1, polygon);
        return new javafx.scene.Group(g);
    }
    // END REMOVE ME
}
