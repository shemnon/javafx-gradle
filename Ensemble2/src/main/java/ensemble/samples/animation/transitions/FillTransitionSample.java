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
 * A sample in which the filling of a shape changes over a given time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.FillTransition
 * @see javafx.animation.FillTransitionBuilder
 * @see javafx.animation.Transition
 */
public class FillTransitionSample extends Sample {

    private FillTransition fillTransition;

    public FillTransitionSample() {
        super(100,100);
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setFill(Color.DODGERBLUE);
        getChildren().add(rect);
        
        fillTransition = FillTransitionBuilder.create()
            .duration(Duration.seconds(3))
            .shape(rect)
            .fromValue(Color.RED)
            .toValue(Color.DODGERBLUE)
            .cycleCount(Timeline.INDEFINITE)
            .autoReverse(true)
            .build();
    }

    @Override public void play() {
        fillTransition.play();
    }

    @Override public void stop() {
        fillTransition.stop();
    }
    // REMOVE ME
    public static Node createIconContent() {
        Rectangle rect = new Rectangle(25, 25, 64, 64);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.web("#349b00"));
        final FillTransition ft = FillTransitionBuilder
                .create()
                .duration(Duration.seconds(3))
                .shape(rect)
                .fromValue(Color.web("#349b00"))
                .toValue(Color.RED).cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        Rectangle mouseRect = new Rectangle(0, 0, 114, 114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {           
            @Override public void handle(javafx.scene.input.MouseEvent e) { ft.play(); }              
        });
        mouseRect.setOnMouseExited(new EventHandler<javafx.scene.input.MouseEvent>() {            
            @Override public void handle(javafx.scene.input.MouseEvent e) { ft.pause(); }                
        });
        return new javafx.scene.Group(rect, mouseRect);
    }
    // END REMOVE ME
}
