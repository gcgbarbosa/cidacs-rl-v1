package com.cidacs.rl.search;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordComparator;
import com.cidacs.rl.record.RecordModel;
import com.cidacs.rl.record.RecordPairModel;
import com.cidacs.rl.util.Permutation;
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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Searching {
    private StandardAnalyzer analyzer = new StandardAnalyzer();
    private Directory index;
    private IndexSearcher searcher;
    private QueryParser queryParser;
    private IndexReader reader;
    private TopScoreDocCollector collector;
    private ConfigModel config;
    private SearchingUtils seachingUtils;
    private Permutation permutation;

    public Searching(ConfigModel config) {
        this.config = config;
        seachingUtils = new SearchingUtils();
        permutation = new Permutation();

        try {
            this.index = FSDirectory.open(Paths.get(config.getDbIndex()));
            this.reader = DirectoryReader.open(this.index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RecordPairModel getCandidatePairFromRecord(RecordModel record){
        int HITS = 100;
        String strBusca;
        ArrayList<RecordModel> tmpCandidates;
        final ArrayList<ColumnRecordModel> filteredColumns;
        ArrayList<ColumnRecordModel> tmpColumns;
        RecordComparator recordComparator = new RecordComparator(this.config);
        RecordPairModel tmpCandidate;

        filteredColumns = this.seachingUtils.filterUnusedColumns(record.getColumnRecordModels());
        // filter unused columns
        String recordId = record.getColumnRecordModels().get(0).getValue();


        // FASE 1
        strBusca = this.seachingUtils.getStrQueryExact(filteredColumns);
        // DO THE QUERY
        tmpCandidates = this.searchCandidateRecordsFromStrQuery(strBusca, HITS, recordId);
        // IF ANY RESULT WAS FOUND
        if(tmpCandidates.isEmpty() == false){
            tmpCandidate = recordComparator.findBestCandidatePair(record,tmpCandidates);

            if(tmpCandidate.getScore() >= 0.95) {
                return tmpCandidate;
            }
        }


        // FASE 2
        tmpCandidates = new ArrayList<RecordModel>();
        //tmpCandidates.add(candidate);
        // GENERATE POSSIBLE COMBINATIONS
        ArrayList<ArrayList<Integer>> combinacoes = this.permutation.combine(filteredColumns.size(), filteredColumns.size()-1);
        // DO EACH SEARCH BASED ON COMBINATIONS GENERATED
        for(ArrayList<Integer> combinacao : combinacoes){
            // seleciona as colunas a partir da função de combinação
            tmpColumns = this.permutation.getPermutationsOfRecordColumns(filteredColumns, combinacao);
            // constrói a string de busca
            strBusca = this.seachingUtils.getStrQueryExact(tmpColumns);
            // add resultadoss
            tmpCandidates.addAll(this.searchCandidateRecordsFromStrQuery(strBusca, HITS, recordId));

        }
        if(tmpCandidates.isEmpty() == false){
            tmpCandidate = recordComparator.findBestCandidatePair(record,tmpCandidates);

            if(tmpCandidate.getScore() >= 0.95) {
                return tmpCandidate;
            }
        }

        // FASE 3
        strBusca = this.seachingUtils.getStrQueryFuzzy(filteredColumns);
        // DO THE QUERY
        tmpCandidates = this.searchCandidateRecordsFromStrQuery(strBusca, HITS, recordId);
        // IF ANY RESULT WAS FOUND
        if(tmpCandidates.isEmpty() == false){
            tmpCandidate = recordComparator.findBestCandidatePair(record,tmpCandidates);
            return tmpCandidate;
        }

        RecordPairModel tmp;
        return null;
    }

    public ArrayList<RecordModel> searchCandidateRecordsFromStrQuery(String busca, int hits, String idCandidate){
        int tmpDocId;
        Document tmpDocument;
        ArrayList<RecordModel> recordsFound;
        ScoreDoc[] tmpScoreDocs;

        recordsFound = new ArrayList<RecordModel>();
        String tmp;
        RecordModel tmpRecordModel;

        this.searcher = new IndexSearcher(reader);
        this.collector = TopScoreDocCollector.create(hits);
        this.queryParser = new QueryParser("<default field>", this.analyzer);
        try {
            this.searcher.search(this.queryParser.parse(busca), this.collector);
            //
            tmpScoreDocs = this.collector.topDocs().scoreDocs;

            if(tmpScoreDocs.length > 0) {
                for(int i=0; i< tmpScoreDocs.length; i++){
                    tmpDocId = tmpScoreDocs[i].doc;

                    tmpDocument = this.searcher.doc(tmpDocId);

                    // for debugging
                    //tmp = this.fromLuceneDocumentoToRecord(tmpDocument).toString();
                    //System.out.println(tmp);

                    tmpRecordModel = this.fromLuceneDocumentToRecord(tmpDocument);
                    this.seachingUtils.getStrQueryFuzzy(tmpRecordModel.getColumnRecordModels());
                    this.seachingUtils.getStrQueryExact(tmpRecordModel.getColumnRecordModels());

                    recordsFound.add(tmpRecordModel);
                }
            }

        } catch (IOException e){
            System.out.println("Row "+idCandidate+" could not be linked.");
        } catch (ParseException e) {
            System.out.println("Row "+idCandidate+" could not be linked.");
        }

        return recordsFound;

    }

    private RecordModel fromLuceneDocumentToRecord(Document document){
        ColumnRecordModel tmpRecordColumnRecord;
        String tmpValue;
        String tmpId;
        String tmpType;
        ArrayList tmpRecordColumns;

        tmpRecordColumns = new ArrayList<ColumnRecordModel>();
        for(ColumnConfigModel column : this.config.getColumns()){
            tmpId = column.getId();
            tmpValue = document.get(tmpId);
            tmpType = column.getType();
            tmpRecordColumnRecord = new ColumnRecordModel(tmpId, tmpType, tmpValue);
            tmpRecordColumns.add(tmpRecordColumnRecord);
        }
        RecordModel recordModel = new RecordModel(tmpRecordColumns);
        return recordModel;
    }
}
