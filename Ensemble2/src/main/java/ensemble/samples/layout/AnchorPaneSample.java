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
package ensemble.samples.layout;

import ensemble.Sample;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;

// REMOVE ME
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;

// END REMOVE ME

/**
 * An example of an AnchorPane layout.
 *
 * @see javafx.scene.layout.AnchorPane
 * @related controls/text/SimpleLabel
 * @resource icon-48x48.png
 */
public class AnchorPaneSample extends Sample {
    private static final Image ICON_48 = new Image(AnchorPaneSample.class.getResourceAsStream("icon-48x48.png"));
    public AnchorPaneSample() {
    
        AnchorPane anchorPane = new AnchorPane();

        Label label1 = new Label("We are all in an AnchorPane.");        
        ImageView imageView = new ImageView(ICON_48);
        Button button1 = new Button("Submit");

        anchorPane.getChildren().addAll(label1, imageView, button1);

        AnchorPane.setTopAnchor(label1, Double.valueOf(2));
        AnchorPane.setLeftAnchor(label1, Double.valueOf(20));
        AnchorPane.setTopAnchor(button1, Double.valueOf(40));
        AnchorPane.setLeftAnchor(button1, Double.valueOf(20));
        AnchorPane.setTopAnchor(imageView, Double.valueOf(75));
        AnchorPane.setLeftAnchor(imageView, Double.valueOf(20));

        getChildren().add(anchorPane);
    }
    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        AnchorPane anchorPane = new AnchorPane();

        Rectangle rectangle = new Rectangle(62, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        anchorPane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());

        Rectangle r1 = new Rectangle(14, 14, Color.web("#1c89f4"));
        Rectangle r2 = new Rectangle(45, 10, Color.web("#349b00"));
        Rectangle r3 = new Rectangle(35, 14, Color.web("#349b00"));

        anchorPane.getChildren().addAll(r1, r2, r3);
        AnchorPane.setTopAnchor(r1, Double.valueOf(1));
        AnchorPane.setLeftAnchor(r1, Double.valueOf(1));
        AnchorPane.setTopAnchor(r2, Double.valueOf(20));
        AnchorPane.setLeftAnchor(r2, Double.valueOf(1));
        AnchorPane.setBottomAnchor(r3, Double.valueOf(1));
        AnchorPane.setRightAnchor(r3, Double.valueOf(5));

        sp.getChildren().addAll(rectangle, anchorPane);
        return new Group(sp);
    }
    // END REMOVE ME
}
