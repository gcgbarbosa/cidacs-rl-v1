package com.cidacs.rl.config;

import com.cidacs.rl.record.ColumnRecordModel;


import java.util.ArrayList;

public class ConfigModel {
    private String dbA;
    private String dbB;
    private String dbIndex;

    private ArrayList<ColumnConfigModel> columns;

    /* CONFIG TESTE
    public ConfigModel() {
        ArrayList<ColumnConfigModel> tmpColumnConfig = new ArrayList<ColumnConfigModel>();

        // colocar todas as configs aqui
        this.dbA = "assets/dbA.csv";
        this.dbB = "assets/dbB.csv";
        this.dbIndex = "assets/dbAIndex";

        tmpColumnConfig.add(new ColumnConfigModel("id", "id","id_sim", "", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("restrictionDate", "restriction","DTOBITO", "", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("name", "name","NOME", "", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("fatherName", "name","NOMEPAI", "", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("motherName", "name","NOMEMAE", "", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("birthDate", "date","DTNASC", "", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("gender", "categorical","SEXO", "", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("ibge", "ibge","CODMUNRES", "", 1.0 ));

        this.columns = tmpColumnConfig;
    } */
    /*
    public ConfigModel() {
        ArrayList<ColumnConfigModel> tmpColumnConfig = new ArrayList<ColumnConfigModel>();

        // colocar todas as configs aqui
        this.dbA = "assets/sim.csv";
        this.dbB = "assets/horus.csv";
        this.dbIndex = "assets/simIndex";

        tmpColumnConfig.add(new ColumnConfigModel("id", "id","id_sim", "id_horus", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("restrictionDate", "restriction","DTOBITO", "dt_atualizacao_plus_cadastro", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("name", "name","NOME", "NO_NOME", 1.0 ));
        //tmpColumnConfig.add(new ColumnConfigModel("fatherName", "name","NOMEPAI", "NO_PAI", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("motherName", "name","NOMEMAE", "NO_MAE", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("birthDate", "date","DTNASC", "DT_NASCIMENTO", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("gender", "categorical","SEXO", "TP_SEXO", 0.5 ));
        tmpColumnConfig.add(new ColumnConfigModel("ibge", "ibge","CODMUNRES", "CO_MUNICIPIO_IBGE", 0.12 ));

        this.columns = tmpColumnConfig;
    }*/
    /*(
    public ConfigModel() {
        ArrayList<ColumnConfigModel> tmpColumnConfig = new ArrayList<ColumnConfigModel>();

        // colocar todas as configs aqui
        this.dbA = "assets/sim.csv";
        this.dbB = "assets/horus.csv";
        this.dbIndex = "assets/simIndex";

        tmpColumnConfig.add(new ColumnConfigModel("id", "id","id_sim", "0", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("restrictionDate", "restriction","DTOBITO", "1", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("name", "name","NOME", "2", 1.0 ));
        //tmpColumnConfig.add(new ColumnConfigModel("fatherName", "name","NOMEPAI", "3", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("motherName", "name","NOMEMAE", "4", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("birthDate", "date","DTNASC", "5", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("gender", "categorical","SEXO", "6", 0.5 ));
        tmpColumnConfig.add(new ColumnConfigModel("ibge", "ibge","CODMUNRES", "7", 0.12 ));

        this.columns = tmpColumnConfig;
    }*/


    public ConfigModel() {
        ArrayList<ColumnConfigModel> tmpColumnConfig = new ArrayList<ColumnConfigModel>();

        // colocar todas as configs aqui
        this.dbA = "assets/horus.csv";
        this.dbB = "assets/sih.csv";
        this.dbIndex = "assets/horusIndex";

        tmpColumnConfig.add(new ColumnConfigModel("id", "id","id_horus", "0", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("restrictionDate", "restriction","dt_atualizacao_plus_cadastro", "1", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("name", "name","NO_NOME", "2", 1.0 ));
        //tmpColumnConfig.add(new ColumnConfigModel("fatherName", "name","NO_PAI", "3", 0.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("motherName", "name","NO_MAE", "4", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("birthDate", "date","DT_NASCIMENTO", "5", 1.0 ));
        tmpColumnConfig.add(new ColumnConfigModel("gender", "categorical","TP_SEXO", "6", 0.5 ));
        tmpColumnConfig.add(new ColumnConfigModel("ibge", "ibge","CO_MUNICIPIO_IBGE", "7", 0.12 ));

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

    public void setColumns(ArrayList<ColumnConfigModel> columns) {
        this.columns = columns;
    }
}
