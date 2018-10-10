package tp1;

/**
*
* @author jlovonm
*/

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class TFTotal extends ClassicSimilarity {

	IndexReader indexR;
	
	public TFTotal() {
		super();
		 
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return 1.0f;
	}
	
	public float tf(float freq) {
		return freq;
	}
	
}
