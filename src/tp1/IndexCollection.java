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
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import tp1.BM11Similarity;
import tp1.TFLog;
import tp1.TFMax;
import tp1.TFSum;
import tp1.TFTotal;

/**
 *
 * @author moreno
 */
public class IndexCollection {

    String filename;
    String titleString;
    String indexPath;
    IndexWriter writer;

    public IndexCollection(String filename, String indexPath) {
        this.filename = filename;
        this.indexPath = indexPath;
    }

    public void index() throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            System.err.println("Filename " + filename + " does not exist");
            return;
        }
        process();
    }

    private void indexDoc(String title) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        writer.addDocument(doc);
    }

    public void process() throws IOException {
        Path path = new File(indexPath).toPath();
        Directory dir = FSDirectory.open(path);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        
        iwc.setSimilarity(new TFLog());
        boolean create = true;
        if (create) {
            iwc.setOpenMode(OpenMode.CREATE);
        } else {
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }
        writer = new IndexWriter(dir, iwc);
        try {
            CSVParser csvFileParser = CSVFormat.DEFAULT.parse(new FileReader(new File(filename)));
            for (CSVRecord csvRecord : csvFileParser) {
                indexDoc(csvRecord.get(1));
            }
        } catch (IOException e) {
            Logger.getLogger(IndexCollection.class.getName()).log(Level.SEVERE, null, e);
        } finally{
            writer.close();
        }
    }
}
