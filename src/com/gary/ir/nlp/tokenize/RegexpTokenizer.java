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

package com.gary.ir.nlp.tokenize;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Gary Munnelly
 */
public class RegexpTokenizer extends Tokenizer {

    private static final Pattern DEFAULT_PATTERN = 
            Pattern.compile("'?\\w+('?\\w*)*");
    
    private Pattern pattern;
    
    public RegexpTokenizer() {
        this(RegexpTokenizer.DEFAULT_PATTERN);
    }
    
    public RegexpTokenizer(Pattern pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public String[] tokenize(String text) {
        Matcher m = this.pattern.matcher(text);
        
        // Linked list insertion is cheaper than ArrayList
        LinkedList<String> l = new LinkedList<>();                
        
        // Add each substring that matches the pattern to our list of results
        while( m.find() ) {
            l.add(m.group());
        }       
        
        // Convert the linked list to an array
        return l.toArray(new String[0]);
    }    
}
