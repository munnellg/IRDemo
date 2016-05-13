/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gary.ir.index;

import com.gary.ir.nlp.stemmer.Stemmer;

/**
 *
 * @author Gary Munnelly
 */
public class IndexSingleton {
    private static InvertedIndex index;
    
    static {
        IndexSingleton.index = new InvertedIndex();
    }
    
    public static InvertedIndex getIndex() {
        return IndexSingleton.index;
    }    
    
    public static void newIndex() {
        IndexSingleton.index = new InvertedIndex();
    }
    
    public static void rebuildIndex() {
        IndexSingleton.index.rebuild();
    }
}
