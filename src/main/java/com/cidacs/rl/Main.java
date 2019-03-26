package com.cidacs.rl;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.io.CsvReader;
import com.cidacs.rl.linkage.Linkage;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordModel;
import com.cidacs.rl.record.RecordPairModel;
import com.cidacs.rl.search.Indexing;
import org.apache.commons.csv.CSVRecord;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("cidacs-rl").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        ConfigModel config = new ConfigModel();
        CsvReader csvReader = new CsvReader();
        Indexing indexing = new Indexing(config);
        //Searching searching = new Searching(config);
        Linkage linkage = new Linkage(config);

        RecordModel testRecord;
        RecordPairModel testPair;

        // read database A
        Iterable<CSVRecord> dbACsvRecords;
        dbACsvRecords = csvReader.getCsvIterable(config.getDbA());
        indexing.index(dbACsvRecords);


        // read database B
        //Iterable<CSVRecord> dbBCsvRecords;
        //dbBCsvRecords = csvReader.getCsvIterable(config.getDbB());
        //linkage.link(dbBCsvRecords);


        JavaRDD<String> distFile = sc.textFile(config.getDbB(), 448);

        distFile.map(new Function<String, String>() {
            public String call(String stringCsv) {
                ConfigModel config = new ConfigModel();
                Linkage linkage = new Linkage(config);

                int tmpIndex;
                String tmpValue;
                String tmpId;
                String tmpType;

                // quebrar a string csv
                String tmp[];
                tmp = stringCsv.split(",");

                // as vezes a ultima camada esta vazia e ele nao pega o numero certo de colunas
                // tive que dar esse armengue
                ArrayList<String> tmpArr = new ArrayList<>();
                for(String t: tmp){
                    tmpArr.add(t);
                }
                if(tmpArr.size()==6){
                    tmpArr.add("");
                    tmpArr.add("");
                    System.out.println("Linha com menos 2 colunas? "+ stringCsv);
                }
                if(tmpArr.size()==7){
                    tmpArr.add("");
                }
                RecordModel tmpRecord = new RecordModel();
                ArrayList<ColumnRecordModel> tmpRecordColumns = new ArrayList<ColumnRecordModel>();


                for(ColumnConfigModel column : config.getColumns()){
                    tmpIndex = Integer.parseInt(column.getIndexB());
                    try {
                        tmpValue = tmpArr.get(tmpIndex).replaceAll("[^A-Z0-9 ]", "").replaceAll("\\s+", " ");
                        if(tmpValue.equals(" ")){
                            tmpValue = "";
                        }
                        tmpId = column.getId();
                        tmpType = column.getType();
                        tmpRecordColumns.add(new ColumnRecordModel(tmpId, tmpType, tmpValue));
                    } catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
                tmpRecord.setColumnRecordModels(tmpRecordColumns);


                return linkage.linkSpark(tmpRecord);
            }
        }).saveAsTextFile("assets/result");



        //System.out.println("dak;sa;s'á(sASDHKA)SDJASD di123 ASHJDIASHD asdasd".replaceAll("[^A-Z0-9 ]", ""));
        // read database B
        //testRecord = searching.searchCandidateRecordsFromStrQuery("name: JOSE",10).get(0);
        //System.out.println(testRecord);
        //testPair = searching.getCandidatePairFromRecord(testRecord);
        //System.out.println(testPair.getRecordA());
        //System.out.println(testPair.getRecordB());

    }
}
