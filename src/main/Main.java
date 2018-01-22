/* TODO
-- colocar todas as configs no json [DONE]
-- configurar a classe de settings para ler tudo do json [DONE]
-- fazer getters e setters para columns [DONE]
-- converter as bases para o formato simplificado  [DONE]
-- colocar o sistema para usar as informações lidas do json [DONE]
-- criar um queryBuilder que lide com os diferentes tipos de dados [DONE]
-- Refatorar classe buscador automatico

-- colocar separador no config
-- COLOCAR UMA SEÇÃO DE CODIGO PARA ALERTAR PARA ERROS NA ESTRUTURA DA BASE
*/

package main;

import buscador.BuscadorAutomatico;
import indexador.Indexador;
import org.apache.lucene.queryparser.classic.ParseException;
import util.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.nio.file.Files;
import java.nio.file.Paths;;


public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        // OBJECT WITH CONFIGS
        ConfigLoader opts = new ConfigLoader();
        // HOUSING FOR FINAL RESULT
        ArrayList<ArrayList<String>> resultFinal;
        // NEEDED OBJECTS
        //Buscador buscador;
        IO io = new IO();
        //Distancia dist = new Distancia(opts.getColumns());
        //
        if (Files.notExists(Paths.get(opts.getBaseMaiorIndexada()))) {
            Indexador indexador = new Indexador(opts);
            try {
                indexador.indexar();
                System.out.println("Indexing process finished.");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Error while indexing file.");
            }
        } else {
            //String busca = "LEONARDO SANTOS DE LIMA;LUCELIA SANTOS DO ANJOS;2004-02-19;3547809;1";
            BuscadorAutomatico buscadorAutomatico = new BuscadorAutomatico(opts);
            //
            resultFinal = buscadorAutomatico.buscar();
            //
            resultFinal.sort((ArrayList<String> arr1, ArrayList<String> arr2) -> -1 * arr1.get(5).compareTo(arr2.get(5)));
            //
            io.writeBuscaAutomatica(
                resultFinal,
                 "assets/result_a_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv");
        }
    }
}
