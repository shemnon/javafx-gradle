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
package ensemble.samples.layout;

import ensemble.Sample;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
// REMOVE ME
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
// END REMOVE ME

/**
 * An example of a GridPane layout. There is more than one approach to using a
 * GridPane. First, the code can specify which rows and/or columns should
 * contain the content. Second, the code can alter the constraints of the
 * rows and/or columns themselves, either by specifying the preferred minimum
 * or  maximum heights or widths, or by specifying the percentage of the
 * GridPane that belongs to certain rows or columns.
 * 
 * @see javafx.scene.layout.GridPane
 * @related controls/text/SimpleLabel
 * @resource icon-48x48.png
 */
public class GridPaneSample extends Sample {
    private static final Image ICON_48 = new Image(GridPaneSample.class.getResourceAsStream("icon-48x48.png"));
    public GridPaneSample() {
      
        VBox vbox = new VBox();

        //grid1 places the child by specifying the rows and columns in
        // GridPane.setContraints()
        Label grid1Caption = new Label("The example below shows GridPane content placement by specifying rows and columns:");
        grid1Caption.setWrapText(true);
        GridPane grid1 = new GridPane();
        grid1.setHgap(4);
        grid1.setVgap(6);
        grid1.setPadding(new Insets(18, 18, 18, 18));
        ObservableList<Node> content = grid1.getChildren();

        Label label = new Label("Name:");
        GridPane.setConstraints(label, 0, 0);
        GridPane.setHalignment(label, HPos.RIGHT);
        content.add(label);

        label = new Label("John Q. Public");
        GridPane.setConstraints(label, 1, 0, 2, 1);
        GridPane.setHalignment(label, HPos.LEFT);
        content.add(label);

        label = new Label("Address:");
        GridPane.setConstraints(label, 0, 1);
        GridPane.setHalignment(label, HPos.RIGHT);
        content.add(label);

        label = new Label("12345 Main Street, Some City, CA");
        GridPane.setConstraints(label, 1, 1, 5, 1);
        GridPane.setHalignment(label, HPos.LEFT);
        content.add(label);

        vbox.getChildren().addAll(grid1Caption, grid1, new Separator());

        //grid2 places the child by influencing the rows and columns themselves
        //via GridRowInfo and GridColumnInfo. This grid uses the preferred
        //width/height and max/min width/height.
        Label grid2Caption = new Label("The example below shows GridPane content placement by influencing the rows and columns themselves.");
        grid2Caption.setWrapText(true);
        grid2Caption.setWrapText(true);
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(18, 18, 18, 18));
        RowConstraints rowinfo = new RowConstraints(40, 40, 40);
        ColumnConstraints colinfo = new ColumnConstraints(90, 90, 90);

        for (int i = 0; i <= 2; i++) {
            grid2.getRowConstraints().add(rowinfo);
        }

        for (int j = 0; j <= 2; j++) {
            grid2.getColumnConstraints().add(colinfo);
        }
        
        Label category = new Label("Category:");
        GridPane.setHalignment(category, HPos.RIGHT);
        Label categoryValue = new Label("Wines");             
        Label company = new Label("Company:");
        GridPane.setHalignment(company, HPos.RIGHT);
        Label companyValue = new Label("Acme Winery");
        Label rating = new Label("Rating:");
        GridPane.setHalignment(rating, HPos.RIGHT);
        Label ratingValue = new Label("Excellent");

        ImageView imageView = new ImageView(ICON_48);
        GridPane.setHalignment(imageView, HPos.CENTER);

        //Place content
        GridPane.setConstraints(category, 0, 0);
        GridPane.setConstraints(categoryValue, 1, 0);
        GridPane.setConstraints(company, 0, 1);
        GridPane.setConstraints(companyValue, 1, 1);
        GridPane.setConstraints(imageView, 2, 1);
        GridPane.setConstraints(rating, 0, 2);
        GridPane.setConstraints(ratingValue, 1, 2);
        grid2.getChildren().addAll(category, categoryValue, company, companyValue, imageView, rating, ratingValue);

        vbox.getChildren().addAll(grid2Caption, grid2, new Separator());

        //grid3 places the child by influencing the rows and columns
        //via GridRowInfo and GridColumnInfo. This grid uses the percentages
        Label grid3Caption = new Label("The example below shows GridPane content placement by influencing row and column percentages.  Also, grid lines are made visible in this example.  The lines can be helpful in debugging.");
        grid3Caption.setWrapText(true);
        GridPane grid3 = new GridPane();
        grid3.setPadding(new Insets(18, 18, 18, 18));
        grid3.setGridLinesVisible(true);
        RowConstraints rowinfo3 = new RowConstraints();
        rowinfo3.setPercentHeight(50);

        ColumnConstraints colInfo2 = new ColumnConstraints();
        colInfo2.setPercentWidth(25);

        ColumnConstraints colInfo3 = new ColumnConstraints();
        colInfo3.setPercentWidth(50);

        grid3.getRowConstraints().add(rowinfo3);//2*50 percent
        grid3.getRowConstraints().add(rowinfo3);

        grid3.getColumnConstraints().add(colInfo2); //25 percent
        grid3.getColumnConstraints().add(colInfo3); //50 percent
        grid3.getColumnConstraints().add(colInfo2); //25 percent

        Label condLabel = new Label(" Member Name:");
        GridPane.setHalignment(condLabel, HPos.RIGHT);
        GridPane.setConstraints(condLabel, 0, 0);
        Label condValue = new Label("MyName");
        GridPane.setMargin(condValue, new Insets(0, 0, 0, 10));
        GridPane.setConstraints(condValue, 1, 0);
      
        Label acctLabel = new Label("Member Number:");
        GridPane.setHalignment(acctLabel, HPos.RIGHT);
        GridPane.setConstraints(acctLabel, 0, 1);
        TextField textBox = new TextField("Your number");
        GridPane.setMargin(textBox, new Insets(10, 10, 10, 10));
        GridPane.setConstraints(textBox, 1, 1);

        Button button = new Button("Help");
        GridPane.setConstraints(button, 2, 1);
        GridPane.setMargin(button, new Insets(10, 10, 10, 10));
        GridPane.setHalignment(button, HPos.CENTER);

        GridPane.setConstraints(condValue, 1, 0);
        grid3.getChildren().addAll(condLabel, condValue, button, acctLabel, textBox);

        vbox.getChildren().addAll(grid3Caption, grid3);

        getChildren().add(vbox);
    }

    // REMOVE ME
    public static Node createIconContent() {
        StackPane sp = new StackPane();

        Rectangle rectangle = new Rectangle(62, 62, Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);

        Group g = new Group();
        for (int i = 0; i < 5; i++) {
            Rectangle r = new Rectangle();
            r.setY(i * 10);
            r.setWidth(62);
            r.setHeight(10);
            r.setFill(Color.TRANSPARENT);
            r.setStroke(Color.BLACK);
            g.getChildren().add(r);
        }
        for (int j = 0; j < 5; j++) {
            Rectangle r = new Rectangle();
            r.setX(j * 10);
            r.setWidth(10);
            r.setHeight(62);
            r.setFill(Color.TRANSPARENT);
            r.setStroke(Color.BLACK);
            g.getChildren().add(r);
        }

        Rectangle smallerRec = new Rectangle(10, 20, 35, 10);
        smallerRec.setFill(Color.web("#1c89f4"));

        Rectangle smallerRec2 = new Rectangle(5, 3, 20, 10);
        smallerRec2.setFill(Color.web("#349b00"));

        Group recGroup = new Group();
        recGroup.getChildren().addAll(smallerRec, smallerRec2);
        sp.getChildren().addAll(rectangle, g, recGroup);
        return new Group(sp);
    }
    // END REMOVE ME
}
