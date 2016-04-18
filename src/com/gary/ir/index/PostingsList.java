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

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *
 * @author Gary Munnelly
 */
public class PostingsList {
    String term;
    int frequency;
    TreeMap<String, Posting> postings;
    
    public PostingsList(String term) {
        this.term = term;
        this.frequency = 0;
        this.postings = new TreeMap<>();
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public int getNumPostings() {
        return this.postings.keySet().size();
    }
    
    public Collection<Posting> getPostings() {
        return this.postings.values();
    }
    
    public void addPosting(Posting posting) {
        this.postings.put(posting.getDocId(), posting);
        this.frequency+=posting.getFrequency();
    }
    
    public Posting getPosting(String doc_id) {
        return this.postings.get(doc_id);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        builder.append(this.term);
        builder.append("\": {\n");
        builder.append("\t\"frequency\" : ");
        builder.append(this.frequency);
        builder.append(",\n");
        builder.append("\t\"postings\" : {\n");
        
        for(Posting posting : this.postings.values()) {
            builder.append("\t\t");            
            builder.append(posting.toString().replaceAll("\n", "\n\t\t"));
            builder.append(",\n");
        }
        
        builder.append("\t}\n");
        builder.append("}");
        return builder.toString();
    }
}
