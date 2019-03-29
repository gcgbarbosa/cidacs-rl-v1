package com.cidacs.rl.linkage;

import com.cidacs.rl.config.ColumnConfigModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordModel;
import com.cidacs.rl.record.RecordPairModel;
import com.cidacs.rl.search.Searching;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Linkage {
    private ConfigModel config;

    public Linkage(ConfigModel config) {
        this.config = config;
    }

    public String linkSpark(RecordModel record) {
        Searching searching = new Searching(this.config);
        LinkageUtils linkageUtils = new LinkageUtils();
        RecordPairModel testPair;
        testPair = searching.getCandidatePairFromRecord(record);
        try {
            return linkageUtils.fromRecordPairToCsv(testPair);
        } catch (NullPointerException e) {
            System.out.println("Row " + record.getColumnRecordModels().get(0).getValue() + " could not be linked.");
        }
        return "";
    }

    public void link(Iterable<CSVRecord> csvRecords) {
        RecordModel tmpRecordModel;
        Searching searching = new Searching(this.config);
        LinkageUtils linkageUtils = new LinkageUtils();

        String resultPath = "assets/result_a_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv";

        RecordPairModel testPair;

        Path path = Paths.get(resultPath);

        boolean isHeaderPrinted=false;

        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CSVRecord csvRecord : csvRecords) {
            tmpRecordModel = this.fromCSVRecordToRecord(csvRecord);
            testPair = searching.getCandidatePairFromRecord(tmpRecordModel);
            try {
                if (isHeaderPrinted == false) {
                    isHeaderPrinted = true;
                    writer.write(linkageUtils.getCsvHeaderFromRecordPair(testPair) + "\n");
                }
                writer.write(linkageUtils.fromRecordPairToCsv(testPair) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Row "+tmpRecordModel.getColumnRecordModels().get(0).getValue()+" could not be linked.");
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //System.out.println(testPair.getRecordA());
        //System.out.println(testPair.getRecordB());
        //System.out.println(testPair.getScore());
        //System.out.println("#######");
        //System.out.println(tmpRecordModel.getColumnRecordModels().get(4).getValue());

    }

    public RecordModel fromCSVRecordToRecord(CSVRecord csvRecord){
        ColumnRecordModel tmpRecordColumnRecord;
        String tmpIndex;
        String tmpValue;
        String tmpId;
        String tmpType;
        ArrayList<ColumnRecordModel> tmpRecordColumns;

        tmpRecordColumns = new ArrayList<ColumnRecordModel>();
        for(ColumnConfigModel column : config.getColumns()){
            tmpIndex = column.getIndexB();
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
