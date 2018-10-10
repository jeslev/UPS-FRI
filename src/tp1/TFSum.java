package tp1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.Similarity.SimScorer;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.SmallFloat;

import tp1.TFSum.IDFStats;

/**
*
* @author jlovonm
*/

public class TFSum extends Similarity {
	
	/** Cache of decoded bytes. */
	static final float[] OLD_NORM_TABLE = new float[256];

	static {
		for (int i = 0; i < 256; i++) {
			OLD_NORM_TABLE[i] = SmallFloat.byte315ToFloat((byte)i);
			}
		}
	
	
	public TFSum() {}
	
	protected boolean discountOverlaps = true;

	public void setDiscountOverlaps(boolean v) {
		discountOverlaps = v;
	}

	public boolean getDiscountOverlaps() {
		return discountOverlaps;
	}
	  
	 /** Cache of decoded bytes. */
	  private static final float[] OLD_LENGTH_TABLE = new float[256];
	  private static final float[] LENGTH_TABLE = new float[256];

	  static {
	    for (int i = 1; i < 256; i++) {
	      float f = SmallFloat.byte315ToFloat((byte)i);
	      OLD_LENGTH_TABLE[i] = 1.0f / (f*f);
	    }
	    OLD_LENGTH_TABLE[0] = 1.0f / OLD_LENGTH_TABLE[255]; // otherwise inf

	    for (int i = 0; i < 256; i++) {
	      LENGTH_TABLE[i] = SmallFloat.byte4ToInt((byte) i);
	    }
	  }
	
	/***********************************************************************
	/***********************************************************************
	 
	 Fonctions modifiées pour l'implementation:
	 
	 - IDF 
	 - TF 
	 - computerNorm
	 - computerWeigthed
	 - score
	  
	 ***********************************************************************
	 ***********************************************************************/
	
	protected float idf(long docFreq, long docCount) {
		return (float) 1.0;
	}
	  
	public float tf(float freq, float n_d) {
		return freq / n_d;
	}
	
	@Override
	public final long computeNorm(FieldInvertState state) {
		final int numTerms = discountOverlaps ? state.getLength() - state.getNumOverlap() : state.getLength();
		
		
	    int indexCreatedVersionMajor = state.getIndexCreatedVersionMajor();
	    if (indexCreatedVersionMajor >= 7) {
	    	return SmallFloat.intToByte4(numTerms);
	    } else {
	    	return SmallFloat.floatToByte315((float) (1 / ( Math.sqrt(numTerms) )  ));
	    }
	 }
	  
	  
	/** Implemented as <code>1 / (distance + 1)</code>. */
	public float sloppyFreq(int distance) {
		return 1.0f / (distance + 1);
	}
	  
	/** The default implementation returns <code>1</code> */
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		return 1;
	}
	  
	public Explanation idfExplain(CollectionStatistics collectionStats, TermStatistics termStats) {
		final long df = termStats.docFreq();
		final long docCount = collectionStats.docCount() == -1 ? collectionStats.maxDoc() : collectionStats.docCount();
		final float idf = idf(df, docCount);
		return Explanation.match(idf, "idf(docFreq=" + df + ", docCount=" + docCount + ")");
	}
	  
	public Explanation idfExplain(CollectionStatistics collectionStats, TermStatistics termStats[]) {
		double idf = 0d; // sum into a double before casting into a float
		List<Explanation> subs = new ArrayList<>();
		for (final TermStatistics stat : termStats ) {
			Explanation idfExplain = idfExplain(collectionStats, stat);
		    subs.add(idfExplain);
		    idf += idfExplain.getValue();
		}
		return Explanation.match((float) idf, "idf(), sum of:", subs);
	}
	  

	 @Override
	 public final SimWeight computeWeight(float boost, CollectionStatistics collectionStats, TermStatistics... termStats) {
		 final Explanation idf = termStats.length == 1?idfExplain(collectionStats, termStats[0]) : idfExplain(collectionStats, termStats);
		 float[] normTable = new float[256];
		 float[] oldnormTable = new float[256];
		 for (int i = 1; i < 256; ++i) {
			 int length = SmallFloat.byte4ToInt((byte) i);
			 //float norm = (float) (1.0 / Math.sqrt(length));
			 float norm = (float) (1.0 * length);
			 oldnormTable[i] = OLD_LENGTH_TABLE[i] ;
			 normTable[i] = LENGTH_TABLE[i] ;
		 }
		 normTable[0] = 1f / normTable[255];
		 return new IDFStats(collectionStats.field(), boost, idf, oldnormTable, normTable);
	 }
	 
	 
	 @Override
	 public final SimScorer simScorer(SimWeight stats, LeafReaderContext context) throws IOException {
		 IDFStats idfstats = (IDFStats) stats;
		 
		 
		return new TFIDFSimScorer(idfstats, context.reader().getMetaData().getCreatedVersionMajor(), context.reader().getNormValues(idfstats.field) );
	 }
	  
	 private final class TFIDFSimScorer extends SimScorer {
		 private final IDFStats stats;
		 private final float weightValue;
		 private final NumericDocValues norms;
		 private final float[] lengthCache;
		 private final float[] cache;
		 
		 
		 TFIDFSimScorer(IDFStats stats, int indexCreatedVersionMajor, NumericDocValues norms) throws IOException {
			 this.stats = stats;
		     this.weightValue = stats.queryWeight;
		     this.norms = norms;
		     if (indexCreatedVersionMajor >= 7) {
		         lengthCache = LENGTH_TABLE;
		         cache = stats.normTable;
		       } else {
		         lengthCache = OLD_LENGTH_TABLE;
		         cache = stats.oldnormTable;
		       }
		 }
		    
		 @Override
		 public float score(int doc, float freq) throws IOException {
			//final float raw = tf(freq) * weightValue; // compute tf(f)*weight
			 if (norms == null) {
				 final float raw = tf(freq,(float) 1.0) * weightValue;
				 return raw;
		     } else {
		    	 float normValue;
		    	 if (norms.advanceExact(doc)) {
		    		 normValue = cache[(int) (norms.longValue() & 0xFF)];
		    	 } else {
		    		 normValue = cache[0];
		    	 }
		    	 final float raw = tf(freq,normValue) * weightValue;
		    	 return raw ;  // 
		     }
		 }
		    
		 @Override
		 public float computeSlopFactor(int distance) {
			 return sloppyFreq(distance);
		 }

		 @Override
		 public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
			 return scorePayload(doc, start, end, payload);
		 }

		 @Override
		 public Explanation explain(int doc, Explanation freq) throws IOException {
			 return null; //explainScore(doc, freq, stats, norms, normTable);
		 }
	 }
	  
	  
	 static class IDFStats extends SimWeight {
		 private final String field;
		 /** The idf and its explanation */
		 private final Explanation idf;
		 private final float boost;
		 private final float queryWeight;
		 final float[] normTable, oldnormTable;
		    
		 public IDFStats(String field, float boost, Explanation idf, float[] normTable, float[] oldnormTable) {
			 // TODO: Validate?
		     this.field = field;
		     this.idf = idf;
		     this.boost = boost;
		     this.queryWeight = boost * idf.getValue();
		     this.normTable = normTable;
		     this.oldnormTable = oldnormTable;
		 }
	 }  

	 private Explanation explainField(int doc, Explanation freq, IDFStats stats, NumericDocValues norms, float[] normTable) throws IOException {
		 Explanation tfExplanation = Explanation.match(tf(freq.getValue(), (float) 1.0), "tf(freq="+freq.getValue()+"), with freq of:", freq);
		 float norm;
		 if (norms == null) {
			 norm = 1f;
			 
		 } else if (norms.advanceExact(doc) == false) {
			 norm = 0f;
		 } else {
			 norm = normTable[(int) (norms.longValue() & 0xFF)];
		 }
		    
		 Explanation fieldNormExpl = Explanation.match(
				 norm,
				 "fieldNorm(doc=" + doc + ")");

		 return Explanation.match(
				 tfExplanation.getValue() * stats.idf.getValue() * fieldNormExpl.getValue(),
				 "fieldWeight in " + doc + ", product of:",
				 tfExplanation, stats.idf, fieldNormExpl);
		  }

	 private Explanation explainScore(int doc, Explanation freq, IDFStats stats, NumericDocValues norms, float[] normTable) throws IOException {
		 Explanation queryExpl = Explanation.match(stats.boost, "boost");
		 Explanation fieldExpl = explainField(doc, freq, stats, norms, normTable);
		 if (stats.boost == 1f) {
			 return fieldExpl;
		 }
		 return Explanation.match(
				 queryExpl.getValue() * fieldExpl.getValue(),
				 "score(doc="+doc+",freq="+freq.getValue()+"), product of:",
				 queryExpl, fieldExpl);
		 }
}
