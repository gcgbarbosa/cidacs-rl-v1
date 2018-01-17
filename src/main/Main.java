package main;

import buscador.Buscador;
import buscador.BuscadorAutomatico;
import buscador.Distancia;
import indexador.Indexador;
import org.apache.lucene.queryparser.classic.ParseException;
import util.*;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        // OBJECT WITH CONFIGS
        ConfigLoader opts = new ConfigLoader('a');
        // HOUSING FOR FINAL RESULT
        ArrayList<ArrayList<String>> resultFinal;
        // NEEDED OBJECTS
        Buscador buscador;
        IO io = new IO();
        Distancia dist = new Distancia(opts.getColumnIndex());
        // AUTOMATIC MODE
        if (opts.getMode() == 'a') {
            buscador = new Buscador(opts.getBaseMaiorIndexada(), opts.getHeader());
            BuscadorAutomatico buscadorAutomatico = new BuscadorAutomatico(opts.getBaseMenor(), buscador, opts.getColumnIndex());
            resultFinal = buscadorAutomatico.buscar();
            resultFinal.sort((ArrayList<String> arr1, ArrayList<String> arr2) -> -1 * arr1.get(5).compareTo(arr2.get(5)));
            io.writeBuscaAutomatica(resultFinal, "assets/result_a_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv");
        }
        // SINGLE SEARCH MODE
        if (opts.getMode() == 'b') {
            String busca = "WILLIAN ALVES GUIA DOS SANTOS;2001-12-02;ELAINE DA SILVA;291360;1";
            String[] v_busca = busca.split(";");
            //System.out.print("NOM_PESSOA:("+addTermsToString(v_busca[0].split(" "), "~")+")  NOM_COMPLETO_MAE_PESSOA:("+addTermsToString(v_busca[2].split(" "), "~") +") DTA_NASC_PESSOA:("+v_busca[1].substring(0, 4)+"\\"+v_busca[1].substring(4, 7)+"\\"+v_busca[1].substring(7, 10)+")  munic_res:("+v_busca[3]+"~)^0.1");
            //buscador = new Buscador("assets/baseIndexada", opts.getHeader());
            buscador = new Buscador("assets/cadu2011bahia", opts.getHeader());
            ArrayList<String[]> result = buscador.scoreDocToStr(buscador.buscar("NOM_PESSOA:(" + buscador.addTermsToString(v_busca[0].split(" "), "~") + ")  NOM_COMPLETO_MAE_PESSOA:(" + buscador.addTermsToString(v_busca[2].split(" "), "~") + ") DTA_NASC_PESSOA:(" + v_busca[1].substring(0, 4) + "\\" + v_busca[1].substring(4, 7) + "\\" + v_busca[1].substring(7, 10) + ")  munic_res:(" + v_busca[3] + "~)^0.1", 10000));
            System.out.print(dist.selectTheBest(result, v_busca)[3] + ";" +
                    dist.selectTheBest(result, v_busca)[4] + ";" +
                    dist.selectTheBest(result, v_busca)[5] + ";" +
                    dist.selectTheBest(result, v_busca)[6]
            );
            io.writeBuscaUnica(result, "assets/result_b_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv", opts.getColumnIndex());
        }
        // INDEXING MODE
        if (opts.getMode() == 'i') {
            Indexador indexador = new Indexador(opts.getBaseMaior(), opts.getBaseMaiorIndexada(), opts.getHeader());
            try {
                indexador.readBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // DEBUG MODE
        if (opts.getMode() == 't') {
            /*
            String[] objective = {"JOSE AURINIO DOS SANTOS", "1960-08-01", "", "280030", "1"};
            String[] candidate = {"", "NUM_NIS_PESSOA_ATUAL", "", "", "JOSE AURINO DOS SANTOS", "ALICE RAMOS DOS SANTOS", "1960-08-01", "1", "", "", "", "2800308", "", "", "", "", "", ""};

            String[] objective = {"MARCOS PAULO DA SILVA MELO", "1974-09-27", "", "280320", "1"};
            String[] candidate = {"", "NUM_NIS_PESSOA_ATUAL", "", "", "MARCOS PAULO DA SILVA", "VALDELICE DA SILVA MELO", "1974-09-27", "1", "", "", "", "2803203", "", "", "", "", "", ""};
            */
            String[] objective = {"WILLIAN ALVES GUIA DOS SANTOS", "2001-12-02", "ELAINE DA SILVA", "291360", "1"};
            String[] candidate = {"", "NUM_NIS_PESSOA_ATUAL", "", "", "UILIAM ALVES GUIA DOS SANTOS JUNIOR", "ELAINE DA SILVA", "2001-02-12", "2", "", "", "", "2913606", "", "", "", "", "", ""};

            System.out.print(dist.compareTwoSubjects(candidate, objective));
        }
    }
}
