package com.cidacs.rl.config;

import java.util.ArrayList;

public class ConfigModel {
    private String dbA;
    private String dbB;
    private String dbIndex;

    private ArrayList<ColumnConfigModel> columns = new ArrayList<ColumnConfigModel>();

    public ConfigModel() {

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
