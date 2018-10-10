package tp1;

import org.apache.lucene.search.similarities.BM25Similarity;

/**
*
* @author jlovonm
*/

public class BM15Similarity extends BM25Similarity {

	public BM15Similarity(float k1) {
		super(k1,0.0f);
		
	}
	
}
