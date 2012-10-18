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
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


/**
 * A sample in which various transitions are executed in parallel.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.ParallelTransition
 * @see javafx.animation.ParallelTransitionBuilder
 * @see javafx.animation.Transition
 */
public class ParallelTransitionSample extends Sample {

    private ParallelTransition parallelTransition;

    public ParallelTransitionSample() {
        super(400,150);
        // create rectangle
        Rectangle rect = new Rectangle(-25,-25,50, 50);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.CRIMSON);
        rect.setTranslateX(50);
        rect.setTranslateY(75);
        getChildren().add(rect); 
        // create parallel transition to do all 4 transitions at the same time        
        parallelTransition = ParallelTransitionBuilder.create()
            .node(rect)
            .children(
                FadeTransitionBuilder.create()
                    .duration(Duration.seconds(3))
                    .node(rect)
                    .fromValue(1)
                    .toValue(0.3)
                    .autoReverse(true)
                    .build(),
                TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .fromX(50)
                    .toX(350)
                    .cycleCount(2)
                    .autoReverse(true)
                    .build(),
                RotateTransitionBuilder.create()
                    .duration(Duration.seconds(3))
                    .byAngle(180)
                    .cycleCount(4)
                    .autoReverse(true)
                    .build(),
                ScaleTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .toX(2)
                    .toY(2)
                    .cycleCount(2)
                    .autoReverse(true)
                    .build()
            )
            .cycleCount(Timeline.INDEFINITE)
            .autoReverse(true)
            .build();
        
    }

    @Override public void play() {
        parallelTransition.play();
    }

    @Override public void stop() {
        parallelTransition.stop();
    }

    // REMOVE ME
    public static Node createIconContent() {
        final Rectangle r1 = new Rectangle (20, 20, 20, 20);
        r1.setArcHeight(4);
        r1.setArcWidth(4);
        r1.setFill(Color.web("#349b00"));
        Rectangle r2 = new Rectangle (38, 38, 20, 20);
        r2.setArcHeight(4);
        r2.setArcWidth(4);
        r2.setFill(Color.web("#349b00"));
        r2.setOpacity(0.3);
        r2.setRotate(30);
        Rectangle r3 = new Rectangle (56, 56, 20, 20);
        r3.setArcHeight(4);
        r3.setArcWidth(4);
        r3.setFill(Color.web("#349b00"));
        r3.setOpacity(0.3);
        r3.setRotate(60);
        Rectangle r4 = new Rectangle (74, 74, 20, 20);
        r4.setArcHeight(4);
        r4.setArcWidth(4);
        r4.setFill(Color.web("#349b00"));
        r4.setOpacity(0.3);
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(30,30,84,84);
        line.setStroke(Color.DODGERBLUE);
        line.getStrokeDashArray().setAll(5d, 5d);
        final TranslateTransition tt = new TranslateTransition(Duration.seconds(1),r1);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(54);
        tt.setToY(54);
        final RotateTransition rt = new RotateTransition(Duration.seconds(1),r1);
        rt.setFromAngle(0);
        rt.setToAngle(180);
        final ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(tt,rt);
        pt.setCycleCount(Timeline.INDEFINITE);
        pt.setAutoReverse(true);
        Rectangle mouseRect = new Rectangle (0, 0, 114,114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) { pt.play(); }
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) {
                pt.stop();
                r1.setTranslateX(0);
                r1.setTranslateY(0);
                r1.setRotate(0);
            }
        });
        return new javafx.scene.Group(r1,r2,r3,r4,line,mouseRect);
    }
    // END REMOVE ME
}
