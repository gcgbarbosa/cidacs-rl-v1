package buscador;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Buscador {
    String header[] = null;
    StandardAnalyzer analyzer = new StandardAnalyzer();
    Directory index = null;
    IndexSearcher searcher = null;
    QueryParser queryParser = null;
    IndexReader reader = null;
    TopScoreDocCollector collector = null;

    public Buscador(String baseIn, String[] header) throws IOException {
        this.index = FSDirectory.open(Paths.get(baseIn));
        this.reader = DirectoryReader.open(this.index);
        this.header = header;
    }

    public ScoreDoc[] buscar(String busca, int hits) throws IOException, ParseException {
        this.searcher = new IndexSearcher(reader);
        this.collector = TopScoreDocCollector.create(hits);
        this.queryParser = new QueryParser("<default field>", this.analyzer);
        this.searcher.search(this.queryParser.parse(busca), this.collector);

        return this.collector.topDocs().scoreDocs;
    }

    public ArrayList<String[]> scoreDocToStr(ScoreDoc[] found) throws IOException {
        Document d = null;
        String[] s = null;
        ArrayList<String[]> result = new ArrayList<String[]>();
        int j;
        for (int i = 0; i < found.length; i++) {
            int docId = found[i].doc;
            d = this.searcher.doc(docId);
            s = new String[this.header.length + 1];
            j = 0;
            for (String head : this.header) {
                s[j++] = d.get(head);
            }
            // ADICIONAR SCORE DO LUCENE
            s[j++] = String.valueOf(found[i].score);
            result.add(s);
        }
        return result;
    }

    public String addTermsToString(String[] str, String term) {
        String result = "";
        for (String word : str) {
            result = result + word + term + " ";
        }
        return result.substring(0, result.length());
    }
}
