/*
 * Copyright (c) 2012, Danno Ferrin
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of Danno Ferrin nor the
 *         names of contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.bitbucket.shemnon.javafxplugin.tasks

import com.sun.javafx.tools.packager.CreateBSSParams
import com.sun.javafx.tools.packager.PackagerLib
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.file.collections.DirectoryFileTree
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JavaFXCSSToBinTask extends ConventionTask {

    @TaskAction
    processResources() {

        CreateBSSParams bssParams = new CreateBSSParams();
        getInputFiles().srcDirTrees.each { DirectoryFileTree dirTree ->
            dirTree.visit new FileVisitor() {
                @Override
                void visitDir(FileVisitDetails fileVisitDetails) {
                    // do nothing
                }

                @Override
                void visitFile(FileVisitDetails fileVisitDetails) {
                    File f = fileVisitDetails.getFile();
                    if (f.getName().endsWith(".css")) {
                        bssParams.addResource(dirTree.getDir(), f);
                    }
                }
            }
        }
        bssParams.setOutdir(getDistsDir())

        PackagerLib packager = new PackagerLib();
        packager.generateBSS(bssParams)
    }

    @InputFiles
    SourceDirectorySet inputFiles

    @OutputDirectory
    File distsDir
}
