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
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

// REMOVE ME
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
// END REMOVE ME

/**
 * An example of a FlowPane layout.
 *
 * @see javafx.scene.layout.FlowPane
 * @related controls/text/SimpleLabel
 * @related graphics/images/ImageCreationSample
 * @resource icon-48x48.png
 */

public class FlowPaneSample extends Sample {
    private static final Image ICON_48 = new Image(FlowPaneSample.class.getResourceAsStream("icon-48x48.png"));
    private static int ITEMS = 5;

    public FlowPaneSample() {
        super(400, 400);

        FlowPane flowPane = new FlowPane(2, 4);
        flowPane.setPrefWrapLength(200); //preferred wraplength
        Label[] shortLabels = new Label[ITEMS];
        Label[] longLabels = new Label[ITEMS];
        ImageView[] imageViews = new ImageView[ITEMS];

        for (int i = 0; i < ITEMS; i++) {
            shortLabels[i] = new Label("Short label.");
            longLabels[i] = new Label("I am a slightly longer label.");
            imageViews[i] = new ImageView(ICON_48);
            flowPane.getChildren().addAll(shortLabels[i], longLabels[i], imageViews[i]);
        }
        getChildren().add(flowPane);
    }
    
    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(62, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        fp.setPrefSize(rectangle.getWidth(), rectangle.getHeight());

        Rectangle[] littleRecs = new Rectangle[4];
        Rectangle[] bigRecs = new Rectangle[4];
        for (int i = 0; i < 4; i++) {
            littleRecs[i] = new Rectangle(14, 14, Color.web("#1c89f4"));
            bigRecs[i] = new Rectangle(16, 12, Color.web("#349b00"));
            fp.getChildren().addAll(littleRecs[i], bigRecs[i]);
            FlowPane.setMargin(littleRecs[i], new Insets(2, 2, 2, 2));
        }
        sp.getChildren().addAll(rectangle, fp);
        return new Group(sp);
    }
    // END REMOVE ME
}
