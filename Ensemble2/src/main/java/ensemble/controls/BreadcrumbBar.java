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
package ensemble.controls;

import ensemble.Ensemble2;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * BreadcrumbBar
 */
public class BreadcrumbBar extends HBox {

    private String path;
    private String deliminator = "/";
    private List<Button> buttons = new ArrayList<Button>();

    public BreadcrumbBar() {
        super(0);
        getStyleClass().setAll("breadcrumb-bar");
        setFillHeight(true);
        setAlignment(Pos.CENTER_LEFT);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        String[] parts = path.split(deliminator);
        String pathSoFar = "";
        for (int i=0; i < Math.max(parts.length,buttons.size()); i++) {
            if (i<parts.length) {
                // we have a part for this index
                pathSoFar += (i==0) ? parts[i] : deliminator + parts[i];
                final String currentPath = pathSoFar;
                Button button = null;
                if (i<buttons.size()) {
                    // alread have a button
                    button = buttons.get(i);
                } else {
                    button = new Button(parts[i]);
                    button.setMaxHeight(Double.MAX_VALUE);
                    buttons.add(button);
                    getChildren().add(button);
                }
                button.setVisible(true) ;
                button.setText(parts[i]);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        Ensemble2.getEnsemble2().goToPage(currentPath);
                    }
                });
                if (i == parts.length-1) {
                    if(i==0) {
                        button.getStyleClass().setAll("button","only-button");
                    } else {
                        button.getStyleClass().setAll("button","last-button");
                    }
                } else if (i==0) {
                    button.getStyleClass().setAll("button","first-button");
                } else {
                    button.getStyleClass().setAll("button","middle-button");
                }
            } else {
                // don't need this button for now
                buttons.get(i).setVisible(false);
            }
        }
    }
}
