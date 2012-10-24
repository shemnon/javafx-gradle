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
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A sample in which a node moves from one location to another over a given
 * time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @see javafx.animation.TranslateTransition
 * @see javafx.animation.TranslateTransitionBuilder
 * @see javafx.animation.Transition
 */
public class TranslateTransitionSample extends Sample {

    private TranslateTransition translateTransition;

    public TranslateTransitionSample() {
        super(400,40);
        Circle circle = new Circle(20, Color.CRIMSON);
        circle.setTranslateX(20);
        circle.setTranslateY(20);
        getChildren().add(circle);
        translateTransition = new TranslateTransition(Duration.seconds(4),circle);
        translateTransition.setFromX(20);
        translateTransition.setToX(380);
        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setAutoReverse(true);        
        translateTransition = TranslateTransitionBuilder.create()
                .duration(Duration.seconds(4))
                .node(circle)
                .fromX(20)
                .toX(380)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
    }

    @Override public void play() {
        translateTransition.play();
    }

    @Override public void stop() {
        translateTransition.stop();
    }

    // REMOVE ME
    public static Node createIconContent() {
        Rectangle rect = new Rectangle (20, 20, 20, 20);
        rect.setArcHeight(4);
        rect.setArcWidth(4);
        rect.setFill(Color.web("#349b00"));
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(30,30,84,84);
        line.setStroke(Color.DODGERBLUE);
        line.getStrokeDashArray().setAll(5d, 5d);
        final TranslateTransition tt = new TranslateTransition(Duration.seconds(1),rect);
        tt.setCycleCount(Timeline.INDEFINITE);
        tt.setAutoReverse(true);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(64);
        tt.setToY(64);
        Rectangle mouseRect = new Rectangle (0, 0, 114,114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) { tt.play(); }
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) { tt.pause(); }
        });
        return new javafx.scene.Group(rect,line,mouseRect);
    }
    // END REMOVE ME
}
