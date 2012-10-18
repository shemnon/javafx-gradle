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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

/**
 * A chart that plots bubbles for a series of data points. Bubbles are plotted
 * according to three numeric parameters: value on x axis, value on y axis,
 * and radius of the bubble.
 *
 * @see javafx.scene.chart.BubbleChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.NumberAxis
 */
public class BubbleChartSample extends Sample {

    public BubbleChartSample() {
        NumberAxis xAxis = new NumberAxis("X", 0d, 150d, 20d);

        NumberAxis yAxis = new NumberAxis("Y", 0d, 150d, 20d);

        ObservableList<BubbleChart.Series> bubbleChartData = FXCollections.observableArrayList(
            new BubbleChart.Series("Series 1", FXCollections.observableArrayList(
                new XYChart.Data(30d, 40d, 10d),
                new XYChart.Data(60d, 20d, 13d),
                new XYChart.Data(10d, 90d, 7d),
                new XYChart.Data(100d, 40d, 10d),
                new XYChart.Data(50d, 23d, 5d)))
            ,
            new BubbleChart.Series("Series 2", FXCollections.observableArrayList(
                new XYChart.Data(13d, 100d, 7d),
                new XYChart.Data(20d, 80d, 13d),
                new XYChart.Data(100d, 60d, 10d),
                new XYChart.Data(30d, 40d, 6d),
                new XYChart.Data(50d, 20d, 12d)
            ))
        );

        BubbleChart chart = new BubbleChart(xAxis, yAxis, bubbleChartData);
        getChildren().add(chart);
    }
}
