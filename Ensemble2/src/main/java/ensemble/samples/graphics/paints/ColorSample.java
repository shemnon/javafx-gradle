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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 * Rectangles filled with colors.
 *
 * @related graphics/paints/LinearGradient
 * @related graphics/paints/RadialGradient
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.paint.Color
 * @see javafx.scene.paint.Paint
 */
public class ColorSample extends Sample {

    public ColorSample() {
        super(260,90);
        
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(
                createRectangle(Color.hsb(  0.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb( 30.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb( 60.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(120.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(160.0, 1.0, 1.0)), // hue, saturation, brightness                
                createRectangle(Color.hsb(200.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(240.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(280.0, 1.0, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(320.0, 1.0, 1.0))  // hue, saturation, brightness
                );

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(
                createRectangle(Color.hsb(  0.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb( 30.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb( 60.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(120.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(160.0, 0.5, 1.0)), // hue, saturation, brightness                
                createRectangle(Color.hsb(200.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(240.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(280.0, 0.5, 1.0)), // hue, saturation, brightness
                createRectangle(Color.hsb(320.0, 0.5, 1.0))  // hue, saturation, brightness
                );
        
        HBox hBox3 = new HBox();
        hBox3.setSpacing(10);
        hBox3.getChildren().addAll(
                createRectangle(Color.hsb(  0.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb( 30.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb( 60.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb(120.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb(160.0, 1.0, 0.5)), // hue, saturation, brightness                
                createRectangle(Color.hsb(200.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb(240.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb(280.0, 1.0, 0.5)), // hue, saturation, brightness
                createRectangle(Color.hsb(320.0, 1.0, 0.5))  // hue, saturation, brightness
                );
        
        HBox hBox4 = new HBox();
        hBox4.setSpacing(10);
        hBox4.getChildren().addAll(
                createRectangle(Color.BLACK), //predefined color
                createRectangle(Color.hsb(0, 0, 0.1)), //defined by hue - saturation - brightness
                createRectangle(new Color(0.2, 0.2, 0.2, 1)), //define color as new instance of color
                createRectangle(Color.color(0.3, 0.3, 0.3)), //standard constructor
                createRectangle(Color.rgb(102, 102, 102)), //define color by rgb
                createRectangle(Color.web("#777777")), //define color by hex web value
                createRectangle(Color.gray(0.6)), //define gray color
                createRectangle(Color.grayRgb(179)), //define gray color
                createRectangle(Color.grayRgb(179, 0.5)) //opacity can be adjusted in all constructors
                );
        
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(hBox2);
        vBox.getChildren().add(hBox3);
        vBox.getChildren().add(hBox4);
        
        // show the rectangles
        getChildren().add(vBox);
    }

    private Rectangle createRectangle(Color color) {
        Rectangle rect1 = new Rectangle(0, 45, 20, 20);
        //Fill rectangle with color
        rect1.setFill(color);
        return rect1;
    }

    // REMOVE ME
    public static Node createIconContent() {
        double offset;
        Stop[] stops = new Stop[255];
        for (int y = 0; y < 255; y++) {
            offset = (double) (1.0 / 255) * y;
            int h = (int)((y / 255.0) * 360);
            stops[y] = new Stop(offset, Color.hsb(h, 0.8, 0.9));
        }
        Rectangle rect = new Rectangle(80,80,
                new LinearGradient(0f, 0f, 1f, 1f, true, CycleMethod.NO_CYCLE, stops));
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        return rect;
    }
    // END REMOVE ME
}