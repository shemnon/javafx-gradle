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

package ensemble.samples.graphics.effects;

import ensemble.Sample;
import ensemble.controls.SimplePropertySheet;
import javafx.scene.Node;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A sample that demonstrates how a reflection effect is affected by various settings.
 *
 * @see javafx.scene.effect.Reflection
 * @see javafx.scene.effect.Effect
 * @resource boat.jpg
 */
public class ReflectionSample extends Sample {
    private static final Image BOAT = new Image(ReflectionSample.class.getResourceAsStream("boat.jpg"));
    public ReflectionSample() {
        super(100,200);
        ImageView sample = new ImageView(BOAT);
        sample.setPreserveRatio(true);
        sample.setFitHeight(100);
        final Reflection reflection = new Reflection();
        sample.setEffect(reflection);
        getChildren().add(sample);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Reflection Bottom Opacity", reflection.bottomOpacityProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Reflection Top Opacity", reflection.topOpacityProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Reflection Fraction", reflection.fractionProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Reflection Top Offset", reflection.topOffsetProperty(), -10d, 10d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Text sample = new Text("FX");
        sample.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD,60));
        sample.setStyle("-fx-font-size: 80px;");
        sample.setFill(Color.web("#333333"));
        final Reflection reflection = new Reflection();
        reflection.setTopOffset(-28d);
        reflection.setFraction(0.5);
        sample.setEffect(reflection);
        return sample;
    }
    // END REMOVE ME
}