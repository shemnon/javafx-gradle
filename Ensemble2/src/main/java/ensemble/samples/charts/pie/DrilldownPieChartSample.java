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
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;

/**
 * A pie chart that provides the ability to drill down through data. Selecting a
 * segment in the initial pie chart causes the pie chart to display detailed data
 * for the selected segment. 
 *
 * @see javafx.event.EventHandler
 * @see javafx.scene.chart.PieChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.input.MouseEvent
 * @resource DrilldownChart.css
 */
public class DrilldownPieChartSample extends Sample {

    PieChart.Data A, B, C, D;

    public DrilldownPieChartSample() {
        String drilldownCss = DrilldownPieChartSample.class.getResource("DrilldownChart.css").toExternalForm();

        PieChart pie = new PieChart(
                FXCollections.observableArrayList(
                A = new PieChart.Data("A", 20),
                B = new PieChart.Data("B", 30),
                C = new PieChart.Data("C", 10),
                D = new PieChart.Data("D", 40)));
        ((Parent) pie).getStylesheets().add(drilldownCss);

        setDrilldownData(pie, A, "a");
        setDrilldownData(pie, B, "b");
        setDrilldownData(pie, C, "c");
        setDrilldownData(pie, D, "d");
        getChildren().add(pie);
    }

    private void setDrilldownData(final PieChart pie, PieChart.Data data, final String labelPrefix) {
        data.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                pie.setData(FXCollections.observableArrayList(
                        new PieChart.Data(labelPrefix + "-1", 7),
                        new PieChart.Data(labelPrefix + "-2", 2),
                        new PieChart.Data(labelPrefix + "-3", 5),
                        new PieChart.Data(labelPrefix + "-4", 3),
                        new PieChart.Data(labelPrefix + "-5", 2)));
            }
        });
    }
}
