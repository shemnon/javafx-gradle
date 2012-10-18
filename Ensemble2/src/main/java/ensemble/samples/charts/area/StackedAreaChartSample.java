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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.NumberAxisBuilder;

/**
 * A sample that displays data in a stacked area chart.
 *
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @related charts/area/AreaChart
 */
public class StackedAreaChartSample extends Sample {

    public StackedAreaChartSample() {
        NumberAxis xAxis = NumberAxisBuilder.create()
                           .label("X Values")
                           .lowerBound(1.0d)
                           .upperBound(9.0d)
                           .tickUnit(2.0d).build();
                     
        NumberAxis yAxis = NumberAxisBuilder.create()
                           .label("Y Values")
                           .lowerBound(0.0d)
                           .upperBound(30.0d)
                           .tickUnit(2.0d).build();
                
        ObservableList<StackedAreaChart.Series> areaChartData = FXCollections.observableArrayList(
                new StackedAreaChart.Series("Series 1",FXCollections.observableArrayList(
                    new StackedAreaChart.Data(0,4),
                    new StackedAreaChart.Data(2,5),
                    new StackedAreaChart.Data(4,4),
                    new StackedAreaChart.Data(6,2),
                    new StackedAreaChart.Data(8,6),
                    new StackedAreaChart.Data(10,8)
                )),
                new StackedAreaChart.Series("Series 2", FXCollections.observableArrayList(
                    new StackedAreaChart.Data(0,8),
                    new StackedAreaChart.Data(2,2),
                    new StackedAreaChart.Data(4,9),
                    new StackedAreaChart.Data(6,7),
                    new StackedAreaChart.Data(8,5),
                    new StackedAreaChart.Data(10,7)
                )),
                new StackedAreaChart.Series("Series 3", FXCollections.observableArrayList(
                    new StackedAreaChart.Data(0,2),
                    new StackedAreaChart.Data(2,5),
                    new StackedAreaChart.Data(4,8),
                    new StackedAreaChart.Data(6,6),
                    new StackedAreaChart.Data(8,9),
                    new StackedAreaChart.Data(10,7)
                ))
        );
        StackedAreaChart chart = new StackedAreaChart(xAxis, yAxis, areaChartData);
        getChildren().add(chart);
    }
}
