package tp1;

import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
*
* @author jlovonm
*/



public class IDFTotal extends ClassicSimilarity{

	public IDFTotal() {
		super();
		 
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return (float) - Math.log( docFreq ) ;
	}
	
	public float tf(float freq) {
		return 1.0f;
	}
	
	
}
