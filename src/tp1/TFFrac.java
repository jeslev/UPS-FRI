package tp1;

/**
*
* @author jlovonm
*/


import org.apache.lucene.search.similarities.ClassicSimilarity;

public class TFFrac extends  ClassicSimilarity {

	public float k;
	
	public TFFrac(float k) {
		super();
		this.k = k;
	}
	
	@Override
	public float idf(long docFreq, long docCount) {
		return 1.0f;
	}
	
	@Override
	public float tf(float freq) {
		return (float) freq/(freq + this.k);
	}
	
}
