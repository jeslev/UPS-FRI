package tp1;

/**
*
* @author jlovonm
*/

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

import tp1.TFMax.IDFStats;

public class TFMax extends Similarity {
	
	/** Cache of decoded bytes. */
	static final float[] OLD_NORM_TABLE = new float[256];

	static {
		for (int i = 0; i < 256; i++) {
			OLD_NORM_TABLE[i] = SmallFloat.byte315ToFloat((byte)i);
			}
		}
	
	
	public TFMax() {}
	
	
	protected boolean discountOverlaps = true;

	public boolean getDiscountOverlaps() {
		return discountOverlaps;
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
	  
	public float tf(float freq, float nt_max) {
		return freq / nt_max;
	}
	
	@Override
	public final long computeNorm(FieldInvertState state) {
		final int numTerms = discountOverlaps ? state.getLength() - state.getNumOverlap() : state.getLength();
		
		
		// Mettre le term n(t_max,d) pendant l'indexation
		int tf_max_d = state.getMaxTermFrequency();
		
	    int indexCreatedVersionMajor = state.getIndexCreatedVersionMajor();
	    if (indexCreatedVersionMajor >= 7) {
	    	return SmallFloat.intToByte4(numTerms) *  tf_max_d;
	    } else {
	    	return SmallFloat.floatToByte315((float) (1 / ( Math.sqrt(numTerms)*tf_max_d )  ));
	    }
	 }
	  
	public void setDiscountOverlaps(boolean v) {
		discountOverlaps = v;
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
		 for (int i = 1; i < 256; ++i) {
			 int length = SmallFloat.byte4ToInt((byte) i);
			 //float norm = (float) (1.0 / Math.sqrt(length));
			float norm = (float) (1.0 * length);
			 normTable[i] = norm;
		 }
		 normTable[0] = 1f / normTable[255];
		 return new IDFStats(collectionStats.field(), boost, idf, normTable);
	 }
	 
	 
	 @Override
	 public final SimScorer simScorer(SimWeight stats, LeafReaderContext context) throws IOException {
		 IDFStats idfstats = (IDFStats) stats;
		 final float[] normTable;
		 if (context.reader().getMetaData().getCreatedVersionMajor() >= 7) {
	     // the norms only encode the length, we need a translation table that depends on how lengthNorm is implemented
			 normTable = idfstats.normTable;
		 } else {
			 // the norm is directly encoded in the index
			 normTable = OLD_NORM_TABLE;
		}
		 
		return new TFIDFSimScorer(idfstats, context.reader().getNormValues(idfstats.field), normTable);
	 }
	  
	 private final class TFIDFSimScorer extends SimScorer {
		 private final IDFStats stats;
		 private final float weightValue;
		 private final NumericDocValues norms;
		 private final float[] normTable;
		 
		 
		 TFIDFSimScorer(IDFStats stats, NumericDocValues norms, float[] normTable) throws IOException {
			 this.stats = stats;
		     this.weightValue = stats.queryWeight;
		     this.norms = norms;
		     this.normTable = normTable;
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
		    		 normValue = normTable[(int) (norms.longValue() & 0xFF)];
		    	 } else {
		    		 normValue = 0;
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
			 return explainScore(doc, freq, stats, norms, normTable);
		 }
	 }
	  
	  
	 static class IDFStats extends SimWeight {
		 private final String field;
		 /** The idf and its explanation */
		 private final Explanation idf;
		 private final float boost;
		 private final float queryWeight;
		 final float[] normTable;
		    
		 public IDFStats(String field, float boost, Explanation idf, float[] normTable) {
			 // TODO: Validate?
		     this.field = field;
		     this.idf = idf;
		     this.boost = boost;
		     this.queryWeight = boost * idf.getValue();
		     this.normTable = normTable;
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
