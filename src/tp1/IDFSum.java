package tp1;

/**
*
* @author jlovonm
*/

import org.apache.lucene.search.similarities.ClassicSimilarity;

public class IDFSum extends ClassicSimilarity{

	public IDFSum() {
		super();
		 
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return (float) - Math.log( docFreq / docCount ) ;
	}
	
	public float tf(float freq) {
		return 1.0f;
	}
	
	
}