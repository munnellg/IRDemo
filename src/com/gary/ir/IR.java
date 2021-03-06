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

package com.gary.ir;

import com.gary.ir.gui.MainWindow;
import com.gary.ir.index.InvertedIndex;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;


/**
 *
 * @author Gary Munnelly
 */
public class IR {
    private static final InvertedIndex index = new InvertedIndex();
    
    public static void indexFiles( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();
                
        if (list == null){
            return;
        }

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                indexFiles( f.getAbsolutePath() );
            }
            else {
                index.indexFile(f);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow w = new MainWindow();
            }
        });
    }    
}