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
import org.gradle.api.tasks.JavaExec
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import com.bitbucket.shemnon.javafxplugin.tasks.JavaFXCSSToBinTask
import com.bitbucket.shemnon.javafxplugin.tasks.JavaFXSignJarTask
import com.bitbucket.shemnon.javafxplugin.tasks.GenKeyTask
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ResolvedArtifact


class JavaFXPlugin implements Plugin<Project> {

    public static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "providedCompile";
    public static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "providedRuntime";

    @Override
    void apply(Project project) {
        project.getPlugins().apply(JavaPlugin)
        project.extensions.create('javafx', JavaFXPluginExtension)

        configureConfigurations(project.configurations)

        def jfxrtJarFile = project.files(findJFXJar())
        project.javafx {
            jfxrtJar = jfxrtJarFile
            antJavaFXJar = project.files(findAntJavaFXJar())
            mainClass = "${project.group}${(project.group&&project.name)?'.':''}${project.name}${(project.group||project.name)?'.':''}Main"
            appName = project.name //FIXME capatalize
            packaging = 'all'
            debugKey {
                alias = 'javafxdebugkey'
                keyPass = 'JavaFX'
                keyStore = new File(project.projectDir, 'debug.keyStore')
                storePass = 'JavaFX'
            }
            signingMode = 'debug'
        }


        project.dependencies {
            providedCompile jfxrtJarFile
        }
        project.sourceSets {
            'package' {
                resources {
                    srcDir 'src/deploy/package'
                }
            }
        }

        configureJavaFXCSSToBinTask(project)
        configureJavaFXJarTask(project)
        configureGenerateDebugKeyTask(project)
        configureJavaFXSignJarTask(project)
        configureJFXDeployTask(project)
        configureScenicViewTask(project)
        configureRunTask(project)
        configureDebugTask(project)
    }


    private configureJavaFXCSSToBinTask(Project project) {
        def task = project.task("cssToBin", type: JavaFXCSSToBinTask,
                description: "Converts CSS to Binary CSS.",
                group: 'Build')

        task.conventionMapping.distsDir = {convention, aware -> convention.getPlugin(JavaPluginConvention).sourceSets.main.output.resourcesDir}

        task.conventionMapping.inputFiles = {convention, aware ->
            convention.getPlugin(JavaPluginConvention).sourceSets.main.resources
        }

        project.tasks.getByName("classes").dependsOn(task)
        task.dependsOn(project.tasks.getByName("processResources"))
    }

    private configureJavaFXJarTask(Project project) {
        def task = project.task("jfxJar", type: JavaFXJarTask,
                description: "Jars up the classes and adds JavaFX specific packaging.",
                group: 'Build')

        task.conventionMapping.mainClass = {convention, aware -> project.javafx.mainClass }

        task.conventionMapping.outputDirectory = {convention, aware ->
            "$project.libsDir" as File}
        task.conventionMapping.outputFile = {convention, aware ->
            "${project.archivesBaseName}.jar" as File}

        task.conventionMapping.inputFiles = {convention, aware -> convention.getPlugin(JavaPluginConvention).sourceSets.main.output}
        task.conventionMapping.classpath = {convention, aware ->
            FileCollection compileClasspath = project.convention.getPlugin(JavaPluginConvention).sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].compileClasspath;
            Configuration providedCompile = project.configurations[PROVIDED_COMPILE_CONFIGURATION_NAME];
            FileCollection output = compileClasspath - providedCompile;
        }
    }

    private configureGenerateDebugKeyTask(Project project) {
        def task = project.task("generateDebugKey", type: GenKeyTask,
                description: "Generates the JAvaFX Debug Key.",
                group: 'Build')

        task.conventionMapping.alias     = {convention, aware -> project.javafx.debugKey.alias }
        task.conventionMapping.keyPass   = {convention, aware -> project.javafx.debugKey.keyPass }
        task.conventionMapping.keyStore  = {convention, aware -> project.javafx.debugKey.keyStore }
        task.conventionMapping.storePass = {convention, aware -> project.javafx.debugKey.storePass }
        task.conventionMapping.storeType = {convention, aware -> project.javafx.debugKey.storeType }
        task.conventionMapping.dname     = {convention, aware -> 'CN=JavaFX Gradle Plugin Default Debug Key, O=JavaFX Debug' }
        task.conventionMapping.validity  = {convention, aware -> ((365.25) * 25 as int) /* 25 years */ }
    }

    private configureJavaFXSignJarTask(Project project) {
        def task = project.task("jfxSignJar", type: JavaFXSignJarTask,
                description: "Signs the JavaFX jars the JavaFX way.",
                group: 'Build')

        ['alias', 'keyPass', 'storePass', 'storeType'].each { prop ->
            task.conventionMapping[prop]  = {convention, aware ->
                def jfxc = project.javafx;
                def props = project.properties
                def mode = props['javafx.signingMode']  ?: jfxc.signingMode
                return props?."javafx.${mode}Key.$prop" ?: jfxc?."${mode}Key"?."$prop"
            }
        }
        task.conventionMapping.keyStore  = {convention, aware ->
            def jfxc = project.javafx;
            def props = project.properties
            def mode = props['javafx.signingMode']  ?: jfxc.signingMode
            String keyFile = props?."javafx.${mode}Key.keyStore"
            return keyFile == null ? jfxc?."${mode}Key"?.keyStore : new File(keyFile)
        }

        task.conventionMapping.outdir = {convention, aware -> "$project.libsDir/../signed" as File}

        task.conventionMapping.inputFiles = {convention, aware ->
            FileCollection runtimeClasspath = project.convention.getPlugin(JavaPluginConvention).sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].runtimeClasspath;
            Configuration providedRuntime = project.configurations[PROVIDED_RUNTIME_CONFIGURATION_NAME];
            runtimeClasspath  + project.files("$project.libsDir/${project.archivesBaseName}.jar" as File)- providedRuntime
        }

        task.dependsOn(project.tasks.getByName("jfxJar"))
        task.dependsOn(project.tasks.getByName("generateDebugKey"))
    }

    private configureJFXDeployTask(Project project) {
        def task = project.task("jfxDeploy", type: JavaFXDeployTask,
                description: "Processes the JavaFX jars and generates webstart and native packages.",
                group: 'Build')

        task.conventionMapping.packaging = {convention, aware -> project.javafx.packaging }

        task.conventionMapping.antJavaFXJar = {convention, aware ->
            project.javafx.antJavaFXJar }

        task.conventionMapping.appID = {convention, aware -> project.javafx.appID }
        task.conventionMapping.appName = {convention, aware -> project.javafx.appName }
        task.conventionMapping.mainClass = {convention, aware -> project.javafx.mainClass }


        task.conventionMapping.inputFiles = {convention, aware ->
            project.fileTree("$project.libsDir/../signed").include("*.jar")
        }
        task.conventionMapping.resourcesDir = { convention, aware ->
            project.sourceSets['package'].output.resourcesDir
        }

        task.conventionMapping.distsDir = {convention, aware -> project.distsDir }

        task.conventionMapping.jvmArgs = {convention, aware -> project.javafx.jvmArgs }
        task.conventionMapping.systemProperties = {convention, aware -> project.javafx.systemProperties }
        task.conventionMapping.arguments = {convention, aware -> project.javafx.arguments}

        task.dependsOn(project.tasks.getByName("jfxSignJar"))
        task.dependsOn(project.tasks.getByName("packageClasses"))

        project.tasks.getByName("assemble").dependsOn(task)
        project.tasks.getByName("jar").enabled = false
    }
    
    private void configureRunTask(Project project) {
        JavaExec task = project.task("run", type: JavaExec,
            description: 'Runs the application.',
            group: 'Execution')

        task.classpath = project.sourceSets.main.runtimeClasspath
        task.conventionMapping.main = {convention, aware -> project.javafx.mainClass }
        task.doFirst {
            task.jvmArgs project.javafx.jvmArgs
            task.systemProperties project.javafx.systemProperties
            if (!task.args) task.args = project.javafx.arguments
        }
    }

    private void configureDebugTask(Project project) {
        JavaExec task = project.task("debug", type:JavaExec,
            description: 'Runs the applicaiton and sets up debugging on port 5005.',
            group: 'Execution')

        task.classpath = project.sourceSets.main.runtimeClasspath
        task.conventionMapping.main = {convention, aware -> project.javafx.mainClass }
        task.doFirst {
            task.jvmArgs project.javafx.jvmArgs
            task.systemProperties project.javafx.systemProperties
            if (!task.args) task.args = project.javafx.arguments
        }
        task.debug = true
    }

    private void configureScenicViewTask(Project project) {
        def task = project.task("scenicview", type: DefaultTask,
                description: 'Adds the ScenicView agent to all Execution Tasks.',
                group: 'Tools')

        task.doLast {
            project.configurations {
                scenicview
            }
            project.repositories {
                ivy  { url 'https://repository-javafx-gradle-plugin.forge.cloudbees.com/release' }
            }
            project.dependencies {
                scenicview 'com.fxexperience.scenicview:scenicview:1.3.0'
            }

            project.tasks.findAll {it.group == 'Execution' && it instanceof JavaExec}.each {JavaExec execTask ->
                project.configurations.getByName('scenicview').resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact ra ->
                    execTask.jvmArgs = ["-javaagent:$ra.file.canonicalPath"] + execTask.jvmArgs
                }
            }
        }
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