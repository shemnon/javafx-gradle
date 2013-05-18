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
package ensemble;

import ensemble.config.ProxyDialog;
import ensemble.controls.BreadcrumbBar;
import ensemble.controls.SearchBox;
import ensemble.controls.WindowButtons;
import ensemble.controls.WindowResizeButton;
import ensemble.pages.SamplePage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import netscape.javascript.JSObject;

/**
 * Ensemble Main Application
 */
public class Ensemble2 extends Application {
    static {
        // Enable using system proxy if set
        System.setProperty("java.net.useSystemProxies", "true");
    }
    public static final String DEFAULT_DOCS_URL = "http://download.oracle.com/javafx/2/api/";
    
    private static Ensemble2 ensemble2;
    private static boolean isApplet = false;
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private ToolBar toolBar;
    private SplitPane splitPane;
    private TreeView pageTree;
    private Pane pageArea;
    private Pages pages;
    private Page currentPage;
    private String currentPagePath;
    private Node currentPageView;
    private BreadcrumbBar breadcrumbBar;
    private Stack<Page> history = new Stack<Page>();
    private Stack<Page> forwardHistory = new Stack<Page>();
    private boolean changingPage = false;
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    private WindowResizeButton windowResizeButton;
    public boolean fromForwardOrBackButton = false;
    private StackPane modalDimmer;
    private ProxyDialog proxyDialog;
    private ToolBar pageToolBar;
    private JSObject browser;
    private String docsUrl;
    
    /**
     * Get the singleton instance of Ensemble
     * 
     * @return The singleton instance
     */
    public static Ensemble2 getEnsemble2() {
        return ensemble2;
    }
    
    /**
     * Start the application
     * 
     * @param stage The main application stage
     */
    @Override public void start(final Stage stage) {
        ensemble2 = this;
        stage.setTitle("Ensemble");
        // set default docs location
        docsUrl = System.getProperty("docs.url") != null ?
                System.getProperty("docs.url") : DEFAULT_DOCS_URL; 
        // create root stack pane that we use to be able to overlay proxy dialog
        StackPane layerPane = new StackPane();
        // check if applet
        try {
            browser = getHostServices().getWebContext();
            isApplet =  browser != null;
        } catch (Exception e) {
            isApplet = false;
        }
        if (!isApplet) {
            stage.initStyle(StageStyle.UNDECORATED);
            // create window resize button
            windowResizeButton = new WindowResizeButton(stage, 1020,700);
            // create root
            root = new BorderPane() {
                @Override protected void layoutChildren() {
                    super.layoutChildren();
                    windowResizeButton.autosize();
                    windowResizeButton.setLayoutX(getWidth() - windowResizeButton.getLayoutBounds().getWidth());
                    windowResizeButton.setLayoutY(getHeight() - windowResizeButton.getLayoutBounds().getHeight());
                }
            };
            root.getStyleClass().add("application");
        } else {
            root = new BorderPane();
            root.getStyleClass().add("applet");
        }
        root.setId("root");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(root);
        // create scene
        boolean is3dSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
        scene = new Scene(layerPane, 1020, 700, is3dSupported);
        if (is3dSupported) {
            //RT-13234
            scene.setCamera(new PerspectiveCamera());
        }
        scene.getStylesheets().add(Ensemble2.class.getResource("ensemble2.css").toExternalForm());
        // create modal dimmer, to dim screen when showing modal dialogs
        modalDimmer = new StackPane();
        modalDimmer.setId("ModalDimmer");
        modalDimmer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                t.consume();
                hideModalMessage();
            }
        });
        modalDimmer.setVisible(false);
        layerPane.getChildren().add(modalDimmer);
        // create main toolbar
        toolBar = new ToolBar();
        toolBar.setId("mainToolBar");
        ImageView logo = new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/logo.png")));
        HBox.setMargin(logo, new Insets(0,0,0,5));
        toolBar.getItems().add(logo);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);
        Button highlightsButton = new Button();
        highlightsButton.setId("highlightsButton");
        highlightsButton.setMinSize(120, 66);
        highlightsButton.setPrefSize(120, 66);
        highlightsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                goToPage(Pages.HIGHLIGHTS);
            }
        });
        toolBar.getItems().add(highlightsButton);
        Button newButton = new Button();
        newButton.setId("newButton");
        newButton.setMinSize(120,66);
        newButton.setPrefSize(120,66);
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                goToPage(Pages.NEW);
            }
        });
        toolBar.getItems().add(newButton);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        toolBar.getItems().add(spacer2);
        ImageView searchTest = new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/search-text.png")));
        toolBar.getItems().add(searchTest);
        SearchBox searchBox = new SearchBox();
        HBox.setMargin(searchBox, new Insets(0,5,0,0));
        toolBar.getItems().add(searchBox);
        toolBar.setPrefHeight(66);
        toolBar.setMinHeight(66);
        toolBar.setMaxHeight(66);
        GridPane.setConstraints(toolBar, 0, 0);
        if (!isApplet) {
            // add close min max
            final WindowButtons windowButtons = new WindowButtons(stage);
            toolBar.getItems().add(windowButtons);
            // add window header double clicking
            toolBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        windowButtons.toogleMaximized();
                    }
                }
            });
            // add window dragging
            toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    mouseDragOffsetX = event.getSceneX();
                    mouseDragOffsetY = event.getSceneY();
                }
            });
            toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    if(!windowButtons.isMaximized()) {
                        stage.setX(event.getScreenX()-mouseDragOffsetX);
                        stage.setY(event.getScreenY()-mouseDragOffsetY);
                    }
                }
            });
        }
        // create page tree toolbar
        ToolBar pageTreeToolBar = new ToolBar() {
            @Override public void requestLayout() {
                super.requestLayout();
                // keep the height of pageToolBar in sync with pageTreeToolBar so they always match
                if (pageToolBar != null && getHeight() != pageToolBar.prefHeight(-1)) {
                    pageToolBar.setPrefHeight(getHeight());
                }
            }
        };
        pageTreeToolBar.setId("page-tree-toolbar");
        pageTreeToolBar.setMinHeight(29);
        pageTreeToolBar.setMaxWidth(Double.MAX_VALUE);
        ToggleGroup pageButtonGroup = new ToggleGroup();
        final ToggleButton allButton = new ToggleButton("All");
        allButton.setToggleGroup(pageButtonGroup);
        allButton.setSelected(true);
        final ToggleButton samplesButton = new ToggleButton("Samples");
        samplesButton.setToggleGroup(pageButtonGroup);
        final ToggleButton docsButton = new ToggleButton("Document");
        docsButton.setToggleGroup(pageButtonGroup);
        InvalidationListener treeButtonNotifyListener = new InvalidationListener() {
            public void invalidated(Observable ov) {
                if(allButton.isSelected()) {
                    pageTree.setRoot(pages.getRoot());
                } else if(samplesButton.isSelected()) {
                    pageTree.setRoot(pages.getSamples());
                } else if(docsButton.isSelected()) {
                    pageTree.setRoot(pages.getDocs());
                }
            }
        };
        allButton.selectedProperty().addListener(treeButtonNotifyListener);
        samplesButton.selectedProperty().addListener(treeButtonNotifyListener);
        docsButton.selectedProperty().addListener(treeButtonNotifyListener);
        pageTreeToolBar.getItems().addAll(allButton, samplesButton, docsButton);
        // create page tree
        pages = new Pages();
        proxyDialog = new ProxyDialog(stage, pages);
        proxyDialog.loadSettings();
        proxyDialog.getDocsInBackground(true,null);
        pages.parseSamples();
        pageTree = new TreeView();
        pageTree.setId("page-tree");
        pageTree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pageTree.setRoot(pages.getRoot());
        pageTree.setShowRoot(false);
        pageTree.setEditable(false);
        pageTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pageTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {
                if (!changingPage) {
                    Page selectedPage = (Page)pageTree.getSelectionModel().getSelectedItem();
                    if (selectedPage!=pages.getRoot()) goToPage(selectedPage);
                }
            }
        });
        // create left split pane
        BorderPane leftSplitPane = new BorderPane();
        leftSplitPane.setTop(pageTreeToolBar);
        leftSplitPane.setCenter(pageTree);
        // create page toolbar
        pageToolBar = new ToolBar();
        pageToolBar.setId("page-toolbar");
        pageToolBar.setMinHeight(29);
        pageToolBar.setMaxSize(Double.MAX_VALUE, Control.USE_PREF_SIZE);
        if (!isApplet) {
            Button backButton = new Button();
            backButton.setGraphic(new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/back.png"))));
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) { back(); }
            });
            backButton.setMaxHeight(Double.MAX_VALUE);
            Button forwardButton = new Button();
            forwardButton.setGraphic(new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/forward.png"))));
            forwardButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    forward();
                }
            });
            forwardButton.setMaxHeight(Double.MAX_VALUE);
            Button reloadButton = new Button();
            reloadButton.setGraphic(new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/reload.png"))));
            reloadButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    reload();
                }
            });
            reloadButton.setMaxHeight(Double.MAX_VALUE);
            pageToolBar.getItems().addAll(backButton, forwardButton, reloadButton);
        }
        breadcrumbBar = new BreadcrumbBar();
        pageToolBar.getItems().add(breadcrumbBar);
        if (!isApplet) {
            Region spacer3 = new Region();
            HBox.setHgrow(spacer3, Priority.ALWAYS);
            Button settingsButton = new Button();
            settingsButton.setId("SettingsButton");
            settingsButton.setGraphic(new ImageView(new Image(Ensemble2.class.getResourceAsStream("images/settings.png"))));
            settingsButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    showProxyDialog();
                }
            });
            settingsButton.setMaxHeight(Double.MAX_VALUE);
            pageToolBar.getItems().addAll(spacer3, settingsButton);
        }
        // create page area
        pageArea = new Pane() {
            @Override protected void layoutChildren() {
                for (Node child:pageArea.getChildren()) {
                    child.resizeRelocate(0, 0, pageArea.getWidth(), pageArea.getHeight());
                }
            }
        };
        pageArea.setId("page-area");
        // create right split pane
        BorderPane rightSplitPane = new BorderPane();
        rightSplitPane.setTop(pageToolBar);
        rightSplitPane.setCenter(pageArea);
        // create split pane
        splitPane = new SplitPane();
        splitPane.setId("page-splitpane");
        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setConstraints(splitPane, 0, 1);
        splitPane.getItems().addAll(leftSplitPane, rightSplitPane);
        splitPane.setDividerPosition(0, 0.25);

        this.root.setTop(toolBar);
        this.root.setCenter(splitPane);
        // add window resize button so its on top
        if (!isApplet) {
            windowResizeButton.setManaged(false);
            this.root.getChildren().add(windowResizeButton);
        }
        // expand first level of the tree
        for (TreeItem child: pages.getRoot().getChildren()) {
            if (child == pages.getHighlighted() || child == pages.getNew()) continue;
            child.setExpanded(true);
            for (TreeItem child2: (ObservableList<TreeItem<String>>)child.getChildren()) {
                child2.setExpanded(true);
            }
        }
        // goto initial page
        if (isApplet) {
            String hashLoc = getBrowserHashLocation();
            if (hashLoc != null) {
                goToPage(hashLoc);
            } else {
                // default to all samples
                goToPage(pages.getSamples());
            }
        } else {
            // default to all samples
            goToPage(pages.getSamples());
        }
        // show stage
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Called from JavaScript in the browser when the page hash location changes
     * 
     * @param hashLoc The new has location, e.g. #SAMPLES
     */
    public void hashChanged(String hashLoc) {
        if (hashLoc != null) {
            // remove # 
            if (hashLoc.length() == 0) {
                hashLoc = null;
            } else {
                hashLoc = hashLoc.substring(1);
            }
            // if new page then navigate to it
            final String path = hashLoc;
            Platform.runLater(new Runnable() {
                public void run() {
                    if (!path.equals(currentPagePath)) goToPage(path);
                }
            });
        }
    }
    
    /**
     * Get the URL of the java doc root directory being used to get 
     * documentation from
     * 
     * @return Documentation directory URL
     */
    public String getDocsUrl() {
        return docsUrl;
    }

    /**
     * Set the URL of the java doc root directory being used to get 
     * documentation from
     * 
     * @param docsUrl Documentation directory URL
     */
    public void setDocsUrl(String docsUrl) {
        this.docsUrl = docsUrl;
    }
    
    /**
     * Fetch the current hash location from the browser via JavaScript
     * 
     * @return Current browsers hash location
     */
    private String getBrowserHashLocation() {
        String hashLoc = null;
        try {
            hashLoc = (String)browser.eval("window.location.hash");
        } catch (Exception e) {
            try {
                System.out.println("Warning failed to get browser location, retrying...");
                hashLoc = (String)browser.eval("window.location.hash");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        // remove #
        if (hashLoc != null) {
            if (hashLoc.length() == 0) {
                hashLoc = null;
            } else {
                hashLoc = hashLoc.substring(1);
            }
        }
        return hashLoc;
    }
    
    /**
     * Show the given node as a floating dialog over the whole application, with 
     * the rest of the application dimmed out and blocked from mouse events.
     * 
     * @param message 
     */
    public void showModalMessage(Node message) {
        modalDimmer.getChildren().add(message);
        modalDimmer.setOpacity(0);
        modalDimmer.setVisible(true);
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1), 
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),1, Interpolator.EASE_BOTH)
        )).build().play();
    }
    
    /**
     * Hide any modal message that is shown
     */
    public void hideModalMessage() {
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1), 
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                        modalDimmer.setVisible(false);
                        modalDimmer.getChildren().clear();
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),0, Interpolator.EASE_BOTH)
        )).build().play();
    }
    
    /**
     * Get the pages object that contains the tree of all avaliable pages
     * 
     * @return Pages containing tree of all pages
     */
    public Pages getPages() {
        return pages;
    }

    /**
     * Change to new page without swapping views, assumes that the current view 
     * is already showing the new page
     * 
     * @param page The new page object
     */
    public void updateCurrentPage(Page page) {
        goToPage(page, true,false,false);
    }
    
    /**
     * Take ensemble to the given page path, navigating there and adding 
     * current page to history
     * 
     * @param pagePath The path for the new page to show
     */
    public void goToPage(String pagePath) {
        goToPage(pages.getPage(pagePath));
    }

    /**
     * Take ensemble to the given page path, navigating there and adding 
     * current page to history.
     * 
     * @param pagePath  The path for the new page to show
     * @param force     Reload page even if its the current page
     */
    public void goToPage(String pagePath, boolean force) {
        goToPage(pages.getPage(pagePath),true,force,true);
    }

    /**
     * Take ensemble to the given page object, navigating there and adding 
     * current page to history.
     * 
     * @param page Page object to show
     */
    public void goToPage(Page page) {
        goToPage(page, true, false, true);
    }
    
    /**
     * Take ensemble to the given page object, navigating there.
     * 
     * @param page          Page object to show
     * @param addHistory    When true add current page to history before navigating
     * @param force         When true reload page if page is current page
     * @param swapViews     If view should be swapped to new page
     */
    private void goToPage(Page page, boolean addHistory, boolean force, boolean swapViews) {
        if(page==null) return;
        if(!force && page == currentPage) {
            return;
        }
        changingPage = true;
        if (swapViews) {
            Node view = page.createView();
            if (view == null) view = new Region(); // todo temp workaround
            // replace view in pageArea if new
            if (force || view != currentPageView) {
                for (Node child:pageArea.getChildren()){
                    if (child instanceof SamplePage.SamplePageView) {
                        ((SamplePage.SamplePageView)child).stop();
                    }
                }
                pageArea.getChildren().setAll(view);
                currentPageView = view;
            }
        }
        // add page to history
        if (addHistory && currentPage!=null) {
            history.push(currentPage);
            forwardHistory.clear();
        }
        currentPage = page;
        currentPagePath = page.getPath();
        // when in applet update location bar
        if (isApplet) {
            try {
                browser.eval("window.location.hash='"+currentPagePath+"';");
            } catch (Exception e) {
                try {
                    System.out.println("Warning failed to set browser location, retrying...");
                    browser.eval("window.location.hash='"+currentPagePath+"';");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        // expand tree to selected node
        Page p = page;
        while (p!=null) {
            p.setExpanded(true);
            p = (Page) p.getParent();
        }
        // update tree selection
        pageTree.getSelectionModel().select(page);
        // update breadcrumb bar
        breadcrumbBar.setPath(currentPagePath);
        // done
        changingPage = false;
    }
    
    /**
     * Check if current call stack was from back or forward button's action
     * 
     * @return True if current call was caused by action on back or forward button
     */
    public boolean isFromForwardOrBackButton() {
        return fromForwardOrBackButton;
    }
    
    /**
     * Got to previous page in history
     */
    public void back() {
        fromForwardOrBackButton = true;
        if (!history.isEmpty()) {      
            Page prevPage = history.pop();
            forwardHistory.push(currentPage);
            goToPage(prevPage,false, false, true);
        }
        fromForwardOrBackButton = false;
    }
    
    /**
     * Utility method for viewing the page history
     */
    private void printHistory() {
        System.out.print("   HISTORY = ");
        for (Page page:history) {
            System.out.print(page.getName()+"->");
        }
        System.out.print("   ["+currentPage.getName()+"]");
        for (Page page:forwardHistory) {
            System.out.print(page.getName()+"->");
        }
        System.out.print("\n");
    }

    /**
     * Go to next page in history if there is one
     */
    public void forward() {
        fromForwardOrBackButton = true;
        if (!forwardHistory.isEmpty()) {
            Page prevPage = forwardHistory.pop();
            history.push(currentPage);
            goToPage(prevPage,false, false, true);
        }
        fromForwardOrBackButton = false;
    }
    
    /**
     * Reload the current page
     */
    public void reload() {
        goToPage(currentPage,false,true, true);
    }
    
    /**
     * Show the dialog for setting proxy to the user
     */
    public void showProxyDialog() {
        showModalMessage(proxyDialog);
    }
    
    /**
     * Java Main Method for launching application when not using JavaFX 
     * Launcher, eg from IDE without JavaFX support
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
