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
import java.nio.file.Path;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
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
    
    public void process(String querystr) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Path path = new File(indexPath).toPath();
        Directory index = FSDirectory.open(path);

        Query q = new QueryParser( "title", analyzer).parse(querystr);

        //int hitsPerPage = 10;
        int hitsPerPage = 5;
        IndexReader reader = DirectoryReader.open(index);
        
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new TFIDFLogBirSmooth());
        //searcher.setSimilarity(choisirSimilarity());
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println("Found " + hits.length + " hits of "+collector.getTotalHits()+".");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") );
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
