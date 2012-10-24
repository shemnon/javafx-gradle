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
package ensemble.samples.scenegraph.node;

import ensemble.Sample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A sample that demonstrates how to create a custom node by extending the
 * Parent class.
 *
 * @see javafx.scene.Parent
 * @see javafx.scene.Node
 */
public class CustomNodeSample extends Sample {

    public CustomNodeSample() {        
        VBox vbox = new VBox();
        MyNode myNode = new MyNode("MyNode");
        MyNode parent = new MyNode("Parent");
        Polygon arrow = createUMLArrow();
        Label extend = new Label("<<extends>>");
        extend.setStyle("-fx-padding: 0 0 0 -1em;");
        vbox.getChildren().addAll(parent,arrow,myNode);
        vbox.setAlignment(Pos.CENTER);        
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));
        hbox.getChildren().addAll(vbox,extend);
        getChildren().addAll(hbox);
    }
    
    public static Polygon createUMLArrow() {
        Polygon polygon = new Polygon(new double[]{
            7.5, 0,
            15, 15,
            7.51, 15,
            7.51, 40,
            7.49, 40,
            7.49, 15,
            0, 15
        });
        polygon.setFill(Color.WHITE);
        polygon.setStroke(Color.BLACK);
        return polygon;
    }

    private static class MyNode extends Parent {
        private Label text;
        
        public MyNode(String name) {
            text = new Label(name);
            text.setStyle("-fx-border-color:black; -fx-padding:3px;");
            text.setLayoutX(4);
            text.setLayoutY(2);
            getChildren().addAll(text);
        }
    }
    
    // REMOVE ME
    private static class MyEnsembleNode extends Parent {
        private Text text;
        private Rectangle rectangle;

        public MyEnsembleNode(String name) {
          text = new Text(name);
          text.setTextOrigin(VPos.TOP);
          text.setLayoutX(4);
          text.setLayoutY(2);
          rectangle = new Rectangle(50, 20, Color.WHITESMOKE);
          rectangle.setStroke(Color.BLACK);
          //add nodes as childrens, order matters, first is on the bottom
          getChildren().addAll(rectangle, text);
        }
    }
    // END REMOVE ME

    // REMOVE ME
    public static Node createIconContent() {
        MyEnsembleNode myNode = new MyEnsembleNode("MyNode");
        myNode.setLayoutY(50);
        MyEnsembleNode parent = new MyEnsembleNode("Parent");
        Polygon arrow = createUMLArrow();
        arrow.setLayoutY(20);
        arrow.setLayoutX(25-7.5);
        Text text = new Text("<<extends>>");
        text.setTextOrigin(VPos.TOP);
        text.setLayoutY(31);
        text.setLayoutX(30);
        return new Group(parent, arrow, text, myNode);
    }
    // END REMOVE ME
}

