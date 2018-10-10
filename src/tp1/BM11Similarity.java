package tp1;

import org.apache.lucene.search.similarities.BM25Similarity;

public class BM11Similarity extends BM25Similarity {

	public BM11Similarity(float k1) {
		super(k1,1.0f);
		
	}
	
}
