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
package ensemble.samples.language.collections;

import javafx.scene.input.MouseEvent;
import ensemble.Sample;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static java.lang.Math.round;
import static java.lang.Math.random;

/**
 * A sample that demonstrates the ObservableList interface, which extends the
 * java.util.List interface. Click the button to change an integer to a new
 * random number in a random position in the list. Once you add a listener,
 * the index of the changed number is displayed to the left of the list.
 * 
 * @see javafx.beans.value.ChangeListener
 * @see javafx.collections.FXCollections
 * @see javafx.collections.ListChangeListener
 * @see javafx.collections.ObservableList
 */
public class ObservableListSample extends Sample {

    public ObservableListSample() {
        super(400,80);

        //create some list with integers
        final List<Integer> listData = new ArrayList<Integer>();
        for (int i=1; i<10; i++) {
            listData.add(i);
        }

        //create observable list from this list of integers by static method of FXCollections class
        final ObservableList<Integer> list = FXCollections.<Integer>observableList(listData);
        
        //create text for showing observable list content
        final Text textList = new Text(0,0, list.toString());
        textList.setStyle("-fx-font-size: 16;");
        textList.setTextOrigin(VPos.TOP);
        textList.setTextAlignment(TextAlignment.CENTER);

        //create text field for showing  message
        final Text textMessage = new Text(0,0, "please add a listener");
        textMessage.setStyle("-fx-font-size: 16;");
        textMessage.setTextOrigin(VPos.TOP);
        textMessage.setTextAlignment(TextAlignment.CENTER);

        //create button for adding random integer to random position in observable list
        Button buttonAddNumber = new Button("Replace randomly integer in list");
        buttonAddNumber.setPrefSize(190, 20);
        buttonAddNumber.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                int randomIndex = (int) (round(random() * list.size()));
                int randomNumber = (int) (round(random()*10));
                list.set(randomIndex, randomNumber );
                //actualise content of the text to see the result
                textList.setText(list.toString());
            }
        });

        //create button for adding listener
        Button buttonAdd = new Button("Add list listener");
        buttonAdd.setPrefSize(190, 20);
        final ListChangeListener<Integer> listener = new ListChangeListener<Integer>() {
                    public void onChanged(Change<? extends Integer> c) {
                        while (c.next()) {
                            textMessage.setText("replacement on index "  + c.getFrom());
                        }
                    }
                };

        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                list.addListener(listener);
                textMessage.setText("listener added");
            }
        });

        //create a button for removing the listener
        Button buttonRemove = new Button("Remove list listener");
        buttonRemove.setPrefSize(190, 20);
        buttonRemove.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                //remove the listener
                list.removeListener(listener);
                textMessage.setText("listener removed");
            }
        });
        
        // show all nodes
        VBox vBoxLeft = new VBox();
        vBoxLeft.setSpacing(10);

        VBox vBoxRight = new VBox();
        vBoxRight.setSpacing(10);
        vBoxRight.setLayoutX(230);
        vBoxRight.setLayoutY(30);
        
        vBoxLeft.getChildren().addAll(buttonAdd, buttonRemove, textMessage);
        vBoxRight.getChildren().addAll(buttonAddNumber, textList);
        getChildren().addAll(vBoxLeft, vBoxRight);
    }

    // REMOVE ME
    public static Node createIconContent() {
        final Text[] text = new Text[4];
        text[0] = new Text("1");
        text[1] = new Text("2");
        text[2] = new Text("3");
        text[3] = new Text("4");

        text[0].setStyle("-fx-font-size: 28;");
        text[1].setStyle("-fx-font-size: 28;");
        text[2].setStyle("-fx-font-size: 28;");
        text[3].setStyle("-fx-font-size: 28;");

        final javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(javafx.util.Duration.millis(150), ":)", new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                int randomPos = (int) (java.lang.Math.random()*3.9);
                int randomNum = (int) (java.lang.Math.random()*8.9+1);
                text[randomPos].setText(Integer.toString(randomNum));
                for (int i=0; i<4; i++) {
                    text[i].setFill(Color.BLACK);
                }
                text[randomPos].setFill(Color.RED);
            }
        });
        timeline.getKeyFrames().add(keyFrame);

        javafx.scene.shape.Rectangle mouseRect = new javafx.scene.shape.Rectangle (0, 0, 114,114);
        mouseRect.setFill(Color.TRANSPARENT);
        mouseRect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                timeline.playFromStart();
            }
        });
        mouseRect.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                timeline.stop();
                for (int i=0; i<4; i++) {
                    text[i].setFill(Color.BLACK);
                }
            }
        });
        javafx.scene.layout.HBox hBox = new javafx.scene.layout.HBox();
        hBox.setSpacing(3);
        hBox.setLayoutX(26);
        hBox.setLayoutY(44);
        hBox.getChildren().addAll(text);
        return new javafx.scene.Group(hBox, mouseRect);
    }
    // END REMOVE ME
}
