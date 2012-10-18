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

import java.io.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Netbeans NBInstallation
 */
public class NBInstallation {

    public static final Comparator VERSION_COMPARATOR = new VersionComparator();
    public static final Comparator LAST_USED_COMPARATOR = new LastUsedComparator();

    // there are two versions of log file
    private static final String searchStr1 = "Installation; User Dir.";
    private static final String searchStr2 = "Installation";

    // regexp for matching nb cluster, e.g. nb5.5
    private static final Pattern NB_CLUSTER_REGEX = Pattern.compile("[/\\\\]nb(\\d*(\\.\\d+)*)$");
    // regexp for matching start of windows path, e.g. e:\
    private static final Pattern WIN_ROOT_REGEX = Pattern.compile("^[a-zA-Z]:\\\\");

    private static final String[] NON_CLUSTER_DIRS = new String[]{"etc", "bin", "harness"};

    private File userDir;
    private File installDir;
    private String ideVersion;

    private File logFile;
    private File lockFile;

    private String versionParts[];

    /**
     * Creates a new instance of NetBeansInstallation
     */
    public NBInstallation(File userDir) {
        this.userDir = userDir;
        logFile = new File(new File(new File(userDir, "var"), "log"), "messages.log");
        ideVersion = findVersion();
        try {
            installDir = findInstallDir();
        } catch (Exception ex) {
            ///System.out.println("Exception during searching for install dir: " + exc2String(ex));
        }
        versionParts = getVersionParts(ideVersion);
        lockFile = new File(userDir, "lock");
    }

    public boolean isLocked() {
        return lockFile.exists();
    }

    public boolean isValid() {
        return isNBUserdir() && getVersionParts(ideVersion) != null &&
                installDir != null && installDir.exists() && installDir.isDirectory() &&
                new File(installDir, "bin").exists();
    }

    public File getInstallDir() {
        return installDir;
    }

    public File getExecDir() {
        return new File(installDir, "bin");
    }

    /**
     * Tries to find important files for each project type in any cluster in installDir, XXX should be extended to look
     * also in userDir!
     */
    public boolean canHandle(Collection c) {
        int toBeFound = 0;
        int foundFiles = 0;
        for (Object aC : c) {
            ProjectType pt = (ProjectType) aC;
            String impFiles[] = pt.getImportantFiles();
            toBeFound += impFiles.length;
            String[] clusterPaths = getClusterDirPaths();
            for (String impFile : impFiles) {
                String iPath = impFile.replace('/', File.separatorChar);
                for (String clusterPath : clusterPaths) {
                    String searchPath = clusterPath + File.separator + iPath;
//                    System.out.println("Looking for: " + searchPath);
                    File f = new File(searchPath);
                    if (f.exists()) {
//                        System.out.println("File: " + f.getAbsolutePath() + " exists.");
                        foundFiles++;
                    }
                }
            }
        }
        return foundFiles == toBeFound;
    }

    /**
     * Returns all dirs under installDir that are not listed in NON_CLUSTER_DIRS, they are considered to be clusters
     */
    public File[] getClusterDirs() {
        return installDir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    for (int i = 0; i < NON_CLUSTER_DIRS.length; i++) {
                        if (f.getName().equals(NON_CLUSTER_DIRS[i])) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private String[] getClusterDirPaths() {
        File[] files = getClusterDirs();
        String dirPaths[] = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            dirPaths[i] = files[i].getAbsolutePath();
        }
        return dirPaths;
    }

    public long lastUsed() {
        if (logFile.exists()) {
            return logFile.lastModified();
        }
        return 0L;
    }

    // from 5.5.1beta2 => 5.5.1
    public String numVersion() {
        if (versionParts != null && !versionParts[0].equals("")) {
            return versionParts[0];
        }
        // fallback to avoid problems when dev version
        // userdir doesn't have a numeric version
        return "1.0";
    }

    // from 5.5.1beta2 => beta
    public String releaseType() {
        if (versionParts != null && !versionParts[1].equals("")) {
            return versionParts[1];
        }
        return "";
    }

    // from 5.5.1beta2 => 2
    public String releaseVersion() {
        if (versionParts != null && !versionParts[2].equals("")) {
            return versionParts[2];
        }
        return "";
    }

    public String toString() {
        return userDir.getAbsolutePath();
    }

    // ---

    public static class LastUsedComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            return signum(((NBInstallation) arg0).lastUsed() -
                    ((NBInstallation) arg1).lastUsed());
        }

        private int signum(long diff) {
            if (diff > 0) return 1;
            if (diff < 0) return -1;
            return 0;
        }

    }

    public static class VersionComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            int retVal = 0;
            String v0 = ((NBInstallation) arg0).numVersion();
            String v1 = ((NBInstallation) arg1).numVersion();
            // this is because dev version doesn't have any numbers,
            // so 'dev' means lower version always
            if (v0.equals("")) {
                retVal = -1;
            } else if (v1.equals("")) {
                retVal = 1;
            }
            if (retVal == 0) {
                retVal = compareVersions(v0, v1);
            }
            if (retVal == 0) {
                v0 = ((NBInstallation) arg0).releaseType();
                v1 = ((NBInstallation) arg1).releaseType();
                retVal = compareReleaseTypes(v0, v1);
            }
            if (retVal == 0) {
                v0 = ((NBInstallation) arg0).releaseVersion();
                v1 = ((NBInstallation) arg1).releaseVersion();
                retVal = compareVersions(v0, v1);
            }
            return retVal;
        }

    }

    // ---

    private File findInstallDir() throws FileNotFoundException, IOException {
        String dirPath = null;
//        System.out.println("Parsing file: " + logFile.getAbsolutePath());
        BufferedReader logFileReader = new BufferedReader(new FileReader(logFile));
        String line = logFileReader.readLine();
        boolean lineRead;

        while (line != null) {

            lineRead = false;
            if (line.indexOf(searchStr1) != -1) { // old version of log file
//                System.out.println("Found line: " + line);
                int index1 = line.indexOf('=') + 2;
                int index2 = line.indexOf("; ", index1);
                String subStr = line.substring(index1, index2);
//                System.out.println("Found substring: " + subStr);
                StringTokenizer tokenizer = new StringTokenizer(subStr, File.pathSeparator);
                while (tokenizer.hasMoreTokens()) {
                    String instPart = tokenizer.nextToken();
//                    System.out.println("Testing token: " + instPart);
                    // regex matcher looking for nb cluster e.g. nb5.5
                    Matcher matcher = NB_CLUSTER_REGEX.matcher(instPart);
                    if (matcher.find()) {
                        File f = new File(instPart).getParentFile();
//                        System.out.println("Found file: " + f.getAbsolutePath());
                        if (f.exists()) {
                            dirPath = f.getAbsolutePath();
                        }
                    }
                }
            } else if (line.indexOf(searchStr2) != -1) { // new version of log file
//                System.out.println("Found line: " + line);
                int index = line.indexOf('=') + 2;
                String tLine = line.substring(index).trim();
                boolean matching;

                do {
                    matching = false;
                    // startsWith("/") OR startsWith("e:\")
                    if (tLine.startsWith("/") || matchWinRoot(tLine)) { // correct line matched
                        matching = true;
//                        System.out.println("Matching line: " + tLine);
                        Matcher matcher = NB_CLUSTER_REGEX.matcher(tLine);
                        if (matcher.find()) { // nb cluster matched
                            File f = new File(tLine).getParentFile();
//                            System.out.println("Found file: " + f.getAbsolutePath());
                            if (f.exists()) {
                                dirPath = f.getAbsolutePath();
                            }
                        }
                        line = logFileReader.readLine();
                        lineRead = true;
                        tLine = line.trim();
                    }
                } while (matching);

            }

            if (!lineRead) {
                line = logFileReader.readLine();
            }

        }

        if (dirPath != null) {
            return new File(dirPath);
        } else {
            return null;
        }
    }

    private boolean matchWinRoot(String line) {
        Matcher matcher = WIN_ROOT_REGEX.matcher(line);
        return matcher.find();
    }

    // XXX the version might be read from log file
    private String findVersion() {
        return userDir.getName();
    }

    // check if build.properties and var/log/messages.log files exist
    private boolean isNBUserdir() {
        File buildProps = new File(userDir, "build.properties");
        if (buildProps.exists() && logFile.exists()) {
            return true;
        }
        return false;
    }

    // ---

    // will be used to validate folder as NB install dir
    // after user selects some folder
    public static boolean isNBInstallation(File f) {
        if (new File(f, "bin").exists()) {
            return true;
        }
        return false;
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Utils

    /**
     * Compares two NB versions (only numbers), e.g. 5.5.1, 6.0, returns negative if first version number parameter is
     * lower than second, positive if first is higher and 0 if both versions are the same
     */
    public static int compareVersions(String verStr1, String verStr2) {
        int vd1[] = parseVersionString(verStr1);
        int vd2[] = parseVersionString(verStr2);
        int len1 = vd1.length;
        int len2 = vd2.length;
        int max = Math.max(len1, len2);
        for (int i = 0; i < max; i++) {
            int d1 = ((i < len1) ? vd1[i] : 0);
            int d2 = ((i < len2) ? vd2[i] : 0);
            if (d1 != d2) {
                return d1 - d2;
            }
        }
        return 0;
    }

    /**
     * Compare release types in following way: dev < beta < rc < ""
     */
    public static int compareReleaseTypes(String relType1, String relType2) {
        int retVal = 0;
        if (relType1.equals(relType2)) {
            retVal = 0;
        } else if (relType1.equals("")) {
            retVal = 1;
        } else if (relType2.equals("")) {
            retVal = -1;
        } else if (relType1.equals("dev") && ((relType2.equals("beta") || relType2.equals("rc")))) {
            retVal = -1;
        } else if (relType2.equals("dev") && ((relType1.equals("beta") || relType1.equals("rc")))) {
            retVal = 1;
        } else if (relType1.equals("beta") && relType2.equals("rc")) {
            retVal = -1;
        } else if (relType2.equals("beta") && relType1.equals("rc")) {
            retVal = 1;
        }
        return retVal;
    }

    private static int[] parseVersionString(String s) throws NumberFormatException {
        StringTokenizer st = new StringTokenizer(s, ".", true);
        int len = st.countTokens();
        if ((len % 2) == 0) {
            throw new NumberFormatException("Even number of pieces in a spec version: '" + s + "'"); // NOI18N
        }
        int i = 0;
        int[] digits = new int[len / 2 + 1];
        boolean expectingNumber = true;
        while (st.hasMoreTokens()) {
            if (expectingNumber) {
                expectingNumber = false;
                int piece = Integer.parseInt(st.nextToken());
                if (piece < 0) {
                    throw new NumberFormatException("Spec version component < 0: " + piece); // NOI18N
                }
                digits[i++] = piece;
            } else {
                if (!".".equals(st.nextToken())) { // NOI18N
                    throw new NumberFormatException("Expected dot in spec version: '" + s + "'"); // NOI18N
                }
                expectingNumber = true;
            }
        }
        return digits;
    }

    /**
     * Returns parts of the NB version number if it matches the regexp e.g. '1.2.3beta2' = > [0] == 1.2.3, [1] == beta,
     * [2] == 2 if not matches returns null
     */
    public static String[] getVersionParts(String s) {
        if (s == null) {
            return null;
        }
        String retVal[] = new String[]{"", "", ""};
        Pattern p = Pattern.compile("(\\d*(\\.\\d+)*)([a-zA-Z]*)(\\d*)");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            retVal[0] = m.group(1);
            retVal[1] = m.group(3);
            retVal[2] = m.group(4);
            return retVal;
        }
        return null;
    }

    /**
     *
     */
    public static String exc2String(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        writer.flush();
        return writer.toString();
    }

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
    private static final String winLauncher  = "netbeans.exe";
    private static final String unixLauncher = "netbeans";
    private static final String macLauncher  = "netbeans";
    
    public static String getPlatformLauncher() {
        String retVal = "";
        if (OS_NAME.indexOf("win") != -1) {
            retVal = winLauncher;
        } else if (OS_NAME.indexOf("unix") != -1) {
            retVal = unixLauncher;
        } else if (OS_NAME.indexOf("linux") != -1) {
            retVal = unixLauncher;
        } else if (OS_NAME.indexOf("mac os") != -1) {
            retVal = macLauncher;
        } else if (OS_NAME.indexOf("solaris") != -1) {
            retVal = unixLauncher;
        } else if (OS_NAME.indexOf("sunos") != -1) {
            retVal = unixLauncher;
        }
        return retVal;
    }
}