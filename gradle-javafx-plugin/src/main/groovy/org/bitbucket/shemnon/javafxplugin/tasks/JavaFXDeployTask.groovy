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

import com.sun.javafx.tools.packager.DeployParams
import com.sun.javafx.tools.packager.DeployParams.RunMode
import com.sun.javafx.tools.packager.Log
import com.sun.javafx.tools.packager.PackagerLib
import com.sun.javafx.tools.packager.bundlers.Bundler
import net.sf.image4j.codec.bmp.BMPEncoder
import net.sf.image4j.codec.ico.ICOEncoder
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.util.ConfigureUtil

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.List

class JavaFXDeployTask extends ConventionTask {


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

    @InputDirectory
    File resourcesDir

    @OutputDirectory
    File distsDir

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
            } catch (MissingPropertyException ignored) {
                getLogger().error("JavaFXDeployTask.codebase is only available in JavaFX 8 or later, codebase setting is ignored")
            }
        }

        jvmArgs.each { deployParams.addJvmArg(it) }
        systemProperties.each {k, v -> deployParams.addJvmProperty(k, v)}
        deployParams.arguments = arguments

        File packageResourcesOutput = project.sourceSets['package'].output.resourcesDir
        processIcons(packageResourcesOutput)

        // hack
        deployParams.class.classLoader.addURL(packageResourcesOutput.parentFile.toURI().toURL())


        PackagerLib packager = new PackagerLib();
        Log.setLogger(new Log.Logger(getVerbose()) {
            @Override
            void info(String msg) {
                getLogger().info(msg)
            }

            @Override
            void verbose(String msg) {
                if (getVerbose()) {
                    info(msg)
                } else {
                    debug(msg)
                }
            }

            @Override
            void debug(String msg) {
                getLogger().debug(msg)
            }
        } as Log.Logger)

        packager.generateDeploymentPackages(deployParams)
        Log.setLogger(null)
    }

    def icon(Closure closure) {
        icons.add(new IconInfo(closure))
    }


    protected void processIcons(File destination) {
        if (Os.isFamily(Os.FAMILY_MAC)) {
            processMacOSXIcons(destination);
        }
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            processWindowsIcons(destination);
        }
        if (Os.isFamily(Os.FAMILY_UNIX) && !Os.isFamily(Os.FAMILY_MAC)) {
            processLinuxIcons(destination)
        }
    }

    protected void processMacOSXIcons(File destination) {
        processMacOSXIcns('shortcut',
                new File(destination, "macosx/${project.javafx.appName}.icns"))
        processMacOSXIcns('volume',
                new File(destination, "macosx/${project.javafx.appName}-volume.icns"))
    }

    def macIcnsSizes = [16,32,128,256,512]
    protected void processMacOSXIcns(String kind, File iconLocation) {
        // get explicit
        def dest = "$project.buildDir/icons/${kind}.iconset"
        project.mkdir(dest)
        boolean createIcon = false
        for (IconInfo ii : icons) {
            if (kind == ii.kind) {
                if (ii.width != ii.height) {
                    logger.info("Icon $ii.href for $ii.kind rejected from MacOSX bundling because it is not square: $ii.width x $ii.height")
                    continue;
                }
                if (ii.scale != 1 && ii.scale != 2) {
                    logger.info("Icon $ii.href for $ii.kind rejected from MacOSX bundling because it has an invalid scale")
                    continue;
                }
                int index = macIcnsSizes.indexOf(ii.height)
                if (index == -1) {
                    logger.info("Icon $ii.href for $ii.kind rejected from MacOSX bundling because it is an unsupported dimension.  $macIcnsSizes dimensions are supported")
                    continue;
                }
                File file = project.file(ii.href)
                if (!file.exists()) {
                    // try to resolve relative to output
                    file = new File(getResourcesDir(), ii.href)
                }
                if (!file.isFile()) {
                    logger.error("Icon $ii.href for $ii.kind rejected from MacOSX bundling because $ii.href does not exist or it is a directory.")
                    continue;
                }

                ant.copy(file: file, toFile: "$dest/icon_${ii.width}x${ii.height}${ii.scale == 2 ? '@2x': ''}.png")
                createIcon = true
            }

        }
        if (createIcon) {
            project.exec {
                executable 'iconutil'
                args ('--convert', 'icns', dest)
            }
            ant.copy(file: "$project.buildDir/icons/${kind}.icns", toFile: iconLocation)
        }
    }

    protected void processWindowsIcons(File destination) {
        processWindowsIco('shortcut',
                new File(destination, "windows/${project.javafx.appName}.ico"))
        processWidnowsBMP('setup-icon',
                new File(destination, "windows/${project.javafx.appName}-setup-icon.bmp"))
    }

    protected void processWidnowsBMP(String kind, File destination) {
        boolean processed = false
        for (IconInfo ii : icons) {
            if (kind == ii.kind) {

                File file = project.file(ii.href)
                if (!file.exists()) {
                    // try to resolve relative to output
                    file = new File(getResourcesDir(), ii.href)
                }
                if (!file.isFile()) {
                    logger.error("Icon $ii.href for $ii.kind rejected from Windows bundling because $ii.href does not exist or it is a directory.")
                    continue;
                }
                if (processed) {
                    logger.info("Icon $ii.href for $ii.kind rejected from Windows bundling because only one icon can be used.")
                    continue;
                }

                Image icon = Toolkit.defaultToolkit.getImage(file.toURI().toURL())


                double scale = Math.min(Math.min(55.0 / icon.width, 58.0 / icon.height), 1.0)

                BufferedImage bi = new BufferedImage((int)icon.width*scale, (int)icon.height*scale, BufferedImage.TYPE_INT_ARGB)
                def g = bi.graphics
                def t = new AffineTransform()
                println scale
                t.scale(scale, scale)
                g.transform = t
                g.drawImage(icon, 0, 0, null)
                BMPEncoder.write(bi, destination)
                processed = true
            }
        }
    }

    protected void processWindowsIco(String kind, File destination) {
        Map<Integer, BufferedImage> images = new TreeMap<Integer, BufferedImage>()
        for (IconInfo ii : icons) {
            if (kind == ii.kind) {
                File file = project.file(ii.href)
                if (!file.exists()) {
                    // try to resolve relative to output
                    file = new File(getResourcesDir(), ii.href)
                }
                if (!file.isFile()) {
                    logger.error("Icon $ii.href for $ii.kind rejected from Windows bundling because $ii.href does not exist or it is a directory.")
                    continue;
                }
                if (ii.scale != 1) {
                    logger.info("Icon $ii.href for $ii.kind rejected from Widnows bundling because it has a scale other than '1'")
                    continue;
                }

                Image icon = Toolkit.defaultToolkit.getImage(file.toURI().toURL())

                if (icon.width != icon.height) {
                    logger.info("Icon $ii.href for $ii.kind rejected from Windows bundling because it is not square: $icon.width x $icon.height")
                    continue;
                }
                BufferedImage bi = new BufferedImage(icon.width, icon.height, BufferedImage.TYPE_INT_ARGB)
                bi.graphics.drawImage(icon, 0, 0, null)
                images.put(bi.width, bi)
            }

        }
        if (images) {
            destination.parentFile.mkdirs()
            List<BufferedImage> icons = images.values() as List
            icons.addAll(images.values())
            icons.addAll(images.values())
            int imageCount = images.size()
            int[] depths = [-1]*imageCount + [8]*imageCount + [4]*imageCount
            ICOEncoder.write(icons, depths, destination)
        }
    }

    protected void processLinuxIcons(File destination) {
        File icon16, icon32
        for (IconInfo ii : icons) {
            if ('shortcut' == ii.kind) {

                File file = project.file(ii.href)
                if (!file.exists()) {
                    // try to resolve relative to output
                    file = new File(getResourcesDir(), ii.href)
                }
                if (!file.isFile()) {
                    logger.error("Icon $ii.href for $ii.kind rejected from Linux bundling because $ii.href does not exist or it is a directory.")
                    continue;
                }
                if (ii.scale != 1) {
                    logger.info("Icon $ii.href for $ii.kind rejected from Linux bundling because it has a scale other than '1'")
                    continue;
                }

                Image icon = Toolkit.defaultToolkit.getImage(file.toURI().toURL())

                if (icon.width == 32 && icon.height == 32) {
                    icon32 = file
                } else if (icon.width == 16 && icon.height == 16) {
                    icon16 = file
                } else {
                    logger.info("Icon $ii.href for $ii.kind rejected from Linux bundling because it is ${icon.width}x${icon.height} and only 16x16 and 32x32 icons are used")
                }
            }
        }

        File icon = icon32 ?: icon16
        if (icon) {
             ant.copy(file: icon, toFile: new File(destination, "linux/${project.javafx.appName.replaceAll('\\s', '')}.png"))
        }

    }
}

class IconInfo {
    String href
    String kind = 'default'
    int width = -1
    int height = -1
    int depth = -1
    double scale = 1 // for retina
    RunMode mode = RunMode.ALL


    public IconInfo(Closure configure) {
        ConfigureUtil.configure(configure, this)
    }
}
