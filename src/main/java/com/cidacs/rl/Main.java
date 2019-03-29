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
        // Reading config
        ConfigReader confReader = new ConfigReader();
        ConfigModel config = confReader.readConfig();
        
        // Declare a CsvReader for indexing the smaller database
        CsvReader csvReader = new CsvReader();
        Indexing indexing = new Indexing(config);

        // read database A using CSV
        Iterable<CSVRecord> dbACsvRecords;
        dbACsvRecords = csvReader.getCsvIterable(config.getDbA());
        indexing.index(dbACsvRecords);

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

        dsb.javaRDD().map(new Function<Row, String>() {
            public String call(Row row){
                // Reading config
                ConfigReader confReader = new ConfigReader();
                ConfigModel config = confReader.readConfig();

                Linkage linkage = new Linkage(config);

                RecordModel tmpRecord = new RecordModel();
                ArrayList<ColumnRecordModel> tmpRecordColumns = new ArrayList<ColumnRecordModel>();
                
                for(ColumnConfigModel column : config.getColumns()){
                    try {
                        String tmpValue = row.getAs(column.getIndexB());
                        tmpValue = tmpValue.replaceAll("[^A-Z0-9 ]", "").replaceAll("\\s+", " ");
                        if(tmpValue.equals(" ")){
                            tmpValue = "";
                        }
                        String tmpId = column.getId();
                        String tmpType = column.getType();
                        tmpRecordColumns.add(new ColumnRecordModel(tmpId, tmpType, tmpValue));
                    } catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
                tmpRecord.setColumnRecordModels(tmpRecordColumns);


                return linkage.linkSpark(tmpRecord);
            }
        }).saveAsTextFile("assets/result");

        spark.stop();
    }
}
