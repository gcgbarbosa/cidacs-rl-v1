package com.cidacs.rl;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.config.ConfigReader;
import com.cidacs.rl.io.CsvHandler;
import com.cidacs.rl.linkage.Linkage;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordModel;
import com.cidacs.rl.search.Indexing;
import org.apache.commons.csv.CSVRecord;

import org.apache.spark.api.java.function.Function;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        // Reading config
        ConfigReader confReader = new ConfigReader();
        ConfigModel config = confReader.readConfig();
        
        // Declare a CsvReader for indexing the smaller database
        CsvHandler csvHandler = new CsvHandler();
        Indexing indexing = new Indexing(config);

        // read database A using CSV
        Iterable<CSVRecord> dbACsvRecords;
        dbACsvRecords = csvHandler.getCsvIterable(config.getDbA());
        indexing.index(dbACsvRecords);

        // Start Spark session
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
            .load(config.getDbB());

        dsb.javaRDD().map(new Function<Row, String>() {
            public String call(Row row){
                // Reading config again
                // Using the config from outer scope throws java not serializable error
                ConfigReader confReader = new ConfigReader();
                ConfigModel config = confReader.readConfig();
                // same with linkage
                Linkage linkage = new Linkage(config);

                // place holder variables to instanciate an record object
                RecordModel tmpRecord = new RecordModel();
                ArrayList<ColumnRecordModel> tmpRecordColumns = new ArrayList<ColumnRecordModel>();
                
                // convert row to RecordModel
                for(ColumnConfigModel column : config.getColumns()){
                    try {
                        String tmpValue = row.getAs(column.getIndexB());
                        // Remove anything that is not a uppercase letter and a digit
                        tmpValue = tmpValue.replaceAll("[^A-Z0-9 ]", "").replaceAll("\\s+", " ");
                        // if the value is equal to one space, add empty string instead
                        if(tmpValue.equals(" ")){
                            tmpValue = "";
                        }
                        //
                        String tmpId = column.getId();
                        // maybe it is not necessary to have the tipe of the variable replicated
                        // FIXME: add function to config that allows for consulting the type of the variable
                        // using the ID
                        String tmpType = column.getType();
                        // add new column 
                        tmpRecordColumns.add(new ColumnRecordModel(tmpId, tmpType, tmpValue));
                    } catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
                // set the column to record
                tmpRecord.setColumnRecordModels(tmpRecordColumns);
                //
                return linkage.linkSpark(tmpRecord);
            }
            private static final long serialVersionUID = 1L;
        }).saveAsTextFile("assets/result_" + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Calendar.getInstance().getTime()));

        csvHandler.writeHeaderFromConfig("assets/header.csv", config);
        //
        // Write header to file
        spark.stop();
    }
}
