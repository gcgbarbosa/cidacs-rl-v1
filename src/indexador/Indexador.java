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
import java.util.HashMap;
import java.util.Map.Entry;

public class Indexador {
    private String baseIn;
    private String baseOut;

    private String[] header = null;

    public Indexador(String baseIn, String baseOut, String[] header) {
        this.baseIn = baseIn;
        this.baseOut = baseOut;
        this.header = header;
    }

    private static void addColumnsToIndex(IndexWriter inWriter, HashMap<String, String> fields) throws IOException {
        Document doc = new Document();
        for (Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            doc.add(new TextField(key, value, Field.Store.YES));
        }
        inWriter.addDocument(doc);
    }

    public void readBase() throws Exception {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = FSDirectory.open(Paths.get(this.baseOut));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter inWriter = new IndexWriter(index, config);

        FileReader fr = new FileReader(this.baseIn);
        BufferedReader br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            s = s.replaceAll("\"", "");
            String columns[] = s.split(";");
            Indexador.addColumnsToIndex(inWriter, strToMap(columns));
        }
        fr.close();
        inWriter.close();
    }

    private HashMap<String, String> strToMap(String[] c) {
        HashMap<String, String> fields = new HashMap<String, String>();
        int i = 0;
        for (String head : this.header) {
            fields.put(head, c[i]);
            i++;
        }
        return fields;
    }
}
