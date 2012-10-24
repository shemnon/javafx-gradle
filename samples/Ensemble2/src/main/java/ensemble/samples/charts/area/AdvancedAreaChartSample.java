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
package ensemble.samples.charts.area;

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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Duration;

/**
 *
 * An advanced area chart with a variety of actions and settable properties
 * for experimenting with the charts features.
 *
 * @see javafx.scene.chart.AreaChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.LineChart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class AdvancedAreaChartSample extends Sample {

    public AdvancedAreaChartSample() {
        getChildren().add(createChart());
    }

    protected AreaChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis,yAxis);
        // setup chart
        ac.setTitle("Area Chart Example");
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");
        // add starting data
        for (int s=0;s<3;s++) {
            XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
            series.setName("Data Series "+s);
            double x = 0;
            while (x<95) {
                series.getData().add(new XYChart.Data<Number,Number>(x, Math.random()*99));
                x += 5 + (15*Math.random());
            }
            series.getData().add(new XYChart.Data<Number,Number>(99d, Math.random()*99));
            ac.getData().add(series);
        }
        return ac;
    }

   // REMOVE ME
    @Override public Node getSideBarExtraContent() {
        return createPropertySheet();
    }

    @Override public String getSideBarExtraContentTitle() {
        return "Chart Properties";
    }

    protected PropertySheet createPropertySheet() {
        final AreaChart<Number,Number> ac = (AreaChart<Number,Number>)getChildren().get(0);
        final NumberAxis xAxis = (NumberAxis)ac.getXAxis();
        final NumberAxis yAxis = (NumberAxis)ac.getYAxis();
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (ac.getData() != null && !ac.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = ac.getData().get((int)(Math.random()*ac.getData().size()));
                    if(s!=null) {
                        double x = Math.random()*50;
                        double y = Math.random()*50;
                        s.getData().add(0, new AreaChart.Data<Number,Number>(x,y));
                    }
                }
            }
        };
        EventHandler<ActionEvent> insertDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number, Number>> data = ac.getData();
                if (data != null && !data.isEmpty()) {
                    XYChart.Series<Number, Number> s = data.get((int) (Math.random() * data.size()));
                    if(s!=null) s.getData().add((int)(s.getData().size()*Math.random()) ,
                            new LineChart.Data<Number,Number>(Math.random()*1000, Math.random()*1000));
                }
            }
        };
        EventHandler<ActionEvent> addDataItemNegative = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number, Number>> data = ac.getData();
                if (data != null && !data.isEmpty()) {
                    XYChart.Series<Number, Number> s = data.get((int) (Math.random() * data.size()));
                    if(s!=null) s.getData().add(new AreaChart.Data<Number,Number>(Math.random()*-200, Math.random()*-200));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ChartActions.deleteDataItem(ac);
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number, Number>> data = ac.getData();
                if (data != null && !data.isEmpty()) {
                    XYChart.Series<Number, Number> s = data.get((int) (Math.random() * (data.size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        XYChart.Data<Number,Number> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setXValue(d.getXValue().doubleValue() + (Math.random() * 50) -25);
                            d.setYValue(Math.random() * 1000);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (ac.getData() == null) {
                    ac.setData(FXCollections.<Series<Number, Number>>observableArrayList());
                }                
                AreaChart.Series<Number,Number> series = new AreaChart.Series<Number,Number>();
                series.setName("Data Series 1");
                double x = 0;
                for (int i=0; i<10; i++) {
                    x += Math.random()*100;
                    series.getData().add(new AreaChart.Data<Number, Number>(x, Math.random()*800));
                }
                ac.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (ac.getData() != null && !ac.getData().isEmpty()) {
                    ac.getData().remove((int)(Math.random() * ac.getData().size()));
                }
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (ac.getData() == null) {
                    return;
                }
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: ac.getData()) {
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
                if (ac.getData() == null) {
                    return;
                }
                ac.setAnimated(false);
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: ac.getData()) {
                                for (XYChart.Data<Number, Number> data: series.getData()) {
                                    data.setXValue(-500 + Math.random()*1000);
                                    data.setYValue(-500 + Math.random()*1000);
                                }
                            }
                        }
                    })
                );
                tl.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        ac.setAnimated(true);
                    }
                });
                tl.setCycleCount(100);
                tl.play();
            }
        };
        EventHandler<ActionEvent> removeAllData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ac.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number,Number>> data = FXCollections.observableArrayList();
                for (int j=0; j<5;j++) {
                    AreaChart.Series<Number,Number> series = new AreaChart.Series<Number,Number>();
                    series.setName("Data Series "+j);
                    double x = 0;
                    for (int i=0; i<10; i++) {
                        x += Math.random()*100;
                        series.getData().add(new AreaChart.Data<Number,Number>(x, Math.random()*800));
                    }
                    data.add(series);
                }
                ac.setData(data);
            }
        };
        //Create X/Y side overriding values to filter out unhelpful values
        List<Enum> xValidSides = new ArrayList();       
        xValidSides.add(Side.BOTTOM);
        xValidSides.add(Side.TOP);

        List<Enum> yValidSides = new ArrayList();       
        yValidSides.add(Side.LEFT);
        yValidSides.add(Side.RIGHT);

        // create property sheet
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Insert Data Item",insertDataItem),
                PropertySheet.createProperty("Add Data Item Negative",addDataItemNegative),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Add Series",addSeries),
                PropertySheet.createProperty("Delete Series",deleteSeries),
                PropertySheet.createProperty("Remove All Data",removeAllData),
                PropertySheet.createProperty("Set New Data",setNewData)
            ),
            new PropertySheet.PropertyGroup("Chart Properties",
                PropertySheet.createProperty("Title",ac.titleProperty()),
                PropertySheet.createProperty("Title Side",ac.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",ac.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("XY Chart Properties",
                PropertySheet.createProperty("Vertical Grid Line Visible",ac.verticalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Horizontal Grid Line Visible",ac.horizontalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Alternative Column Fill Visible",ac.alternativeColumnFillVisibleProperty()),
                PropertySheet.createProperty("Alternative Row Fill Visible",ac.alternativeRowFillVisibleProperty()),
                PropertySheet.createProperty("Vertical Zero Line Visible",ac.verticalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Horizontal Zero Line Visible",ac.horizontalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Animated",ac.animatedProperty())
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
