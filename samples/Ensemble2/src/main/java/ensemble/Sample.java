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
package ensemble;

import ensemble.controls.SimplePropertySheet;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Base class for all Ensemble samples. Samples can either support layout resizing or they can be fixed size if needed
 * for simple graphics etc. If you need fixed size use the constructor with width and height arguments.
 */
public class Sample extends Pane {
    protected static final Image ICON_48 = new Image(Ensemble2.class.getResourceAsStream("images/icon-48x48.png"));
    protected static final Image BRIDGE = new Image(Ensemble2.class.getResourceAsStream("images/sanfran.jpg"));

    private Node controls = null;
    private boolean isFixedSize;

    /**
     * Create a resizable sample
     */
    public Sample() {
        isFixedSize = false;
        VBox.setVgrow(this, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
    }

    /**
     * Create a fixed size sample
     *
     * @param width The fixed width of the sample
     * @param height The fixed height of the sample
     */
    public Sample(double width, double height) {
        isFixedSize = true;
        setMaxSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);
    }

    @Override protected void layoutChildren() {
        if (isFixedSize) {
            super.layoutChildren();
        } else {
            List<Node> managed = getManagedChildren();
            double width = getWidth();
            ///System.out.println("width = " + width);
            double height = getHeight();
            ///System.out.println("height = " + height);
            double top = getInsets().getTop();
            double right = getInsets().getRight();
            double left = getInsets().getLeft();
            double bottom = getInsets().getBottom();
            for (int i = 0; i < managed.size(); i++) {
                Node child = managed.get(i);
                layoutInArea(child, left, top,
                               width - left - right, height - top - bottom,
                               0, Insets.EMPTY, true, true, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    public void play(){}
    public void stop(){}

    protected void setControls(SimplePropertySheet.PropDesc...properties) {
        controls = new SimplePropertySheet(properties);
    }

    public Node getControls() {
        return controls;
    }

    /**
     * Samples can override this if they want to provide extra content for the side bar
     *
     * @return A node to be added at the bottom of the side bar
     */
    public Node getSideBarExtraContent() {
        return null;
    }
    
    /**
     * Samples can override this if they want to provide extra content for the side bar. This provides the title.
     *
     * @return The title for the extra content section provided by the sample
     */
    public String getSideBarExtraContentTitle() {
        return null;
    }

}
