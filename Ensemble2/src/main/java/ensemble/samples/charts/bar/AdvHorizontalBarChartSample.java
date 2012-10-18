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
package ensemble.samples.charts.bar;

import ensemble.Sample;
import ensemble.controls.PropertySheet;
import ensemble.util.ChartActions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * An advanced horizontal bar chart with a variety of actions and settable
 * properties for experimenting with the charts features.
 *
 * @see javafx.scene.chart.BarChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class AdvHorizontalBarChartSample extends Sample {

    public AdvHorizontalBarChartSample() {
        getChildren().add(createChart());
    }

    protected BarChart<Number, String> createChart() {
        final String[] years = {"2007", "2008", "2009"};
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        final BarChart<Number,String> bc = new BarChart<Number,String>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Horizontal Bar Chart Example");
        yAxis.setLabel("Year");
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        xAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number,String> series1 = new XYChart.Series<Number,String>();
        series1.setName("Data Series 1");
        XYChart.Series<Number,String> series2 = new XYChart.Series<Number,String>();
        series2.setName("Data Series 2");
        XYChart.Series<Number,String> series3 = new XYChart.Series<Number,String>();
        series3.setName("Data Series 3");
        series1.getData().add(new XYChart.Data<Number,String>(567, years[0]));
        series1.getData().add(new XYChart.Data<Number,String>(1292, years[1]));
        series1.getData().add(new XYChart.Data<Number,String>(2180, years[2]));
        series2.getData().add(new XYChart.Data<Number,String>(956, years[0]));
        series2.getData().add(new XYChart.Data<Number,String>(1665, years[1]));
        series2.getData().add(new XYChart.Data<Number,String>(2450, years[2]));
        series3.getData().add(new XYChart.Data<Number,String>(800, years[0]));
        series3.getData().add(new XYChart.Data<Number,String>(1000, years[1]));
        series3.getData().add(new XYChart.Data<Number,String>(2800, years[2]));
        bc.getData().add(series1);
        bc.getData().add(series2);
        bc.getData().add(series3);
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
        final BarChart<Number,String> bc = (BarChart<Number,String>)getChildren().get(0);
        final NumberAxis xAxis = (NumberAxis)bc.getXAxis();
        final CategoryAxis yAxis = (CategoryAxis)bc.getYAxis();
        // create actions
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, String> s = bc.getData().get((int)(Math.random()*(bc.getData().size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        XYChart.Data<Number, String> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setXValue(Math.random()*1500d);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                XYChart.Series<Number,String> series = new XYChart.Series<Number,String>();
                series.setName("Data Series 1");
                CategoryAxis cAxis = ((CategoryAxis)bc.getYAxis());
                for (String category : cAxis.getCategories()) {
                    series.getData().add(new XYChart.Data<Number,String>(Math.random()*3800, category));
                }
                bc.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    bc.getData().remove((int)(Math.random() * bc.getData().size()));
                }
            }
        };
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ChartActions.addDataItemNumStr(bc);
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ChartActions.deleteDataItem(bc);
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
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Add Series",addSeries),
                PropertySheet.createProperty("Delete Series",deleteSeries)
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
                // Category Axis Properties
                PropertySheet.createProperty("Start Margin", yAxis.startMarginProperty()),
                PropertySheet.createProperty("End Margin", yAxis.endMarginProperty()),
                PropertySheet.createProperty("Gap Start And End", yAxis.gapStartAndEndProperty())
            )
        );
    }
    // END REMOVE ME
}
