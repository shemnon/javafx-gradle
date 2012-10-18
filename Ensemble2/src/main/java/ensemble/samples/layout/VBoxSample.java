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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// REMOVE ME
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
// END REMOVE ME

/**
 * An example of a VBox layout.
 *
 * @see javafx.scene.layout.VBox
 * @related controls/text/SimpleLabel
 * @related controls/buttons/CheckBoxes
 */
public class VBoxSample extends Sample {

    public VBoxSample() {
        super(400, 100);

        CheckBox cb1 = new CheckBox("Breakfast");
        CheckBox cb2 = new CheckBox("Lunch");
        CheckBox cb3 = new CheckBox("Dinner");

        VBox vboxMeals = new VBox(5);
        vboxMeals.getChildren().addAll(cb1, cb2, cb3);

        Label label = new Label("Select one or more meals:");
        VBox vboxOuter = new VBox(10);

        vboxOuter.getChildren().addAll(label, vboxMeals);
        vboxOuter.setAlignment(Pos.CENTER_LEFT);

        getChildren().add(vboxOuter);
    }

    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        VBox vbox = new VBox(3);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(5, 5, 5, 5));

        Rectangle rectangle = new Rectangle(32, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        vbox.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        Rectangle r1 = new Rectangle(18, 14, Color.web("#1c89f4"));
        Rectangle r2 = new Rectangle(18, 14, Color.web("#349b00"));
        Rectangle r3 = new Rectangle(18, 20, Color.web("#349b00"));

        vbox.getChildren().addAll(r1, r2, r3);
        sp.getChildren().addAll(rectangle, vbox);
        return new Group(sp);
    }
    // END REMOVE ME
}