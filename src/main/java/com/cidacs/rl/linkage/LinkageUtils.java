package com.cidacs.rl.linkage;

import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordPairModel;

public class LinkageUtils {
    public String fromRecordPairToCsv(RecordPairModel recordPair){
        String csvResult = "";
        for(ColumnRecordModel column: recordPair.getRecordA().getColumnRecordModels()){
            csvResult=csvResult+column.getValue()+";";
        }
        for(ColumnRecordModel column: recordPair.getRecordB().getColumnRecordModels()){
            csvResult=csvResult+column.getValue()+";";
        }
        csvResult = csvResult + recordPair.getScore();
        return csvResult;
    }

    public String getCsvHeaderFromRecordPair(RecordPairModel recordPair){
        String headerResult = "";
        for(ColumnRecordModel column: recordPair.getRecordA().getColumnRecordModels()){
            headerResult=headerResult+column.getId()+"_dba;";
        }
        for(ColumnRecordModel column: recordPair.getRecordB().getColumnRecordModels()){
            headerResult=headerResult+column.getId()+"_dbb;";
        }
        headerResult = headerResult + "score";
        return headerResult;
    }
}
