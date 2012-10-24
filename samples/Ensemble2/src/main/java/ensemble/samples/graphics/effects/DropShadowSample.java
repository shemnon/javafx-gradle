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
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A sample that demonstrates how a drop shadow effect is affected by various settings.
 *
 * @see javafx.scene.effect.DropShadow
 * @see javafx.scene.effect.Effect
 */
public class DropShadowSample extends Sample {

    public DropShadowSample() {
        Text sample = new Text(0,40,"DropShadow Effect");
        sample.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD,36));
        final DropShadow dropShadow = new DropShadow();
        sample.setEffect(dropShadow);
        getChildren().add(sample);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("Radius", dropShadow.radiusProperty(), 0d, 20d),
                new SimplePropertySheet.PropDesc("Offset X", dropShadow.offsetXProperty(), -10d, 10d),
                new SimplePropertySheet.PropDesc("Offset Y", dropShadow.offsetYProperty(), -10d, 10d),
                new SimplePropertySheet.PropDesc("Spread", dropShadow.spreadProperty(), 0d, 1d),
                new SimplePropertySheet.PropDesc("Color", dropShadow.colorProperty())
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        Text sample = new Text("FX");
        sample.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD,80));
        sample.setStyle("-fx-font-size: 80px;");
        sample.setFill(Color.web("#333333"));
        final DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(4);
        dropShadow.setOffsetY(6);
        dropShadow.setColor(Color.rgb(0,0,0,0.7));
        sample.setEffect(dropShadow);
        return sample;
    }
    // END REMOVE ME
}