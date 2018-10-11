package tp1;

/**
*
* @author jlovonm
*/

import org.apache.lucene.search.similarities.ClassicSimilarity;

public class TFIDFTotalBIrSmooth extends ClassicSimilarity {
	
	public TFIDFTotalBIrSmooth() {
		super();
	 
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return (float) - Math.log( (docFreq+0.5) / (docCount - docFreq + 1.0) ) ;
	}
	
	public float tf(float freq) {
		return freq;
	}
	
}
