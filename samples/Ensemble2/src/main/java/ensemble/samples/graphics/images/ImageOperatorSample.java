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

package ensemble.samples.graphics.images;

import ensemble.Sample;
import ensemble.controls.SimplePropertySheet;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A sample that demonstrates the use of Image Operators.
 *
 * @related graphics/images/ImageProperties
 * @see javafx.scene.image.Image
 * @see javafx.scene.image.ImageView
 * @see javafx.scene.image.PixelWriter
 * @see javafx.scene.image.WritableImage
 */
public class ImageOperatorSample extends Sample {
    
    SimpleDoubleProperty gridSize = new SimpleDoubleProperty(3.0);
    SimpleDoubleProperty hueFactor = new SimpleDoubleProperty(12.0);
    SimpleDoubleProperty hueOffset = new SimpleDoubleProperty(240.0);
    
    public ImageOperatorSample() {
        final WritableImage img = new WritableImage(300, 300);
        gridSize.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        hueFactor.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        hueOffset.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        renderImage(img, 3.0, 12.0, 240.0);

        ImageView view = new ImageView(img);
        getChildren().add(view);
        
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Grid Size", gridSize, 0d, 10d),
                new SimplePropertySheet.PropDesc("Hue Factor", hueFactor, 0d, 32d),
                new SimplePropertySheet.PropDesc("Hue Offset", hueOffset, 0d, 360d)
        );
        // END REMOVE ME
    }

    private static void renderImage(WritableImage img, double gridSize, double hueFactor, double hueOffset) {
        PixelWriter pw = img.getPixelWriter();
        double w = img.getWidth();
        double h = img.getHeight();
        double xRatio = 0.0;
        double yRatio = 0.0;
        double hue = 0.0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                xRatio = x/w;
                yRatio = y/h;
                hue = Math.sin(yRatio*(gridSize*Math.PI))*Math.sin(xRatio*(gridSize*Math.PI))*Math.tan(hueFactor/20.0)*360.0 + hueOffset;
                Color c = Color.hsb(hue, 1.0, 1.0);
                pw.setColor(x, y, c);
            }
        }
    }
    
    // REMOVE ME
    public static Node createIconContent() {
        final WritableImage img = new WritableImage(80, 80);
        renderImage(img, 3.0, 12.0, 240.0);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(80);
        return iv;
    }
    // END REMOVE ME
}

