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

package ensemble.samples.graphics.images;

import ensemble.Sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * A sample that demonstrates the use of two different constructors in the Image
 * class.
 *
 * @related graphics/images/ImageProperties
 * @see javafx.scene.image.Image
 * @see javafx.scene.image.ImageView
 * @resource icon-48x48.png
 */
public class ImageCreationSample extends Sample {
    private static final Image ICON_48 = new Image(ImageCreationSample.class.getResourceAsStream("icon-48x48.png"));
    public ImageCreationSample() {
        // load and display a image resource from classpath
        ImageView sample1 = new ImageView(ICON_48);               
        // load and display a image resource from url
        ImageView sample2 = new ImageView(
                new Image("http://java.com/images/jv0h.jpg",400,100,true, true));
        //show
        VBox hb = new VBox(10);
        hb.getChildren().addAll(sample1,sample2);
        getChildren().add(hb);
    }

    // REMOVE ME
    public static Node createIconContent() {
        //TODO better icon?
        return new ImageView(ICON_48);
    }
    // END REMOVE ME
}