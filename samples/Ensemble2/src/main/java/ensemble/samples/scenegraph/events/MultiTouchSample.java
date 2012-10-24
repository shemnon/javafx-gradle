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
package ensemble.samples.scenegraph.events;

import javafx.event.EventHandler;
import ensemble.Sample;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A sample that demonstrates multi-touch support. You can zoom in and out of
 * the images and also rotate the images with multi-touch.
 *
 * @see javafx.scene.input.MouseEvent
 * @see javafx.event.EventHandler
 * @see javafx.scene.input.RotateEvent
 * @see javafx.scene.input.ZoomEvent
 * 
 * @resource warning.png
 * @resource animal1.jpg
 * @resource animal2.jpg
 * @resource animal3.jpg
 */
public class MultiTouchSample extends Sample {
    static final int IMAGEVIEW_WIDTH = 200;
    static final int IMAGEVIEW_HEIGHT = 200;
    private ImageView postView;
    private Rectangle clipRect;
    private MultiTouchPane multiTouchPane;

    public MultiTouchSample() {
        multiTouchPane = new MultiTouchPane();
        getChildren().add(multiTouchPane);

    }

    public class MultiTouchPane extends Region {

        public MultiTouchPane() {
            clipRect = new Rectangle();
            clipRect.setSmooth(false);
            setClip(clipRect);

            Image post = new Image(MultiTouchSample.class.getResource("warning.png").toExternalForm(), false);
            postView = new ImageView(post);

            for (int i = 1; i <= 3; i++) {
                Image img = new Image(MultiTouchSample.class.getResource("animal" + i + ".jpg").toExternalForm(), false);
                MultiTouchImageView iv = new MultiTouchImageView(img);
                getChildren().add(iv);
            }

            getChildren().add(postView);
        }
       
        @Override protected void layoutChildren() {
            final double w = getWidth();
            final double h = getHeight();
            clipRect.setWidth(w);
            clipRect.setHeight(h);
            for (Node child : getChildren()) {
                if (child == postView) {
                    postView.relocate(w - 15 - postView.getLayoutBounds().getWidth(), 0);
                } else if (child.getLayoutX() == 0 && child.getLayoutY() == 0) {
                    final double iw = child.getBoundsInParent().getWidth();
                    final double ih = child.getBoundsInParent().getHeight();
                    child.setLayoutX((w - iw) * Math.random() + 100);
                    child.setLayoutY((h - ih) * Math.random() + 100);
                }
            }
        }
    }

    public class MultiTouchImageView extends StackPane {

        private ImageView imageView;
        private double lastX, lastY, startScale, startRotate;

        public MultiTouchImageView(Image img) {
            setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 8, 0, 0, 2));

            imageView = new ImageView(img);
            imageView.setSmooth(true);
            getChildren().add(imageView);

            setOnMousePressed(new EventHandler<MouseEvent>() {             
                @Override public void handle(MouseEvent event) {

                    lastX = event.getX();
                    lastY = event.getY();
                    toFront();
                    postView.toFront();
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {                
                @Override public void handle(MouseEvent event) {

                    double layoutX = getLayoutX() + (event.getX() - lastX);
                    double layoutY = getLayoutY() + (event.getY() - lastY);

                    if ((layoutX >= 0) && (layoutX <= (getParent().getLayoutBounds().getWidth()))) {
                        setLayoutX(layoutX);
                    }

                    if ((layoutY >= 0) && (layoutY <= (getParent().getLayoutBounds().getHeight()))) {
                        setLayoutY(layoutY);
                    }

                    if ((getLayoutX() + (event.getX() - lastX) <= 0)) {
                        setLayoutX(0);
                    }
                }
            });
            addEventHandler(ZoomEvent.ZOOM_STARTED, new EventHandler<ZoomEvent>() {                
                @Override public void handle(ZoomEvent event) {
                    startScale = getScaleX();
                }
            });
            addEventHandler(ZoomEvent.ZOOM, new EventHandler<ZoomEvent>() {               
                @Override public void handle(ZoomEvent event) {
                    setScaleX(event.getTotalZoomFactor());
                    setScaleY(event.getTotalZoomFactor());
                }
            });
            addEventHandler(RotateEvent.ROTATION_STARTED, new EventHandler<RotateEvent>() {                
                @Override public void handle(RotateEvent event) {
                    startRotate = getRotate();
                }
            });
            addEventHandler(RotateEvent.ROTATE, new EventHandler<RotateEvent>() {               
                @Override public void handle(RotateEvent event) {
                    setRotate(event.getTotalAngle());
                }
            });

        }
    }
}
