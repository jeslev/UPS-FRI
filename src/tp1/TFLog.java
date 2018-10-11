package tp1;

import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
*
* @author jlovonm
*/

public class TFLog extends ClassicSimilarity {

	
	
	@Override
	public float idf(long docFreq, long docCount) {
		return 1.0f;
	}
	
	@Override
	public float tf(float freq) {
		return (float) Math.log(1.0+freq);
	}
	
}
