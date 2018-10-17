package tp1;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TPRI5 {

	String nameFileXML;
    String nameFolderIndex;
	
	public TPRI5() {
		nameFileXML = "/home/jeslev/masterUPS/M1/RI/TP/simplewiki.csv";
        nameFolderIndex = "indexRI/";
	}
	
	public void index() {
		IndexCollection mywikipedia = new IndexCollection(nameFileXML,nameFolderIndex);
        try {
            mywikipedia.index();
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	
	public void query(String query, int count, PrintWriter writer) {
		
		// mots importants du query
		String[] mots = query.split(" ");
		
		List<String> motImpts = new ArrayList<String>();
		for(int i=0;i<mots.length;i++) {
			if(mots[i].charAt(0)>='A' && mots[i].charAt(0)<='Z') motImpts.add(mots[i]);
		}
		
		for(int i=0;i<motImpts.size();i++) {
			System.out.println("Term : "+motImpts.get(i));
			QuerySimple qs = new QuerySimple(nameFolderIndex);
			
			try {
	            qs.process(motImpts.get(i), query, count+1+i, writer);
	        } catch (Exception ex) {
	            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
	        }
			System.out.println();
		}
		
		

		
        
	}
	
	
}
