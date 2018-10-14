package tp1;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TPRI3 {

	String nameFileXML;
    String nameFolderIndex;
	
	public TPRI3() {
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
	
	
    public void query(String q1, String q2, String q3) {
        QuerySimple qs = new QuerySimple(nameFolderIndex);
        try {
            qs.process(q1, q2, q3);
        } catch (Exception ex) {
            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void query(String query) {
		
		// mots importants du query
		String[] mots = query.split(" ");
		
		List<String> motImpts = new ArrayList<String>();
		for(int i=0;i<mots.length;i++) {
			if(mots[i].charAt(0)>='A' && mots[i].charAt(0)<='Z') motImpts.add(mots[i]);
		}
		
		for(int i=0;i<motImpts.size();i++)
			System.out.print(motImpts.get(i) + " - ");
		
		System.out.println();

		//		QuerySimple qs = new QuerySimple(nameFolderIndex);
//        try {
//            qs.process(query);
//        } catch (Exception ex) {
//            Logger.getLogger(TPRI1.class.getName()).log(Level.SEVERE, null, ex);
//        }
	}
	
	
}
