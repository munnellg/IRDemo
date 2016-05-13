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

import com.gary.ir.gui.index.listeners.TermSelectedListenerI;
import com.gary.ir.index.IndexSingleton;
import com.gary.ir.index.InvertedIndex;
import com.gary.ir.index.PostingsList;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Gary Munnelly
 */
class IndexViewPanel extends JPanel implements MouseListener {
    
    private JTable dataTable;
    private DefaultTableModel dataModel;    
    LinkedList<TermSelectedListenerI> listeners;
    
    private String[] tableHeaders = {
        "Term",
        "Frequency",
        "Document Count"
    };
    
    public IndexViewPanel() {
        this.setLayout(new BorderLayout());
        
        this.dataModel = 
                new DefaultTableModel(new Object[0][], this.tableHeaders) {
            Class[] types = { String.class, Integer.class, Integer.class };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };     
        
        TableRowSorter sorter = new TableRowSorter(this.dataModel);  
        
        this.dataTable = new JTable(this.dataModel);
        this.dataTable.setRowSorter(sorter);
        this.dataTable.addMouseListener(this);
        this.add(new JScrollPane(this.dataTable), BorderLayout.CENTER);
        this.listeners = new LinkedList<>();
        renderIndex();
    }
    
    public void indexUpdated() {
        renderIndex();
    }
    
    private void renderIndex() {
        InvertedIndex index = IndexSingleton.getIndex();
        this.dataModel.setRowCount(0);
        
        if( index != null ) {           
            String [] terms = index.getIndexedTerms();
            Object [] data = new Object[this.tableHeaders.length];
            PostingsList postings;
            
            for(String term : terms) {
                postings = index.getTermPostingsList(term);
                
                data[0] = term;
                data[1] = postings.getFrequency();
                data[2] = postings.getNumPostings();
                
                this.dataModel.addRow(data);
            }
        }
    }

    public void addTermSelectedListener(TermSelectedListenerI termSelectedListenerI) {
        this.listeners.add(termSelectedListenerI);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = this.dataTable.rowAtPoint(e.getPoint());
                
        String data = (String) this.dataTable.getValueAt(row, 0);
        
        for(TermSelectedListenerI listener : listeners) {
            listener.termSelected(data);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }
}
