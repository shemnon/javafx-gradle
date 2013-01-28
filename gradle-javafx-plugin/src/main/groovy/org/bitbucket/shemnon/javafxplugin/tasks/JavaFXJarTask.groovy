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
package org.bitbucket.shemnon.javafxplugin.tasks

import com.sun.javafx.tools.packager.CreateJarParams
import com.sun.javafx.tools.packager.Log
import com.sun.javafx.tools.packager.PackagerLib
import org.gradle.api.JavaVersion;
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.file.FileCollection

/**
 * Created by IntelliJ IDEA.
 * User: dannoferrin
 * Date: 3/5/11
 * Time: 7:18 AM
 */
public class JavaFXJarTask extends ConventionTask {

    @TaskAction
    processResources() {
        CreateJarParams createJarParams = new CreateJarParams();

        if (JavaVersion.current().java8Compatible) {
            createJarParams.addResource(null, getJarFile())
        } else {
            // Boo!  Hiss!  Under JDK7 we must explode!
            File exploded = "build/jdk7Compat/${getJarFile().name}" as File
            exploded.mkdirs()
            project.copy {
                from project.zipTree(getJarFile())
                into exploded
            }
            createJarParams.addResource(exploded, ".")
        }

        createJarParams.applicationClass = getMainClass()
        createJarParams.arguments = getArguments()
        createJarParams.classpath = getClasspath().files.collect {it.name}.join ' '
        createJarParams.css2bin = false
        createJarParams.embedLauncher = getEmbedLauncher()
        createJarParams.outdir = getJarFile().parentFile
        createJarParams.outfile = getJarFile().name


        // not provided, fix later
        //createJarParams.manifestAttrs
        //createJarParams.fxVersion
        //createJarParams.fallback
        //createJarParams.preloader


        PackagerLib packager = new PackagerLib();

        Log.setLogger(new Log.Logger(true) {
            @Override
            void info(String msg) {
                getLogger().info(msg)
            }

            @Override
            void verbose(String msg) {
                debug(msg)
            }

            @Override
            void debug(String msg) {
                getLogger().debug(msg)
            }
        } as Log.Logger)

        packager.packageAsJar(createJarParams)

        Log.setLogger(null)

    }

    String mainClass
    boolean embedLauncher
    List<String> arguments

    @InputFiles
    FileCollection classpath

    @InputFile
    File jarFile
}
