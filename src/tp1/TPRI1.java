/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        TPRI1 wfr = new TPRI1();
        wfr.index();
        //wfr.query("text:Thomas and Mario are strikers playing in Munich");
        wfr.query("text:the white house");

    
//        for(int i=0;i<=10;i++) {
//        	System.out.println("Tipo "+i);
//        	wfr.index(i);
//            wfr.query("title:the white house",i);
//        }
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
            qs.process(query);
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void query(String query, int simIndex) {
        QuerySimple qs = new QuerySimple(nameFolderIndex, simIndex);
        try {
            qs.process(query);
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
