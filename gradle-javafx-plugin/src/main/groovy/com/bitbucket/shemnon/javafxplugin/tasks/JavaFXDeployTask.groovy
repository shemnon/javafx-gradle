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

import com.sun.javafx.tools.packager.DeployParams
import com.sun.javafx.tools.packager.DeployParams.RunMode;
import com.sun.javafx.tools.packager.PackagerLib
import com.sun.javafx.tools.packager.bundlers.Bundler;
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.internal.ConventionTask
import org.gradle.api.file.FileCollection
import org.gradle.util.ConfigureUtil

class JavaFXDeployTask extends ConventionTask {

    @TaskAction
    processResources() {
        DeployParams deployParams = new DeployParams();

        deployParams.version = getProject().getVersion() // FIXME make a convention property

        // these deploy params are currently not set
        //java.lang.String preloader;
        //java.util.List<com.sun.javafx.tools.packager.Param> params;
        //java.util.List<com.sun.javafx.tools.packager.HtmlParam> htmlParams;
        //java.util.List<java.lang.String> arguments;
        //boolean embedCertificates;
        //boolean isExtension;
        //boolean isSwingApp;
        //boolean includeDT;
        //java.lang.String placeholder;
        //java.lang.String appId;
        //java.util.List<com.sun.javafx.tools.ant.Callback> callbacks;
        //java.util.List<com.sun.javafx.tools.packager.DeployParams.Template> templates;
        //java.lang.String jrePlatform;
        //java.lang.String fxPlatform;
        //java.util.List<java.lang.String> jvmargs;
        //java.util.Map<java.lang.String,java.lang.String> properties;
        //java.lang.String fallbackApp;
        //java.util.List<com.sun.javafx.tools.packager.DeployParams.Icon> icons;


        deployParams.width = getWidth()
        deployParams.height = getHeight()
        deployParams.setEmbeddedDimensions(getWidth() as String, getHeight() as String) // FIXME make a convention property
        deployParams.setOutdir(getDistsDir())
        deployParams.embedJNLP = getEmbedJNLP()
        deployParams.outfile = getAppName() //FIXME duplicate with app name
        switch (getPackaging().toLowerCase()) {
            case "false":
            case "none":
                deployParams.bundleType = Bundler.BundleType.NONE
                deployParams.targetFormat = null
                break;
            case "all":
            case "true":
                deployParams.bundleType = Bundler.BundleType.ALL
                deployParams.targetFormat = null
                break;
            case "image":
                deployParams.bundleType = Bundler.BundleType.IMAGE
                deployParams.targetFormat = null
                break;
            case "installer":
                deployParams.bundleType = Bundler.BundleType.INSTALLER
                deployParams.targetFormat = null
                break;
            default:
                // assume the packageing is for a specific type
                deployParams.bundleType = Bundler.BundleType.INSTALLER
                deployParams.targetFormat = getPackaging()

        }
        
        deployParams.verbose = getVerbose()

        deployParams.id = getAppID()
        deployParams.appName = getAppName() // FIXME duplicate with title
        deployParams.applicationClass = getMainClass()

        getInputFiles() each { File f ->
            deployParams.addResource(f.parentFile, f);
        }

        deployParams.title = getAppName()
        deployParams.category = getCategory()
        deployParams.copyright = getCopyright()
        deployParams.description = getDescription()
        deployParams.licenseType = getLicenseType()
        deployParams.vendor = getVendor()

        deployParams.systemWide = getInstallSystemWide()
        deployParams.needMenu = getMenu()
        deployParams.needShortcut = getShortcut()

        deployParams.allPermissions = true //FIXME hardcoded

        deployParams.updateMode = getUpdateMode()
        deployParams.offlineAllowed = getOfflineAllowed()
        for (IconInfo ii : icons) {
            deployParams.addIcon(ii.href, ii.kind, ii.width, ii.height, ii.depth, ii.mode);
        }
        if (getCodebase() != null) {
            try {
                deployParams.codebase = getCodebase()
            } catch (MissingPropertyException moe) {
                getLogger().error("JavaFXDeployTask.codebase is only available in JavaFX 8 or later, codebase setting is ignored")
            }
        }

        jvmArgs.each { deployParams.addJvmArg(it) }
        systemProperties.each {k, v -> deployParams.addJvmProperty(k, v)}
        deployParams.arguments = arguments

        PackagerLib packager = new PackagerLib();
        packager.generateDeploymentPackages(deployParams)
    }

    def icon(Closure closure) {
         icons.add(new IconInfo(closure))
    }

    String packaging

    FileCollection antJavaFXJar

    String appID
    String appName

    boolean verbose = false

    String mainClass
    int width = 1024
    int height = 768
    boolean embedJNLP = false
    String updateMode = "background"
    boolean offlineAllowed = true
    String codebase

    List<String> jvmArgs = []
    Map<String, String> systemProperties = [:]
    List<String> arguments = []

    // deplpy/info attributes
    String category
    String copyright
    String description
    String licenseType
    String vendor
    List<IconInfo> icons = []

    // deploy/preferences attributes
    Boolean installSystemWide
    boolean menu
    boolean shortcut

    @InputFiles
    FileCollection inputFiles


    @OutputDirectory
    File distsDir
}

class IconInfo {
    String href
    String kind = 'default'
    int width = DeployParams.Icon.UNDEFINED
    int height = DeployParams.Icon.UNDEFINED
    int depth = DeployParams.Icon.UNDEFINED
    RunMode mode = RunMode.ALL


    public IconInfo(Closure configure) {
        ConfigureUtil.configure(configure, this)
    }
}
