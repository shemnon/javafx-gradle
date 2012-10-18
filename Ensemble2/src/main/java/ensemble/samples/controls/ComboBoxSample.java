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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

/**
 * A sample that shows both an un-editable and an editable ComboBox.
 *
 * @see javafx.scene.control.ComboBox
 * @see javafx.scene.control.ComboBoxBuilder
 */
public class ComboBoxSample extends Sample {

    private final ObservableList strings = FXCollections.observableArrayList(
            "Option 1", "Option 2", "Option 3",
            "Option 4", "Option 5", "Option 6",
            "Longer ComboBox item",
            "Option 7", "Option 8", "Option 9",
            "Option 10", "Option 12");

    public ComboBoxSample() {

        HBox hbox = HBoxBuilder.create().alignment(Pos.CENTER).spacing(15).build();
               
        //Non-editable combobox. Created with a builder
        ComboBox uneditableComboBox = ComboBoxBuilder.create()
                .id("uneditable-combobox")
                .promptText("Make a choice...")
                .items(FXCollections.observableArrayList(strings.subList(0, 8))).build();

        //Editable combobox. Use the default item display length
        ComboBox<String> editableComboBox = new ComboBox<String>();
        editableComboBox.setId("second-editable");
        editableComboBox.setPromptText("Edit or Choose...");
        editableComboBox.setItems(strings);
        editableComboBox.setEditable(true);

        hbox.getChildren().addAll(uneditableComboBox, editableComboBox);
        getChildren().add(hbox);
    }
}

