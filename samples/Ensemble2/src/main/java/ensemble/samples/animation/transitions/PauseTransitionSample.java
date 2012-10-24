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
 * A sample in which a node pauses over a given time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.PauseTransition
 * @see javafx.animation.PauseTransitionBuilder
 * @see javafx.animation.Transition
 */
public class PauseTransitionSample extends Sample {

    private Animation animation;   

    public PauseTransitionSample() {
        super(400,150);
        // create rectangle
        Rectangle rect = new Rectangle(-25,-25,50, 50);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.CRIMSON);
        rect.setTranslateX(50);
        rect.setTranslateY(75);
        getChildren().add(rect);
        
        animation = SequentialTransitionBuilder.create()
            .node(rect)
            .children(
                TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .fromX(50)
                    .toX(200)
                    .build(),
                PauseTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .build(),
                TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .fromX(200)
                    .toX(350)
                    .build()
            )
            .cycleCount(Timeline.INDEFINITE)
            .autoReverse(true)
            .build();                
    }

    @Override public void play() {
        animation.play();
    }

    @Override public void stop() {
        animation.stop();
    }
    // REMOVE ME

    public static Node createIconContent() {
        Rectangle rect = new Rectangle(20, 20, 20, 20);
        rect.setArcHeight(4);
        rect.setArcWidth(4);
        rect.setFill(Color.web("#349b00"));
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(30, 30, 84, 84);
        line.setStroke(Color.DODGERBLUE);
        line.getStrokeDashArray().setAll(5d, 5d);
        Rectangle r4 = new Rectangle(74, 74, 20, 20);
        r4.setArcHeight(4);
        r4.setArcWidth(4);
        r4.setFill(Color.RED);
        r4.setOpacity(0.3);
        final SequentialTransition st = SequentialTransitionBuilder.create()
                .node(rect)
                .children(
                    TranslateTransitionBuilder.create()
                        .duration(Duration.seconds(2))
                        .fromX(0).fromY(0)
                        .toX(54)
                        .toY(54)
                        .build(),
                    PauseTransitionBuilder.create()
                        .duration(Duration.seconds(1))
                        .build())
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        Rectangle mouseRect = new Rectangle(0, 0, 114, 114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {            
            @Override public void handle(javafx.scene.input.MouseEvent e) { st.play(); }                        
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {            
            @Override public void handle(javafx.scene.input.MouseEvent e) { st.pause(); }                            
        });
        return new javafx.scene.Group(rect, r4, line, mouseRect);
    }
    // END REMOVE ME
}
