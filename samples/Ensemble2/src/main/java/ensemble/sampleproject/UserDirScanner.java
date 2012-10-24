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

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Netbeans UserDirScanner
 */
public class UserDirScanner {
    
    public static NBInstallation[] suitableNBInstallations(File homeDir, String minVersion, Comparator<NBInstallation> comp) {
        File nbUserHome = new File(homeDir, ".netbeans");
        List<NBInstallation> list = allNBInstallations(nbUserHome);
//        System.out.println("All found NetBeans installations: " + list);
        
        NBInstallation devNbi = null;
        // find dev NBInstallation
        for (NBInstallation nbi : list) {
            // 1.0 version means no version number exists
            if (nbi.numVersion().equals("1.0") &&
                    nbi.releaseType().equals("dev") &&
                    nbi.releaseVersion().equals("")) {
                devNbi = nbi;
            }
        }
        if (minVersion.equals("dev")) {
            if (devNbi != null) {
                return new NBInstallation[] { devNbi };
            }
            return new NBInstallation[] { };
        }
        
        Collections.sort(list, comp);
        for (ListIterator listIter = list.listIterator(); listIter.hasNext(); ) {
            NBInstallation nbi = (NBInstallation) listIter.next();
            // in case we don't want dev builds -> || nbi.releaseType().equals("dev")) {
            if (NBInstallation.compareVersions(minVersion, nbi.numVersion()) > 0) {
                listIter.remove();
            }
        }
        Collections.reverse(list);
        // add dev to the end of the list here
        if (devNbi != null) {
            list.add(devNbi);
        }
        return list.toArray(new NBInstallation[list.size()]);
    }
    
    // returns all valid installations of NB found in ${HOME}/.netbeans
    private static List<NBInstallation> allNBInstallations(File nbUserHome) {
        File files[] = nbUserHome.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        List<NBInstallation> list = new ArrayList<NBInstallation>();
        // files might be null here, e.g. if there is no .netbeans folder
        if (files != null) {
            for (File file : files) {
                // creating NB installation is based on userdir
                NBInstallation nbi = new NBInstallation(file);
                if (nbi.isValid()) {
                    list.add(nbi);
                }
            }
        }
        return list;
    }
    
}