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

import com.gary.ir.index.IndexSingleton;
import com.gary.ir.index.InvertedIndex;
import com.gary.ir.index.Posting;
import com.gary.ir.index.PostingsList;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Gary Munnelly
 */
public class IndexTermViewPanel extends JPanel {    
    private JScrollPane contentPane;    
    private JTree tree;
    private String selectedTerm;
    
    public IndexTermViewPanel() {
        this.setLayout(new BorderLayout());
        this.selectedTerm = "";
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.selectedTerm);
        
        this.tree = new JTree(top);
        this.tree.setShowsRootHandles(false);
        this.contentPane = new JScrollPane(this.tree);
        
        this.add(this.contentPane, BorderLayout.CENTER);
    }
            
    public String getSelectedTerm() {
        return this.selectedTerm;
    }
        
    public void setSelectedTerm( String selectedTerm ) {
        this.selectedTerm = selectedTerm;
        renderIndex();
    }
    
    public void renderIndex() {
        InvertedIndex index = IndexSingleton.getIndex();
        
        if( index == null ) {
            return;
        }
        
        PostingsList postingsList = 
                index.getTermPostingsList(this.selectedTerm);
        
        if(postingsList != null) {
            Collection<Posting> postings = postingsList.getPostings();

            DefaultTreeModel model = (DefaultTreeModel)this.tree.getModel();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.selectedTerm);
            for(Posting posting : postings) {
                DefaultMutableTreeNode  node = new DefaultMutableTreeNode(posting.getDocId());
                for(int i : posting.getReferences()) {
                    DefaultMutableTreeNode pos = new DefaultMutableTreeNode(i);
                    node.add(pos);
                }
                root.add(node);
            }
            model.setRoot(root);
        }
    }
    
    public void indexUpdated() {
        renderIndex();
    }
}
