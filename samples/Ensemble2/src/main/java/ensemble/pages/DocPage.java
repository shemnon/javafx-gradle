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

import ensemble.DocsHelper;
import ensemble.Ensemble2;
import ensemble.Page;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class/Interface Documentation Page
 *
 */
public class DocPage extends Page {
    private static final String WEB_VIEW_WINDOW_CORNER_STYLECLASS =  "web-view-window-corner";
    private static DocPane docPane;
    private String docUrl;
    private ObservableList<SamplePage> relatedSamples = FXCollections.observableArrayList();
    private String anchor;

    public DocPage(String className, String docUrl) {
        super(className);
        this.docUrl = docUrl;
    }

    public ObservableList<SamplePage> getRelatedSamples() { return relatedSamples; }
    public String getDocUrl() {
        if (anchor != null) {
            String url = docUrl + '#' + anchor;
            anchor = null;
            return url;
        }
        return docUrl;
    }

    /**
     * Set the anchor to use for the next time this doc page is displayed via createView() It is only used once
     * then cleared.
     * @return
     */
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    @Override public Node createView() {
        if (docPane==null) docPane = new DocPane();
        docPane.setDocPage(this);
        return docPane;
    }

    private static class DocPane extends Pane {
        private DocPage docPage;
        private WebEngine engine;
        private WebView webView;
        private ScrollPane sidePane;

        private DocPane() {
            webView = new WebView();
            webView.setContextMenuEnabled(false);
            engine = webView.getEngine();
            // listen to documents and let them know we are ensemble.ensemble
            engine.documentProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    // Add our custom css to document
                    Document doc = engine.getDocument();
                    if (doc == null) {
                        return;
                    }
                    if(engine.getTitle().contains("404")) {
                        // remove all children of body, ie all page content
                        org.w3c.dom.Node body = engine.getDocument().getElementsByTagName("body").item(0);
                        org.w3c.dom.Node first;
                        while ((first = body.getFirstChild()) != null) {
                            body.removeChild(first);
                        }
                        // put in message to user 
                        body.setTextContent("There is no documentation for this package.");
                        // add some css styling to make it look nicer
                        org.w3c.dom.Node head = doc.getDocumentElement().getElementsByTagName("head").item(0);
                        Element style = doc.createElement("style");
                        style.setTextContent(
                                        "body {\n" +
                                        "    background-color: #f6f6f6;\n" +
                                        "    color: #404040;\n" +
                                        "    font-family: sans-serif;\n" +
                                        "    font-size: 2em;\n" +
                                        "}\n");
                        if (head != null) head.appendChild(style);
                    } else {
                        org.w3c.dom.Node head = doc.getDocumentElement().getElementsByTagName("head").item(0);
                        Element style = doc.createElement("style");
                        style.setTextContent(
                                        //Remove nav bars for javadocs generated with JDK7
                                        "ul.navList { display: none; }\n" +
                                        "ul.subNavList { display: none; }\n" +
                                        "div.topNav { display: none; }\n" +
                                        "div.bottomNav { display: none; }\n" +
                                        "div.subNav { display: none; }\n" +   
                                        //Remove nav bars and change style for javadocs generated sith JDK6
                                        "body {\n" +
                                        "    background-color: #f6f6f6;\n" +
                                        "    color: #404040;\n" +
                                        "    font-family: sans-serif;\n" +
                                        "}\n" +
                                        "hr:first-of-type { display: none; }\n" +
                                        "hr:nth-of-type(2) { display: none; }\n" +
                                        "table[cellpadding=\"1\"]:first-of-type { display: none; }\n" +
                                        "table[cellpadding=\"1\"]:last-of-type { display: none; }\n" +
                                        "hr:last-of-type { display: none; }\n" +                     
                                        "img[src$=\"resources/inherit.gif\"] { display: none; }\n" +
                                        "h1, h2 {\n" +
                                        "    font-size: 170%;\n" +
                                        "    color: #0072aa;\n" +
                                        "    margin: 0; padding: 0;\n" +
                                        "}\n" +
                                        "h2 br { display: none; }\n" +
                                        "h2 font { display: block; }\n" +
                                        "a { color: #0072aa; }\n" +
                                        "hr {\n" +
                                        "    background-color: #c7c6c6;\n" +
                                        "    height: 1px;\n" +
                                        "    border: 0;\n" +
                                        "}\n" +
                                        "table,tr,td,th {\n" +
                                        "    border: 0;\n" +
                                        "    background: none;\n" +
                                        "}\n" +
                                        ".TableHeadingColor b {\n" +
                                        "    font-size: 12pt;\n" +
                                        "}\n" +
                                        ".TableHeadingColor {\n" +
                                        "    background: #0072aa;\n" +
                                        "    color: white;\n" +
                                        "}\n" +
                                        ".TableSubHeadingColor {\n" +
                                        "    background: #f6f6f6;\n" +
                                        "    border: 0;\n" +
                                        "}\n" +
                                        ".TableRowColor {\n" +
                                        "    background: none;\n" +
                                        "}");
                        if (head != null) head.appendChild(style);
                    }                   
                }
            });
            engine.locationProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    String newUrl = engine.getLocation();
                    String path = DocsHelper.getPagePath(newUrl, Ensemble2.getEnsemble2().getDocsUrl());
                    docPage = (DocPage) Ensemble2.getEnsemble2().getPages().getPage(path);
                    if (!Ensemble2.getEnsemble2().isFromForwardOrBackButton()) {
                        Ensemble2.getEnsemble2().updateCurrentPage(docPage);
                    }
                }
            });
            sidePane = new ScrollPane();
            sidePane.getStyleClass().add("noborder-scroll-pane");
            sidePane.setFitToWidth(true);
            getChildren().addAll(webView, sidePane);
        }

        public void setDocPage(DocPage docPage) {
            this.docPage = docPage;
            String docUrl = docPage.getDocUrl();
            // show content
            engine.load(docUrl);
            updateSidebar();
        }

        @Override protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            double sideWidth = 170;
            if (docPage != null && !docPage.getRelatedSamples().isEmpty()) {
                webView.resizeRelocate(0,0,w-sideWidth,h);
                sidePane.resizeRelocate(w-sideWidth,0,sideWidth,h);
            } else {
                webView.resizeRelocate(0,0,w,h);
            }
        }

        private void updateSidebar() {
            if (docPage != null) {
                ObservableList<SamplePage> relatedSamples = docPage.getRelatedSamples();
                if (!(relatedSamples.isEmpty())) {
                    sidePane.setContent(createSideBar(relatedSamples));
                    sidePane.setVisible(true);
                    webView.getStyleClass().remove(WEB_VIEW_WINDOW_CORNER_STYLECLASS);
                } else {
                    sidePane.setContent(new Pane());
                    sidePane.setVisible(false);
                    webView.getStyleClass().add(WEB_VIEW_WINDOW_CORNER_STYLECLASS);
                }
            } else {
                sidePane.setContent(new Pane());
                sidePane.setVisible(false);
                webView.getStyleClass().add(WEB_VIEW_WINDOW_CORNER_STYLECLASS);
            }
            requestLayout();
        }

        private Node createSideBar(ObservableList<SamplePage> relatedSamples) {
            GridPane sidebar = new GridPane() {
                // stretch to allways fill height of scrollpane
                @Override protected double computePrefHeight(double width) {
                    return Math.max(
                            super.computePrefHeight(width),
                            getParent().getBoundsInLocal().getHeight()
                    );
                }
            };
            sidebar.getStyleClass().add("right-sidebar");
            sidebar.setMaxWidth(Double.MAX_VALUE);
            sidebar.setMaxHeight(Double.MAX_VALUE);
            int sideRow = 0;
            // create side bar content
            // description
            Label discTitle = new Label("Related Samples");
            discTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(discTitle, 0, sideRow++);
            sidebar.getChildren().add(discTitle);
            // add sample tiles
            for (SamplePage sp: relatedSamples) {
                Node tile = sp.createTile();
                GridPane.setConstraints(tile, 0, sideRow++);
                sidebar.getChildren().add(tile);
            }
            return sidebar;
        }
    }
}
