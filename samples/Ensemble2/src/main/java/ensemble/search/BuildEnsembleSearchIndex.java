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
package ensemble.search;

import ensemble.DocsHelper;
import ensemble.model.SampleInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate the lucene index that Ensemble uses for its search
 */
public class BuildEnsembleSearchIndex {

    public static void main(String[] args) throws Exception{
        File samplesFilesDir = new File("ensemble/");
        File indexDir = new File("ensemble/search/index");
        File docDir = new File("../../../artifacts/sdk/docs/api");
        File samplesDir = new File("ensemble/samples");
        // create index
        ///System.out.println("Indexing to directory '" + indexDir + "'...");
        long start = System.currentTimeMillis();
        Directory dir = FSDirectory.open(indexDir);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
        iwc.setOpenMode(OpenMode.CREATE);
        // generate and write index of all java doc and samples
        IndexWriter writer = new IndexWriter(dir, iwc);

        List<String> samplesFileList = new ArrayList<String>();

        indexSamples(writer, samplesDir, samplesFileList);
        try {
            indexJavaDocAllClasses(writer, docDir);
        } catch (Exception e) {
            System.out.println("\nWarning: We were not able to locate the JavaFX API documentation for your build environment.\n"
                    + "Ensemble search will not include the API documentation.\n"); 
        }
        writer.close();
        // create a listAll.txt file that is used
        FileWriter listAllOut = new FileWriter(new File(indexDir,"listAll.txt"));
        for (String fileName: dir.listAll()) {
            if (!"listAll.txt".equals(fileName)) { // don't include the "listAll.txt" file
                Long length = dir.fileLength(fileName);
                listAllOut.write(fileName);
                listAllOut.write(':');
                listAllOut.write(length.toString());
                listAllOut.write('\n');
            }
        }
        listAllOut.flush();
        listAllOut.close();

        FileWriter sampleFilesCache = new FileWriter(new File(samplesFilesDir,"samplesAll.txt"));
        for (String oneSample: samplesFileList) {
                sampleFilesCache.write(oneSample);
                sampleFilesCache.write('\n');
        }
        sampleFilesCache.flush();
        sampleFilesCache.close();

        // print time taken
        ///System.out.println(System.currentTimeMillis() - start + " total milliseconds");
    }


    private static void indexJavaDocAllClasses(IndexWriter writer, File javaDocRoot)  throws Exception {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(javaDocRoot,"allclasses-noframe.html")));
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append('\n');
        }
        reader.close();
        // parse package
        Matcher matcher = findClassUrl.matcher(builder);
        while (matcher.find()) {
            String classUrl = matcher.group(1);
            ///System.out.println("\n\nclassUrl = " + classUrl);
            indexDocs(writer, new File(javaDocRoot, classUrl));
        }

        /*
SampleInfo{
    sourceFileUrl='file:/Volumes/Store/Projects/presidio/jfx/apps/internal/Ensemble/src/ensemble/samples/animation/timelines/InterpolatorSample.java',
    packageName='ensemble.samples.animation.timelines',
    className='ensemble.samples.animation.timelines.InterpolatorSample',
    description='This sample demostrates the interpolator property of the KeyValues: from default linear interpolation of the values between the KeyFrames to powerful Spline interpolator. There are 5 circles with adjustable opacity in the demo. Each circle moves with different interpolator, the first visibles are ones with default(linear) and easy_both interpolators. ',
    apiClasspaths=[javafx.animation.Interpolator, javafx.animation.KeyFrame, javafx.animation.KeyValue, javafx.animation.Timeline, javafx.util.Duration],
    relatesSamplePaths=[],
    resourceUrls=[]
}
         */
    }

    /**
     * Index all the sample java files in a directory or a single sample file
     *
     * @param writer
     * @param file
     * @throws IOException
     */
    private static void indexSamples(IndexWriter writer, File file, List<String> samplesFilesList) throws IOException {
        ///System.out.println("BuildEnsembleSearchIndex.indexSamples("+file+")");
        ///System.out.println("file.exists() = " + file.exists());
        ///System.out.println("file.isDirectory() = " + file.isDirectory());
        if (file.isDirectory()) {
          //  String savedDirName = file.getParent() + file.getName() + "/";
          //  System.out.println("indexSamples: savedDirName = " + savedDirName);
            String[] files = file.list();
            if (files != null) {
                for (String fileName : files) {
                    indexSamples(writer, new File(file, fileName), samplesFilesList);
                }
            }
        } else if (file.getName().toLowerCase().endsWith(".java")) {
            // read file contents into a string
            StringBuilder contentBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    contentBuilder.append(line);
                    contentBuilder.append('\n');
                }
            } finally {
                br.close();
            }
            final String fileContent = contentBuilder.toString();
            String sourceFileUrl = file.toURI().toString();          
            String unqualifiedClassName = sourceFileUrl.substring(sourceFileUrl.lastIndexOf('/')+1, sourceFileUrl.length()-5);
            SampleInfo sampleInfo = new SampleInfo(sourceFileUrl, unqualifiedClassName, fileContent);

           // System.out.println("sampleInfo = " + sampleInfo);
            //Samples file for building pages later
            samplesFilesList.add(sourceFileUrl);

            // write class entry to index
            addDocument(writer, DocumentType.SAMPLE,
                new Field("name", sampleInfo.getName(), Field.Store.YES, Field.Index.ANALYZED),
                new Field("description", sampleInfo.getDescription(), Field.Store.NO, Field.Index.ANALYZED),
                new Field("shortDescription", sampleInfo.getDescription().substring(0, Math.min(160, sampleInfo.getDescription().length())),
                        Field.Store.YES, Field.Index.NOT_ANALYZED),
                new Field("ensemblePath", sampleInfo.getEnsemblePath(), Field.Store.YES, Field.Index.NOT_ANALYZED)
            );
        }
    }

    /**
     * Index a JavaDoc page for a single class, interface or enum
     *
     * @param writer The index writer to add documents to
     * @param file The javadoc html file
     * @throws IOException If there was a problem indexing the file
     */
    private static void indexDocs(IndexWriter writer, File file) throws IOException {
        // read file contents into a string
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append('\n');
            }
        } finally {
            br.close();
        }
        final String content = contentBuilder.toString();
        // get file url
        String fileUrl = convertUrlToOracleDotCom(file.toURI().toString());
        // get ensemble path
        final String ensembleUrl = DocsHelper.getPagePath(fileUrl, "http://download.oracle.com/javafx/2.0/api/");
        // extract package and class
        Matcher packageAndClassMatcher = PACKAGE_AND_CLASS.matcher(content);
        // search and if we fail to find ignore this file
        if (!packageAndClassMatcher.find()) {
            ///System.out.println("!!!! Ignoring [" + file + "] because no class or package was found");
            return;
        } else {
            ///System.out.println("Adding [" + file + "]");
        }
        ///System.out.println("        fileUrl = " + fileUrl);
        String packageName = packageAndClassMatcher.group(1);
        ///System.out.println("        packageName = " + packageName);
        String classType = packageAndClassMatcher.group(2).toLowerCase();
        ///System.out.println("        classType = " + classType);
        String className = packageAndClassMatcher.group(3);
        ///System.out.println("        className = " + className);
        // extract document type
        DocumentType documentType = DocumentType.CLASS;
        if ("enum".equals(classType)) {
            documentType = DocumentType.ENUM;
        }
        // extract javadoc description
        Matcher classDescriptionMatcher = CLASS_DESCRIPTION.matcher(content);
        String classDescription = "";
        if (classDescriptionMatcher.find()) {
            classDescription = cleanHTML(classDescriptionMatcher.group(1));
        }
        ///System.out.println("classDescription = " + classDescription);
        // write class entry to index
        addDocument(writer, documentType,
                new Field("name", className, Field.Store.YES, Field.Index.ANALYZED),
                new Field("description", classDescription, Field.Store.NO, Field.Index.ANALYZED),
                new Field("shortDescription", classDescription.substring(0,Math.min(160,classDescription.length())),
                        Field.Store.YES, Field.Index.NOT_ANALYZED),
                new Field("package", packageName, Field.Store.YES, Field.Index.ANALYZED),
                new Field("url", fileUrl, Field.Store.YES, Field.Index.NOT_ANALYZED),
                new Field("ensemblePath", ensembleUrl, Field.Store.YES, Field.Index.NOT_ANALYZED)
        );

        // extract properties
        Matcher propertySummaryMatcher = PROPERTY_SUMMARY.matcher(content);
        if (propertySummaryMatcher.find()) {
            String propertySummaryTable = propertySummaryMatcher.group(1);
            Matcher propertyMatcher = PROPERTY.matcher(propertySummaryTable);
            while (propertyMatcher.find()) {
                String url = propertyMatcher.group(1);
                String propertyName = propertyMatcher.group(2);
                String description = cleanHTML(propertyMatcher.group(3));
                ///System.out.println("            propertyName = " + propertyName);
                ///System.out.println("                    description = " + description);
                ///System.out.println("                    url = " + url);
                url = fileUrl + "#" + propertyName;
                ///System.out.println("                    oracle url = " + url);
                // write class entry to index
                addDocument(writer, DocumentType.PROPERTY,
                        new Field("name", propertyName, Field.Store.YES, Field.Index.ANALYZED),
                        new Field("description", description, Field.Store.NO, Field.Index.ANALYZED),
                        new Field("shortDescription", description.substring(0,Math.min(160,description.length())),
                                Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("className", className, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("package", packageName, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("ensemblePath", ensembleUrl + "#" + propertyName, Field.Store.YES, Field.Index.NOT_ANALYZED)
                );
            }
        }
        // extract methods
        Matcher methodSummaryMatcher = METHOD_SUMMARY.matcher(content);
        if (methodSummaryMatcher.find()) {
            String methodSummaryTable = methodSummaryMatcher.group(1);
            Matcher methodMatcher = PROPERTY.matcher(methodSummaryTable);
            while (methodMatcher.find()) {
                String url = methodMatcher.group(1);
                String methodName = methodMatcher.group(2);
                String description = cleanHTML(methodMatcher.group(3));
                ///System.out.println("            methodName = " + methodName);
                ///System.out.println("                    description = " + description);
                ///System.out.println("                    url = " + url);
                url = fileUrl + "#" + methodName+"()";
                ///System.out.println("                    oracle url = " + url);
                // write class entry to index
                addDocument(writer, DocumentType.METHOD,
                        new Field("name", methodName, Field.Store.YES, Field.Index.ANALYZED),
                        new Field("description", description, Field.Store.NO, Field.Index.ANALYZED),
                        new Field("shortDescription", description.substring(0,Math.min(160,description.length())),
                                Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("className", className, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("package", packageName, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("ensemblePath", ensembleUrl + "#" + methodName + "()", Field.Store.YES, Field.Index.NOT_ANALYZED)
                );
            }
        }
        // extract fields
        Matcher fieldSummaryMatcher = FIELD_SUMMARY.matcher(content);
        if (fieldSummaryMatcher.find()) {
            String fieldSummaryTable = fieldSummaryMatcher.group(1);
            Matcher fieldMatcher = PROPERTY.matcher(fieldSummaryTable);
            while (fieldMatcher.find()) {
                String url = fieldMatcher.group(1);
                String fieldName = fieldMatcher.group(2);
                String description = cleanHTML(fieldMatcher.group(3));
                ///System.out.println("            fieldName = " + fieldName);
                ///System.out.println("                    description = " + description);
                ///System.out.println("                    url = " + url);
                url = fileUrl + "#" + fieldName;
                ///System.out.println("                    oracle url = " + url);
                // write class entry to index
                addDocument(writer, DocumentType.FIELD,
                        new Field("name", fieldName, Field.Store.YES, Field.Index.ANALYZED),
                        new Field("description", description, Field.Store.NO, Field.Index.ANALYZED),
                        new Field("shortDescription", description.substring(0,Math.min(160,description.length())),
                                Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("className", className, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("package", packageName, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("ensemblePath", ensembleUrl + "#" + fieldName, Field.Store.YES, Field.Index.NOT_ANALYZED)
                );
            }
        }
        // extract enums
        Matcher enumSummaryMatcher = ENUM_SUMMARY.matcher(content);
        if (enumSummaryMatcher.find()) {
            String enumSummaryTable = enumSummaryMatcher.group(1);
            Matcher enumMatcher = PROPERTY.matcher(enumSummaryTable);
            while (enumMatcher.find()) {
                String url = enumMatcher.group(1);
                String enumName = enumMatcher.group(2);
                String description = cleanHTML(enumMatcher.group(3));
                ///System.out.println("            enumName = " + enumName);
                ///System.out.println("                    description = " + description);
                ///System.out.println("                    url = " + url);
                url = fileUrl + "#" + enumName;
                ///System.out.println("                    oracle url = " + url);
                // write class entry to index
                addDocument(writer, DocumentType.ENUM,
                        new Field("name", enumName, Field.Store.YES, Field.Index.ANALYZED),
                        new Field("description", description, Field.Store.NO, Field.Index.ANALYZED),
                        new Field("shortDescription", description.substring(0,Math.min(160,description.length())),
                                Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("url", url, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("className", className, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("package", packageName, Field.Store.YES, Field.Index.NOT_ANALYZED),
                        new Field("ensemblePath", ensembleUrl+ "#" + enumName, Field.Store.YES, Field.Index.NOT_ANALYZED)
                );
            }
        }
    }

    /**
     * Create a new document and write it to the given writer
     *
     * @param writer       The writer to write out to
     * @param documentType The document type to save in the doc
     * @param fields       The searchable and data fields to write into doc
     * @throws IOException If there was problem writing doc
     */
    private static void addDocument(IndexWriter writer, DocumentType documentType, Field... fields) throws IOException {
        // make a new, empty document
        Document doc = new Document();
        // add doc type field
        doc.add(new Field("documentType", documentType.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        // add other fields
        if (fields != null) {
            for (Field field : fields) doc.add(field);
        }
        // write into index, assuming we are recreating every time
        writer.addDocument(doc);
    }

    /**
     * Clean HTML, removing all tags and un-escaping so that we can index it cleanly
     *
     * @param html The html to clean
     * @return cleaned html
     */
    private static String cleanHTML(String html) {
        html = html.replaceAll("(&nbsp;|\\s|[ ])+", " ").trim(); // cleanup whitespace
        html = html.replaceAll("<.*?>", " "); // remove html tags
        html = html.replaceAll("&lt;", "<"); // un-escape <
        html = html.replaceAll("&gt;", ">"); // un-escape >
        html = html.replaceAll("&quot;", "\""); // un-escape "
        html = html.replaceAll("&apos;", "\'"); // un-escape '
        html = html.replaceAll("&amp;", "&"); // un-escape &
        return html;
    }

    /**
     * Convert local and relative docs urls into absolute urls on oracle.com server
     *
     * @param url The local or relative doc url
     * @return Absolute oracle.com url for given doc
     */
    private static String convertUrlToOracleDotCom(String url) {
        //http://download.oracle.com/javafx/2.0/api/index.html
        //http://download.oracle.com/javafx/2.0/api/javafx/scene/transform/Affine.html
        if (url.startsWith("../")) {
            // from - ../../../javafx/scene/shape/CubicCurveTo.html#xProperty
            // to   - http://download.oracle.com/javafx/2.0/api/javafx/scene/shape/CubicCurveTo.html#xProperty
            return  url.replaceAll(".*?/javafx/", "http://download.oracle.com/javafx/2.0/api/javafx/");
        } else {
            // from - file:///Users/jp202575/Projects/presidio/jfx/artifacts/sdk/docs/api/javafx/scene/shape/Ellipse.html
            // to   - http://download.oracle.com/javafx/2.0/api/javafx/scene/shape/Ellipse.html
            return url.replaceAll(".*?/sdk/docs/api/", "http://download.oracle.com/javafx/2.0/api/");
        }
    }

    /*
    Pull class urls from all classes page
     */
    private static final Pattern findClassUrl = Pattern.compile("A\\s+HREF=\\\"([^\\\"]+)\\\"");
    /*
    <H2>
    <FONT SIZE="-1">
    javafx.scene</FONT>
    <BR>
    Class Node</H2>

    GROUP 1 = Package
    GROUP 2 = Class Type
    GROUP 3 = Class
     */
    private static Pattern PACKAGE_AND_CLASS = Pattern.compile("<H2>\\s*<FONT SIZE=\"-1\">\\s*([^<]+)</FONT>\\s*<BR>\\s*(Class|Interface|Enum) ([^<&]+).*?</H2>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    </PRE>
    <P>
    ...
    <HR>

    GROUP 1 = Class JavaDoc Description
     */
    private static Pattern CLASS_DESCRIPTION = Pattern.compile("</PRE>\\s*<P>(.*?)<HR>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    <A NAME="property_summary"><!-- --></A>
    <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
    ....
    </TABLE>

    GROUP 1 = Property Summary Table
     */
    private static Pattern PROPERTY_SUMMARY = Pattern.compile("NAME=\"property_summary\">.*?<TABLE[^>]+>(.*?)</TABLE>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    <A NAME="method_summary"><!-- --></A>
    <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
    ....
    </TABLE>

    GROUP 1 = Method Summary Table
     */
    private static Pattern METHOD_SUMMARY = Pattern.compile("NAME=\"method_summary\">.*?<TABLE[^>]+>(.*?)</TABLE>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    <A NAME="enum_constant_summary"><!-- --></A>
    <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
    ....
    </TABLE>

    GROUP 1 = Enum Summary Table
     */
    private static Pattern ENUM_SUMMARY = Pattern.compile("NAME=\"enum_constant_summary\">.*?<TABLE[^>]+>(.*?)</TABLE>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    <A NAME="field_summary"><!-- --></A>
    <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
    ....
    </TABLE>

    GROUP 1 = Field Summary Table
     */
    private static Pattern FIELD_SUMMARY = Pattern.compile("NAME=\"field_summary\">.*?<TABLE[^>]+>(.*?)</TABLE>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    /*
    <TD><CODE><B><A HREF="../../javafx/scene/Node.html#boundsInLocalProperty">boundsInLocal</A></B></CODE>
    <BR>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The rectangular bounds of this <code>Node</code> in the node's
     untransformed local coordinate space.</TD>

    GROUP 1 = Url
    GROUP 2 = Name
    GROUP 2 = Description
     */
    private static Pattern PROPERTY = Pattern.compile("<TD>.*?<A HREF=\"([^\"]*)\">([^<]*)</A>.*?<BR>(.*?)</TD>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
}
