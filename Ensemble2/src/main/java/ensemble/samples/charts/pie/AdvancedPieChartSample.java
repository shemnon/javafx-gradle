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
package ensemble.samples.charts.pie;

import ensemble.Sample;
import ensemble.controls.PropertySheet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.util.Duration;

/**
 * An advanced pie chart with a variety of actions and settable properties.
 *
 * @see javafx.scene.chart.PieChart
 * @see javafx.scene.chart.Chart
 */
public class AdvancedPieChartSample extends Sample {
    private int itemNameIndex = 1;

    public AdvancedPieChartSample() {
        getChildren().add(createChart());
    }

    protected PieChart createChart() {
        final PieChart pc = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("Sun", 20),
            new PieChart.Data("IBM", 12),
            new PieChart.Data("HP", 25),
            new PieChart.Data("Dell", 22),
            new PieChart.Data("Apple", 30)
        ));
        // setup chart
        pc.setId("BasicPie");
        pc.setTitle("Pie Chart Example");
        return pc;
    }

    // REMOVE ME
    @Override public Node getSideBarExtraContent() {
        return createPropertySheet();
    }

    @Override public String getSideBarExtraContentTitle() {
        return "Chart Properties";
    }

    protected PropertySheet createPropertySheet() {
        final PieChart pc = (PieChart)getChildren().get(0);
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() == null) pc.setData(FXCollections.<PieChart.Data>observableArrayList());
                pc.getData().add(new PieChart.Data("item"+(itemNameIndex++), (int)(Math.random()*50)+10));
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null && !pc.getData().isEmpty()) {
                    pc.getData().remove((int) (Math.random() * pc.getData().size()));
                }
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null && !pc.getData().isEmpty()) {
                    PieChart.Data d = pc.getData().get((int)(Math.random()*(pc.getData().size())));
                    if (d!=null) {
                        d.setName("newItem"+(itemNameIndex++));
                        d.setPieValue((int)(Math.random()*50)+10);
                    }
                }
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null) {
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(
                        new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                for (PieChart.Data d: pc.getData()) {
                                     d.setName("newItem"+(itemNameIndex++));
                                     d.setPieValue((int)(Math.random()*50)+10);
                                }
                            }
                    }));
                    tl.setCycleCount(30);
                    tl.play();
                }
            }
        };
        EventHandler<ActionEvent> animateDataFast = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null) {
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(
                        new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                for (PieChart.Data d: pc.getData()) {
                                     d.setName("newItem"+(itemNameIndex++));
                                     d.setPieValue((int)(Math.random()*50)+10);
                                }
                            }
                    }));
                    tl.setCycleCount(100);
                    tl.play();
                }
            }
        };
        EventHandler<ActionEvent> clearData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                pc.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                pc.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Monday", 20),
                    new PieChart.Data("Tuesday", 12),
                    new PieChart.Data("Wednesday", 25),
                    new PieChart.Data("Thursday", 22),
                    new PieChart.Data("Friday", 30)
                ));
            }
        };
        // create property editor
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Clear Data",clearData),
                PropertySheet.createProperty("Set New Data",setNewData)
            ),
            new PropertySheet.PropertyGroup("Chart Properties",
                PropertySheet.createProperty("Title",pc.titleProperty()),
                PropertySheet.createProperty("Title Side",pc.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",pc.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("PieChart Properties",
                PropertySheet.createProperty("Label Line Length",pc.labelLineLengthProperty()),
                PropertySheet.createProperty("Labels Visible",pc.labelsVisibleProperty()),
                PropertySheet.createProperty("Start Angle",pc.startAngleProperty()),
                PropertySheet.createProperty("Clockwise",pc.clockwiseProperty())
            )
        );
    }
    // END REMOVE ME
}
