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

import ensemble.pages.CategoryPage;
import ensemble.pages.SamplePage;
import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;

/**
 * SampleHelper
 *
 */
public class SampleHelper {
    private static final String SAMPLES_PACKAGE_ROOT = "ensemble/samples/";

    public static void getSamples(CategoryPage rootPage) {
        try {
            // Get the URL for this class, if it is a jar URL, then get the
            // filename associated with it.
            URL classUrl = SampleHelper.class.getResource("SampleHelper.class");
            String classUrlString = classUrl.toString();
            // check if its a file or jar, handle each
            if (classUrlString.startsWith("file:")) {
                File classFile = new File(classUrl.toURI());
                File ensemble2Dir = classFile.getParentFile();
                File samplesDir = new File(ensemble2Dir,"samples");
          //      findAllSamples(samplesDir,rootPage);
                findAllSamples(rootPage);
            } else if (classUrlString.startsWith("jar:") && classUrlString.indexOf("!") != -1) {
                // Strip out the "jar:" and everything after and including the "!"
                String jarFileUrl = classUrlString.substring(4, classUrlString.lastIndexOf("!"));
                findAllSamplesInJar(jarFileUrl,rootPage);
            }  else {
                throw new UnsatisfiedLinkError("Invalid URL for class: " + classUrlString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void findAllSamples(String jarFileUrl,CategoryPage dirPage) throws IOException, URISyntaxException {
        // create map for CategoryPages
        Map<String,CategoryPage> categoryPageMap = new HashMap<String, CategoryPage>();
        // walk jar entries
        JarFile jarFile = new JarFile(new File(new URI(jarFileUrl)));
        Enumeration<JarEntry> entrysEnum = jarFile.entries();
        while (entrysEnum.hasMoreElements()) {
            JarEntry entry = entrysEnum.nextElement();
            String name = entry.getName();
            if (name.startsWith("ensemble/samples/") &&
                    name.endsWith(".java")) {
                String url = "jar:"+jarFileUrl+"!/"+name;
                // create sample page
                String fileName = name.substring(name.lastIndexOf('/')+1,name.length()-5);
                SamplePage samplePage = new SamplePage(formatName(fileName),url);
                // add to parent category
                String parentPath = name.substring(SAMPLES_PACKAGE_ROOT.length(),name.lastIndexOf('/'));
                CategoryPage parentCategoryPage = getCategoryPageForPath(parentPath, dirPage, categoryPageMap);
                parentCategoryPage.getChildren().add(samplePage);
            }
        }
    }

    private static void findAllSamplesInJar(String jarFileUrl,CategoryPage dirPage) throws IOException, URISyntaxException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(SampleHelper.class.getResourceAsStream("samplesAll.txt")));
        String line;
        List<String> sampleUrls = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {
                sampleUrls.add(line);
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // create map for CategoryPages
        Map<String,CategoryPage> categoryPageMap = new HashMap<String, CategoryPage>();
        for (String oneSampleUrl:sampleUrls) {
            String name = oneSampleUrl.substring(oneSampleUrl.indexOf("ensemble/samples/"), oneSampleUrl.length());
            if (name.startsWith("ensemble/samples/") && name.endsWith(".java")) {
                String url = "jar:"+jarFileUrl.replace(" ", "%20")+"!/"+name; 
                // System.out.println("findAllSamplesInJar: url = " + url);
                // create sample page
                String fileName = name.substring(name.lastIndexOf('/')+1,name.length()-5);
                SamplePage samplePage = new SamplePage(formatName(fileName),url);
                // add to parent category
                String parentPath = name.substring(SAMPLES_PACKAGE_ROOT.length(),name.lastIndexOf('/'));
                CategoryPage parentCategoryPage = getCategoryPageForPath(parentPath, dirPage, categoryPageMap);
                parentCategoryPage.getChildren().add(samplePage);
            }
        }
    }

    private static CategoryPage getCategoryPageForPath(String path,CategoryPage dirPage,
                                                           Map<String,CategoryPage> categoryPageMap) {
        CategoryPage categoryPage = categoryPageMap.get(path);
        if (categoryPage == null) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash == -1) {
                // found root level category so create
                categoryPage = new CategoryPage(formatName(path));
                dirPage.getChildren().add(categoryPage);
            } else {
                // get parent
                CategoryPage parentCategoryPage = getCategoryPageForPath(path.substring(0,lastSlash),dirPage, categoryPageMap);
                // create new sub-category
                categoryPage = new CategoryPage(formatName(path.substring(lastSlash+1,path.length())));
                parentCategoryPage.getChildren().add(categoryPage);
            }
            categoryPageMap.put(path,categoryPage);
        }
        return categoryPage;
    }

    private static void findAllSamples(CategoryPage dirPage) {
        //getResourceAsStream for file
        BufferedReader reader = new BufferedReader(new InputStreamReader(SampleHelper.class.getResourceAsStream("samplesAll.txt")));
        String line;
        List<String> sampleUrls = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {
                sampleUrls.add(line);
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Map<String, CategoryPage> categoryPageMap = new HashMap<String, CategoryPage>();
        for (String oneSampleUrl : sampleUrls) {
            // create sample page
            String fileName = oneSampleUrl.substring(oneSampleUrl.lastIndexOf('/') + 1, oneSampleUrl.length() - 5);
            SamplePage samplePage = new SamplePage(formatName(fileName), oneSampleUrl);
            // add to parent category;
            String[] pathParts = oneSampleUrl.split("ensemble/samples/");
            //  String parentPath = oneSampleUrl.substring(SAMPLES_PACKAGE_ROOT.length(),oneSampleUrl.lastIndexOf('/'));
            String parentPath = pathParts[1].substring(0, pathParts[1].lastIndexOf('/'));
            CategoryPage parentCategoryPage = getCategoryPageForPath(parentPath, dirPage, categoryPageMap);
            parentCategoryPage.getChildren().add(samplePage);
        }
    }

    private static void findAllSamples(File dir,CategoryPage dirPage) {
        for (File child:dir.listFiles()) {
            String name = child.getName();
            if (child.isDirectory()) {
                CategoryPage cp = new CategoryPage(formatName(name));
                dirPage.getChildren().add(cp);
                findAllSamples(child, cp);
            } else if(child.getName().endsWith(".java")) {
                // handle sample src
                String sampleName = name.substring(0,name.lastIndexOf('.'));
                SamplePage sp = new SamplePage(formatName(sampleName),child.toURI().toString());
                dirPage.getChildren().add(sp);
            }
        }
    }

    public static String formatName(String packageName) {
        // remove ending "Sample" from name
        if(packageName.endsWith("Sample")) packageName = packageName.substring(0,packageName.length()-"Sample".length());
        // add spaces
        packageName = packageName.replaceAll("([\\p{Upper}\\d])"," $1");
        // uppercase first char
        packageName = packageName.substring(0,1).toUpperCase() + packageName.substring(1);
        return packageName.trim();
    }
}
