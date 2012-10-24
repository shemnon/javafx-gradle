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
package ensemble.model;

import ensemble.Pages;
import ensemble.SampleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains all the information we need about a Ensemble sample
 */
public class SampleInfo {
    private static final Pattern findPackage = Pattern.compile("[ \\t]*package[ \\t]*([^;]*);\\s*");
    private static final Pattern findJavaDocComment = Pattern.compile("\\/\\*\\*(.*?)\\*\\/\\s*",Pattern.DOTALL);
    private final String name;
    private final String sourceFileUrl;
    private final String ensemblePath;
    private final String packageName;
    private final String className;
    private final String description;
    private final String[] apiClasspaths;
    private final String[] relatesSamplePaths;
    private final String[] resourceUrls;

    public SampleInfo(String sourceFileUrl, String unqualifiedClassName, String fileContent) {
        this.sourceFileUrl = sourceFileUrl;
        this.name = SampleHelper.formatName(unqualifiedClassName);
        // extract package
        Matcher matcher = findPackage.matcher(fileContent);
        if (!matcher.find()) throw new IllegalArgumentException("Failed to find package statement in sample file ["+sourceFileUrl+"]");
        packageName = matcher.group(1);
        className = packageName+"."+unqualifiedClassName;
        // build ensemble path
        String[] parts = packageName.substring("ensemble.samples.".length()).split("\\.");
        String path = "";
        for(String part:parts) {
            path = path+'/'+ SampleHelper.formatName(part);
        }
        ensemblePath = Pages.SAMPLES+path+"/"+name;
        // parse the src file comment
        matcher = findJavaDocComment.matcher(fileContent);
        if (!matcher.find()) throw new IllegalArgumentException("Failed to find java doc comment in sample file ["+sourceFileUrl+"]");
        String javaDocComment = matcher.group(1);
//            System.out.println("javaDocComment = " + javaDocComment);
        String[] lines = javaDocComment.split("\\n");
//            for (String jdocline:lines) {
//                System.out.println("jdocline = " + jdocline);
//            }
        String[] lines2 = javaDocComment.split("([ \\t]*\\n[ \\t]*\\*[ \\t]*)+");
        StringBuilder descBuilder = new StringBuilder();
        List<String> relatedList = new ArrayList<String>();
        List<String> seeList = new ArrayList<String>();
        List<String> resourceList = new ArrayList<String>();
        for (String jdocline:lines2) {
//                System.out.println("jdocline2 = " + jdocline);
            String trimedLine = jdocline.trim();
            if (trimedLine.length()!= 0) {
                if(trimedLine.startsWith("@related")) {
                    relatedList.add(trimedLine.substring(8).trim());
                } else if(trimedLine.startsWith("@see")) {
                    seeList.add(trimedLine.substring(4).trim());
                } else if(trimedLine.startsWith("@resource")) {
                    // todo resolve to a URL
                    resourceList.add(trimedLine.substring(9).trim());
                } else {
                    descBuilder.append(trimedLine);
                    descBuilder.append(' ');
                }
            }
        }
        description = descBuilder.toString();
        relatesSamplePaths = relatedList.toArray(new String[relatedList.size()]);
        apiClasspaths = seeList.toArray(new String[seeList.size()]);
        resourceUrls = resourceList.toArray(new String[resourceList.size()]);
    }

    public String getName() {
        return name;
    }

    public String getSourceFileUrl() {
        return sourceFileUrl;
    }

    public String getEnsemblePath() {
        return ensemblePath;
    }

    public String getClassName() {
        return className;
    }

    public String getDescription() {
        return description;
    }

    public String[] getApiClasspaths() {
        return apiClasspaths;
    }

    public String[] getRelatesSamplePaths() {
        return relatesSamplePaths;
    }

    public String[] getResourceUrls() {
        return resourceUrls;
    }

    @Override public String toString() {
        return "SampleInfo{" +
                "\n     sourceFileUrl='" + sourceFileUrl + '\'' +
                "\n     name='" + name + '\'' +
                "\n     packageName='" + packageName + '\'' +
                "\n     ensemblePath='" + ensemblePath + '\'' +
                "\n     className='" + className + '\'' +
                "\n     description='" + description + '\'' +
                "\n     apiClasspaths=" + (apiClasspaths == null ? null : Arrays.asList(apiClasspaths)) +
                "\n     relatesSamplePaths=" + (relatesSamplePaths == null ? null : Arrays.asList(relatesSamplePaths)) +
                "\n     resourceUrls=" + (resourceUrls == null ? null : Arrays.asList(resourceUrls)) +
                "\n}";
    }
}
