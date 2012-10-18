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
package ensemble.samples.controls.buttons;

import ensemble.Sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

/**
 * Toggle buttons in a toggle group.
 *
 * @see javafx.scene.control.ToggleButton
 * @related controls/ChoiceBox
 * @related controls/Accordion
 * @related controls/toolbar/ToolBar
 */
public class ToggleButtonSample extends Sample {

    public ToggleButtonSample() {
        // create label to show result of selected toggle button
        final Label label = new Label();
        label.setStyle("-fx-font-size: 2em;");
        // create 3 toggle buttons and a toogle group for them
        final ToggleButton tb1 = new ToggleButton("Cat");
        final ToggleButton tb2 = new ToggleButton("Dog");
        final ToggleButton tb3 = new ToggleButton("Horse");
        ToggleGroup group = new ToggleGroup();
        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
        tb3.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) {
                if(selectedToggle!=null) {
                    label.setText(((ToggleButton) selectedToggle).getText());
                }
                else {
                    label.setText("...");
                }
            }
        });
        // select the first button to start with
        group.selectToggle(tb1);
        // add buttons and label to grid and set their positions
        GridPane.setConstraints(tb1,0,0);
        GridPane.setConstraints(tb2,1,0);
        GridPane.setConstraints(tb3,2,0);
        GridPane.setConstraints(label,0,1,3,1);
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        getChildren().add(grid);
        grid.getChildren().addAll(tb1, tb2, tb3, label);
    }
}

