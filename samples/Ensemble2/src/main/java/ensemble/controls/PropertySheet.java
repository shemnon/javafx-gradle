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

import java.util.ArrayList;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.util.Arrays;
import java.util.List;

/**
 * PropertySheet for editing groups of bean properties
 */
public class PropertySheet extends Accordion {

    private final List<PropertyGroup> propertyGroups;

    public PropertySheet(PropertyGroup ... propertyGroups) {
        getStyleClass().add("property-sheet");
        this.propertyGroups = Arrays.asList(propertyGroups);
        for(PropertyGroup pg: propertyGroups) {
            getPanes().add(pg.create());
        }
        
        if (propertyGroups.length > 0) getPanes().get(0).setExpanded(true);
        setMinHeight(USE_PREF_SIZE);
    }

    public static Property createProperty(String name, EventHandler<ActionEvent> action) {
        return new ActionPropertyImpl(name, action);
    }
    public static Property createProperty(String name, StringProperty prop) {
        return new StringPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, IntegerProperty prop) {
        return new IntegerPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, DoubleProperty prop) {
        return createProperty(name, prop, false);
    }
   public static Property createProperty(String name, DoubleProperty prop, boolean disable) {
        return new DoublePropertyImpl(name, prop, disable);
    }
    public static Property createProperty(String name, BooleanProperty prop) {
        return new BooleanPropertyImpl(name, prop);
    }
    public static Property createProperty(String name, ObjectProperty prop) {
        if (prop.getValue() instanceof Enum) {
            return new EnumPropertyImpl(name, prop, new ArrayList<Enum>());
        } else {
            return null;
        }
    }
    public static Property createProperty(String name, ObjectProperty prop, List<Enum> overridingValues) {
        if (prop.getValue() instanceof Enum) {
            return new EnumPropertyImpl(name, prop, overridingValues);
        } else {
            return null;
        }
    }

    /**
     * A named group of properties
     */
    public static class PropertyGroup {
        private final String title;
        private final Property[] properties;

        public PropertyGroup(String title,Property ... properties) {
            this.title = title;
            this.properties = properties;
        }

        private TitledPane create() {
            GridPane grid = new GridPane();
            grid.setMaxWidth(Double.MAX_VALUE);
            TitledPane titledPane = new TitledPane(title, grid);
            grid.getColumnConstraints().addAll(
                    new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.CENTER, true),
                    new ColumnConstraints(50, USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true)
            );
            int row = 0;
            for(Property prop: properties) {
                if (prop == null){
                    continue;
                } else if (prop instanceof ActionPropertyImpl) {
                    Label propLabel = new Label(prop.getName()+":");
                    propLabel.getStyleClass().add("property-label");
                    Node editor = prop.getEditor();
                    GridPane.setHalignment(editor, HPos.CENTER);
                    GridPane.setHgrow(editor, Priority.ALWAYS);
                    GridPane.setMargin(editor, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(editor, 0);
                    GridPane.setColumnSpan(editor, 2);
                    GridPane.setRowIndex(editor, row);
                    grid.getChildren().add(editor);
                    row ++;
                } else {
                    Label propLabel = new Label(prop.getName()+":");
                    propLabel.setMaxWidth(Double.MAX_VALUE);
                    propLabel.setAlignment(Pos.CENTER_RIGHT);
                    GridPane.setHalignment(propLabel, HPos.RIGHT);
                    GridPane.setMargin(propLabel, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(propLabel,0);
                    GridPane.setRowIndex(propLabel, row);
                    grid.getChildren().add(propLabel);
                    Node editor = prop.getEditor();
                    GridPane.setHgrow(editor, Priority.ALWAYS);
                    GridPane.setMargin(editor, new Insets(2,2,2,2));
                    GridPane.setColumnIndex(editor,1);
                    GridPane.setRowIndex(editor, row);
                    grid.getChildren().add(editor);
                    row ++;
                }
            }
            return titledPane;
        }
    }

    /**
     * Model class describing a property
     */
    public static abstract class Property {
        private String name;

        protected Property(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract Node getEditor();
    }

    /**
     * Implementation of a hyperlink action type property
     */
    static class ActionPropertyImpl extends Property {
        private Hyperlink editor = new Hyperlink();

        ActionPropertyImpl(String name, EventHandler<ActionEvent> action) {
            super(name);
            editor.setText(name);
            editor.setOnAction(action);
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    /**
     * Implementation of a string type property
     */
    static class StringPropertyImpl extends Property {
        private TextField editor = new TextField();
        private String value = null;

        StringPropertyImpl(String name, final StringProperty sp) {
            super(name);
            if (sp.getValue() != null) {
                editor.setText(sp.getValue());
            }
            sp.addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> valueModel, String oldValue, String newValue) {
                    if (value == null || !value.equals(newValue))
                            editor.setText(valueModel.getValue());
                }
            });
            editor.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent t) {
                    String newValue = editor.getText();
                    if (value == null || !value.equals(newValue))
                         sp.setValue(newValue);
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    /**
     * Implementation of a double number type property
     */
    static class DoublePropertyImpl extends Property {
        private TextField editor = new TextField();

        DoublePropertyImpl(String name, final DoubleProperty dp, boolean disable) {
            super(name);
            editor.setText(Double.toString(dp.getValue()));
            editor.setDisable(disable);
            dp.addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> valueModel, Number oldValue, Number newValue) {
                    editor.setText(newValue.toString());
                }
            });
            editor.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent t) {
                    try {
                        String newValue = editor.getText();
                        dp.setValue(Double.parseDouble(newValue));
                    } catch (NumberFormatException e) {}
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    /**
     * Implementation of a integer number type property
     */
    static class IntegerPropertyImpl extends Property {
        private TextField editor = new TextField();

        IntegerPropertyImpl(String name, final IntegerProperty ip) {
            super(name);
            editor.setText(Integer.toString(ip.getValue()));
            ip.addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> valueModel, Number oldValue, Number newValue) {
                    editor.setText(newValue.toString());
                }
            });
            editor.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent t) {
                    try {
                        String newValue = editor.getText();
                        ip.set(Integer.parseInt(newValue));
                    } catch (NumberFormatException e) {}
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    /**
     * Implementation of a boolean type property
     */
    static class BooleanPropertyImpl extends Property {
        private CheckBox editor = new CheckBox();

        BooleanPropertyImpl(String name, final BooleanProperty bp) {
            super(name);
            editor.setSelected(bp.getValue());
            bp.addListener(new ChangeListener<Boolean>() {
                @Override public void changed(ObservableValue<? extends Boolean> valueModel, Boolean oldValue, Boolean newValue) {
                    editor.setSelected(newValue);
                }
            });
            editor.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> valueModel, Boolean oldValue, Boolean newValue) {
                    bp.setValue(newValue);
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }

    /**
     * Implementation of a enum type property
     */
    static class EnumPropertyImpl extends Property {
        private ChoiceBox<Enum> editor = new ChoiceBox<Enum>();

        EnumPropertyImpl(String name, final ObjectProperty<Enum> ep, List<Enum> overridingValues) {
            super(name);
            Enum val =  ep.get();
            if ((overridingValues != null) && (overridingValues.size() > 0)) {
                for (Enum enumOpt : overridingValues) {
                    editor.getItems().add(enumOpt);
                }
            } else {
                for (Object enumOpt : val.getDeclaringClass().getEnumConstants()) {
                    editor.getItems().add((Enum)enumOpt);
                }
            }
            editor.getSelectionModel().select(val);
            ep.addListener(new ChangeListener<Enum>() {
                @Override public void changed(ObservableValue<? extends Enum> valueModel, Enum oldValue, Enum newValue) {
                    editor.getSelectionModel().select(newValue);
                }
            });
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Enum>() {
                @Override public void changed(ObservableValue<? extends Enum> valueModel, Enum oldValue, Enum newValue) {
                    ep.setValue(newValue);
                }
            });
        }

        @Override public Node getEditor() {
            return editor;
        }
    }
}
