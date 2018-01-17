package buscador;

import org.apache.lucene.queryparser.classic.ParseException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuscadorAutomatico {
    String baseIn = null;
    Buscador buscador = null;
    HashMap<String, Point> columnIndex = null;

    public BuscadorAutomatico(String baseIn, Buscador buscador, HashMap<String, Point> columnIndex) {
        this.baseIn = baseIn;
        this.buscador = buscador;
        this.columnIndex = columnIndex;
    }

    public ArrayList<ArrayList<String>> buscar() throws IOException, ParseException {
        double MIN_SCORE = 0.9;

        FileReader fr = new FileReader(this.baseIn);
        BufferedReader br = new BufferedReader(fr);
        String strBusca = null;
        ArrayList<String[]> result = null;
        ArrayList<ArrayList<String>> resultFinal = new ArrayList<ArrayList<String>>();
        String[] candidate = null;
        Distancia dist = new Distancia(this.columnIndex);
        boolean missing;
        String s;
        String c[];
        while ((s = br.readLine()) != null) {
            missing = false;
            c = s.replaceAll("\"", "").split(";");
            // CHECK MISSING
            if (c[this.columnIndex.get("DATA_NASC").x].equals(" ") || c[this.columnIndex.get("COD_IBGE").x].equals(" ")){
                missing = true;
            }
            // CLEAR ALL THE PAST RESULTS
            result = new ArrayList<String[]>();
            //munic_​res
            if(!missing) {
                // FIRST SEARCH STRING
                strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\" " +
                        "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\" " +
                        "+NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\" " +
                        "+COD_MUNIC_IBGE:" + c[this.columnIndex.get("COD_IBGE").x] + "~1 " +
                        "+COD_SEXO_PESSOA:\"" + c[this.columnIndex.get("COD_SEXO").x] + "\" ";
                // DO THE QUERY
                result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                // SELECT THE BEST OPTION
                candidate = dist.selectTheBest(result, c);
            }
            // CHECK IF THE CANDIDATE FULLFILL THE REQUIREMENTS
            if(candidate != null && Double.valueOf(candidate[candidate.length - 1]) > 0.95 && !missing){
                //System.out.println(strBusca);
                resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 1));
            } else {
                if(!missing) {
                    // LEVEL 2
                    // 1 Search
                    strBusca = "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\" " +
                            "+NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\" " +
                            "+COD_MUNIC_IBGE:" + c[this.columnIndex.get("COD_IBGE").x] + "~1 " +
                            "+COD_SEXO_PESSOA:\"" + c[this.columnIndex.get("COD_SEXO").x] + "\" ";
                    // Perform query
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 100)));

                    // 2 Search
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\" " +
                            "+NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\" " +
                            "+COD_MUNIC_IBGE:" + c[this.columnIndex.get("COD_IBGE").x] + "~1 " +
                            "+COD_SEXO_PESSOA:\"" + c[this.columnIndex.get("COD_SEXO").x] + "\" ";
                    // Perform query
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));

                    // 2 Search
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\" " +
                            "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\" " +
                            "+COD_MUNIC_IBGE:" + c[this.columnIndex.get("COD_IBGE").x] + "~1 " +
                            "+COD_SEXO_PESSOA:\"" + c[this.columnIndex.get("COD_SEXO").x] + "\" ";
                    // Perform query
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 100)));

                    // 3 Search
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\" " +
                            "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\" " +
                            "+NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\" " +
                            "+COD_SEXO_PESSOA:\"" + c[this.columnIndex.get("COD_SEXO").x] + "\" ";
                    // Perform query
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 100)));

                    // 4 Search
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\" " +
                            "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\" " +
                            "+NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\" " +
                            "+COD_MUNIC_IBGE:" + c[this.columnIndex.get("COD_IBGE").x] + "~1 ";
                    // Perform query
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 100)));
                    candidate = dist.selectTheBest(result, c);
                }
                // Select the best candidate
                if(candidate != null && Double.valueOf(candidate[candidate.length - 1]) > 0.95 && !missing){
                    resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 2));
                } else {
                    //System.out.println("|" + this.columnIndex.get("DTA_NASC_PESSOA") + "|");
                    //result = new ArrayList<String[]>();
                    // LVL 3
                    strBusca = "NOM_PESSOA:(" + buscador.addTermsToString(c[this.columnIndex.get("NOME").x].split(" "), "~") + ") ";
                    if(!c[this.columnIndex.get("NOME_MAE").x].equals(" ")){
                        strBusca = strBusca + "NOM_COMPLETO_MAE_PESSOA:(" + buscador.addTermsToString(c[this.columnIndex.get("NOME_MAE").x].split(" "), "~") + ") ";
                    }

                    if(!c[this.columnIndex.get("DATA_NASC").x].equals(" ")) {
                        strBusca = strBusca +
                                "DTA_NASC_PESSOA:("
                                + c[this.columnIndex.get("DATA_NASC").x].substring(0, 4) + "\\-"
                                + c[this.columnIndex.get("DATA_NASC").x].substring(5, 7) + "\\-"
                                + c[this.columnIndex.get("DATA_NASC").x].substring(8, 10) + ") ";
                    }
                    if(!c[this.columnIndex.get("COD_IBGE").x].equals(" ")) {
                        strBusca = strBusca + "COD_MUNIC_IBGE:(" + c[this.columnIndex.get("COD_IBGE").x] + "~) ";
                    }
                    if(!c[this.columnIndex.get("COD_SEXO").x].equals(" ")) {
                        strBusca = strBusca + "COD_SEXO_PESSOA:" + c[this.columnIndex.get("COD_SEXO").x];
                    }

                    // PERFORM QUERY
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    // SELECT THE BEST CANDIDATE
                    candidate = dist.selectTheBest(result, c);
                    // CHECK WHETHER THE CANDIDATE FULFILL THE REQUIREMENTS
                    if(candidate == null){
                        candidate = new String[]{"NUM_NIS_PESSOA_ATUAL", "NU_NIS_ORIGINAL", "COD_FAMILIAR_FAM", "NOM_PESSOA", "COD_SEXO_PESSOA", "DTA_NASC_PESSOA", "NOM_COMPLETO_MAE_PESSOA", "COD_IBGE_MUNIC_NASC_PESSOA", "COD_EST_CADASTRAL_MEMB", "COD_MUNIC_IBGE"};
                    }
                    System.out.println(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 3));
                    resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 3));
                }
            }


            /*
            // LEVEL 2
            if (result.size() == 0) {
                result = new ArrayList<String[]>();
                strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0 +NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0";
                result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                strBusca = "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0 +NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                if (result.size() > 0) {
                    candidate = dist.selectTheBest(result, c);
                    resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 2));
                } else {
                    // LEVEL 3
                    result = new ArrayList<String[]>();
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    strBusca = "+NOM_PESSOA:\"" + c[this.columnIndex.get("NOME").x] + "\"~0 +NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    strBusca = "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0 +NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0 ";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    strBusca = "+DTA_NASC_PESSOA:\"" + c[this.columnIndex.get("DATA_NASC").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                    strBusca = "NOM_COMPLETO_MAE_PESSOA:\"" + c[this.columnIndex.get("NOME_MAE").x] + "\"~0 +munic_​res:" + c[this.columnIndex.get("COD_IBGE").x] + "~1";
                    result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));

                    if (result.size() > 0) {
                        candidate = dist.selectTheBest(result, c);
                        resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 3));
                    } else {
                        // LEVEL 4
                        strBusca = "NOM_PESSOA:(" + this.buscador.addTermsToString(c[this.columnIndex.get("NOME").x].split(" "), "~") + ")  "
                                + "NOM_COMPLETO_MAE_PESSOA:(" + this.buscador.addTermsToString(c[this.columnIndex.get("NOME").x].split(" "), "~") + ") "
                                + "DTA_NASC_PESSOA:(" + c[this.columnIndex.get("DATA_NASC").x].substring(0, 4) + "\\" + c[this.columnIndex.get("DATA_NASC").x].substring(4, 7) + "\\" + c[this.columnIndex.get("DATA_NASC").x].substring(7, 10) + ")  "
                                + "munic_res:(" + c[this.columnIndex.get("COD_IBGE").x] + "~)^0.1";
                        result.addAll(this.buscador.scoreDocToStr(buscador.buscar(strBusca, 1000)));
                        if (result.size() > 0) {
                            candidate = dist.selectTheBest(result, c);
                            resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 4));
                        } else {
                            candidate = new String[100];
                            resultFinal.add(printLine(c, candidate, 0.0, 0));
                        }
                    }
                }
            }*/
        }
        fr.close();
        return resultFinal;
    }

    public ArrayList<String> printLine(String[] objective, String[] candidate, double score, int level) {
        ArrayList<String> result = new ArrayList<String>();
        result.add(objective[this.columnIndex.get("NOME").x] + "|" + candidate[this.columnIndex.get("NOME").y]);
        result.add(objective[this.columnIndex.get("NOME_MAE").x] + "|" + candidate[this.columnIndex.get("NOME_MAE").y]);
        result.add(objective[this.columnIndex.get("DATA_NASC").x] + "|" + candidate[this.columnIndex.get("DATA_NASC").y]);
        result.add(objective[this.columnIndex.get("COD_IBGE").x] + "|" + candidate[this.columnIndex.get("COD_IBGE").y]);
        result.add(objective[this.columnIndex.get("COD_SEXO").x] + "|" + candidate[this.columnIndex.get("COD_SEXO").y]);
        result.add(String.valueOf(score));
        result.add(String.valueOf(level));
        result.add(candidate[1]);
        return result;
    }
}