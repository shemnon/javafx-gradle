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
package com.bitbucket.shemnon.javafxplugin.tasks;

import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.internal.ConventionTask
import org.gradle.api.file.FileCollection

class JavaFXDeployTask extends ConventionTask {

    @TaskAction
    processResources() {
        ant.taskdef(name: 'fxDeploy',
                classname: 'com.sun.javafx.tools.ant.DeployFXTask',
                classpath: (getAntJavaFXJar() + project.files(project.sourceSets.'package'.allSource.srcDirs)).asPath)

        ant.fxDeploy(
                width: getWidth(),
                height: getHeight(),
                outDir: getDistsDir(),
                embedJNLP: getEmbedJNLP(),
                outFile: getAppName(),
                nativeBundles: getPackaging(),
                verbose: getVerbose()

        ) {

            application(
                    id: getAppID(),
                    name: getAppName(),
                    mainClass: getMainClass()
            )
            resources {
                getInputFiles().filter() { it.file } each {
                    fileset(file: it)
                }

            }
            info(
                title: getAppName(),
                category: getCategory(),
                copyright: getCopyright(),
                description: getDescription(),
                license: getLicense(),
                vendor: getVendor()
            )
            preferences(
                createPreferencesAttributes()
            )

            permissions(elevated: 'true')
        }
    }

    String packaging

    FileCollection antJavaFXJar

    String appID
    String appName
    String mainClass

    int width = 1024
    int height = 768
    boolean embedJNLP = false
    boolean verbose = false


    // deplpy/info attributes
    String category
    String copyright
    String description
    String license
    String vendor

    // deploy/preferences attributes
    Boolean installSystemWide
    Boolean menu
    Boolean shortcut


    @InputFiles
    FileCollection inputFiles


    @OutputDirectory
    File distsDir

    protected Map createPreferencesAttributes() {
        def res = [:]
        if (getInstallSystemWide() != null) {
            res.install = getInstallSystemWide()
        }
        if (getMenu() != null) {
            res.menu = getMenu()
        }
        if (getShortcut() != null) {
            res.shortcut = getShortcut()
        }
        return res
    }

}
