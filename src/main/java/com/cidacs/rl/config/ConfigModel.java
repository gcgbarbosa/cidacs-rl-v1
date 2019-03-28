package com.cidacs.rl.config;

import com.cidacs.rl.record.ColumnRecordModel;


import java.util.ArrayList;

public class ConfigModel {
    private String dbA;
    private String dbB;
    private String dbIndex;

    private ArrayList<ColumnConfigModel> columns = new ArrayList<ColumnConfigModel>();
    
    public ConfigModel() {
        ArrayList<ColumnConfigModel> tmpColumnConfig = new ArrayList<ColumnConfigModel>();

        // colocar todas as configs aqui
        this.dbA = "assets/dsa.csv";
        this.dbB = "assets/dsb.csv";
        this.dbIndex = "assets/dsb-index";

        tmpColumnConfig.add(new ColumnConfigModel("id", "id","code", "0", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("ibge", "ibge","municipality_residence", "1", 0.12 ));
        tmpColumnConfig.add(new ColumnConfigModel("name", "name","name", "2", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("motherName", "name","mother_name", "3", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("birthDate", "date","birth_date", "4", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("gender", "categorical","gender", "5", 0.5 ));

        this.columns = tmpColumnConfig;
    }

    public ConfigModel(String dbA, String dbB, String dbIndex, ArrayList<ColumnConfigModel> columns) {

        this.dbA = dbA;
        this.dbB = dbB;
        this.dbIndex = dbIndex;
        this.columns = columns;
    }


    public String getDbA() {
        return dbA;
    }

    public void setDbA(String dbA) {
        this.dbA = dbA;
    }

    public String getDbB() {
        return dbB;
    }

    public void setDbB(String dbB) {
        this.dbB = dbB;
    }

    public String getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(String dbIndex) {
        this.dbIndex = dbIndex;
    }

    public ArrayList<ColumnConfigModel> getColumns() {
        return columns;
    }

    public void addColumn(ColumnConfigModel column) {
        this.columns.add(column);
    }

    public void setColumns(ArrayList<ColumnConfigModel> columns) {
        this.columns = columns;
    }
}
