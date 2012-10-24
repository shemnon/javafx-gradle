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
package ensemble.samples.scenegraph.node;

import ensemble.controls.SimplePropertySheet;
import javafx.event.EventHandler;
import ensemble.Sample;
import javafx.beans.Observable;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Node;
// REMOVE ME
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
// END REMOVE ME
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * A sample that demonstrates some properties of nodes. Use the radio buttons
 * to send any of the rectangles to the front or back. Use the controls to
 * change opacity or horizontal position.
 *
 * @see javafx.scene.Node
 */
public class NodePropertiesSample extends Sample {
    private Rectangle rectA;
    private Rectangle rectB;
    private Rectangle rectC;

    public NodePropertiesSample() {
        super(300,100);
        //X position of node = X + LayoutX + TranslateX
        rectA = new Rectangle(50, 50, Color.LIGHTSALMON);
        //set position of node temporary (can be changed after)
        rectA.setTranslateX(10);

        rectB = new Rectangle(50, 50, Color.LIGHTGREEN);
        //set position of node when addinf to some layout
        rectB.setLayoutX(20);
        rectB.setLayoutY(10);

        rectC = new Rectangle(50, 50, Color.DODGERBLUE);
        //last posibility of setting X position of node
        rectC.setX(30);
        rectC.setY(20);

        //opacity of node can be set
        rectC.setOpacity(0.8);

        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Rectangle A translate X", rectA.translateXProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle B translate X", rectB.translateXProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle C translate X", rectC.translateXProperty(), 0d, 50d),
                new SimplePropertySheet.PropDesc("Rectangle A Opacity", rectA.opacityProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Rectangle B Opacity", rectB.opacityProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Rectangle C Opacity", rectC.opacityProperty(), 0d, 1d)
                );

        getChildren().add(createRadioButtons());
        // END REMOVE ME

        Group g = new Group(rectA, rectB, rectC);
        g.setLayoutX(160 + 35);
        getChildren().addAll(g);
    }


    // REMOVE ME
    private HBox createRadioButtons() {
        //creates a radio buttons, for each rectangle 2 buttons with action .toFront() and toBack()
        ToggleGroup tg = new ToggleGroup();
        
        VBox vBox1 = new VBox();
        vBox1.setSpacing(5);
        vBox1.setLayoutY(6);
        vBox1.getChildren().addAll(
                createRadioButton(rectA, "A.toFront()", true, tg),
                createRadioButton(rectB, "B.toFront()", true, tg),
                createRadioButton(rectC, "C.toFront()", true, tg)
                );

        VBox vBox2 = new VBox();
        vBox2.setSpacing(5);
        vBox2.setLayoutY(6);
        vBox2.getChildren().addAll(
                createRadioButton(rectA, "A.toBack()", false, tg),
                createRadioButton(rectB, "B.toBack()", false, tg),
                createRadioButton(rectC, "C.toBack()", false, tg)
                );

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(vBox1, vBox2);
        return hBox;
    }

    private RadioButton createRadioButton(final Node rect, String name, final boolean toFront, ToggleGroup tg) {
        final RadioButton radioButton = new RadioButton(name);
        radioButton.setToggleGroup(tg);
        radioButton.selectedProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (radioButton.isSelected()) {
                    if (toFront) {
                        rect.toFront();
                    } else {
                        rect.toBack();
                    }
                }
            }
        });

        return radioButton;
    }

    public static Node createIconContent() {
        
        final Rectangle A = new Rectangle(30, 30, Color.WHITE);
        A.setLayoutY(15);
        A.setStroke(Color.BLACK);

        final Rectangle B = new Rectangle(30, 30, Color.WHITE);
        B.setLayoutX(7.5);
        B.setLayoutY(7.5);
        B.setStroke(Color.BLACK);

        final Rectangle C = new Rectangle(30, 30, Color.WHITE);
        C.setLayoutX(15);
        C.setStroke(Color.BLACK);

        javafx.scene.Group g = new javafx.scene.Group(A, B, C);
        g.setLayoutX(34.5);
        g.setLayoutY(34.5);

        //add a shuffing effect
        final javafx.animation.Timeline tl = new javafx.animation.Timeline();
        tl.setCycleCount(javafx.animation.Timeline.INDEFINITE);

        javafx.animation.KeyFrame keyFrameA = new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent t) {
                A.toFront();
            }
        });
        javafx.animation.KeyFrame keyFrameB = new javafx.animation.KeyFrame(javafx.util.Duration.millis(600), new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent t) {
                B.toFront();
            }
        });
        javafx.animation.KeyFrame keyFrameC = new javafx.animation.KeyFrame(javafx.util.Duration.millis(900), new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent t) {
                C.toFront();
            }
        });

        tl.getKeyFrames().addAll(keyFrameA, keyFrameB, keyFrameC);

        Rectangle mouseRect = new Rectangle (0, 0, 114,114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) {
                tl.playFromStart();
            }
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) {
                tl.stop();

            }
        });
        return new javafx.scene.Group(mouseRect, g);
    }
    // END REMOVE ME
}

