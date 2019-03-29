package com.cidacs.rl.search;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordModel;
import org.apache.commons.csv.CSVRecord;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Indexing {
    ConfigModel config;
    IndexWriter inWriter;

    public Indexing(ConfigModel config) {
        this.config = config;
    }

    public void index(Iterable<CSVRecord> csvRecords){
        RecordModel tmpRecordModel;

        Path dbIndexPath = Paths.get(this.config.getDbIndex());

        if (Files.exists(dbIndexPath)){
            System.out.println("There is a database already indexed with the name provided.");
        } else {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            Directory index = null;
            try {
                index = FSDirectory.open(dbIndexPath);
                this.inWriter = new IndexWriter(index, config);

                for (CSVRecord csvRecord : csvRecords) {
                    tmpRecordModel = this.fromCSVRecordToRecord(csvRecord);
                    this.addRecordToIndex(tmpRecordModel);
                }

                this.inWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addRecordToIndex (RecordModel record){
        Document doc = new Document();
        for(ColumnRecordModel column: record.getColumnRecordModels()){
            doc.add(new TextField(column.getId(), column.getValue(),  Field.Store.YES));
        }
        try {
            this.inWriter.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RecordModel fromCSVRecordToRecord(CSVRecord csvRecord){
        ColumnRecordModel tmpRecordColumnRecord;
        String tmpIndex;
        String tmpValue;
        String tmpId;
        String tmpType;
        ArrayList<ColumnRecordModel> tmpRecordColumns;

        tmpRecordColumns = new ArrayList<ColumnRecordModel>();
        for(ColumnConfigModel column : config.getColumns()){
            tmpIndex = column.getIndexA();
            tmpValue = csvRecord.get(tmpIndex).replaceAll("[^A-Z0-9 ]", "").replaceAll("\\s+", " ");
            if(tmpValue.equals(" ")){
                tmpValue = "";
            }
            tmpId = column.getId();
            tmpType = column.getType();

            tmpRecordColumnRecord = new ColumnRecordModel(tmpId, tmpType, tmpValue);
            tmpRecordColumns.add(tmpRecordColumnRecord);
        }
        RecordModel recordModel = new RecordModel(tmpRecordColumns);
        return recordModel;
    }
}
