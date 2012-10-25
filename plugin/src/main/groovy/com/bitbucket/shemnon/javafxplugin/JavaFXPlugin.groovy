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
package com.bitbucket.shemnon.javafxplugin

import org.gradle.api.Project
import org.gradle.api.Plugin
import com.bitbucket.shemnon.javafxplugin.tasks.JavaFXDeployTask
import com.bitbucket.shemnon.javafxplugin.tasks.JavaFXJarTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.SourceSet
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import com.bitbucket.shemnon.javafxplugin.tasks.JavaFXCSSToBinTask


class JavaFXPlugin implements Plugin<Project> {

    public static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "providedCompile";
    public static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "providedRuntime";

    @Override
    void apply(Project project) {
        project.getPlugins().apply(JavaPlugin)
        configureConfigurations(project.configurations)

        def jfxrtJarFile = project.files(findJFXJar())
        project.convention.plugins.javafx = new JavaFXPluginConvention(project, {
                jfxrtJar = jfxrtJarFile
                antJavaFXJar = project.files(findAntJavaFXJar())
                mainClass = "${project.group}${(project.group&&project.name)?'.':''}${project.name}${(project.group||project.name)?'.':''}Main"
                appName = project.name //FIXME capatalize
                packaging = 'all'
                debugKey {
                    alias = 'javafxdebugkey'
                    dname = 'CN=JavaFX Debug,C=US'
                    validity = ((365.25) * 25 as int) // 25 years
                    keypass = 'JavaFX'
                    keystore = new File(project.projectDir, 'debug.keystore')
                    storepass = 'JavaFX'
                    sigfile = 'JavaFXDebug'
                }
            })


        project.dependencies {
            providedCompile jfxrtJarFile
        }
        project.sourceSets {
            'package' {
                resources {
                    srcDir 'src/main'
                }
            }
        }

        configureJFXDeployTask(project)
        configureJavaFXJarTask(project)
        configureJavaFXCSSToBinTask(project)
    }

    private configureJFXDeployTask(Project project) {
        def task = project.task("jfxDeploy", description: "Processes the JavaFX jars and generates webstart and native packages", type: JavaFXDeployTask)

        task.conventionMapping.packaging = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).packaging }

        task.conventionMapping.antJavaFXJar = {convention, aware ->
            convention.getPlugin(JavaFXPluginConvention).antJavaFXJar }

        task.conventionMapping.appID = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).appID }
        task.conventionMapping.appName = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).appName }
        task.conventionMapping.mainClass = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).mainClass }

        task.conventionMapping.inputFiles = {convention, aware -> convention.getPlugin(JavaPluginConvention).sourceSets.main.runtimeClasspath }
        task.conventionMapping.inputFiles = {convention, aware ->
            FileCollection runtimeClasspath = project.convention.getPlugin(JavaPluginConvention).sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].runtimeClasspath;
            Configuration providedRuntime = project.configurations[PROVIDED_RUNTIME_CONFIGURATION_NAME];
            runtimeClasspath  + project.files("$project.libsDir/${project.archivesBaseName}.jar" as File)- providedRuntime
        }
        //TODO substract provided runtime

        task.conventionMapping.distsDir = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).distsDir }

        project.tasks.getByName("assemble").dependsOn(task)
        project.tasks.getByName("jar").enabled = false
    }


    private configureJavaFXJarTask(Project project) {
        def task = project.task("jfxJar", description: "Jars up the classes and adds JavaFX specific packaging", type: JavaFXJarTask)

        task.conventionMapping.antJavaFXJar = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).antJavaFXJar }

        task.conventionMapping.mainClass = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).mainClass }
        task.conventionMapping.appName = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).appName }

        task.conventionMapping.outputFile = {convention, aware ->
            "$project.libsDir/${project.archivesBaseName}.jar" as File}

        task.conventionMapping.inputFiles = {convention, aware ->
            FileCollection compileClasspath = project.convention.getPlugin(JavaPluginConvention).sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].compileClasspath;
            Configuration providedCompile = project.configurations[PROVIDED_COMPILE_CONFIGURATION_NAME];
            FileCollection output = convention.getPlugin(JavaPluginConvention).sourceSets.main.output
            compileClasspath + output - providedCompile;
        }

        project.tasks.getByName("jfxDeploy").dependsOn(task)
    }

    private configureJavaFXCSSToBinTask(Project project) {
        def task = project.task("cssToBin", description: "Converts CSS to Binary CSS", type: JavaFXCSSToBinTask)

        task.conventionMapping.antJavaFXJar = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).antJavaFXJar }
        task.conventionMapping.jfxrtJar = {convention, aware -> convention.getPlugin(JavaFXPluginConvention).jfxrtJar }

        task.conventionMapping.distsDir = {convention, aware -> convention.getPlugin(JavaPluginConvention).sourceSets.main.output.resourcesDir}

        task.conventionMapping.inputFiles = {convention, aware ->
            project.fileTree(dir: convention.getPlugin(JavaPluginConvention).sourceSets.main.output.resourcesDir, include: '**/*.css')
        }

        project.tasks.getByName("jfxJar").dependsOn(task)
    }

    public void configureConfigurations(ConfigurationContainer configurationContainer) {
        Configuration provideCompileConfiguration = configurationContainer.add(PROVIDED_COMPILE_CONFIGURATION_NAME).setVisible(false).
                setDescription("Additional compile classpath for libraries that should not be part of the WAR archive.");
        Configuration provideRuntimeConfiguration = configurationContainer.add(PROVIDED_RUNTIME_CONFIGURATION_NAME).setVisible(false).
                extendsFrom(provideCompileConfiguration).
                setDescription("Additional runtime classpath for libraries that should not be part of the WAR archive.");
        configurationContainer.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(provideCompileConfiguration);
        configurationContainer.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME).extendsFrom(provideRuntimeConfiguration);
    }

    public File findJFXJar() {
        def javafxHome = System.env['JFXRT_HOME']
        File jfxrtHome
        if (javafxHome) {
            jfxrtHome = "${javafxHome}" as File
        } else {
            final javaHome = System.env['JAVA_HOME']
            if (javaHome)
                jfxrtHome = "${javaHome}/jre/lib"  as File
            else
                jfxrtHome = "${System.properties['java.home']}/lib" as File
        }

        File jfxrtJar = new File((File) jfxrtHome, "jfxrt.jar")
        if (!jfxrtJar.exists()) {
            println ("""    Please set the environment variable JFXRT_HOME
    to the directory that contains jfxrt.jar, or set JAVA_HOME.""")

        }
        println "JavaFX runtime jar: ${jfxrtJar}"
        return jfxrtJar
    }

    public File findAntJavaFXJar() {
        def javafxHome = System.env['JFXRT_HOME']
        File jfxrtHome
        if (javafxHome) {
            jfxrtHome = "${javafxHome}" as File
        } else {
            final javaHome = System.env['JAVA_HOME']
            if (javaHome)
                jfxrtHome = "${javaHome}/lib"  as File
            else
                jfxrtHome = "${System.properties['java.home']}/../lib" as File
        }

        File antjfxjar = new File((File)jfxrtHome, "ant-javafx.jar")
        if (!antjfxjar.exists()) {
            println("""    Please set the environment variable JFXRT_HOME
    to the directory that contains jfxrt.jar, or set JAVA_HOME.""")

        }
        println "JavaFX ant jar: ${antjfxjar}"
        return antjfxjar
    }
}
