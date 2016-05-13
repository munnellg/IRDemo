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

import com.gary.ir.nlp.stemmer.Stemmer;
import com.gary.ir.nlp.tokenize.RegexpTokenizer;
import com.gary.ir.nlp.tokenize.Tokenizer;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *
 * @author Gary Munnelly
 */
public class InvertedIndex {

    private Tokenizer tokenizer;
    private Stemmer stemmer;
    private TreeMap <String, PostingsList> index;
    private TreeMap <String, Integer> doc_index;
    
    public InvertedIndex() {
        this.index = new TreeMap<>();
        this.doc_index = new TreeMap<>();
        this.tokenizer = new RegexpTokenizer();
        this.stemmer = null;
    }
    
    public String [] getIndexedTerms() {
        return this.index.keySet().toArray(new String[0]);
    }
    
    public PostingsList getTermPostingsList( String term ) {
        return this.index.get(term);
    }
    
    public void rebuild() {
        TreeMap <String, Integer> documents = doc_index;
        
        doc_index = new TreeMap<>();
        index = new TreeMap<>();
        
        for(String fpath : documents.keySet()) {
            File f = new File(fpath);
            this.indexFile(f);
        }
    }
    
    public void indexFile(File f) {
        String [] tokens;
        
        try {
            // Tokenize the document into words
            tokens = tokenizer.tokenize(f);
            if(stemmer != null) {
                for(int i=0; i<tokens.length; i++){
                    tokens[i] = this.stemmer.stem(tokens[i]);
                }
            }
        } catch (IOException ex) {
            // Don't index the file if it couldn't be found. Just report the
            // error and quit
            System.out.println("Unable to open file " + f.getAbsoluteFile());
            return;
        }
        
        // Generate postings for this file.
        TreeMap<String,Posting> postings = generatePostings(f.getAbsolutePath(), tokens);
               
        // Fold the postings into our index
        for(String key : postings.keySet()) {
            PostingsList list;
            list = this.index.getOrDefault(key, new PostingsList(key));
            list.addPosting(postings.get(key));
            this.index.putIfAbsent(key, list);
        }
        
        // We'll need to keep track of the document length too, so store that
        this.doc_index.put(f.getAbsolutePath(), tokens.length);
    }

    public Tokenizer getTokenizer() {
        return this.tokenizer;
    }
    
    public void setStemmer(Stemmer stemmer) {
        this.stemmer = stemmer;
    }
    
    public Stemmer getStemmer() {
        return this.stemmer;
    }
    
    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }    
        
    public LinkedList<Posting> getTermPostings( String query ) {        
        return null;
    }
    
    public int getTermInDocumentFrequency(String doc_id, String term) {
        PostingsList pl = this.index.get(term);
        
        if(pl == null) {
            return 0;
        }
        
        Posting p = pl.getPosting(doc_id);
        
        if(p == null) {
            return 0;
        }
        
        return p.getFrequency();
    }
    
    public int getNumTermsIndexed() {
        return this.index.size();
    }
    
    public int getNumDocumentsIndexed() {
        return this.doc_index.size();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("\"Index\": {\n");        
        for(PostingsList list : this.index.values()) {
            builder.append("\t\t");
            builder.append(list.toString().replaceAll("\n", "\n\t\t"));
            builder.append(",\n");
        }
        builder.append("\t}\n");
        builder.append("}");
        return builder.toString();
    }
    
    private TreeMap<String, Posting> generatePostings(String doc_id, String [] tokens) {
        TreeMap<String,Posting> postings = new TreeMap<>();
            
        if(tokens != null) {
            for(int i=0; i<tokens.length; i++) {
                Posting posting;
                String normed = tokens[i].toLowerCase();                
                posting = postings.getOrDefault(normed, new Posting(doc_id));                
                posting.addReference(i);
                postings.putIfAbsent(normed, posting);
            }
        }
        
        return postings;
    }
}
