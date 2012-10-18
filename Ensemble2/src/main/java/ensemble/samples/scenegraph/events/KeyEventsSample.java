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
package ensemble.samples.scenegraph.events;

import ensemble.Sample;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A sample that demonstrates various key events and their usage. Type in the
 * text box to view the triggered events: key pressed, key typed and key 
 * released. Pressing the Shift, Ctrl, and Alt keys also trigger events.
 *
 * @see javafx.scene.input.KeyCode
 * @see javafx.scene.input.KeyEvent
 * @see javafx.event.EventHandler
 */
public class KeyEventsSample extends Sample {
  
    public KeyEventsSample() {
        //create a console for logging key events
        final ListView<String> console = new ListView<String>(FXCollections.<String>observableArrayList());
        // listen on the console items and remove old ones when we get over 10 items
        console.getItems().addListener(new ListChangeListener<String>() {
            @Override public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if (change.getList().size() > 20) change.getList().remove(0);
                }
            }
        });
        // create text box for typing in
        final TextField textBox = new TextField();
        textBox.setPromptText("Write here");
        textBox.setStyle("-fx-font-size: 34;");
        //add a key listeners
        textBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                console.getItems().add("Key Pressed: " + ke.getText());
            }
        });
        textBox.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                console.getItems().add("Key Released: " + ke.getText());
            }
        });
        textBox.setOnKeyTyped(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                String text = "Key Typed: " + ke.getCharacter();
                if (ke.isAltDown()) {
                    text += " , alt down";
                }
                if (ke.isControlDown()) {
                    text += " , ctrl down";
                }
                if (ke.isMetaDown()) {
                    text += " , meta down";
                }
                if (ke.isShiftDown()) {
                    text += " , shift down";
                }
                console.getItems().add(text);
            }
        });
        VBox vb = new VBox(10);
        vb.getChildren().addAll(textBox, console);
        getChildren().add(vb);
    }

    // REMOVE ME
    public static Node createIconContent() {
        javafx.scene.Group g = new javafx.scene.Group(
                    createKey("A", 0d, 28d),
                    createKey("S", 25d, 28d),
                    createKey("D", 50d, 28d),
                    createKey("W", 20d, 0d)
                    );
        g.setLayoutX(30);
        g.setLayoutY(20);
        PerspectiveTransform pt = new PerspectiveTransform();
        pt.setLlx(0);
        pt.setLly(90);
        pt.setLrx(90);
        pt.setLry(90);
        pt.setUlx(15);
        pt.setUly(10);
        pt.setUrx(80);
        pt.setUry(5);
        g.setEffect(pt);
        return g;
    }

    private static Group createKey(String character, double x, double y) {
        javafx.scene.text.Text text = new javafx.scene.text.Text(3, 2, character);
        text.setFill(Color.WHITESMOKE);
        text.setTextOrigin(javafx.geometry.VPos.TOP);
        javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle(20, 20);
        rectangle.setStroke(Color.BLACK);
        rectangle.setEffect(new DropShadow());
        rectangle.setFill(Color.DARKGRAY);
        rectangle.setArcHeight(7);
        rectangle.setArcWidth(7);
        javafx.scene.Group g = new javafx.scene.Group(rectangle, text);
        g.setLayoutX(x);
        g.setLayoutY(y);
        return g;
    }
    // END REMOVE ME
}