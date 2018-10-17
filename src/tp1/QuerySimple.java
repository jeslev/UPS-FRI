/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;


/**
 *
 * @author jgmorenof
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class QuerySimple {

    String filename;
    String titleString;
    String indexPath;
    int indexSimilarity;

    public QuerySimple(String indexPath) {
        this.indexPath = indexPath;
        this.indexSimilarity = -1;
    }

    public QuerySimple(String indexPath, int i) {
        this.indexPath = indexPath;
        this.indexSimilarity = i;
    }
    
 //   public void process(String querystr1, String querystr2, String querystr3, String query4) throws IOException, ParseException {
  public void process(String term, String query, int count, PrintWriter writer) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Path path = new File(indexPath).toPath();
        Directory index = FSDirectory.open(path);

        Query q = new QueryParser( "text", analyzer).parse(query);
        
        Query q1 = new QueryParser( "text", analyzer).parse(term);
   
        BooleanQuery bq = new BooleanQuery.Builder()
        		.add(q1, BooleanClause.Occur.MUST)
        		.add(q, BooleanClause.Occur.MUST)
        		.build();
       
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new BM25Similarity());
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(bq, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println("Found " + hits.length + " hits of "+collector.getTotalHits()+".");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            float scoreId = hits[i].score;
            Document d = searcher.doc(docId);
            writer.println(count+ " Q0 " + d.get("title").replaceAll(" ", "_")+" "+(i+1)+" "+scoreId+" LOVON" );
        }

        reader.close();
    }
    
    public Similarity choisirSimilarity() {
    	Similarity sim = null; 
    	switch (indexSimilarity) {
    	case 0:
			sim =  new TFTotal();
			break;
    	case 1:
    		sim =  new TFMax();
			break;
		case 2:
			sim =  new TFSum();
			break;
		case 3:
			sim =  new TFLog();
			break;
		case 4:
			sim =  new TFFrac(1.2f);
			break;
		case 5:
			sim =  new TFBM25();
			break;
		case 6:
			sim =  new IDFTotal();
			break;
		case 7:
			sim =  new IDFSum();
			break;
		case 8:
			sim =  new IDFSmooth();
			break;
		case 9:
			sim =  new IDFBir();
			break;
		case 10:
			sim =  new IDFBirSmooth();
			break;
		default:
			break;
		}
    	
    	return sim;
    }
   
}
