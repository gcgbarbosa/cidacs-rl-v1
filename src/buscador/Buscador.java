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

import util.Column;
import util.ConfigLoader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Buscador {
    StandardAnalyzer analyzer = new StandardAnalyzer();
    Directory index;
    IndexSearcher searcher;
    QueryParser queryParser;
    IndexReader reader;
    TopScoreDocCollector collector;
    ConfigLoader opts;

    public Buscador(ConfigLoader opts) throws IOException {
        this.opts = opts;
        this.index = FSDirectory.open(Paths.get(opts.getBaseMaiorIndexada()));
        this.reader = DirectoryReader.open(this.index);
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
            s = new String[this.opts.getColumns().size() + 1];
            j = 0;
            for (Column c : this.opts.getColumns()) {
                s[j++] = d.get(c.getName());
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
