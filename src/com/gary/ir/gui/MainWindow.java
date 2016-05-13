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

package com.gary.ir.gui;

import com.gary.ir.gui.index.listeners.DocumentsAddedListenerI;
import com.gary.ir.gui.index.listeners.IndexRebuiltListenerI;
import com.gary.ir.gui.index.listeners.NewIndexListenerI;
import com.gary.ir.gui.index.listeners.TermSelectedListenerI;
import com.gary.ir.gui.indexViewers.IndexPanel;
import com.gary.ir.gui.menus.MainMenuBar;
import com.gary.ir.index.IndexSingleton;
import com.gary.ir.index.InvertedIndex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Gary Munnelly
 */
public class MainWindow extends JFrame {
    
    private final MainMenuBar menubar;
    private final JTabbedPane content;
    private IndexPanel indexPanel;
    private JLabel report;
            
    public MainWindow() {
        super("IR Demo");
        this.setLayout(new BorderLayout());        
        this.setPreferredSize(new Dimension(1024, 720));
                
        this.menubar = new MainMenuBar();        
                
        this.content = new JTabbedPane();
        this.indexPanel = new IndexPanel();
        
        this.menubar.addNewIndexListener(new NewIndexListenerI() {
            @Override
            public void newIndex() {
                IndexSingleton.newIndex();
                indexPanel.indexUpdated();
            }
        });
        
        this.indexPanel.addDocumentsAddedListener(new DocumentsAddedListenerI() {
            @Override
            public void documentsAdded(File[] files) {                
                processFiles(files);
                indexPanel.indexUpdated();
            }            
        });
        
        this.indexPanel.addTermSelectedListener( new TermSelectedListenerI () {

            @Override
            public void termSelected(String term) {
                indexPanel.displayTerm(term);
            }            
        });
        
        this.indexPanel.addIndexRebuiltListener(new IndexRebuiltListenerI() {

            @Override
            public void indexRebuilt() {
                indexPanel.indexUpdated();
            }
        });
        
        this.setJMenuBar(this.menubar);
        
        this.content.addTab("Index", this.indexPanel);
         
        this.report = new JLabel("");
        
        this.add(this.content, BorderLayout.CENTER);
        this.add(this.report, BorderLayout.SOUTH);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void processFiles( File[] files ) {
        for(File f : files) {
            indexFile(f);
        }
        this.report.setText("");
    }
    
    public void indexFile( File file ) {
        InvertedIndex index = IndexSingleton.getIndex();
        
        if ( file.isDirectory() ) {
            File[] list = file.listFiles();
            for(File f : list) {
                this.indexFile( f );
            }            
        }
        else {
            this.report.setText("Indexing " + file.getAbsolutePath());            
            index.indexFile(file);
        }
    }
}
