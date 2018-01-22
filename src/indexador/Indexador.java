package indexador;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import util.ConfigLoader;
import util.Column;

public class Indexador {
    private ConfigLoader opts;

    public Indexador(ConfigLoader opts) {
        this.opts = opts;
    }

    private static void addColumnsToIndex(String fields[], ArrayList<Column> columns, IndexWriter inWriter) throws IOException {
        Document doc = new Document();
        for(Column c: columns){
            doc.add(new TextField(c.getName(), fields[c.getColumn()],  Field.Store.YES));
        }
        inWriter.addDocument(doc);
    }

    public void indexar() throws Exception {
        // setup for lucene index
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = FSDirectory.open(Paths.get(this.opts.getBaseMaiorIndexada()));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter inWriter = new IndexWriter(index, config);
        // setup read files
        FileReader fr = new FileReader(this.opts.getBaseMaior());
        BufferedReader br = new BufferedReader(fr);
        String tmp;
        String fields[];
        while ((tmp = br.readLine()) != null) {
            tmp = tmp.replaceAll("\"", "");
            fields = tmp.split(";");
            Indexador.addColumnsToIndex(fields, this.opts.getColumns(), inWriter);
        }
        fr.close();
        inWriter.close();
    }
}
