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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

/**
 * A sample that displays data in a stacked bar chart.
 *
 * @see javafx.scene.chart.StackedBarChart
 * @see javafx.scene.chart.CategoryAxisBuilder
 * @see javafx.scene.chart.NumberAxis
 * @related charts/bar/BarChart
 *
 */
public class StackedBarChartSample extends Sample {

    public StackedBarChartSample() {
        String[] years = {"2007", "2008", "2009"};
        CategoryAxis xAxis = CategoryAxisBuilder.create()
                .categories(FXCollections.<String>observableArrayList(years)).build();
        NumberAxis yAxis = NumberAxisBuilder.create()
                           .label("Units Sold")
                           .lowerBound(0.0d)
                           .upperBound(10000.0d)
                           .tickUnit(1000.0d).build();
        ObservableList<StackedBarChart.Series> barChartData = FXCollections.observableArrayList(
            new StackedBarChart.Series("Region 1", FXCollections.observableArrayList(
               new StackedBarChart.Data(years[0], 567d),
               new StackedBarChart.Data(years[1], 1292d),
               new StackedBarChart.Data(years[2], 1292d)
            )),
            new StackedBarChart.Series("Region 2", FXCollections.observableArrayList(
               new StackedBarChart.Data(years[0], 956),
               new StackedBarChart.Data(years[1], 1665),
               new StackedBarChart.Data(years[2], 2559)
            )),
            new StackedBarChart.Series("Region 3", FXCollections.observableArrayList(
               new StackedBarChart.Data(years[0], 1154),
               new StackedBarChart.Data(years[1], 1927),
               new StackedBarChart.Data(years[2], 2774)
            ))
        );
        
        StackedBarChart chart = new StackedBarChart(xAxis, yAxis, barChartData, 25.0d);
        getChildren().add(chart);
    }
}
