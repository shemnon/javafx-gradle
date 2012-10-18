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
package ensemble.sampleproject;

import ensemble.util.Utils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.scene.Node;

/**
 * Helper class to build a Netbeans project for a sample and open it in Netbeans
 */
public class SampleProjectBuilder {
    private static final Pattern findPackage = Pattern.compile("[ \\t]*package[ \\t]*([^;]*);\\s*");
    private static final Pattern findMultilineComment = Pattern.compile("\\/\\*(.*?)\\*\\/\\s*",Pattern.DOTALL);
    private static final Pattern findRemoveMeBlock = Pattern.compile("\\s+//\\s+REMOVE ME.*?END REMOVE ME",Pattern.DOTALL);
    private static final Pattern findEnsembleImport = Pattern.compile("\\nimport ensemble.*?;");
    private static final Pattern findSuperCall = Pattern.compile("super\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)\\s*;");
    private static final Pattern findPlayMethod = Pattern.compile("public\\s+void\\s+play\\s*\\(\\)\\s*\\{");
    private static final Pattern findCreate3DContentMethod = Pattern.compile("public\\s+Node\\s+create3dContent\\s*\\(\\)\\s*\\{");

    public static String getClassName(String urlToSampleJavaFile) {
        return urlToSampleJavaFile.substring(
                urlToSampleJavaFile.lastIndexOf('/')+1,
                urlToSampleJavaFile.lastIndexOf('.'));
    }

    /**
     * Convert source from the form we write in Ensemble to a standalone runnable application
     *
     * @param urlToSampleJavaFile Url to the sample java source file
     * @return The converted source code as a string
     */
    public static String loadAndConvertSampleCode(String urlToSampleJavaFile) {
        // load source file
        String source = Utils.loadFile(urlToSampleJavaFile);
        // check if 3d sample
        final boolean is3D = source.contains("Sample3D");
        // remove comments and package statement
        source = findMultilineComment.matcher(source).replaceFirst("");
        source = findEnsembleImport.matcher(source).replaceAll("");
        source = findPackage.matcher(source).replaceFirst("");
        // remove REMOVE ME ... END REMOVE ME blocks
        source = findRemoveMeBlock.matcher(source).replaceAll("");
        // add imports
        source = "import javafx.application.Application;\n" +
                 "import javafx.scene.Group;\n" +
                 "import javafx.scene.Scene;\n" +
                 "import javafx.stage.Stage;\n" + source;
        if (is3D) {
            source = "import javafx.scene.transform.Rotate;\n" +
                    "import javafx.scene.PerspectiveCamera;\n" +
                    "import javafx.scene.transform.Translate;\n" + source;
        }
        // add 2 lines of copyright
        source = "/**\n" +
                 " * Copyright (c) 2008, 2012 Oracle and/or its affiliates.\n" +
                 " * All rights reserved. Use is subject to license terms.\n" +
                 " */\n" + source;
        // make extends Application
        source = source.replaceAll("extends Sample(3D)?", "extends Application");
        // change constructor to init method
        String className = getClassName(urlToSampleJavaFile);
        source = source.replaceAll("public "+className+"\\(\\) \\{",
                "private void init(Stage primaryStage) {");
        // make getChildren() add to root
        int firstClass = source.indexOf(" class ");
        ///System.out.println("firstClass = " + firstClass);
        int secondClass = source.indexOf(" class ",firstClass+1);
        ///System.out.println("secondClass = " + secondClass);
        if (secondClass != -1) {
            source = source.substring(0,secondClass)
                    .replaceAll("(\\s+)getChildren\\(\\)\\.add", "$1root.getChildren().add")
                    .replaceAll("(\\s+)setOn", "$1root.setOn") +
                    source.substring(secondClass);
        } else {
            source = source
                    .replaceAll("(\\s+)getChildren\\(\\)\\.add", "$1root.getChildren().add")
                    .replaceAll("(\\s+)setOn", "$1root.setOn");
        }
        // add scene creation
        Matcher superCallMatcher = findSuperCall.matcher(source);
        if (superCallMatcher.find()) {
            final String width = superCallMatcher.group(1);
            final String height = superCallMatcher.group(2);
            final String extrasFor3D = is3D ? "\n" +
                    "        primaryStage.getScene().setCamera(new PerspectiveCamera());\n" +
                    "        root.getTransforms().addAll(\n" +
                    "            new Translate("+width+" / 2, "+height+" / 2),\n" +
                    "            new Rotate(180, Rotate.X_AXIS)\n" +
                    "        );\n" +
                    "        root.getChildren().add(create3dContent());"
                    : "";
            source = superCallMatcher.replaceFirst("Group root = new Group();\n" +
                    "        primaryStage.setResizable(false);\n"+
                    "        primaryStage.setScene(new Scene(root, "+
                    superCallMatcher.group(1)+","+superCallMatcher.group(2)+(is3D?",true":"")+"));"
                            +extrasFor3D);
            int lastCloseBrace = source.lastIndexOf('}');
            source = source.substring(0,lastCloseBrace) +
                    "\n    public double getSampleWidth() { return "+superCallMatcher.group(1)+"; }\n" +
                    "\n    public double getSampleHeight() { return "+superCallMatcher.group(2)+"; }\n" +
                    source.substring(lastCloseBrace);
        } else {
            source = source.replaceAll("init\\(Stage primaryStage\\) \\{","init(Stage primaryStage) {\n" +
                    "        Group root = new Group();\n" +
                    "        primaryStage.setScene(new Scene(root));");
        }
        // remove @override from playMethod
        boolean hasPlayMethod = findPlayMethod.matcher(source).find();
        if (hasPlayMethod) source = source.replaceAll("@Override public void play\\(\\)","public void play()");
        // remove @override from playMethod
        boolean hasContent3DMethod = findCreate3DContentMethod.matcher(source).find();
        if (hasContent3DMethod) source = source.replaceAll("@Override public Node create3dContent\\(\\)","public Node create3dContent()");
        // add main and start methods
        int lastCloseBrace = source.lastIndexOf('}');
        source = source.substring(0,lastCloseBrace) + "\n    @Override public void start(Stage primaryStage) throws Exception {\n" +
                "        init(primaryStage);\n" +
                "        primaryStage.show();\n" +
                (hasPlayMethod ? "        play();\n" : "") +
                "    }\n" +
                "    public static void main(String[] args) { launch(args); }\n" + source.substring(lastCloseBrace);
        // return the converted source
        return source;
    }


    public static void createSampleProject(File projectDir, String urlToSampleJavaFile, String[] resourceArray) {
        String nodeLoc = Node.class.getResource("Node.class").toExternalForm();
        String javafxrtPath = nodeLoc.substring(4,nodeLoc.indexOf('!'));
        try {
            File f = new File(new URI(javafxrtPath));
            javafxrtPath = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sampleClassName = getClassName(urlToSampleJavaFile);
//        System.out.println("sampleClassName = " + sampleClassName);
//        // get javafxrt.jar path
//        String javafxrtPath = "jfxrt.jar";
//        String classPath = System.getProperty("java.class.path");
//        for (String path: classPath.split(File.pathSeparator)) {
//            System.out.println("path = " + path);
//            if (path.endsWith("jfxrt.jar")) {
//                javafxrtPath = path;
//                break;
//            }
//        }
        String sep = System.getProperty("file.separator");
        if (sep.equals("\\")) {
            javafxrtPath = javafxrtPath.replaceAll("\\" + sep, "/");
        }
        ///System.out.println("        javafxrtPath = " + javafxrtPath);
        // extract project name
        String projectName = projectDir.toURI().toString();
        projectName = projectName.substring(projectName.lastIndexOf('/')+1);
        ///System.out.println("projectName = " + projectName);
        // create destDir
        projectDir.mkdirs();
        // unzip project template
        try {
            ZipInputStream zipinputstream = new ZipInputStream(
                    SampleProjectBuilder.class.getResourceAsStream("SampleProject.zip"));
            ZipEntry zipentry;
            while ((zipentry = zipinputstream.getNextEntry()) != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                File entryFile = new File(projectDir,entryName);
                if (zipentry.isDirectory()) {
                    entryFile.mkdirs();
                    ///System.out.println("        CREATED DIR -> " + entryFile);
                } else {
                    // assume all are text files, load text file into string so we can process it
                    StringBuilder sb = new StringBuilder();
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipinputstream));
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append('\n');
                    }
                    String contents = sb.toString();
                    // replace any place holders
                    contents = contents.replaceAll("ENSEMBLESAMPLE",projectName);
                    contents = contents.replaceAll("APPLICATIONCLASS",sampleClassName);
                    contents = contents.replaceAll("PATHTOJAVAFXRTJAR",javafxrtPath);
                    // save out file
                    FileWriter fileWriter = new FileWriter(entryFile);
                    fileWriter.write(contents);
                    fileWriter.flush();
                    fileWriter.close();
                    ///System.out.println("        WRITTEN FILE -> " + entryFile);
                }
                zipinputstream.closeEntry();
            }
            zipinputstream.close();
            //Put resources like images under src/
            File srcDestDir = new File(projectDir.getPath()+"/src/");
            loadSampleResourceUrls(srcDestDir,urlToSampleJavaFile, resourceArray);
            // save out source file
            File mainSrcFile = new File(projectDir,"src/"+sampleClassName+".java");
            FileWriter fileWriter = new FileWriter(mainSrcFile);
            fileWriter.write(loadAndConvertSampleCode(urlToSampleJavaFile));
            fileWriter.flush();
            fileWriter.close();
            ///System.out.println("        WRITTEN FILE -> src/"+sampleClassName+".java");
            // open project in netbeans
            loadProject(projectDir, mainSrcFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProject(File projectDir, File mainSrcFile) {
        ///System.out.println("Trying to load project in Netbeans...");
        NBInstallation[] installations = UserDirScanner.suitableNBInstallations(new File(System.getProperty("user.home")),"6.9.0",
                NBInstallation.LAST_USED_COMPARATOR);
        if (installations.length > 0) {
            NBInstallation installation = installations[0];
            String launcher = NBInstallation.getPlatformLauncher();
            ///System.out.println("launcher = " + launcher);
            String cmdArray[] = new String[]{
                    installation.getExecDir().getAbsolutePath() + File.separator + launcher,
                    "--open",
                    projectDir.getAbsolutePath(),
                    mainSrcFile.getAbsolutePath()
            };
            ///System.out.println("Command line: " + Arrays.asList(cmdArray));
            try {
                Process proc = Runtime.getRuntime().exec(cmdArray, null, installation.getExecDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ///System.out.println("Could not find netbeans installed.");
        }
    }

    private static void loadSampleResourceUrls(File destDir, String urlToSampleJavaFile, String[] resourceUrlArray) {
        //get dir from urlToSampleJavaFile
        String sampleJavaFileDir = urlToSampleJavaFile.substring(0,
                urlToSampleJavaFile.lastIndexOf('/') + 1); //include the last forward slash
        List<String> resourceUrlList = Arrays.asList(resourceUrlArray);
        //create resource files for each of the resources we use
        if (!resourceUrlList.isEmpty()) {
            for (String oneResourceUrl : resourceUrlList) {
                String sampleResourceName = oneResourceUrl.substring(
                        oneResourceUrl.lastIndexOf('/') + 1,
                        oneResourceUrl.length());
                try {
                    URL resourceUrl = new URL(sampleJavaFileDir + sampleResourceName);
                    Utils.copyFile(resourceUrl, destDir.getPath() + "/" + sampleResourceName);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
