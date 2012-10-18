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
package ensemble.samples.controls.tabs;

import ensemble.Sample;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;

/**
 * An implementation of tabs using the TabPane class.
 *
 * @see javafx.scene.control.TabPane
 * @see javafx.scene.control.Tab
 * @see javafx.scene.control.TabPane
 */
public class TabSample extends Sample {
    public TabSample() {
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        tabPane.setPrefSize(400, 400);
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        final Tab tab1 = new Tab();
        tab1.setText("Tab 1");
        final Tab tab2 = new Tab();
        tab2.setText("Tab 2");
        final Tab tab3 = new Tab();
        tab3.setText("Tab 3");
        final Tab tab4 = new Tab();
        tab4.setText("Tab 4");
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
        borderPane.setCenter(tabPane);
        getChildren().add(borderPane);
    }
}
