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

import ensemble.model.SampleInfo;
import ensemble.pages.CategoryPage;
import ensemble.pages.DocPage;
import ensemble.pages.SamplePage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;

/**
 * DocsHelper - bunch of static utility methods to help with java docs
 */
public class DocsHelper {
    private static final Pattern findClassUrl = Pattern.compile("(?i)A\\s+HREF=\\\"([^\\\"]+)\\\"");
    /**
     * Create a ensemble page path for a given java doc URL
     * 
     * @param url The URL to java doc page
     * @param docsDirUrl The URL to documents directory
     * @return Ensemble page path for that page
     */
    public static String getPagePath(String url, String docsDirUrl) {
        docsDirUrl = docsDirUrl.replaceAll("/+","/");
        String cleanUrl = url.replaceAll("/+","/");
        String relativePath = Pages.API_DOCS + '/' + cleanUrl.substring(docsDirUrl.length(),cleanUrl.length());
        relativePath = relativePath.replaceAll("\\.html","");
        if (relativePath.endsWith("/package-summary")) {
            relativePath = relativePath.substring(0,relativePath.length()-"/package-summary".length());
        }
        return relativePath;
    }
    
    public static void extractDocsPagesFromAllClassesPage(CategoryPage rootPage, String pageContent, String docsRootDir) {
        // remove any old docs pages
        rootPage.getChildren().clear();
        // add new
        Map<String, DocPage> packagePageMap = new HashMap<String, DocPage>();
        Matcher matcher = findClassUrl.matcher(pageContent);
        while (matcher.find()) {
            String classUrl = matcher.group(1);
            DocPage parent = createPackagePage(classUrl.substring(0, classUrl.lastIndexOf('/')), rootPage, packagePageMap, docsRootDir);
            String className = classUrl.substring(classUrl.lastIndexOf('/') + 1, classUrl.lastIndexOf('.'));
            DocPage docPage = new DocPage(className, docsRootDir + classUrl);
            parent.getChildren().add(docPage);
        }
    }

   private static DocPage createPackagePage(String path, CategoryPage rootPage,Map<String,DocPage> packagePageMap, String docsRootDir) {
        DocPage packagePage = packagePageMap.get(path);
        if (packagePage == null) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash == -1) {
                // found root package
                packagePage = new DocPage(path,docsRootDir+path+"/package-summary.html");
                rootPage.getChildren().add(packagePage);
            } else {
                DocPage parent = createPackagePage(path.substring(0,lastSlash),rootPage, packagePageMap, docsRootDir);
                packagePage = new DocPage(path.substring(lastSlash+1,path.length()),docsRootDir+path+"/package-summary.html");
                parent.getChildren().add(packagePage);
            }
            packagePageMap.put(path,packagePage);
        }
        return packagePage;
    }
   
    // Adds related sample links to doc pages
    public static void syncDocPagesAndSamplePages(CategoryPage samplesPage) {
        ObservableList sPages = samplesPage.getChildren();
        Ensemble2 ensemble2 = Ensemble2.getEnsemble2();
        for (Object oneSamplePage : sPages) {
            if (oneSamplePage instanceof SamplePage) {
                SampleInfo sampleInfo = ((SamplePage) oneSamplePage).getSampleInfo();
                for (String apiClassPath : sampleInfo.getApiClasspaths()) {
                    String path = Pages.API_DOCS + '/' + apiClassPath.replace('.', '/');
                    DocPage docPage = (DocPage) ensemble2.getPages().getPage(path);
                    if (docPage != null) {
                        docPage.getRelatedSamples().add((SamplePage) oneSamplePage);
                    }
                }
            } else {
                syncDocPagesAndSamplePages((CategoryPage) oneSamplePage);
            }
        }
    }
}
