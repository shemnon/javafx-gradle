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
package ensemble.samples.controls;

import ensemble.Sample;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A sample that demonstrates the ColorPicker.
 *
 * @see javafx.scene.control.ColorPicker
 */
public class ColorPickerSample extends Sample {
    public ColorPickerSample() {
        final ColorPicker colorPicker = new ColorPicker(Color.GRAY);
        ToolBar standardToolbar = ToolBarBuilder.create().items(colorPicker).build();

        final Text coloredText = new Text("Colors");
        Font font = new Font(53);
        coloredText.setFont(font);
        final Button coloredButton = new Button("Colored Control");
        Color c = colorPicker.getValue();
        coloredText.setFill(c);
        coloredButton.setStyle(createRGBString(c));

        colorPicker.setOnAction(new EventHandler() {

            public void handle(Event t) {
                Color newColor = colorPicker.getValue();
                coloredText.setFill(newColor);                          
                coloredButton.setStyle(createRGBString(newColor));
            }
        });

        VBox coloredObjectsVBox = VBoxBuilder.create().alignment(Pos.CENTER).spacing(20).children(coloredText, coloredButton).build();        
        VBox outerVBox = VBoxBuilder.create().alignment(Pos.CENTER).spacing(150).padding(new Insets(0, 0, 120, 0)).children(standardToolbar, coloredObjectsVBox).build();
        getChildren().add(outerVBox);
    }

    private String createRGBString(Color c) {
        return "-fx-base: rgb(" + (c.getRed() * 255) + "," + (c.getGreen() * 255) + "," + (c.getBlue() * 255) + ");";
    }
}
