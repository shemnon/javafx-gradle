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
package ensemble.controls;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A very simple property sheet that is used by samples to show controls to play with.
 */
public class SimplePropertySheet extends GridPane {
    private static final NumberFormat twoDp = new DecimalFormat("0.##");

    public SimplePropertySheet(PropDesc...properties) {
        getStyleClass().add("simple-property-sheet");
        setVgap(10);
        setHgap(10);
        int row = 0;
        for (PropDesc property: properties) {
            final PropDesc prop = property;
            Label propName = new Label(prop.name+":");
            propName.getStyleClass().add("sample-control-grid-prop-label");
            GridPane.setConstraints(propName, 0, row);
            getChildren().add(propName);
            if (prop.valueModel instanceof DoubleProperty) {
                final Label valueLabel = new Label(twoDp.format(prop.initialValue));
                GridPane.setConstraints(valueLabel, 2, row);
                final Slider slider = new Slider();
                slider.setMin(prop.min);
                slider.setMax(prop.max);
                slider.setValue(((Number) prop.initialValue).doubleValue());
                GridPane.setConstraints(slider, 1, row);
                slider.setMaxWidth(Double.MAX_VALUE);
                slider.valueProperty().addListener(new InvalidationListener() {
                    @Override public void invalidated(Observable ov) {
                        set(prop.valueModel, slider.getValue());
                        valueLabel.setText(twoDp.format(slider.getValue()));
                    }
                });
                getChildren().addAll(slider, valueLabel);
            } else { //if (prop.property.getType() == Color.class || prop.property.getType() == Paint.class) {
                // FIXME we assume anything that isn't a double property is now a colour property
                final Rectangle colorRect = new Rectangle(20,20, (Color)prop.initialValue);
                colorRect.setStroke(Color.GRAY);
                final Label valueLabel = new Label(formatWebColor((Color)prop.initialValue));
                valueLabel.setGraphic(colorRect);
                valueLabel.setContentDisplay(ContentDisplay.LEFT);
                GridPane.setConstraints(valueLabel, 2, row);
                final SimpleHSBColorPicker colorPicker = new SimpleHSBColorPicker();
                GridPane.setConstraints(colorPicker, 1, row);
                colorPicker.getColor().addListener(new InvalidationListener() {
                    @Override public void invalidated(Observable valueModel) {
                        Color c = colorPicker.getColor().get();
                        set(prop.valueModel,c);
                        valueLabel.setText(formatWebColor(c));
                        colorRect.setFill(c);
                    }
                });
                getChildren().addAll(colorPicker, valueLabel);
            }
            row ++;
        }
    }

    private static String formatWebColor(Color c) {
        String r = Integer.toHexString((int)(c.getRed()*255));
        if (r.length() == 1) r = "0"+r;
        String g = Integer.toHexString((int)(c.getGreen()*255));
        if (g.length() == 1) g = "0"+g;
        String b = Integer.toHexString((int)(c.getBlue()*255));
        if (b.length() == 1) b = "0"+b;
        return "#"+r+g+b;
    }

    public static Object get(ObservableValue valueModel) {
        if (valueModel instanceof DoubleProperty) {
            return ((DoubleProperty)valueModel).get();
        } else if (valueModel instanceof ObjectProperty) {
            return ((ObjectProperty)valueModel).get();
        }

        return null;
    }

    public static void set(ObservableValue valueModel, Object value) {
        if (valueModel instanceof DoubleProperty) {
            ((DoubleProperty)valueModel).set((Double)value);
        } else if (valueModel instanceof ObjectProperty) {
            ((ObjectProperty)valueModel).set(value);
        }
    }

    public static final class PropDesc {
        private String name;
        private Double min;
        private Double max;
        private Object initialValue;
        private ObservableValue valueModel;

        public PropDesc(String name, ObservableValue valueModel) {
            this.name = name;
            this.valueModel = valueModel;
            this.initialValue = get(valueModel);
        }

        public PropDesc(String name, DoubleProperty valueModel, Double min, Double max) {
            this.name = name;
            this.valueModel = valueModel;
            this.initialValue = valueModel.get();
            this.min = min;
            this.max = max;
        }
    }
}
