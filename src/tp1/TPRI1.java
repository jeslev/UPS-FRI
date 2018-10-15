/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.search.BooleanClause;

/**
 *
 * @author jgmorenof
 */
public class TPRI1 {

    /**
     * @param args the command line arguments
     */
    String nameFileXML;
    String nameFolderIndex;

    public TPRI1() {
    	nameFileXML = "/home/jeslev/masterUPS/M1/RI/TP/simplewiki.csv";
        nameFolderIndex = "indexRI/";
    }
    
    public static void main(String[] args) {

    	    	
    	TPRI3 ri = new TPRI3();

    	  
//    	String query = "Thomas and Mario are strikers playing in Munich";
//    	String q1 = "Munich";
//    	String q2 = "Mario";
//    	String q3 = "Thomas";
    	
    	//Q5
    	Scanner sc = new Scanner(System.in);
    	String query = sc.nextLine();
    	//ri.index();
    	ri.query(query);
    	
    }
    
    public void index() {
        IndexCollection mywikipedia = new IndexCollection(nameFileXML,nameFolderIndex);
        try {
            mywikipedia.index();
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void index(int simIndex) {
        IndexCollection mywikipedia = new IndexCollection(nameFileXML,nameFolderIndex,simIndex);
        try {
            mywikipedia.index();
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void query(String query) {
        QuerySimple qs = new QuerySimple(nameFolderIndex);
        try {
           // qs.process(query);
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void query(String query, int simIndex) {
        QuerySimple qs = new QuerySimple(nameFolderIndex, simIndex);
        try {
           // qs.process(query);
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
