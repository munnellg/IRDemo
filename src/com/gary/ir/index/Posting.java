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

package com.gary.ir.index;

import java.util.LinkedList;

/**
 *
 * @author Gary Munnelly
 */
public class Posting {
    private String doc_id;
    private int frequency;
    private LinkedList<Integer> references;
    
    public Posting(String doc_id) {
        this.doc_id = doc_id;
        this.frequency = 0;
        references = new LinkedList();
    }
    
    public void addReference(int ref) {
        this.references.add(ref);
        this.frequency++;
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public String getDocId() {
        return this.doc_id;
    }
    
    public void setDocId( String doc_id ) {
        this.doc_id = doc_id;
    }
    
    public LinkedList<Integer> getReferences() {
        return this.references;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        builder.append(this.doc_id);
        builder.append("\": {\n");
        builder.append("\t\"frequency\" : ");
        builder.append(this.frequency);
        builder.append(",\n");
        builder.append("\t\"references\" : {\n");
        for(int reference : this.references) {
            builder.append("\t\t");
            builder.append(reference);
            builder.append(",\n");
        }
        builder.append("\t}\n");
        builder.append("}");
        return builder.toString();
    }
}
