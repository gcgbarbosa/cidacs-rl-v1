package buscador;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import util.Column;
import util.ConfigLoader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuscadorAutomatico {
    
    String baseIn;
    Buscador buscador;
    HashMap<String, Point> columnIndex;
    ConfigLoader opts;

    public BuscadorAutomatico(ConfigLoader opts) throws IOException {
        this.baseIn = opts.getBaseMenor();
        this.buscador = new Buscador(opts);
        this.opts = opts;
    }

    public String buildQueryExact(ArrayList<Column> cols, String r[]){
        String query = new String();
        for(Column c: cols){
            System.out.println(c.getName());
            if(c.getType().equals("string")){
                query = query + "+" +c.getName() + ":\"" + r[c.getColumn()] + "\" ";
            }
            if(c.getType().equals("date")){
                query = query + "+" +c.getName() + ":\"" + r[c.getColumn()] + "\" ";
            }
            if(c.getType().equals("ibge")){
                query = query + "+" +c.getName() + ":" + r[c.getColumn()] + "~1 ";
            }
            if(c.getType().equals("categorical")){
                query = query + "+" +c.getName() + ":\"" + r[c.getColumn()] + "\" ";
            }
        }
        return query;
    }

    public ArrayList<ArrayList<Integer>> combine(int n, int k) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
     
        if (n <= 0 || n < k)
            return result;
     
        ArrayList<Integer> item = new ArrayList<Integer>();
        dfs(n, k, 1, item, result); // because it need to begin from 1
     
        return result;
    }
     
    private void dfs(int n, int k, int start, ArrayList<Integer> item,
            ArrayList<ArrayList<Integer>> res) {
        if (item.size() == k) {
            res.add(new ArrayList<Integer>(item));
            return;
        }
     
        for (int i = start; i <= n; i++) {
            item.add(i);
            dfs(n, k, i + 1, item, res);
            item.remove(item.size() - 1);
        }
    }

    public ArrayList<Column> getPermutationsOfColumns(ArrayList<Column> cols, ArrayList<Integer> indexes){
        ArrayList<Column> result = new ArrayList<>();
        for (Column c: cols){
            if(indexes.indexOf(c.getColumn())!=-1){
                result.add(c);
            }
        }
        return result;
    }

    public String buildQueryFuzzy(ArrayList<Column> cols, String r[], Buscador buscador){
        String query = new String();

        for(Column c: cols){
            System.out.println(c.getName());
            if(c.getType().equals("string")){
                //query = query + "+" +c.getName() + ":\"" + r[c.getColumn()] + "\" ";
                if(!r[c.getColumn()].equals(" ")){
                    query = query + c.getName() + ":(" + buscador.addTermsToString(r[c.getColumn()].split(" "), "~") + ") ";
                }             
            }
            if(c.getType().equals("date")){
                if(!r[c.getColumn()].equals(" ")) {
                    query = query + c.getName() +
                            ":("
                            + r[c.getColumn()].substring(0, 4) + "\\-"
                            + r[c.getColumn()].substring(5, 7) + "\\-"
                            + r[c.getColumn()].substring(8, 10) + ") ";
                }
            }
            if(c.getType().equals("ibge")){
                if(!r[c.getColumn()].equals(" ")) {
                    query = query +c.getName() + ":" + r[c.getColumn()] + "~1 ";
                }
            }
            if(c.getType().equals("categorical")){
                if(!r[c.getColumn()].equals(" ")) {
                    query = query + c.getName() + ":(" + r[c.getColumn()] + "~) ";
                }
            }
        }
        return query;
    }

    public ArrayList<String> printLine(String[] objective, String[] candidate, double score, int level, ArrayList<Column> columns) {
        ArrayList<String> result = new ArrayList<String>();

        for (Column c: columns){
            result.add(objective[c.getColumn()] + "|" + candidate[c.getColumn()]);
        }
        result.add(String.valueOf(score));
        result.add(String.valueOf(level));
        //result.add(candidate[1]);
        return result;
    }
    
    public ArrayList<ArrayList<String>> buscar() throws IOException, ParseException {
        //double MIN_SCORE = 0.9;
        FileReader fr = new FileReader(this.baseIn);
        BufferedReader br = new BufferedReader(fr);
        String strBusca = null;
        ArrayList<String[]> result = null;
        ArrayList<ArrayList<String>> resultFinal = new ArrayList<ArrayList<String>>();
        String[] candidate = null;
        Distancia dist = new Distancia(this.opts.getColumns());
        boolean missing;
        String s;
        String c[];
        ArrayList<Column> tmpComb;
        ScoreDoc[] tmpScoreDoc;
        ArrayList<String[]> tmpResultSearch;


        while ((s = br.readLine()) != null) {
            // MISSING DEFAULT VALUE
            missing = false;
            // SPLIT BY SPACE
            c = s.replaceAll("\"", "").split(";");
            // CLEAR ALL PAST RESULTS
            result = new ArrayList<String[]>();
            // CONSERTAR (ARMENGUE)
            if (c[2].equals(" ") || c[3].equals(" ")){
                missing = true;
            }
            if(!missing) {
                // FASE 1
                strBusca = this.buildQueryExact(opts.getColumns(), c);
                // DO THE QUERY
                tmpScoreDoc = this.buscador.buscar(strBusca, 100);
                tmpResultSearch = this.buscador.scoreDocToStr(tmpScoreDoc);
                // IF ANY RESULT WAS FOUND
                if(!tmpResultSearch.isEmpty()){
                    // ADD RESULTS
                    result.addAll(tmpResultSearch);
                    // FIND BEST PAIR
                    candidate = dist.selectTheBest(result, c);
                    // IF SCORE
                    if(Double.valueOf(candidate[candidate.length - 1]) > 0.95){
                        resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 1, opts.getColumns()));
                        continue;
                    }
                }
                // FASE 2
                // RESET RESULT ARRAY
                result = new ArrayList<String[]>();
                // ADD LAST CANDIDATE TO RESULT DATASET
                result.add(candidate);
                // GENERATE POSSIBLE COMBINATIONS 
                ArrayList<ArrayList<Integer>> combinacoes = this.combine(opts.getColumns().size(), opts.getColumns().size()-1);
                // DO EACH SEARCH BASED ON COMBINATIONS GENERATED
                for(ArrayList<Integer> combinacao : combinacoes){
                    // seleciona as colunas a partir da função de combinação
                    tmpComb = this.getPermutationsOfColumns(opts.getColumns(), combinacao);
                    // constrói a string de busca
                    strBusca = this.buildQueryExact(tmpComb, c);
                    // adiciona o resultado em result
                    tmpScoreDoc = this.buscador.buscar(strBusca, 100);
                    tmpResultSearch = this.buscador.scoreDocToStr(tmpScoreDoc);
                    result.addAll(tmpResultSearch);
                }
                //
                if(!result.isEmpty()){
                    candidate = dist.selectTheBest(result, c);
                    //
                    if(Double.valueOf(candidate[candidate.length - 1]) > 0.95){
                        resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 2, opts.getColumns()));
                        continue;
                    }
                }
            }
            // FASE 3
            result = new ArrayList<String[]>();
            result.add(candidate);
            //
            strBusca = this.buildQueryFuzzy(opts.getColumns(), c, buscador);
            // DO THE QUERY
            tmpScoreDoc = this.buscador.buscar(strBusca, 100);
            tmpResultSearch = this.buscador.scoreDocToStr(tmpScoreDoc);
            result.addAll(tmpResultSearch);
            // SELECT THE BEST OPTION
            candidate = dist.selectTheBest(result, c);
            resultFinal.add(printLine(c, candidate, Double.valueOf(candidate[candidate.length - 1]), 3, opts.getColumns()));
        }
        fr.close();
        return resultFinal;
    }
}


    