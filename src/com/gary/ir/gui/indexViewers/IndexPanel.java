/*
 * The MIT License
 *
 * Copyright 2016 Gary Munnelly.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gary.ir.gui.indexViewers;

import com.gary.ir.gui.index.listeners.DocumentsAddedListenerI;
import com.gary.ir.gui.index.listeners.IndexRebuiltListenerI;
import com.gary.ir.gui.index.listeners.TermSelectedListenerI;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Gary Munnelly
 */
public class IndexPanel extends JPanel {
    IndexSummaryPanel summary;
    IndexViewPanel indexView;
    IndexTermViewPanel termView;
    
    public IndexPanel() {
        JSplitPane sp = new JSplitPane();        
        JPanel left = new JPanel();
        JPanel right = new JPanel();
        
        this.setLayout(new BorderLayout());        
        left.setLayout(new BorderLayout());
        right.setLayout(new BorderLayout());
        
        this.summary = new IndexSummaryPanel();
        this.indexView = new IndexViewPanel();
        this.termView = new IndexTermViewPanel();
                
        left.add(this.summary, BorderLayout.NORTH);
        left.add(this.termView, BorderLayout.CENTER);
        right.add(this.indexView, BorderLayout.CENTER);
        
        sp.setLeftComponent(left);
        sp.setRightComponent(right);
        sp.setDividerSize(2);
        this.add(sp, BorderLayout.CENTER);
    }
    
    public void indexUpdated() {
        this.summary.indexUpdated();
        this.indexView.indexUpdated();
        this.termView.indexUpdated();
    }
    
    public void addDocumentsAddedListener(DocumentsAddedListenerI listener) {
        this.summary.addDocumentsAddedListener(listener);
    }
    
    public void addIndexRebuiltListener(IndexRebuiltListenerI listener) {
        this.summary.addIndexRebuiltListener(listener);
    }
    
    public void addTermSelectedListener(TermSelectedListenerI termSelectedListenerI) {
        this.indexView.addTermSelectedListener(termSelectedListenerI);
    }

    public void displayTerm(String term) {
        this.termView.setSelectedTerm(term);
    }
}
