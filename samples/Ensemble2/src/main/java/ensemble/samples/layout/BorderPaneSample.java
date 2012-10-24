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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// REMOVE ME
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
// END REMOVE ME

/**
 * An example of  a BorderPane layout, with placement of children in the top,
 * left, center, right, and bottom positions.
 *
 * @see javafx.scene.layout.BorderPane
 * @related controls/text/SimpleLabel
 * @related graphics/images/ImageCreationSample
 * @resource icon-48x48.png
 */
public class BorderPaneSample extends Sample {
    private static final Image ICON_48 = new Image(BorderPaneSample.class.getResourceAsStream("icon-48x48.png"));
    public BorderPaneSample() {
        super(400, 400);
        BorderPane borderPane = new BorderPane();

        //Top content
        Rectangle topRectangle = new Rectangle(400, 23, Color.DARKSEAGREEN);
        topRectangle.setStroke(Color.BLACK);
        borderPane.setTop(topRectangle);

        //Left content
        Label label1 = new Label("Left hand");
        Label label2 = new Label("Choice One");
        Label label3 = new Label("Choice Two");
        Label label4 = new Label("Choice Three");
        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(label1, label2, label3, label4);
        borderPane.setLeft(leftVbox);

        //Right content
        Label rightlabel1 = new Label("Right hand");
        Label rightlabel2 = new Label("Thing A");
        Label rightlabel3 = new Label("Thing B");
        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(rightlabel1, rightlabel2, rightlabel3);
        borderPane.setRight(rightVbox);

        //Center content
        Label centerLabel = new Label("We're in the center area.");
        ImageView imageView = new ImageView(ICON_48);

        //Using AnchorPane only to position items in the center
        AnchorPane centerAP = new AnchorPane();
        AnchorPane.setTopAnchor(centerLabel, Double.valueOf(5));
        AnchorPane.setLeftAnchor(centerLabel, Double.valueOf(20));
        AnchorPane.setTopAnchor(imageView, Double.valueOf(40));
        AnchorPane.setLeftAnchor(imageView, Double.valueOf(30));
        centerAP.getChildren().addAll(centerLabel, imageView);
        borderPane.setCenter(centerAP);

        //Bottom content
        Label bottomLabel = new Label("I am a status message, and I am at the bottom.");
        borderPane.setBottom(bottomLabel);

        getChildren().add(borderPane);
    }
    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        BorderPane borderPane = new BorderPane();

        Rectangle rectangle = new Rectangle(62, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        borderPane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
     
        Rectangle recTop = new Rectangle(62, 5, Color.web("#349b00"));
        recTop.setStroke(Color.BLACK);
        Rectangle recBottom = new Rectangle(62, 14, Color.web("#349b00"));
        recBottom.setStroke(Color.BLACK);
        Rectangle recLeft = new Rectangle(20, 41, Color.TRANSPARENT);
        recLeft.setStroke(Color.BLACK);
        Rectangle recRight = new Rectangle(20, 41, Color.TRANSPARENT);
        recRight.setStroke(Color.BLACK);
        Rectangle centerRight = new Rectangle(20, 41, Color.TRANSPARENT);
        centerRight.setStroke(Color.BLACK);
        borderPane.setRight(recRight);
        borderPane.setTop(recTop);
        borderPane.setLeft(recLeft);
        borderPane.setBottom(recBottom);
        borderPane.setCenter(centerRight);
 
        sp.getChildren().addAll(rectangle, borderPane);
        return new Group(sp);
    }
    // END REMOVE ME
}
