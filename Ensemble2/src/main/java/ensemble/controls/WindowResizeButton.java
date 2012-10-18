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
package ensemble.controls;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Simple draggable area for the bottom right of a window to support resizing.
 */
public class WindowResizeButton extends Region {
    private double dragOffsetX, dragOffsetY;

    public WindowResizeButton(final Stage stage, final double stageMinimumWidth, final double stageMinimumHeight) {
        setId("window-resize-button");
        setPrefSize(11,11);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                dragOffsetX = (stage.getX() + stage.getWidth()) - e.getScreenX();
                dragOffsetY = (stage.getY() + stage.getHeight()) - e.getScreenY();
                e.consume();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                ObservableList<Screen> screens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1);
                final Screen screen;
                if(screens.size()>0) {
                    screen = Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1).get(0);
                }
                else {
                    screen = Screen.getScreensForRectangle(0,0,1,1).get(0);
                }
                Rectangle2D visualBounds = screen.getVisualBounds();             
                double maxX = Math.min(visualBounds.getMaxX(), e.getScreenX() + dragOffsetX);
                double maxY = Math.min(visualBounds.getMaxY(), e.getScreenY() - dragOffsetY);
                stage.setWidth(Math.max(stageMinimumWidth, maxX - stage.getX()));
                stage.setHeight(Math.max(stageMinimumHeight, maxY - stage.getY()));
                e.consume();
            }
        });
    }
}
