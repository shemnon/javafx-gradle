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
package ensemble.samples.charts.bubble;

import ensemble.Sample;
import ensemble.controls.PropertySheet;
import ensemble.util.ChartActions;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Duration;

/**
 * An advanced bubble chart with a variety of actions and settable properties.
 *
 * @see javafx.scene.chart.BubbleChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.ScatterChart
 * @see javafx.scene.chart.XYChart
 */
public class AdvancedBubbleChartSample extends Sample {

    public AdvancedBubbleChartSample() {
        getChildren().add(createChart());
    }

    protected BubbleChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BubbleChart<Number,Number> bc = new BubbleChart<Number,Number>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Advanced BubbleChart");
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");
        // add starting data
        XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();
        series1.setName("Data Series 1");
        for (int i=0; i<20; i++) series1.getData().add(
                new XYChart.Data<Number,Number>(Math.random()*100, Math.random()*100, (Math.random()*10)));
        XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
        series2.setName("Data Series 2");
        for (int i=0; i<20; i++) series2.getData().add(
                new XYChart.Data<Number,Number>(Math.random()*100, Math.random()*100, (Math.random()*10)));
        bc.getData().addAll(series1, series2);
        return bc;
    }

    // REMOVE ME
    @Override public Node getSideBarExtraContent() {
        return createPropertySheet();
    }

    @Override public String getSideBarExtraContentTitle() {
        return "Chart Properties";
    }

    protected PropertySheet createPropertySheet() {
        final BubbleChart<Number,Number> bc = (BubbleChart<Number,Number>)getChildren().get(0);
        final NumberAxis xAxis = (NumberAxis)bc.getXAxis();
        final NumberAxis yAxis = (NumberAxis)bc.getYAxis();
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() != null && !bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random() * bc.getData().size()));
                    if(s!=null) s.getData().add(
                            new BubbleChart.Data<Number,Number>(Math.random()*1000, Math.random()*1000, Math.random()*100));
                }
            }
        };
        EventHandler<ActionEvent> addDataItemNegative = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() != null && !bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random() * bc.getData().size()));
                    if(s!=null) s.getData().add(
                            new BubbleChart.Data<Number,Number>(Math.random()*-1000, Math.random()*-1000, Math.random()*100));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ChartActions.deleteDataItem(bc);
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() != null && !bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random()*(bc.getData().size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        BubbleChart.Data<Number,Number> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setXValue(Math.random() * 1000);
                            d.setYValue(Math.random() * 1000);
                            d.setExtraValue(Math.random() * 100);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() == null) {
                    bc.setData(FXCollections.<Series<Number, Number>>observableArrayList());
                }
                ScatterChart.Series<Number,Number> series = new ScatterChart.Series<Number,Number>();
                series.setName("Data Series 1");
                for (int i=0; i<10; i++) series.getData().add(
                        new BubbleChart.Data<Number,Number>(Math.random()*800, Math.random()*800, Math.random()*100));
                bc.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() != null && !bc.getData().isEmpty()) bc.getData().remove((int)(Math.random() * bc.getData().size()));
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() == null) {
                    return;
                }
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: bc.getData()) {
                                for (XYChart.Data<Number, Number> data: series.getData()) {
                                    data.setXValue(Math.random()*1000);
                                    data.setYValue(Math.random()*1000);
                                }
                            }
                        }
                    })
                );
                tl.setCycleCount(30);
                tl.play();
            }
        };
        EventHandler<ActionEvent> animateDataFast = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() == null) {
                    return;
                }
                bc.setAnimated(false);
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: bc.getData()) {
                                for (XYChart.Data<Number, Number> data: series.getData()) {
                                    data.setXValue(Math.random()*200);
                                    data.setYValue(Math.random()*200);
                                }
                            }
                        }
                    })
                );
                tl.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        bc.setAnimated(true);
                    }
                });
                tl.setCycleCount(100);
                tl.play();
            }
        };
        EventHandler<ActionEvent> removeAllData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                bc.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number,Number>> data = FXCollections.observableArrayList();
                for (int j=0; j<5;j++) {
                    BubbleChart.Series<Number,Number> series = new BubbleChart.Series<Number,Number>();
                    series.setName("Data Series "+j);
                    double x = 0;
                    for (int i=0; i<10; i++) {
                        x += Math.random()*100;
                        series.getData().add(new BubbleChart.Data<Number,Number>(x, Math.random()*800, Math.random()*100));
                    }
                    data.add(series);
                }
                bc.setData(data);
            }
        };

        //Create X/Y side overriding values to filter out unhelpful values
        List<Enum> xValidSides = new ArrayList();
        xValidSides.add(Side.BOTTOM);
        xValidSides.add(Side.TOP);

        List<Enum> yValidSides = new ArrayList();
        yValidSides.add(Side.LEFT);
        yValidSides.add(Side.RIGHT);

        // create property editor
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Add Data Item Negative",addDataItemNegative),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Add Series",addSeries),
                PropertySheet.createProperty("Delete Series",deleteSeries),
                PropertySheet.createProperty("Remove All Data",removeAllData),
                PropertySheet.createProperty("Set New Data",setNewData)
            ),
            new PropertySheet.PropertyGroup("Chart Properties",
                PropertySheet.createProperty("Title",bc.titleProperty()),
                PropertySheet.createProperty("Title Side",bc.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",bc.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("XY Chart Properties",
                PropertySheet.createProperty("Vertical Grid Line Visible",bc.verticalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Horizontal Grid Line Visible",bc.horizontalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Alternative Column Fill Visible",bc.alternativeColumnFillVisibleProperty()),
                PropertySheet.createProperty("Alternative Row Fill Visible",bc.alternativeRowFillVisibleProperty()),
                PropertySheet.createProperty("Vertical Zero Line Visible",bc.verticalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Horizontal Zero Line Visible",bc.horizontalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Animated",bc.animatedProperty())
            ),
            new PropertySheet.PropertyGroup("X Axis Properties",
                PropertySheet.createProperty("Side",xAxis.sideProperty(), xValidSides),
                PropertySheet.createProperty("Label",xAxis.labelProperty()),
                PropertySheet.createProperty("Tick Mark Length",xAxis.tickLengthProperty()),
                PropertySheet.createProperty("Auto Ranging",xAxis.autoRangingProperty()),
                PropertySheet.createProperty("Tick Label Font",xAxis.tickLabelFontProperty()),
                PropertySheet.createProperty("Tick Label Fill",xAxis.tickLabelFillProperty()),
                PropertySheet.createProperty("Tick Label Gap",xAxis.tickLabelGapProperty()),
                // Value Axis Props
                //PropertySheet.createProperty("Scale",xAxis.scaleProperty(), true),
                PropertySheet.createProperty("Lower Bound",xAxis.lowerBoundProperty(), true),
                PropertySheet.createProperty("Upper Bound",xAxis.upperBoundProperty(), true),
                PropertySheet.createProperty("Tick Label Formatter",xAxis.tickLabelFormatterProperty()),
                PropertySheet.createProperty("Minor Tick Length",xAxis.minorTickLengthProperty()),
                PropertySheet.createProperty("Minor Tick Count",xAxis.minorTickCountProperty()),
                // Number Axis Properties
                PropertySheet.createProperty("Force Zero In Range",xAxis.forceZeroInRangeProperty()),
                PropertySheet.createProperty("Tick Unit",xAxis.tickUnitProperty())
            ),
            new PropertySheet.PropertyGroup("Y Axis Properties",
                PropertySheet.createProperty("Side",yAxis.sideProperty(), yValidSides),
                PropertySheet.createProperty("Label",yAxis.labelProperty()),
                PropertySheet.createProperty("Tick Mark Length",yAxis.tickLengthProperty()),
                PropertySheet.createProperty("Auto Ranging",yAxis.autoRangingProperty()),
                PropertySheet.createProperty("Tick Label Font",yAxis.tickLabelFontProperty()),
                PropertySheet.createProperty("Tick Label Fill",yAxis.tickLabelFillProperty()),
                PropertySheet.createProperty("Tick Label Gap",yAxis.tickLabelGapProperty()),
                // Value Axis Props
                //PropertySheet.createProperty("Scale",yAxis.scaleProperty(), true),
                PropertySheet.createProperty("Lower Bound",yAxis.lowerBoundProperty(), true),
                PropertySheet.createProperty("Upper Bound",yAxis.upperBoundProperty(), true),
                PropertySheet.createProperty("Tick Label Formatter",yAxis.tickLabelFormatterProperty()),
                PropertySheet.createProperty("Minor Tick Length",yAxis.minorTickLengthProperty()),
                PropertySheet.createProperty("Minor Tick Count",yAxis.minorTickCountProperty()),
                // Number Axis Properties
                PropertySheet.createProperty("Force Zero In Range",yAxis.forceZeroInRangeProperty()),
                PropertySheet.createProperty("Tick Unit",yAxis.tickUnitProperty())
            )
        );
    }
    // END REMOVE ME
}
