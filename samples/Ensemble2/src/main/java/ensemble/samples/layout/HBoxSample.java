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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

// REMOVE ME
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// END REMOVE ME
/**
 * An example of an HBox layout.
 *
 * @see javafx.scene.layout.HBox
 * @related controls/text/SimpleLabel
 */
public class HBoxSample extends Sample {

    public HBoxSample() {
        super(400, 100);

        //Controls to be added to the HBox
        Label label = new Label("Test Label:");
        TextField tb = new TextField();
        Button button = new Button("Button...");

        //HBox with spacing = 5
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(label, tb, button);
        hbox.setAlignment(Pos.CENTER);
        getChildren().add(hbox);
    }
    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        HBox hbox = new HBox(3);
        hbox.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(70, 25, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        hbox.setPrefSize(rectangle.getWidth(), rectangle.getHeight());

        Rectangle r1 = new Rectangle(14, 14, Color.web("#1c89f4"));
        Rectangle r2 = new Rectangle(14, 14, Color.web("#349b00"));
        Rectangle r3 = new Rectangle(18, 14, Color.web("#349b00"));

        hbox.getChildren().addAll(r1, r2, r3);
        sp.getChildren().addAll(rectangle, hbox);

        return new Group(sp);
    }
    // END REMOVE ME
}
