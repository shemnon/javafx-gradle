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
import javafx.scene.transform.Shear;

/**
 * A sample showing how various settings affect the Shear transform.
 *
 * @see javafx.scene.transform.Shear
 * @see javafx.scene.transform.Transform
 */
public class ShearSample extends Sample {

    public ShearSample() {
        super(125,160);

        //create rectangle
        Rectangle rect = new Rectangle(75, 75, Color.web("#ed4b00", 0.5));
        rect.setTranslateY(50);
        
        Shear shear = new Shear(0.7, 0);
        rect.getTransforms().add(shear);

        //show the rectangles
        getChildren().addAll(rect);

        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Shear X", shear.xProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Shear Y", shear.yProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Shear Pivot X", shear.pivotXProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Shear Pivot Y", shear.pivotYProperty(), 0d, 50d)
        );
        // END REMOVE ME

    }

    // REMOVE ME
    public static Node createIconContent() {
        final Rectangle r1 = new Rectangle (22, 0, 64, 64);
        r1.setArcHeight(4);
        r1.setArcWidth(4);
        r1.setFill(Color.web("#ed4b00",0.5));
        r1.getTransforms().add(new Shear(-0.35, 0));

        Polygon polygon = createArrow();
        polygon.setLayoutX(-5);
        polygon.setLayoutY(-2);
        polygon.setRotate(90);
        

        Rectangle r2 = new Rectangle (0, 0, 64, 64);
        r2.setArcHeight(4);
        r2.setArcWidth(4);
        r2.setFill(Color.web("#ed4b00", 0.25));
        javafx.scene.Group g = new javafx.scene.Group(r2,r1, polygon);
        return new javafx.scene.Group(g);
    }
    // END REMOVE ME    
    
    // REMOVE ME
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
    // END REMOVE ME
}
