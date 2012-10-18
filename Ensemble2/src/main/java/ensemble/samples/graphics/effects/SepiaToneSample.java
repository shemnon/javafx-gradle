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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A sample that demonstrates varying degrees of a sepia tone effect.
 *
 * @see javafx.scene.effect.SepiaTone
 * @see javafx.scene.effect.Effect
 * @resource boat.jpg
 */
public class SepiaToneSample extends Sample {
    private static final Image BOAT = new Image(SepiaToneSample.class.getResourceAsStream("boat.jpg"));
    public SepiaToneSample() {
        ImageView sample = new ImageView(BOAT);
        final SepiaTone sepiaTone = new SepiaTone();
        sepiaTone.setLevel(0.5d);
        sample.setEffect(sepiaTone);
        getChildren().add(sample);
        // REMOVE ME
        setControls(
                new SimplePropertySheet.PropDesc("SepiaTone Level", sepiaTone.levelProperty(), 0d, 1d)
        );
        // END REMOVE ME
    }

    // REMOVE ME
    public static Node createIconContent() {
        ImageView iv = new ImageView(BOAT);
        iv.setFitWidth(80);
        iv.setFitHeight(80);
        iv.setViewport(new Rectangle2D(90,0,332,332));
        final SepiaTone SepiaTone = new SepiaTone();
        SepiaTone.setLevel(1);
        iv.setEffect(SepiaTone);
        return iv;
    }
    // END REMOVE ME
}