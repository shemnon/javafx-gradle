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
package ensemble.pages;

import ensemble.Page;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CategoryPage
 *
 */
public class CategoryPage extends Page {

    public CategoryPage(String name, Page ... pages) {
        super(name);
        getChildren().addAll(Arrays.asList(pages));
    }

    @Override public Node createView() {
        // split children
        List<SamplePage> directChildren = new ArrayList<SamplePage>();
        List<CategoryPage> categoryChildren = new ArrayList<CategoryPage>();
        for (TreeItem child:getChildren()) {
            Page page = (Page)child;
            if (page instanceof SamplePage) {
                directChildren.add((SamplePage)page);
            } else if (page instanceof CategoryPage) {
                categoryChildren.add((CategoryPage)page);
            }
        }
        // create main column
        VBox main = new VBox(8) {
            // stretch to allways fill height of scrollpane
            @Override protected double computePrefHeight(double width) {
                return Math.max(
                        super.computePrefHeight(width),
                        getParent().getBoundsInLocal().getHeight()
                );
            }
        };
        main.getStyleClass().add("category-page");
        // create header
        Label header = new Label(getName());
        header.setMaxWidth(Double.MAX_VALUE);
        header.setMinHeight(Control.USE_PREF_SIZE); // Workaround for RT-14251
        header.getStyleClass().add("page-header");
        main.getChildren().add(header);
        // add direct children
        if(!directChildren.isEmpty()) {
            TilePane directChildFlow = new TilePane(8,8);
            directChildFlow.setPrefColumns(1);
            directChildFlow.getStyleClass().add("category-page-flow");
            for (SamplePage samplePage:directChildren) {
                directChildFlow.getChildren().add(samplePage.createTile());
            }
            main.getChildren().add(directChildFlow);
        }
        // add sub sections
        for(CategoryPage categoryPage:categoryChildren) {
            // create header
            Label categoryHeader = new Label(categoryPage.getName());
            categoryHeader.setMaxWidth(Double.MAX_VALUE);
            categoryHeader.setMinHeight(Control.USE_PREF_SIZE); // Workaround for RT-14251
            categoryHeader.getStyleClass().add("category-header");
            main.getChildren().add(categoryHeader);
            // add direct children
            TilePane directChildFlow = new TilePane(8,8);
            directChildFlow.setPrefColumns(1);
            directChildFlow.getStyleClass().add("category-page-flow");
            addAllCategoriesSampleTiles(categoryPage, directChildFlow);
            main.getChildren().add(directChildFlow);
        }
        // wrap in scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(main);
        return scrollPane;
    }

    private void addAllCategoriesSampleTiles(CategoryPage categoryPage, TilePane pane) {
        for (TreeItem child:categoryPage.getChildren()) {
            Page page = (Page)child;
            if(page instanceof SamplePage) {
                pane.getChildren().add(((SamplePage)page).createTile());
            } else if(page instanceof CategoryPage) {
                addAllCategoriesSampleTiles((CategoryPage)page,pane);
            }
        }
    }

}
