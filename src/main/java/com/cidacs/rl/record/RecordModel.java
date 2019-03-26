package com.cidacs.rl.record;

import java.util.ArrayList;

public class RecordModel {

    private ArrayList<ColumnRecordModel> columnRecordModels;

    public ArrayList<ColumnRecordModel> getColumnRecordModels() {
        return columnRecordModels;
    }

    public void setColumnRecordModels(ArrayList<ColumnRecordModel> columnRecordModels) {
        this.columnRecordModels = columnRecordModels;
    }

    public RecordModel(ArrayList<ColumnRecordModel> columnRecordModels) {

        this.columnRecordModels = columnRecordModels;
    }

    public RecordModel(){

    }
    /*
    @Override
    public String toString() {
        String tmp = "";
        for(ColumnRecordModel columRecord: this.columnRecordModels){
            tmp = tmp + columRecord.getId()+":"+columRecord.getValue()+"\n";
        }
        return tmp.substring(0,tmp.length()-1);
        //return super.toString();
    }*/
}
