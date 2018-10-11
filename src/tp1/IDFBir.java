package tp1;

/**
*
* @author jlovonm
*/

import org.apache.lucene.search.similarities.ClassicSimilarity;

public class IDFBir extends ClassicSimilarity{

	public IDFBir() {
		super();
		 
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return (float) - Math.log( docFreq / (docCount - docFreq) ) ;
	}
	
	public float tf(float freq) {
		return 1.0f;
	}
	
	
}