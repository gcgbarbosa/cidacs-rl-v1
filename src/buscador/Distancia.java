package buscador;

import org.apache.lucene.search.spell.JaroWinklerDistance;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Distancia {
    HashMap<String, Point> columnIndex = null;

    public Distancia(HashMap<String, Point> columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String[] selectTheBest(ArrayList<String[]> candidates, String[] objective) {

        String[] selected = null;
        double score = 0, bigger = 0;

        for (String[] candidate : candidates) {
            score = compareTwoSubjects(candidate, objective);
            if (score > bigger) {
                bigger = score;
                selected = candidate;
            }
        }
        if(selected != null) {
            String[] tmp = {String.valueOf(bigger)};
            System.arraycopy(tmp, 0, selected, selected.length - 1, 1);
        }
        return selected;
    }

    public double compareTwoSubjects(String[] candidate, String[] objective) {
        /*
		System.out.print(objective[this.columnIndex.get("NOME").x]+";"+candidate[this.columnIndex.get("NOME").y]+"\n");
		System.out.print(objective[this.columnIndex.get("NOME_MAE").x]+";"+candidate[this.columnIndex.get("NOME_MAE").y]+"\n");
		System.out.print(objective[this.columnIndex.get("DATA_NASC").x]+";"+candidate[this.columnIndex.get("DATA_NASC").y]+"\n");
		System.out.print(objective[this.columnIndex.get("COD_IBGE").x]+";"+candidate[this.columnIndex.get("COD_IBGE").y]+"\n");
		System.out.print(objective[this.columnIndex.get("COD_SEXO").x]+";"+candidate[this.columnIndex.get("COD_SEXO").y]+"\n");
		*/

        double tmp_total = 2 + (0.125 * 8) + 0.08 + 0.04 + 0.5, score = 0;
        if (objective[this.columnIndex.get("NOME").x].isEmpty() || objective[this.columnIndex.get("NOME").x].equals(" ")) {
            tmp_total = tmp_total - 1;
        } else {
            score = score + getDistanceName(objective[this.columnIndex.get("NOME").x], candidate[this.columnIndex.get("NOME").y]);
        }
        // VARIAVEL NOME DA MAE
        if (objective[this.columnIndex.get("NOME_MAE").x].isEmpty() || objective[this.columnIndex.get("NOME_MAE").x].equals(" ")) {
            tmp_total = tmp_total - 1;
        } else {
            score = score + getDistanceName(objective[this.columnIndex.get("NOME_MAE").x], candidate[this.columnIndex.get("NOME_MAE").y]);
        }
        // VARIAVEL DATA DE NASCIMENTO
        try {
            if (objective[this.columnIndex.get("DATA_NASC").x].isEmpty() || objective[this.columnIndex.get("DATA_NASC").x].equals(" ")) {
                tmp_total = tmp_total - (0.125 * 8);
            } else {
                score = score + getDistanceData(objective[this.columnIndex.get("DATA_NASC").x], candidate[this.columnIndex.get("DATA_NASC").y]);
            }
        } catch (ArrayIndexOutOfBoundsException dta) {
            System.out.println("Data com erro: " + objective[this.columnIndex.get("DATA_NASC").x]);
        }
        // VARIAVEL CODIGO IBGE
        try {
            if (objective[this.columnIndex.get("COD_IBGE").x].isEmpty() || objective[this.columnIndex.get("COD_IBGE").x].equals(" ")) {
                tmp_total = tmp_total - 0.08 - 0.04;
            } else {
                score = score + getDistanceIBGE(objective[this.columnIndex.get("COD_IBGE").x], candidate[this.columnIndex.get("COD_IBGE").y]);
            }
        } catch (StringIndexOutOfBoundsException ibge) {
            System.out.println("Código IBGE com erro: " + objective[this.columnIndex.get("COD_IBGE").x]);
        }
        // VARIAVEL CODIGO SEXO
        try {
            if (objective[this.columnIndex.get("COD_SEXO").x].isEmpty() || objective[this.columnIndex.get("COD_SEXO").x].equals(" ")) {
                tmp_total = tmp_total - 0.5;
            } else {
                if(objective[this.columnIndex.get("NOME").x].split(" ")[0].charAt(objective[this.columnIndex.get("NOME").x].split(" ")[0].length()-1) ==
                candidate[this.columnIndex.get("NOME").y].split(" ")[0].charAt(candidate[this.columnIndex.get("NOME").y].split(" ")[0].length()-1)) {
                    score = score + 0.5;
                } else {
                    score = score + getDistanceLiteral(objective[this.columnIndex.get("COD_SEXO").x], candidate[this.columnIndex.get("COD_SEXO").y]);
                }
            }
        } catch (StringIndexOutOfBoundsException ibge) {
            System.out.println("Código IBGE com erro: " + objective[this.columnIndex.get("COD_IBGE").x]);
        }
        return score / tmp_total;
    }

    private double getDistanceName(String nome1, String nome2) {
        JaroWinklerDistance jaro = new JaroWinklerDistance();
        return jaro.getDistance(nome1, nome2);
    }

    private double getDistanceData(String data1, String data2) {
        double score = 0;
        data1 = data1.replaceAll("-", "");
        data2 = data2.replaceAll("-", "");
        for (int i = 0; i < data1.length(); i++) {
            if (data1.charAt(i) == data2.charAt(i)) {
                score = score + 0.125;
            }
        }
		/*
		if(data1.split("-")[0].equals(data2.split("-")[0])) {
			score += 0.33;
		}
		if(data1.split("-")[1].equals(data2.split("-")[1])) {
			score += 0.33;
		}
		if(data1.split("-")[2].equals(data2.split("-")[2])) {
			score += 0.33;
		}*/
        return score;
    }

    private double getDistanceIBGE(String ibge1, String ibge2) {
        double score = 0;
        if (ibge1.substring(0, 2).equals(ibge2.substring(0, 2))) {
            score = score + 0.04;
            if (ibge1.substring(2, 6).equals(ibge2.substring(2, 6))) {
                score = score + 0.08;
            }
        }
        return score;
    }

    private double getDistanceLiteral(String literal1, String literal2) {
        if (literal1.equals(literal2)) {
            return 0.5;
        }
        return 0.0;
    }
}
