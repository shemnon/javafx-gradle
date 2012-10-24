/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
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
package ensemble.util;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;


/**
 *
 * @author akouznet
 */
public class ChartActions {
    
    public static void addDataItemStrNum(final XYChart<String, Number> chart) {
        if (chart.getData() == null) {
            chart.setData(FXCollections.<XYChart.Series<String, Number>>observableArrayList());
        }
        if (chart.getData().isEmpty()) {
            chart.getData().add(new XYChart.Series<String, Number>());
        }
        
        int sIndex = (int) (Math.random() * chart.getData().size());
        XYChart.Series<String, Number> series = chart.getData().get(sIndex);
        
        Set<String> existingYears = new HashSet<String>();
        for (Data<String, Number> data : series.getData()) {
            existingYears.add(data.getXValue());
        }
        
        int randomYear = 1900 + (int) (Math.round(12 * Math.random()) * 10);
        while (existingYears.contains(Integer.toString(randomYear))) {
            randomYear++;
        }
        series.getData().add(new XYChart.Data<String, Number>(Integer.toString(randomYear), 10 + (Math.random() * 3800)));
    }    
    
    public static void addDataItemNumStr(final XYChart<Number, String> chart) {
        if (chart.getData() == null) {
            chart.setData(FXCollections.<XYChart.Series<Number, String>>observableArrayList());
        }
        if (chart.getData().isEmpty()) {
            chart.getData().add(new XYChart.Series<Number, String>());
        }
        
        int sIndex = (int) (Math.random() * chart.getData().size());
        XYChart.Series<Number, String> series = chart.getData().get(sIndex);
        
        Set<String> existingYears = new HashSet<String>();
        for (Data<Number, String> data : series.getData()) {
            existingYears.add(data.getYValue());
        }
        
        int randomYear = 1900 + (int) (Math.round(12 * Math.random()) * 10);
        while (existingYears.contains(Integer.toString(randomYear))) {
            randomYear++;
        }
        series.getData().add(new XYChart.Data<Number, String>(10 + (Math.random() * 3800), Integer.toString(randomYear)));
    }    
    
    public static void deleteDataItem(XYChart<?, ?> chart) {
        if (chart.getData() == null) {
            return;
        }
        List<Integer> notEmpty = new ArrayList<Integer>();
        for (int i = 0; i < chart.getData().size(); i++) {
            XYChart.Series s = chart.getData().get(i);
            if (s != null && !s.getData().isEmpty()) {
                notEmpty.add(i);
            }
        }
        if (!notEmpty.isEmpty()) {
            XYChart.Series s = chart.getData().get(
                    notEmpty.get((int)(Math.random() * notEmpty.size())));
            s.getData().remove((int) (Math.random() * s.getData().size()));
        }
    }
}
