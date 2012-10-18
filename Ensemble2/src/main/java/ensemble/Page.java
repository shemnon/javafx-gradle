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

import ensemble.pages.DocPage;
import javafx.scene.control.TreeItem;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import javafx.collections.ObservableList;

/**
 * Page
 *
 */
public abstract class Page extends TreeItem<String> {

    protected Page(String name) {
        super(name);
    }

    public void setName(String name){
        setValue(name);
    }

    public String getName() {
        return getValue();
    }

    public String getPath() {
        if (getParent() == null) {
            return getName();
        } else {
            String parentsPath = ((Page)getParent()).getPath();
            if (parentsPath.equalsIgnoreCase("All")) {
                return getName();
            } else {
                return  parentsPath + "/" + getName();
            }
        }
    }

    public abstract Node createView();

    /** find a child with a '/' deliminated path */
    public Page getChild(String path) {
//        System.out.println("Page.getChild("+path+")");
//        new Throwable().printStackTrace(System.out);
        int firstIndex = path.indexOf('/');
        String childName = (firstIndex==-1) ? path : path.substring(0,firstIndex);
        String anchor = null;
        if (childName.indexOf('#') != -1) {
            String[] parts = childName.split("#");
//            System.out.println("childName = " + childName);
            childName = parts[0];
//            System.out.println("childName AFTER = " + childName);
            anchor = (parts.length == 2) ? parts[1] : null;
//            System.out.println("anchor = " + anchor);
        }
//        System.out.println("childName = " + childName);
        for (TreeItem child : getChildren()) {
            Page page = (Page)child;
            if(page.getName().equals(childName)) {
                if(firstIndex==-1) {
                    if (page instanceof DocPage) {
                        ((DocPage)page).setAnchor(anchor);
                    }
                    return page;
                } else {
                    return page.getChild(path.substring(firstIndex+1));
                }
            }
        }
        return null;
    }

    @Override public String toString() {
        return toString("");
    }

    private String toString(String indent) {
        String out = indent + "["+getName()+"] "+getClass().getSimpleName();
        ObservableList<TreeItem<String>> childModel = getChildren();
        if (childModel!=null) {
            for (TreeItem child:childModel) {
                out += "\n"+((Page)child).toString("    "+indent);
            }
        }
        return out;
    }

    public static class GoToPageEventHandler implements EventHandler {
        private String pagePath;

        public GoToPageEventHandler(String pagePath) {
            this.pagePath = pagePath;
        }

        public void handle(Event event) {
            Ensemble2.getEnsemble2().goToPage(pagePath);
        }
    }
}
