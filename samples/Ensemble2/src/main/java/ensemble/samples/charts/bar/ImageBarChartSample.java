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
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * A bar chart that uses CSS to display stacks of car images to indicate data values
 * for categories. 
 *
 * @see javafx.scene.chart.BarChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.CategoryAxis
 * @see javafx.scene.chart.NumberAxis
 * @resource ImageBarChart.css
 * @resource sedan-s.png
 * @resource suv-s.png
 * @resource truck-s.png
 * @resource van-s.png
 *
 */
public class ImageBarChartSample extends Sample {

    public ImageBarChartSample() {

        String imageBarChartCss = ImageBarChartSample.class.getResource("ImageBarChart.css").toExternalForm();

        BarChart barChart = new BarChart(new CategoryAxis(), new NumberAxis());
        barChart.setLegendVisible(false);
        barChart.getStylesheets().add(imageBarChartCss);

        barChart.getData().add(
                new XYChart.Series<String, Integer>("Sales Per Product",
                FXCollections.observableArrayList(
                new XYChart.Data<String, Integer>("SUV", 120),
                new XYChart.Data<String, Integer>("Sedan", 50),
                new XYChart.Data<String, Integer>("Truck", 180),
                new XYChart.Data<String, Integer>("Van", 20))));

        Scene scene = new Scene(barChart, 350, 300);
        scene.getStylesheets().add(ImageBarChartSample.class.getResource("ImageBarChart.css").toString());
        getChildren().add(barChart);
    }
}
