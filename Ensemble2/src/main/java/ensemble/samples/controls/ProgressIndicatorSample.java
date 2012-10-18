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
package ensemble.samples.controls;

import ensemble.Sample;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;

/**
 * A sample that demonstrates the Progress Indicator control in various modes.
 *
 * @see javafx.scene.control.ProgressIndicator
 * @related controls/ProgressBar
 */
public class ProgressIndicatorSample extends Sample {

    public ProgressIndicatorSample() {
        super(400,400);
        
        GridPane g = new GridPane();

        ProgressIndicator p1 = new ProgressIndicator();
        p1.setPrefSize(50, 50);

        ProgressIndicator p2 = new ProgressIndicator();
        p2.setPrefSize(50, 50);
        p2.setProgress(0.25F);

        ProgressIndicator p3 = new ProgressIndicator();
        p3.setPrefSize(50, 50);
        p3.setProgress(0.5F);

        ProgressIndicator p4 = new ProgressIndicator();
        p4.setPrefSize(50, 50);
        p4.setProgress(1.0F);

        g.add(p1, 1, 0);
        g.add(p2, 0, 1);
        g.add(p3, 1, 1);
        g.add(p4, 2, 1);

        g.setHgap(40);
        g.setVgap(40);
        
        getChildren().add(g);
    }
}

