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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class QuerySimple {

    String filename;
    String titleString;
    String indexPath;

    public QuerySimple(String indexPath) {
        this.indexPath = indexPath;
    }

    public void process(String querystr) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Path path = new File(indexPath).toPath();
        Directory index = FSDirectory.open(path);

        Query q = new QueryParser( "title", analyzer).parse(querystr);

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new TFLog());
        
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
}
