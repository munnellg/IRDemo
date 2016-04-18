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

package com.gary.ir.gui.menus;

import com.gary.ir.gui.index.listeners.NewIndexListenerI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Gary Munnelly
 */
public class FileMenu extends JMenu implements ActionListener {
    private final JMenuItem newIndex;
    private final JMenuItem loadIndex;
    private final JMenuItem saveIndex;
    private final JMenuItem quit;
        
    private final LinkedList<NewIndexListenerI> newIndexListeners;
    
    public FileMenu() {
        super("File");
        
        this.newIndex = new JMenuItem("New Index");
        this.loadIndex = new JMenuItem("Load Index");
        this.saveIndex = new JMenuItem("Save Index");        
        this.quit = new JMenuItem("Quit");
        
        this.add(this.newIndex);
        this.newIndex.addActionListener(this);
        this.add(this.loadIndex);
        this.loadIndex.addActionListener(this);
        this.add(this.saveIndex);
        this.saveIndex.addActionListener(this);
        this.addSeparator();
        this.add(this.quit);
        this.quit.addActionListener(this);
        
        this.newIndexListeners = new LinkedList<>();
    }

    public void addNewIndexListener(NewIndexListenerI newIndexListener) {
        this.newIndexListeners.add(newIndexListener);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object clicked = e.getSource();
        
        if(clicked == this.newIndex) {
            for(NewIndexListenerI listener : this.newIndexListeners) {
                listener.newIndex();
            }
        } else if(clicked == this.quit) {
            ((JFrame) this.getTopLevelAncestor()).dispose();
        }
    }
}
