package util;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IO {
    private FileWriter fw;
    private BufferedWriter bw;

    public void writeBuscaAutomatica(ArrayList<ArrayList<String>> lines, String dest) {
        try {
            this.fw = new FileWriter(dest);
            this.bw = new BufferedWriter(fw);

            this.bw.write("NOME_SIN;NOME_CAD;NOME_MAE_SIN;NOME_MAE_CAD;DTA_NASC_SIN;DTA_NASC_CAD;MUNIC_SIN;MUNIC_CAD;SEXO_SIN;SEX_CAD;SCORE;LEVEL;NIS_ATUAL\n");

            for (ArrayList<String> linha : lines) {
                this.bw.write(linha.get(0) + ";" + linha.get(1) + ";" + linha.get(2) + ";" + linha.get(3) + ";" + linha.get(4) + ";" + linha.get(5) + ";" + linha.get(6) + ";" + linha.get(7) + '\n');
            }

            this.bw.close();
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBuscaUnica(ArrayList<String[]> lines, String dest, HashMap<String, Point> columnIndex) {
        try {
            this.fw = new FileWriter(dest);
            this.bw = new BufferedWriter(fw);

            this.bw.write("NOME_CAD;NOME_MAE_CAD;DTA_NASC_CAD;MUNIC_CAD\n");

            for (String[] linha : lines) {
                this.bw.write(
                        linha[columnIndex.get("NOME").y] + ";" +
                                linha[columnIndex.get("NOME_MAE").y] + ";" +
                                linha[columnIndex.get("DATA_NASC").y] + ";" +
                                linha[columnIndex.get("COD_IBGE").y] + "\n"
                );
            }

            this.bw.close();
            this.fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object [][] getTextFromResult(ArrayList<String[]> lines, HashMap<String, Point> columnIndex) {
        //String result = "NOME_CAD;NOME_MAE_CAD;DTA_NASC_CAD;MUNIC_CAD;SEXO;SCORE\n";
        Object [][] result = new Object[lines.size()][9];
        String [] tmp;
        int i=0;
        for (String[] linha : lines) {
            result[i][0] = linha[columnIndex.get("NOME").y];
            result[i][1] = linha[columnIndex.get("NOME_MAE").y];
            result[i][2] = linha[columnIndex.get("DATA_NASC").y];
            result[i][3] = linha[columnIndex.get("COD_SEXO").y];
            result[i][4] = linha[columnIndex.get("COD_IBGE").y];
            result[i][5] = linha[0]; // NIS 1
            //result[i][6] = linha[3]; // COD_FAM 3
            //result[i][7] = linha[16]; // 2011 16
            result[i][6] = linha[linha.length - 1]; //SCORE
            i++;
            /*
            result = result + linha[columnIndex.get("NOME").y] + ";" +
                    linha[columnIndex.get("NOME_MAE").y] + ";" +
                    linha[columnIndex.get("DATA_NASC").y] + ";" +
                    linha[columnIndex.get("COD_SEXO").y] + ";" +
                    linha[columnIndex.get("COD_IBGE").y] + ";" +
                    linha[linha.length - 1] + "\n";
            */
        }
        return result;
    }
}
