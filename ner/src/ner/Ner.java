/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ner;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import java.io.FileInputStream;
import java.io.InputStream;
import org.xml.sax.SAXException;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
/**
 *
 * @author Gary Munnelly
 */
public class Ner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "XML Files", "xml");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {    
                InputStream modelIn = new FileInputStream("/home/gary/downloads/models/en-ner-person.bin");
                TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
                NameFinderME nameFinder = new NameFinderME(model);
                
                modelIn = new FileInputStream("/home/gary/downloads/models/en-sent.bin");
                SentenceModel sentmodel = new SentenceModel(modelIn);
                SentenceDetectorME sentFinder = new SentenceDetectorME(sentmodel);
                
                modelIn = new FileInputStream("/home/gary/downloads/models/en-token.bin");
                TokenizerModel tokModel = new TokenizerModel(modelIn);
                TokenizerME tokenizer = new TokenizerME(tokModel);
                
                for(File file : chooser.getSelectedFiles()) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file);  
                    
                    String text = doc.getElementsByTagName("abstract").item(0).getTextContent();
                    
                    String[] sents = sentFinder.sentDetect(text);
                    
                    for(String sent : sents) {
                        String [] tokens = tokenizer.tokenize(sent);
                        
                        Span [] entities = nameFinder.find(tokens);
                        
                        for(Span span : entities) {
                            StringBuilder builder = new StringBuilder();
                            
                            for(int i=span.getStart(); i<span.getEnd(); i++) {
                                builder.append(tokens[i]);
                                builder.append(' ');
                            }
                            
                            System.out.println(builder.toString());
                        }
                    }
                }
        }
    }
    
}
