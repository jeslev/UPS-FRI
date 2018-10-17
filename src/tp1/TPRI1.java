/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

    	PrintWriter writer = new PrintWriter("results2.tp5", "UTF-8");
    	
    	TPRI5 ri = new TPRI5();
    	//ri.index();
    	  
    	String query = "Thomas and Mario are strikers playing in Munich";
    	ri.query(query,100, writer);
    	
    	query = "Leo scored two goals and assisted Puyol to ensure a 4–0 quarter-final victory over Bayern";
    	ri.query(query,200, writer);
    	
    	query = "Skype software for Mac";
    	ri.query(query,300, writer);
    	
    	query = "Cowboys fans petition Obama to oust Jones";
    	ri.query(query,400, writer);
    	
    	query = "Kate and Henry are known for being devoted to the Anglican church";
    	ri.query(query,500, writer);
    	
    	writer.close();
    	//Q5
//    	Scanner sc = new Scanner(System.in);
//    	String query = sc.nextLine();
    	//ri.index();
//    	ri.query(query);
    	
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
