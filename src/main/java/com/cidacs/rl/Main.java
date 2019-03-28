package com.cidacs.rl;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.config.ConfigReader;
import com.cidacs.rl.io.CsvReader;
import com.cidacs.rl.linkage.Linkage;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordModel;
import com.cidacs.rl.search.Indexing;
import org.apache.commons.csv.CSVRecord;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import scala.reflect.ClassManifestFactory;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
        .builder()
        .appName("Cidacs-RL")
        .config("spark.master", "local[*]")
        .getOrCreate();

        // read dataset
        // assets/dsa.csv
        Dataset<Row> dsb = spark.read().format("csv")
            .option("sep", ",")
            .option("inferSchema", "false")
            .option("header", "true")
            .load("assets/dsb.csv");


        System.out.println(
            dsb.javaRDD().map(new Function<Row, String>() {
                public String call(Row row){
                    
                    return row.getAs("name");
                }
            }).take(10) );

        System.out.println("DS:");
        dsb.show();

        spark.stop();

        /*
        // Spark
        SparkConf conf = new SparkConf().setAppName("cidacs-rl").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        // Reading config
        ConfigReader confReader = new ConfigReader();
        ConfigModel config = confReader.readConfig();

        CsvReader csvReader = new CsvReader();
        Indexing indexing = new Indexing(config);

        //Searching searching = new Searching(config);
        //Linkage linkage = new Linkage(config);

        //RecordModel testRecord;
        //RecordPairModel testPair;

        // read database A using CSV
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
                // Reading config
                ConfigReader confReader = new ConfigReader();
                ConfigModel config = confReader.readConfig();

                Linkage linkage = new Linkage(config);

                int tmpIndex;
                String tmpValue;
                String tmpId;
                String tmpType;

                // quebrar a string csv
                String tmp[];
                tmp = stringCsv.split(",");

                RecordModel tmpRecord = new RecordModel();
                ArrayList<ColumnRecordModel> tmpRecordColumns = new ArrayList<ColumnRecordModel>();
                

                for(ColumnConfigModel column : config.getColumns()){
                    tmpIndex = Integer.parseInt(column.getIndexB());
                    try {
                        tmpValue = tmp[tmpIndex].replaceAll("[^A-Z0-9 ]", "").replaceAll("\\s+", " ");
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

        sc.close();
        */
    }
}
