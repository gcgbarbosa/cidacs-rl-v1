package buscador;

import org.apache.lucene.search.spell.JaroWinklerDistance;

import java.util.ArrayList;
import util.Column;;


public class Distancia {
    ArrayList<Column> columns = null;

    public Distancia(ArrayList<Column> columns) {
        this.columns = columns;
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
        double tmp_total = 2 + (0.125 * 8) + 0.08 + 0.04 + 0.5, score = 0;

        for(Column c : this.columns){
            // PARA NOME E NOME DA MAE
            if (c.getType().equals("string")){
                if(objective[c.getColumn()].isEmpty() || objective[c.getColumn()].equals(" ")){
                    tmp_total = tmp_total - c.getWeight();
                } else {
                    score = score + getDistanceString(objective[c.getColumn()], candidate[c.getColumn()], c.getWeight());
                }
            }
            // PARA DATA DE NASCIMENTO
            if (c.getType().equals("date")){
                try {
                    if (objective[c.getColumn()].isEmpty() || objective[c.getColumn()].equals(" ")) {
                        tmp_total = tmp_total - c.getWeight();
                    } else {
                        score = score + getDistanceDate(objective[c.getColumn()], candidate[c.getColumn()], c.getWeight());
                    }
                } catch (ArrayIndexOutOfBoundsException dta) {
                    System.out.println("Data com erro: " + objective[c.getColumn()]);
                }
            }
            // PARA CODIGO DO MUNIC
            if (c.getType().equals("ibge")){
                try {
                    if (objective[c.getColumn()].isEmpty() || objective[c.getColumn()].equals(" ")) {
                        tmp_total = tmp_total - c.getWeight();
                    } else {
                        score = score + getDistanceIBGE(objective[c.getColumn()], candidate[c.getColumn()], c.getWeight());
                    }
                } catch (StringIndexOutOfBoundsException ibge) {
                    System.out.println("Código IBGE com erro: " + objective[c.getColumn()]);
                }
            }
            // PARA SEXO
            if (c.getType().equals("categorical")){
                try {
                    if (objective[c.getColumn()].isEmpty() || objective[c.getColumn()].equals(" ")) {
                        tmp_total = tmp_total - c.getWeight();
                    } else {
                        if(objective[c.getColumn()].charAt(objective[c.getColumn()].length()-1) ==
                        candidate[c.getColumn()].charAt(candidate[c.getColumn()].length()-1)) {
                            score = score + c.getWeight();
                        } else {
                            score = score + getDistanceCategorical(objective[c.getColumn()], candidate[c.getColumn()], c.getWeight());
                        }
                    }
                } catch (StringIndexOutOfBoundsException ibge) {
                    System.out.println("Código IBGE com erro: " + objective[c.getColumn()]);
                }
            }
        }
        return score / tmp_total;
    }

    private double getDistanceString(String nome1, String nome2, double w) {
        JaroWinklerDistance jaro = new JaroWinklerDistance();
        return w*jaro.getDistance(nome1, nome2);
    }

    private double getDistanceDate(String data1, String data2, double w) {
        double score = 0;
        data1 = data1.replaceAll("-", "");
        data2 = data2.replaceAll("-", "");
        for (int i = 0; i < data1.length(); i++) {
            if (data1.charAt(i) == data2.charAt(i)) {
                score = score + w/data1.length();
            }
        }
        return score;
    }

    private double getDistanceIBGE(String ibge1, String ibge2, double w) {
        double score = 0;

        if (ibge1.substring(0, 2).equals(ibge2.substring(0, 2))) {
            score = score + w/3;
            if (ibge1.substring(2, 6).equals(ibge2.substring(2, 6))) {
                score = score + w/2;
            }
        }
        return score;
    }

    private double getDistanceCategorical(String literal1, String literal2, double w) {
        if (literal1.equals(literal2)) {
            return w;
        }
        return 0.0;
    }
}
