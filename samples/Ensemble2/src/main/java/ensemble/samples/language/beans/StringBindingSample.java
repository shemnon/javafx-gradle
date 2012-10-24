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
package ensemble.samples.language.beans;

import ensemble.Sample;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
// REMOVE ME
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// END REMOVE ME


/**
 * A sample that demonstrates how to bind text properties so the
 * value of the bound property is updated automatically when the value
 * of the original property is changed.
 *
 * @see javafx.beans.binding.StringBinding
 * @see javafx.scene.control.TextField
 * @see javafx.scene.control.Label
 */
public class StringBindingSample extends Sample {

    public StringBindingSample() {
        final SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
        final TextField dateField = new TextField();
        dateField.setPromptText("Enter a birth date");
        dateField.setMaxHeight(TextField.USE_PREF_SIZE);
        dateField.setMaxWidth(TextField.USE_PREF_SIZE);

        Label label = new Label();
        label.textProperty().bind(new StringBinding() {
            {
                bind(dateField.textProperty());
            }            
            @Override protected String computeValue() {
                try {
                    Date date = format.parse(dateField.getText());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    Date today = new Date();
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(today);

                    if (c.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) - 1
                            && c.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                        return "You were born yesterday";
                    } else {
                        return "You were born " + format.format(date);
                    }
                } catch (Exception e) {
                    return "Please enter a valid birth date (mm/dd/yyyy)";
                }
            }
        });

        VBox vBox = new VBox(7);
        vBox.setPadding(new Insets(12));
        vBox.getChildren().addAll(label, dateField);
        getChildren().add(vBox);
    }

    // REMOVE ME
    public static Node createIconContent() {
        Text text = new Text("abc");
        text.setTextOrigin(VPos.TOP);
        text.setLayoutX(10);
        text.setLayoutY(11);
        text.setFill(Color.BLACK);
        text.setOpacity(0.5);
        text.setFont(Font.font(null, FontWeight.BOLD, 20));
        text.setStyle("-fx-font-size: 20px;");

        Text text2 = new Text("abc");
        text2.setTextOrigin(VPos.TOP);
        text2.setLayoutX(28);
        text2.setLayoutY(51);
        text2.setFill(Color.BLACK);
        text2.setFont(javafx.scene.text.Font.font(null, FontWeight.BOLD, 20));
        text2.setStyle("-fx-font-size: 20px;");
                
        Line line = new Line(30, 32, 45, 57);
        line.setStroke(Color.DARKMAGENTA);

        return new javafx.scene.Group(text, line, text2);
    }
    // END REMOVE ME
}
