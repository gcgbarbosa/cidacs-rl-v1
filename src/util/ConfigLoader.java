package util;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by george on 8/3/16.
 */
public class ConfigLoader {
    private String baseMenor;
    private String baseMaior;

    private String baseMaiorIndexada;

    private String[] header;

    private char mode;

    HashMap<String, Point> columnIndex;

    public ConfigLoader(String baseMaior, String baseMenor, String baseMaiorIndexada, char mode, String[] header){

    }

    public ConfigLoader(char mode){
        HashMap<String, Point> columnIndex = new HashMap<>();
        // SINAN-BA
        /*
        columnIndex.put("NOME", new Point(0, 3));
        columnIndex.put("NOME_MAE", new Point(2, 6));
        columnIndex.put("DATA_NASC", new Point(1, 5));
        columnIndex.put("COD_IBGE", new Point(3, 9));
        columnIndex.put("COD_SEXO", new Point(4, 4));
        */
        // DEFAULT (BASE_MENOR, BASE_MAIOR)
        columnIndex.put("NOME", new Point(0, 4));
        columnIndex.put("NOME_MAE", new Point(2, 5));
        columnIndex.put("DATA_NASC", new Point(1, 6));
        columnIndex.put("COD_IBGE", new Point(3, 11));
        columnIndex.put("COD_SEXO", new Point(4, 7));


        this.setMode(mode);
        this.setColumnIndex(columnIndex);
        this.setBaseMaior("assets/baseIndexacao.csv");
        this.setBaseMaiorIndexada("assets/baseIndexada");
        this.setBaseMenor("assets/basePesquisa.csv");

        String[] header = {"NISES", "NUM_NIS_PESSOA_ATUAL", "NU_NIS_ORIGINAL", "COD_FAMILIAR_FAM", "NOM_PESSOA", "NOM_COMPLETO_MAE_PESSOA", "DTA_NASC_PESSOA", "COD_SEXO_PESSOA", "COD_PARENTESCO_RF_PESSOA", "COD_IBGE_MUNIC_NASC_PESSOA", "DAT_CADASTRAMENTO_FAM", "munic_​res", "Status", "A2007", "A2008", "A2009", "A2011", "A2012"};
        this.setHeader(header);

    }

    public HashMap<String, Point> getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(HashMap<String, Point> columnIndex) {
        this.columnIndex = columnIndex;
    }

    public char getMode() {
        return mode;
    }

    public void setMode(char mode) {
        this.mode = mode;
    }

    public String getBaseMenor() {
        return baseMenor;
    }

    public void setBaseMenor(String baseMenor) {
        this.baseMenor = baseMenor;
    }

    public String getBaseMaior() {
        return baseMaior;
    }

    public void setBaseMaior(String baseMaior) {
        this.baseMaior = baseMaior;
    }

    public String getBaseMaiorIndexada() {
        return baseMaiorIndexada;
    }

    public void setBaseMaiorIndexada(String baseMaiorIndexada) {
        this.baseMaiorIndexada = baseMaiorIndexada;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }
}
