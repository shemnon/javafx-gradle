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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;

/**
 * A chart that fills in the area between a line of data points and the axes.
 * Good for comparing accumulated totals over time.
 *
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.NumberAxis
 * @related charts/line/LineChart
 * @related charts/scatter/ScatterChart
 */
public class AreaChartSample extends Sample {

    public AreaChartSample() {
        NumberAxis xAxis = new NumberAxis("X Values", 1.0d, 9.0d, 2.0d);
        xAxis.setTickLength(12.0f);
        NumberAxis yAxis = new NumberAxis("Y Values", 0.0d, 10.0d, 2.0d);
        ObservableList<AreaChart.Series> areaChartData = FXCollections.observableArrayList(
                new AreaChart.Series("Series 1",FXCollections.observableArrayList(
                    new AreaChart.Data(0,4),
                    new AreaChart.Data(2,5),
                    new AreaChart.Data(4,4),
                    new AreaChart.Data(6,2),
                    new AreaChart.Data(8,6),
                    new AreaChart.Data(10,8)
                )),
                new AreaChart.Series("Series 2", FXCollections.observableArrayList(
                    new AreaChart.Data(0,8),
                    new AreaChart.Data(2,2),
                    new AreaChart.Data(4,9),
                    new AreaChart.Data(6,7),
                    new AreaChart.Data(8,5),
                    new AreaChart.Data(10,7)
                )),
                new AreaChart.Series("Series 3", FXCollections.observableArrayList(
                    new AreaChart.Data(0,2),
                    new AreaChart.Data(2,5),
                    new AreaChart.Data(4,8),
                    new AreaChart.Data(6,6),
                    new AreaChart.Data(8,9),
                    new AreaChart.Data(10,7)
                ))
        );
        AreaChart chart = new AreaChart(xAxis, yAxis, areaChartData);
        getChildren().add(chart);
    }
}
