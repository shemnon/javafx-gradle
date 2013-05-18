
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

import ensemble.*;
import ensemble.model.SampleInfo;
import ensemble.sampleproject.SampleProjectBuilder;
import ensemble.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

/**
 * SamplePage
 */
public class SamplePage extends Page {
    private static WebEngine engine = null;
    private static WebView webView = null;
    private SampleInfo sampleInfo;
    private Class sampleClass;
    private String rawCode;
    private String htmlCode;

    public SamplePage(String name, String sourceFileUrl) throws IllegalArgumentException {
        super(name);
        String unqualifiedClassName = sourceFileUrl.substring(sourceFileUrl.lastIndexOf('/')+1,
                sourceFileUrl.length()-5);
        try {
            // load src into String
            StringBuilder builder = new StringBuilder();
            URI uri = new URI(sourceFileUrl);
            InputStream in = uri.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            reader.close();
            // parse sample info
            sampleInfo = new SampleInfo(sourceFileUrl, unqualifiedClassName, builder.toString());
            // load class
            sampleClass = getClass().getClassLoader().loadClass(sampleInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add API back references
        Ensemble2 ensemble2 = Ensemble2.getEnsemble2();
        for (String apiClassPath : sampleInfo.getApiClasspaths()) {
            String path = Pages.API_DOCS+'/'+apiClassPath.replace('.','/');
            DocPage docPage = (DocPage) ensemble2.getPages().getPage(path);
            if (docPage != null) {
                docPage.getRelatedSamples().add(this);
//            } else {
//                System.err.println("Failed to find documentation page for classpath ["+apiClassPath+"] from @see in sample ["+sourceFileUrl+"]");
            }
        }
    }

    public SamplePage(SamplePage pageToClone) {
        super(pageToClone.getName());
        this.sampleInfo = pageToClone.sampleInfo;
        this.sampleClass = pageToClone.sampleClass;
    }

    public SampleInfo getSampleInfo() {
        return sampleInfo;
    }

    @Override public Node createView() {
        // check if 3d sample and on supported platform
        //System.out.println("sampleClass.getSuperclass() == Sample3D.class = " + (sampleClass.getSuperclass() == Sample3D.class));
        //System.out.println("Platform.isSupported(ConditionalFeature.SCENE3D) = " + Platform.isSupported(ConditionalFeature.SCENE3D));
        if (sampleClass.getSuperclass() == Sample3D.class && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            Label error =  new Label("JavaFX 3D is currently not supported on your configuration.");
            error.setStyle("-fx-text-fill: orangered; -fx-font-size: 1.4em;");
            error.setWrapText(true);
            error.setAlignment(Pos.CENTER);
            error.setTextAlignment(TextAlignment.CENTER);
            return error;
        }
        //  load the code
        loadCode();
        try {
            // create main grid
            //final FlowSafeVBox main = new FlowSafeVBox();
            final VBox main = new VBox(8);
            main.getStyleClass().add("sample-page");
            // create header
            Label header = new Label(getName());
            header.getStyleClass().add("page-header");
            main.getChildren().add(header);
            // create sample area
            final StackPane sampleArea = new StackPane();
            VBox.setVgrow(sampleArea, Priority.SOMETIMES);
            main.getChildren().add(sampleArea);
            // create sample
            final Sample sample = (Sample)sampleClass.newInstance();
            sampleArea.getChildren().add(sample);
            // create sample controls
            Node sampleControls = sample.getControls();
            if(sampleControls != null) {
                Label subHeader = new Label("Play with these:");
                subHeader.getStyleClass().add("page-subheader");
                main.getChildren().add(subHeader);
                main.getChildren().add(sampleControls);
            }
            // create code view
            WebView webView = getWebView();
            webView.setPrefWidth(300);
            engine.loadContent(htmlCode);
            ToolBar codeToolBar = new ToolBar();
            codeToolBar.setId("code-tool-bar");
            final Button saveProjectButton = new Button("Save Netbeans Project...");
            saveProjectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    File initialDir = FileSystemView.getFileSystemView().getDefaultDirectory();
                    ///System.out.println("initialDir = " + initialDir);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Netbeans Project As:");
                    fileChooser.setInitialDirectory(initialDir);
                    File result = fileChooser.showSaveDialog(saveProjectButton.getScene().getWindow());
                    if (result != null) SampleProjectBuilder.createSampleProject(result, sampleInfo.getSourceFileUrl(), sampleInfo.getResourceUrls());
                }
            });
            Button copyCodeButton = new Button("Copy Source");
            copyCodeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    Map<DataFormat,Object> clipboardContent = new HashMap<DataFormat, Object>();
                    clipboardContent.put(DataFormat.PLAIN_TEXT, rawCode);
                    clipboardContent.put(DataFormat.HTML, htmlCode);
                    Clipboard.getSystemClipboard().setContent(clipboardContent);
                }
            });
            codeToolBar.getItems().addAll(saveProjectButton,copyCodeButton);
            BorderPane codeTabPane = new BorderPane();
            codeTabPane.setTop(codeToolBar);
            codeTabPane.setCenter(webView);

            // create border pane for main content and sidebar
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(main);
            borderPane.setRight(createSideBar(sample));
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.getStyleClass().add("noborder-scroll-pane");
            scrollPane.setContent(borderPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setMinWidth(725);
            // create tab pane
            final TabPane tabPane = new SamplePageView(sample);
            tabPane.setId("source-tabs");
            final Tab sampleTab = new Tab();
            sampleTab.setText("Sample");
            sampleTab.setContent(scrollPane);
            sampleTab.setClosable(false);
            final Tab sourceTab = new Tab();
            sourceTab.setText("Source Code");
            sourceTab.setContent(codeTabPane);
            sourceTab.setClosable(false);
            tabPane.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
                @Override public void invalidated(Observable ov) {
                    if (tabPane.getSelectionModel().getSelectedItem() == sampleTab) {
                        sample.play();
                    } else {
                        sample.stop();
                    }
                }
            });
            tabPane.getTabs().addAll(sampleTab,sourceTab);
            return tabPane;
        } catch (Exception e) {
            e.printStackTrace();
            return new Text("Failed to create sample because of ["+e.getMessage()+"]");
        }
    }

    private Node createSideBar(Sample sample) {
        GridPane sidebar = new GridPane();
        sidebar.getStyleClass().add("right-sidebar");
        sidebar.setMaxWidth(Double.MAX_VALUE);
        sidebar.setMaxHeight(Double.MAX_VALUE);
        int sideRow = 0;
        // create side bar content
        // description
        Label discTitle = new Label("Description");
        discTitle.getStyleClass().add("right-sidebar-title");
        GridPane.setConstraints(discTitle, 0, sideRow++);
        sidebar.getChildren().add(discTitle);
        Text disc = new Text(sampleInfo.getDescription());
        disc.setWrappingWidth(200);
        disc.getStyleClass().add("right-sidebar-body");
        GridPane.setConstraints(disc, 0, sideRow++);
        sidebar.getChildren().add(disc);
        // docs
        if (sampleInfo.getApiClasspaths()!=null && sampleInfo.getApiClasspaths().length>0) {
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label docsTitle = new Label("API Documentation");
            docsTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(docsTitle, 0, sideRow++);
            sidebar.getChildren().add(docsTitle);
            for (String docPath:sampleInfo.getApiClasspaths()) {
                Hyperlink link = new Hyperlink(docPath);
                link.setOnAction(new GoToPageEventHandler(Pages.API_DOCS+'/'+docPath.replace('.','/')));
                GridPane.setConstraints(link, 0, sideRow++);
                sidebar.getChildren().add(link);
            }
        }
        // related
        if (sampleInfo.getRelatesSamplePaths()!=null && sampleInfo.getRelatesSamplePaths().length>0) {
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label relatedTitle = new Label("Related");
            relatedTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(relatedTitle, 0, sideRow++);
            sidebar.getChildren().add(relatedTitle);
            for (String relatedPath:sampleInfo.getRelatesSamplePaths()) {
                String[] parts = relatedPath.split("/");
                Hyperlink link = new Hyperlink(parts[parts.length-1]);
                //convert path
                String path = "";
                for(String part:parts) {
                    path = path+'/'+ SampleHelper.formatName(part);
                }
                link.setOnAction(new GoToPageEventHandler(Pages.SAMPLES+path));
                ///System.out.println("Pages.SAMPLES+path==>" + Pages.SAMPLES + path);
                GridPane.setConstraints(link, 0, sideRow++);
                sidebar.getChildren().add(link);
            }
        }
        // resources
        // TODO add back in later
//        if (resourceUrls!=null && resourceUrls.length>0) {
//            Separator separator = new Separator();
//            separator.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//            sidebar.getChildren().add(separator);
//            Label docsTitle = new Label("Resources");
//            docsTitle.getStyleClass().add("right-sidebar-title");
//            docsTitle.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//            sidebar.getChildren().add(docsTitle);
//            for (String resourceUrl:resourceUrls) {
//                String[] parts = resourceUrl.split("/");
//                Hyperlink link = new Hyperlink(parts[parts.length-1]);
//                link.setLayoutInfo(new GridLayoutInfo(sideRow++, 0));
//                sidebar.getChildren().add(link);
//            }
//        }
        // sample extras
        Node sampleExtras = sample.getSideBarExtraContent();
        if (sampleExtras != null) {
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label docsTitle = new Label(sample.getSideBarExtraContentTitle());
            docsTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(docsTitle, 0, sideRow++);
            sidebar.getChildren().add(docsTitle);
            GridPane.setConstraints(sampleExtras, 0, sideRow++);
            sidebar.getChildren().add(sampleExtras);
        }
        return sidebar;
    }

    private Node getIcon() {
        URL url = sampleClass.getResource(sampleClass.getSimpleName()+".png");
        if (url != null) {
            ImageView imageView = new ImageView(new Image(url.toString()));
            return imageView;
        } else {
            ImageView imageView = new ImageView(new Image(Ensemble2.class.getResource("images/icon-overlay.png").toString()));
            imageView.setMouseTransparent(true);
            Rectangle overlayHighlight = new Rectangle(-8,-8,130,130);
            overlayHighlight.setFill(new LinearGradient(0,0.5,0,1,true, CycleMethod.NO_CYCLE, new Stop[]{ new Stop(0,Color.BLACK), new Stop(1,Color.web("#444444"))}));
            overlayHighlight.setOpacity(0.8);
            overlayHighlight.setMouseTransparent(true);
            overlayHighlight.setBlendMode(BlendMode.ADD);
            Rectangle background = new Rectangle(-8,-8,130,130);
            background.setFill(Color.web("#b9c0c5"));
            Group group = new Group(background);
            Rectangle clipRect = new Rectangle(114,114);
            clipRect.setArcWidth(38);
            clipRect.setArcHeight(38);
            group.setClip(clipRect);
            Node content = createIconContent();
            if (content != null) {
                content.setTranslateX((int)((114-content.getBoundsInParent().getWidth())/2)-(int)content.getBoundsInParent().getMinX());
                content.setTranslateY((int)((114-content.getBoundsInParent().getHeight())/2)-(int)content.getBoundsInParent().getMinY());
                group.getChildren().add(content);
            }
            group.getChildren().addAll(overlayHighlight,imageView);
            // Wrap in extra group as clip dosn't effect layout without it
            return new Group(group);
        }
    }

    public Node createIconContent() {
        try {
            Method createIconContent = sampleClass.getDeclaredMethod("createIconContent");
            return (Node)createIconContent.invoke(sampleClass);
        } catch (NoSuchMethodException e) {
            System.err.println("Sample ["+getName()+"] is missing a icon.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node createTile() {
        Button tile = new Button(getName().trim(),getIcon());
        tile.setMinSize(140,145);
        tile.setPrefSize(140,145);
        tile.setMaxSize(140,145);
        tile.setContentDisplay(ContentDisplay.TOP);
        tile.getStyleClass().clear();
        tile.getStyleClass().add("sample-tile");
        tile.setOnAction(new EventHandler() {
            public void handle(Event event) {
                Ensemble2.getEnsemble2().goToPage(SamplePage.this);
            }
        });
        return tile;
    }

    protected WebView getWebView() {
        if (engine == null) {
            webView = new WebView();
            webView.setContextMenuEnabled(false);
            engine = webView.getEngine();
        }
        return webView;
    }

    private String shCoreJs;
    private String shBrushJScript;
    private String shCoreDefaultCss;


    private void loadCode() {
        // load syntax highlighter
        if (shCoreJs == null) shCoreJs = Utils.loadFile(Ensemble2.class.getResource("syntaxhighlighter/shCore.js")) +";";
        if (shBrushJScript == null) shBrushJScript = Utils.loadFile(Ensemble2.class.getResource("syntaxhighlighter/shBrushJava.js"));
        if (shCoreDefaultCss == null) shCoreDefaultCss =
                Utils.loadFile(Ensemble2.class.getResource("syntaxhighlighter/shCoreDefault.css")).replaceAll("!important","");
        // load and convert source
        String source = SampleProjectBuilder.loadAndConvertSampleCode(sampleInfo.getSourceFileUrl());
        // store raw code
        rawCode = source;
        // escape < & >
        source = source.replaceAll("&","&amp;");
        source = source.replaceAll("<","&lt;");
        source = source.replaceAll(">","&gt;");
        source = source.replaceAll("\"","&quot;");
        source = source.replaceAll("\'","&apos;");
        // create content
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("    <head>\n");
        html.append("    <script type=\"text/javascript\">\n");
        html.append(shCoreJs);
        html.append('\n');
        html.append(shBrushJScript);
        html.append("    </script>\n");
        html.append("    <style>\n");
        html.append(shCoreDefaultCss);
        html.append('\n');
        html.append("        .syntaxhighlighter {\n");
        html.append("			overflow: visible;\n");
        if (Utils.isMac()) {
            html.append("			font: 12px Ayuthaya !important; line-height: 150% !important; \n");
            html.append("		}\n");
            html.append("		code { font: 12px Ayuthaya !important; line-height: 150% !important; } \n");
        } else {
            html.append("			font: 12px monospace !important; line-height: 150% !important; \n");
            html.append("		}\n");
            html.append("		code { font: 12px monospace !important; line-height: 150% !important; } \n");
        }
        html.append("		.syntaxhighlighter .preprocessor { color: #060 !important; }\n");
        html.append("		.syntaxhighlighter .comments, .syntaxhighlighter .comments a  { color: #009300 !important; }\n");
        html.append("		.syntaxhighlighter .string  { color: #555 !important; }\n");
        html.append("		.syntaxhighlighter .value  { color: blue !important; }\n");
        html.append("		.syntaxhighlighter .keyword  { color: #000080 !important; }\n");
        html.append("    </style>\n");
        html.append("    </head>\n");
        html.append("<body>\n");
        html.append("    <pre class=\"brush: java;gutter: false;toolbar: false;\">\n");
        html.append(source);
        html.append('\n');
        html.append(
                "    </pre>\n" +
                        "    <script type=\"text/javascript\"> SyntaxHighlighter.all(); </script>\n" +
                        "</body>\n" +
                        "</html>\n");
        htmlCode = html.toString();
    }

    public static class SamplePageView extends TabPane {
        private Sample sample;

        public SamplePageView(Sample sample) {
            super();
            this.sample = sample;
        }

        public void stop() {
            sample.stop();
        }
    }
}
