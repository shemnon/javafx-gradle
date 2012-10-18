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
package ensemble.samples.animation.transitions;

import ensemble.Sample;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A sample in which a node scales larger and smaller over a given time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.ScaleTransition
 * @see javafx.animation.ScaleTransitionBuilder
 * @see javafx.animation.Transition
 */
public class ScaleTransitionSample extends Sample {

    private ScaleTransition scaleTransition;

    public ScaleTransitionSample() {
        super(150,150);
        Rectangle rect = new Rectangle(50, 50, 50, 50);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.ORANGE);
        getChildren().add(rect);
        scaleTransition = ScaleTransitionBuilder.create()
                .node(rect)
                .duration(Duration.seconds(4))
                .toX(3)
                .toY(3)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
    }

    @Override public void play() {
        scaleTransition.play();
    }

    @Override public void stop() {
        scaleTransition.stop();
    }

    // REMOVE ME
    public static Node createIconContent() {
        final Rectangle r1 = new Rectangle (50, 50, 14, 14);
        r1.setArcHeight(4);
        r1.setArcWidth(4);
        r1.setFill(Color.web("#349b00"));
        Rectangle r2 = new Rectangle (40, 40, 34, 34);
        r2.setArcHeight(8);
        r2.setArcWidth(8);
        r2.setFill(Color.web("#349b00"));
        r2.setOpacity(0.25);
        Rectangle r3 = new Rectangle (25, 25, 64, 64);
        r3.setArcHeight(15);
        r3.setArcWidth(15);
        r3.setFill(Color.web("#349b00"));
        r3.setOpacity(0.25);
        javafx.scene.Group g = new javafx.scene.Group(r1,r2,r3);
        final ScaleTransition st = new ScaleTransition(Duration.seconds(1),r1);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(5);
        st.setToY(5);
        Rectangle mouseRect = new Rectangle (0, 0, 114,114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) { st.play(); }
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) { 
                st.pause(); 
            }
        });
        return new javafx.scene.Group(g,mouseRect);
    }
    // END REMOVE ME
}
