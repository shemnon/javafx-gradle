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
package ensemble.config;

import ensemble.DocsHelper;
import ensemble.Ensemble2;
import ensemble.Pages;
import ensemble.pages.CategoryPage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ProxyDialog extends VBox {
    private static final String PROXY_ADDRESS = "ProxyAddress";
    private static final String PROXY_PORT = "ProxyPort";
    private static final String DOCS_LOCATION = "DocsLocation";
    /** Drag offsets for window dragging */
    private double mouseDragOffsetX, mouseDragOffsetY;
    private Button okBtn;
    private Pages pages;
    private final Window owner;
    private final LocalDirPanel localDirPanel;
    private final ProxyPanel proxyPanel;
    private final Tab proxyTab;
    private final Tab docsTab;
    private final TabPane options;

    public ProxyDialog(final Window owner, final Pages pages) {
        this.owner = owner;
        this.pages = pages;
        setId("ProxyDialog");
        setSpacing(10);
        setMaxSize(430, USE_PREF_SIZE);
        // block mouse clicks
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                t.consume();
            }
        });

        Text explanation = new Text("An Internet connection is required to "
                + "access some samples and documentation. Either specify a web "
                + "proxy or a path to the documentation installed locally.");
        explanation.setWrappingWidth(400);

        BorderPane explPane = new BorderPane();
        VBox.setMargin(explPane, new Insets(5, 5, 5, 5));
        explPane.setCenter(explanation);
        BorderPane.setMargin(explanation, new Insets(5, 5, 5, 5));

        // create title
        Label title = new Label("Internet Access Needed");
        title.setId("title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        getChildren().add(title);

        proxyTab = new Tab("Set Proxy");
        proxyPanel = new ProxyPanel();
        proxyTab.setContent(proxyPanel);

        docsTab = new Tab("Locate Docs Locally");
        localDirPanel = new LocalDirPanel();
        docsTab.setContent(localDirPanel);

        options = new TabPane();
        options.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        options.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        options.getTabs().addAll(proxyTab, docsTab);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setId("cancelButton");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                Ensemble2.getEnsemble2().hideModalMessage();
            }
        });
        cancelBtn.setMinWidth(74);
        cancelBtn.setPrefWidth(74);
        HBox.setMargin(cancelBtn, new Insets(0, 8, 0, 0));
        okBtn = new Button("Save");
        okBtn.setId("saveButton");
        okBtn.setDefaultButton(true);
        okBtn.setDisable(true);
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                final Properties settings = new Properties();
                if (options.getSelectionModel().getSelectedItem() == proxyTab) {
                    settings.put(PROXY_ADDRESS, proxyPanel.getHostNameBox().getText());
                    settings.put(PROXY_PORT, proxyPanel.getPortBox().getText());
                    //proxy for internet
                    setWebProxy(proxyPanel.getHostNameBox().getText(), proxyPanel.getPortBox().getText());
                } else {
                    settings.put(DOCS_LOCATION, localDirPanel.getDocsUrl());
                    retrieveLocalDocs(localDirPanel.getDocsUrl());
                }
                // hide dialog
                Ensemble2.getEnsemble2().hideModalMessage();
                // try fetching docs
                getDocsInBackground(false, new Runnable() {
                    public void run() {
                        // save settings to file if we were successful getting docs with the settings
                        try {
                            File ensembleSettings = new File(System.getProperty("user.home"),".ensemble-settings");
                            FileOutputStream fout = new FileOutputStream(ensembleSettings);
                            settings.save(fout, "JavaFX 2.0 - Ensemble Settings");
                            fout.flush();
                            fout.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        okBtn.setMinWidth(74);
        okBtn.setPrefWidth(74);

        HBox bottomBar = new HBox(0);
        bottomBar.setAlignment(Pos.BASELINE_RIGHT);
        bottomBar.getChildren().addAll(cancelBtn, okBtn);
        VBox.setMargin(bottomBar, new Insets(20, 5, 5, 5));

        getChildren().addAll(explPane, options, bottomBar);
    }

    public void getDocsInBackground(final boolean showProxyDialogOnFail, final Runnable callBackOnSuccess) {
        final FetchDocListTask task = new FetchDocListTask(Ensemble2.getEnsemble2().getDocsUrl());
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    // extract all the docs pages from the all classes page
                    DocsHelper.extractDocsPagesFromAllClassesPage(
                            (CategoryPage)Ensemble2.getEnsemble2().getPages().getDocs(),
                            task.getValue(), 
                            Ensemble2.getEnsemble2().getDocsUrl());
                    // update docs pages cross links to samples
                    DocsHelper.syncDocPagesAndSamplePages(
                            (CategoryPage)Ensemble2.getEnsemble2().getPages().getSamples());
                    if (callBackOnSuccess != null) callBackOnSuccess.run();
                } else if (newState == Worker.State.FAILED) {
                    if (showProxyDialogOnFail) {
                        Ensemble2.getEnsemble2().showProxyDialog();
                    }
                }
            }
        });
        new Thread(task).start();
    }
    
    /**
     * Load proxy or documentation location settings from properties
     */
    public void loadSettings() {
        // check if we have saved setings
        File ensembleSettings = new File(System.getProperty("user.home"),".ensemble-settings");
        if (ensembleSettings.exists() && ensembleSettings.isFile()) {
            final Properties settings = new Properties();
            try {
                settings.load(new FileInputStream(ensembleSettings));
                if (settings.containsKey(DOCS_LOCATION)) {
                    String location = settings.getProperty(DOCS_LOCATION);
                    options.getSelectionModel().select(docsTab);
                    localDirPanel.setDocsUrl(location);
                    retrieveLocalDocs(location);
                } else if (settings.containsKey(PROXY_ADDRESS) && settings.containsKey(PROXY_PORT)) {
                    String address = settings.getProperty(PROXY_ADDRESS);
                    String port = settings.getProperty(PROXY_PORT);
                    proxyPanel.hostNameBox.setText(address);
                    proxyPanel.portBox.setText(port);
                    options.getSelectionModel().select(proxyTab);
                    setWebProxy(address, port);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWebProxy(String hostName, String port) {
        if (hostName != null) {
            System.setProperty("http.proxyHost", hostName);
            if (port != null) {
                System.setProperty("http.proxyPort", port);
            }
        }
        Ensemble2.getEnsemble2().setDocsUrl(Ensemble2.DEFAULT_DOCS_URL);
    }

    public void retrieveLocalDocs(String docUrl) {
        Ensemble2.getEnsemble2().setDocsUrl(docUrl);
    }

    private class ProxyPanel extends GridPane {
        TextField hostNameBox;
        TextField portBox;

        public ProxyPanel() {
            setPadding(new Insets(8));
            setHgap(5.0F);
            setVgap(5.0F);

            int rowIndex = 0;

            Label label2 = new Label("Host Name");
            label2.setId("proxy-dialog-label");
            GridPane.setConstraints(label2, 0, rowIndex);

            Label label3 = new Label("Port");
            label3.setId("proxy-dialog-label");
            GridPane.setConstraints(label3, 1, rowIndex);
            getChildren().addAll(label2, label3);

            rowIndex++;
            hostNameBox = new TextField();
            hostNameBox.setPromptText("proxy.mycompany.com");
            hostNameBox.setPrefColumnCount(20);
            GridPane.setConstraints(hostNameBox, 0, rowIndex);

            portBox = new TextField();
            portBox.setPromptText("8080");
            portBox.setPrefColumnCount(10);
            GridPane.setConstraints(portBox, 1, rowIndex);

            ChangeListener<String> textListener = new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    okBtn.setDisable(
                            hostNameBox.getText() == null || hostNameBox.getText().isEmpty()
                            || portBox.getText() == null || portBox.getText().isEmpty());
                }
            };
            hostNameBox.textProperty().addListener(textListener);
            portBox.textProperty().addListener(textListener);

            getChildren().addAll(hostNameBox, portBox);
        }

        public TextField getHostNameBox() {
            return hostNameBox;
        }

        public TextField getPortBox() {
            return portBox;
        }
    }

    private class LocalDirPanel extends GridPane {
        private String docsUrl = null;
        private final TextField textField;

        public LocalDirPanel() {
            setPadding(new Insets(8));
            setHgap(5.0F);
            setVgap(5.0F);

            int rowIndex = 0;
            Label parentDirLabel = new Label("Local javadoc index.html file");
            parentDirLabel.setId("parent-dir-label");
            GridPane.setConstraints(parentDirLabel, 0, rowIndex);
            getChildren().add(parentDirLabel);

            rowIndex++;
            textField = new TextField();
            textField.setEditable(false);
            GridPane.setConstraints(textField, 0, rowIndex,1,1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

            Button button = new Button("Browse...");
            button.setId("browseButton");
            button.setMinWidth(USE_PREF_SIZE);
            GridPane.setConstraints(button, 1, rowIndex);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("JavaFX 2.0 Javadoc location");
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("html", "*.html");
                    fileChooser.getExtensionFilters().add(filter);
                    File selectedFile = fileChooser.showOpenDialog(owner);
                    
                    okBtn.setDisable(selectedFile == null);
                    if (selectedFile != null) {
                        textField.setText(selectedFile.getAbsolutePath());
                        docsUrl = selectedFile.toURI().toString();
                        docsUrl = docsUrl.substring(0,docsUrl.lastIndexOf('/') + 1);
                    }
                }
            });
            getChildren().addAll(textField, button);
        }

        public String getDocsUrl() {
            return docsUrl;
        }

        public void setDocsUrl(String docsUrl) {
            this.docsUrl = docsUrl;
            textField.setText(docsUrl+"index.html");
        }
    }
}
