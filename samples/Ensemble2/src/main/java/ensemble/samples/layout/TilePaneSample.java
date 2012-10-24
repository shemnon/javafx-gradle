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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

// REMOVE ME
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
// END REMOVE ME

/**
 * An example of a TilePane layout.
 *
 * @see javafx.scene.layout.TilePane
 * @related graphics/images/ImageCreationSample
 * @resource icon-48x48.png
 */
public class TilePaneSample extends Sample {
    private static final Image ICON_48 = new Image(TilePaneSample.class.getResourceAsStream("icon-48x48.png"));
    public TilePaneSample() {
   
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(3); //preferred columns

        Button[] buttons = new Button[18];
        for (int j = 0; j < buttons.length; j++) {
            buttons[j] = new Button("button" + (j + 1), new ImageView(ICON_48));
            tilePane.getChildren().add(buttons[j]);
        }
        getChildren().add(tilePane);
    }

    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();
        TilePane iconTilePane = new TilePane();
        iconTilePane.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(62, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        iconTilePane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());

        Rectangle[] rec = new Rectangle[9];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = new Rectangle(14, 14, Color.web("#349b00"));
            TilePane.setMargin(rec[i], new Insets(2, 2, 2, 2));
        }
        iconTilePane.getChildren().addAll(rec);
        sp.getChildren().addAll(rectangle, iconTilePane);
        return new Group(sp);
    }
    // END REMOVE ME
}
