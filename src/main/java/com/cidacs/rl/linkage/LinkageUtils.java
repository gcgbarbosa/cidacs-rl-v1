package com.cidacs.rl.linkage;

import com.cidacs.rl.record.ColumnRecordModel;
import com.cidacs.rl.record.RecordPairModel;
import com.cidacs.rl.config.ConfigModel;
import com.cidacs.rl.config.ColumnConfigModel;

public class LinkageUtils {
    public String fromRecordPairToCsv(RecordPairModel recordPair){
        String csvResult = "";
        for(ColumnRecordModel column: recordPair.getRecordA().getColumnRecordModels()){
            csvResult=csvResult+column.getValue()+",";
        }
        for(ColumnRecordModel column: recordPair.getRecordB().getColumnRecordModels()){
            csvResult=csvResult+column.getValue()+",";
        }
        csvResult = csvResult + recordPair.getScore();
        return csvResult;
    }

    public String getCsvHeaderFromRecordPair(RecordPairModel recordPair){
        String headerResult = "";
        for(ColumnRecordModel column: recordPair.getRecordA().getColumnRecordModels()){
            headerResult=headerResult+column.getId()+"_dsa,";
        }
        for(ColumnRecordModel column: recordPair.getRecordB().getColumnRecordModels()){
            headerResult=headerResult+column.getId()+"_dsb,";
        }
        headerResult = headerResult + "score";
        return headerResult;
    }

    public String getCsvHeaderFromConfig(ConfigModel config){
        String headerResult = "";
        // for each column a add to result
        for (ColumnConfigModel col: config.getColumns()){
            headerResult = headerResult + col.getIndexA()+"_dsa,";
        }
        // for each column b add to result
        for (ColumnConfigModel col: config.getColumns()){
            headerResult = headerResult + col.getIndexB()+"_dsb,";
        }
        headerResult = headerResult.substring(0, headerResult.length()-1);
        return headerResult;
    }
}
