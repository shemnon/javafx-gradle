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
package ensemble.samples.charts.line;

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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.util.Duration;

/**
 * An advanced line chart with a variety of actions and settable properties.
 *
 * @see javafx.scene.chart.LineChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class AdvancedLineChartSample extends Sample {

    public AdvancedLineChartSample() {
        getChildren().add(createChart());
    }

    protected LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number,Number> lc = new LineChart<Number,Number>(xAxis,yAxis);
        // setup chart
        lc.setTitle("Basic LineChart");
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");
        // add starting data
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Data Series 1");
        series.getData().add(new XYChart.Data<Number,Number>(20d, 50d));
        series.getData().add(new XYChart.Data<Number,Number>(40d, 80d));
        series.getData().add(new XYChart.Data<Number,Number>(50d, 90d));
        series.getData().add(new XYChart.Data<Number,Number>(70d, 30d));
        series.getData().add(new XYChart.Data<Number,Number>(170d, 122d));
        lc.getData().add(series);
        return lc;
    }

   // REMOVE ME
    @Override public Node getSideBarExtraContent() {
        return createPropertySheet();
    }

    @Override public String getSideBarExtraContentTitle() {
        return "Chart Properties";
    }

    protected PropertySheet createPropertySheet() {
        final LineChart<Number,Number> lc = (LineChart<Number,Number>)getChildren().get(0);
        final NumberAxis xAxis = (NumberAxis)lc.getXAxis();
        final NumberAxis yAxis = (NumberAxis)lc.getYAxis();
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() == null) {
                    lc.setData(FXCollections.observableArrayList(
                            new XYChart.Series<Number, Number>( FXCollections.observableArrayList(
                                    new XYChart.Data<Number, Number>(Math.random()*1000, Math.random()*1000)
                            ))
                    ));
                } else if (lc.getData().isEmpty()) {
                    lc.getData().add(new XYChart.Series<Number, Number>( FXCollections.observableArrayList(
                            new XYChart.Data<Number, Number>(Math.random()*1000, Math.random()*1000)
                    )));
                } else {
                    XYChart.Series<Number, Number> s = lc.getData().get((int)(Math.random() * lc.getData().size()));
                    if(s!=null) s.getData().add(new LineChart.Data<Number,Number>(Math.random()*1000, Math.random()*1000));
                }
            }
        };
        EventHandler<ActionEvent> insertDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() != null && !lc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = lc.getData().get((int)(Math.random() * lc.getData().size()));
                    if(s!=null) s.getData().add((int)(s.getData().size()*Math.random()) ,
                            new LineChart.Data<Number,Number>(Math.random()*1000, Math.random()*1000));
                }
            }
        };
        EventHandler<ActionEvent> addDataItemNegative = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() == null) {
                    lc.setData(FXCollections.observableArrayList(
                            new XYChart.Series<Number, Number>( FXCollections.observableArrayList(
                                    new XYChart.Data<Number, Number>(Math.random()*-200, Math.random()*-200)
                            ))
                    ));
                } else if (lc.getData().isEmpty()) {
                    lc.getData().add(new XYChart.Series<Number, Number>( FXCollections.observableArrayList(
                            new XYChart.Data<Number, Number>(Math.random()*-200, Math.random()*-200)
                    )));
                } else {
                    XYChart.Series<Number, Number> s = lc.getData().get((int)(Math.random() * lc.getData().size()));
                    if(s!=null) s.getData().add(new LineChart.Data<Number,Number>(Math.random()*-200, Math.random()*-200));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ChartActions.deleteDataItem(lc);
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() != null && !lc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = lc.getData().get((int)(Math.random()*(lc.getData().size())));
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
                if (lc.getData() == null) {
                    lc.setData(FXCollections.<Series<Number, Number>>observableArrayList());
                }
                LineChart.Series<Number,Number> series = new LineChart.Series<Number,Number>();
                series.setName("Data Series 1");
                double x = 0;
                for (int i=0; i<10; i++) {
                    x += Math.random()*100;
                    series.getData().add(new LineChart.Data<Number,Number>(x, Math.random()*800));
                }
                if (lc.getData() == null) lc.setData(FXCollections.<XYChart.Series<Number,Number>>observableArrayList());
                lc.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() != null && !lc.getData().isEmpty()) {
                    lc.getData().remove((int)(Math.random() * lc.getData().size()));
                }
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (lc.getData() == null) {
                    return;
                }
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: lc.getData()) {
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
                if (lc.getData() == null) {
                    return;
                }
                lc.setAnimated(false);
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: lc.getData()) {
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
                        lc.setAnimated(true);
                    }
                });
                tl.setCycleCount(100);
                tl.play();
            }
        };
        EventHandler<ActionEvent> removeAllData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                lc.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number,Number>> data = FXCollections.observableArrayList();
                for (int j=0; j<5;j++) {
                    LineChart.Series<Number,Number> series = new LineChart.Series<Number,Number>();
                    series.setName("Data Series "+j);
                    double x = 0;
                    for (int i=0; i<10; i++) {
                        x += Math.random()*100;
                        series.getData().add(new LineChart.Data<Number,Number>(x, Math.random()*800));
                    }
                    data.add(series);
                }
                lc.setData(data);
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
                PropertySheet.createProperty("Title",lc.titleProperty()),
                PropertySheet.createProperty("Title Side",lc.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",lc.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("XY Chart Properties",
                PropertySheet.createProperty("Vertical Grid Line Visible",lc.verticalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Horizontal Grid Line Visible",lc.horizontalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Alternative Column Fill Visible",lc.alternativeColumnFillVisibleProperty()),
                PropertySheet.createProperty("Alternative Row Fill Visible",lc.alternativeRowFillVisibleProperty()),
                PropertySheet.createProperty("Vertical Zero Line Visible",lc.verticalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Horizontal Zero Line Visible",lc.horizontalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Animated",lc.animatedProperty())
            ),
            new PropertySheet.PropertyGroup("Line Chart Properties",
                PropertySheet.createProperty("Create Symbols",lc.createSymbolsProperty())
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
