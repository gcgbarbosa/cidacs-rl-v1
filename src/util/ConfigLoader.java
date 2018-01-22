package util;

import java.util.ArrayList;

import org.json.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ohackgb on 8/3/16.
 */
public class ConfigLoader {
    private String baseMenor;
    private String baseMaior;
    private String baseMaiorIndexada;
    private String[] header;
    private char mode;

    ArrayList<Column> columns;
    /*
    C0NFIGURACAO PADRAO
    */
    public ConfigLoader(){    
        ArrayList<Column> columns = new ArrayList<>();
        Column tmp = null;

        try {
            // read json
            String content = new String(Files.readAllBytes(Paths.get("assets/config.json")));
            // parse its content
            JSONObject obj = new JSONObject(content);
            // set base maior
            this.setBaseMaior(obj.getString("db_A"));
            // set base menor
            this.setBaseMenor(obj.getString("db_B"));
            // set base indexada
            this.setBaseMaiorIndexada(obj.getString("db_index"));
            
            JSONArray arr = obj.getJSONArray("columns");
            for (int i = 0; i < arr.length(); i++)
            {
                // variable to read
                tmp = new Column();
                // set contents
                tmp.setName(arr.getJSONObject(i).getString("name"));
                tmp.setType(arr.getJSONObject(i).getString("type"));
                tmp.setColumn(arr.getJSONObject(i).getInt("column")-1);
                tmp.setWeight(arr.getJSONObject(i).getDouble("weight"));
                // add to arrays
                columns.add(tmp);
            }
            // add columns to config
            this.setColumns(columns);
        } catch (IOException e) {
            System.out.println("Config file not found.");
        }
        System.out.println("Configs loaded.");
    }

    public ArrayList<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
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
