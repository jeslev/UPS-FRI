package tp1;

/**
*
* @author jlovonm
*/

import org.apache.lucene.search.similarities.ClassicSimilarity;

public class TFIDFLogBirSmooth extends ClassicSimilarity {

	
	
	@Override
	public float idf(long docFreq, long docCount) {
		return (float) - Math.log( (docFreq+0.5) / (docCount - docFreq + 1.0) ) ;
	}
	
	@Override
	public float tf(float freq) {
		return (float) Math.log(1.0+freq);
	}
	
}