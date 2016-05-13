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

import static com.gary.ir.IR.indexFiles;
import com.gary.ir.gui.index.listeners.DocumentsAddedListenerI;
import com.gary.ir.gui.index.listeners.IndexRebuiltListenerI;
import com.gary.ir.index.IndexSingleton;
import com.gary.ir.index.InvertedIndex;
import com.gary.ir.nlp.stemmer.PorterStemmer;
import com.gary.ir.nlp.stemmer.Stemmer;
import com.gary.ir.nlp.tokenize.RegexpTokenizer;
import com.gary.ir.nlp.tokenize.WhitespaceTokenizer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Gary Munnelly
 */
public class IndexSummaryPanel extends JPanel implements ActionListener {
    
    private final JLabel docCountLabel;
    private final JLabel termCountLabel;
    private final JLabel tokenizerLabel;
    private final JLabel stemmerLabel;
    
    private final JLabel docCountValue;
    private final JLabel termCountValue;
    private final JComboBox<String> tokenizerValue;
    private final JComboBox<String> stemmerValue;
    
    private JButton rebuildIndex;
    private JButton addDocument;
        
    private LinkedList<DocumentsAddedListenerI> documentsAddedListeners;
    private LinkedList<IndexRebuiltListenerI> indexRebuiltListeners;
    
    public IndexSummaryPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        this.docCountLabel = new JLabel("Documents Indexed");
        this.termCountLabel = new JLabel("Terms Indexed");
        this.tokenizerLabel = new JLabel("Tokenizer");
        this.stemmerLabel = new JLabel("Stemmer");
        this.docCountValue = new JLabel("");
        this.termCountValue = new JLabel("");                    
                
        String[] tokenizers = new String[] {"Regular Expression Tokenizer", "Whitespace Tokenizer"};
        String[] stemmers = new String[] {"None", "Porter Stemmer"};
        this.tokenizerValue = new JComboBox<>(tokenizers);
        this.stemmerValue = new JComboBox<>(stemmers);
        
        this.rebuildIndex = new JButton("Rebuild Index");
        this.addDocument = new JButton("Add Document");
        renderIndex();
        
        c.insets = new Insets(5,5,5,5);        
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx=0;
        c.gridy=0;
        c.weightx = 0.5;        
        this.add(this.docCountLabel, c);
        
        c.gridx=1;
        c.gridy=0;      
        c.weightx = 0.5;
        this.add(this.docCountValue, c);
        
        c.gridx=0;
        c.gridy=1;
        c.weightx = 0.5;
        this.add(this.termCountLabel, c);
                
        c.gridx=1;
        c.gridy=1;
        c.weightx = 0.5;
        this.add(this.termCountValue, c);
                
        c.gridx=0;
        c.gridy=2;
        c.weightx = 0.5;
        this.add(this.tokenizerLabel, c);
        
        c.gridx=1;
        c.gridy=2;
        c.weightx = 0.5;
        this.add(this.tokenizerValue, c);
        
        c.gridx=0;
        c.gridy=3;        
        c.weightx = 0.5;
        this.add(this.stemmerLabel, c);
        
        c.gridx=1;
        c.gridy=3;
        c.weightx = 0.5;
        this.add(this.stemmerValue, c);
        
        c.gridx=0;
        c.gridy=4;    
        c.gridwidth=2;
        c.weightx = 0.5;
        this.add(this.addDocument, c);
        
        c.gridx=0;
        c.gridy=5;
        c.gridwidth=2;
        c.weightx = 0.5;
        this.add(this.rebuildIndex, c);
        
        this.addDocument.addActionListener(this);
        this.tokenizerValue.addActionListener(this);
        this.stemmerValue.addActionListener(this);
        this.rebuildIndex.addActionListener(this);
        this.documentsAddedListeners = new LinkedList<>();
        this.indexRebuiltListeners = new LinkedList<>();
    }
        
    public void indexUpdated() {
        renderIndex();
    }
        
    private void renderIndex() {
        InvertedIndex index = IndexSingleton.getIndex();
        if(index == null ) {
            this.docCountValue.setText("N/A");
            this.termCountValue.setText("N/A");
            this.tokenizerValue.setEnabled(false);
            this.stemmerValue.setEnabled(false);
            this.rebuildIndex.setEnabled(false);
            this.addDocument.setEnabled(false);
        } else {
            this.tokenizerValue.setEnabled(true);
            this.docCountValue.setText(String.valueOf(index.getNumDocumentsIndexed()));
            this.termCountValue.setText(String.valueOf(index.getNumTermsIndexed()));            
            
            this.tokenizerValue.setSelectedItem(index.getTokenizer().toString());
            Stemmer s = index.getStemmer();
            if( s == null ) {
                this.stemmerValue.setSelectedItem("None");
            } else {
                this.stemmerValue.setSelectedItem(s.toString());
            }
            this.rebuildIndex.setEnabled(true);
            this.addDocument.setEnabled(true);
        }
    }
    
    public void addDocumentsAddedListener(DocumentsAddedListenerI listener) {
        this.documentsAddedListeners.add(listener);
    }
    
    public void addIndexRebuiltListener(IndexRebuiltListenerI listener) {
        this.indexRebuiltListeners.add(listener);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object clicked = e.getSource();
        
        if(clicked == this.addDocument) {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            
            int returnVal = fc.showOpenDialog(this.getTopLevelAncestor());
                        
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File [] files = fc.getSelectedFiles();
                
                for(DocumentsAddedListenerI l : this.documentsAddedListeners) {
                    l.documentsAdded(files);
                }
            }             
        } else if(clicked == this.tokenizerValue) {
            String tokenizer = (String)this.tokenizerValue.getSelectedItem();
            InvertedIndex index = IndexSingleton.getIndex();
            
            switch(tokenizer) {
                case "Whitespace Tokenizer":
                    index.setTokenizer(new WhitespaceTokenizer());
                    break;
                case "Regular Expression Tokenizer": 
                    index.setTokenizer(new RegexpTokenizer());
                    break;
                default:
                    System.out.println("ERROR: Invalid Tokenizer value " + tokenizer);
            }
            
            renderIndex();
        } else if(clicked == this.rebuildIndex) {
            IndexSingleton.rebuildIndex();
            for(IndexRebuiltListenerI l : this.indexRebuiltListeners) {
                l.indexRebuilt();
            }
        } else if(clicked == this.stemmerValue) {
            String stemmer = (String)this.stemmerValue.getSelectedItem();
            InvertedIndex index = IndexSingleton.getIndex();
            
            switch(stemmer) {
                case "None":
                    index.setStemmer(null);
                    break;
                case "Porter Stemmer": 
                    index.setStemmer(new PorterStemmer());
                    break;
                default:
                    System.out.println("ERROR: Invalid Stemmer value " + stemmer);
            }
            
            renderIndex();
        }
    }
}
