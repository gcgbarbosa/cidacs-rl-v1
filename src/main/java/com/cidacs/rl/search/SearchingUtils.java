package com.cidacs.rl.search;

import com.cidacs.rl.record.ColumnRecordModel;

import java.util.ArrayList;

public class SearchingUtils {

    public String addTildeToEachName(String name) {
        String [] tmp;
        tmp = name.split(" ");

        String result = "";

        for (String strPiece : tmp) {
            result = result + strPiece + "~ ";
        }

        return result.substring(0, result.length()-1);
    }

    public String getStrQueryFuzzy(ArrayList<ColumnRecordModel> columns){
        String query = new String();

        for(ColumnRecordModel column: columns){
            if(column.getType().equals("string")){
                // just in case we need it
            }
            if( column.getType().equals("name")){
                //query = query + "+" +c.getName() + ":\"" + r[c.getColumn()] + "\" ";
                if(column.getValue().isEmpty() == false){
                    query = query + column.getId() + ":(" + this.addTildeToEachName(column.getValue()) + ") ";
                }
            }
            if(column.getType().equals("date")){
                if(column.getValue().isEmpty() == false){
                    query = query +column.getId() + ":" + column.getValue() + "~ ";
                }
            }
            if(column.getType().equals("ibge")){
                if(column.getValue().isEmpty() == false){
                    query = query +column.getId() + ":" + column.getValue() + "~1 ";
                }
            }
            if(column.getType().equals("categorical")){
                if(column.getValue().isEmpty() == false){
                    query = query + column.getId() + ":(" + column.getValue() + "~) ";
                }
            }
        }
        //System.out.print(query);
        return query;
    }

    public String getStrQueryExact(ArrayList<ColumnRecordModel> columns){
        String query = new String();
        for(ColumnRecordModel column: columns){
            if(column.getValue().isEmpty() == false) {
                if (column.getType().equals("name")) {
                    query = query + "+" + column.getId() + ":\"" + column.getValue() + "\" ";
                }
                if (column.getType().equals("date")) {
                    query = query + "+" + column.getId() + ":\"" + column.getValue() + "\" ";
                }
                if (column.getType().equals("ibge")) {
                    query = query + "+" + column.getId() + ":" + column.getValue() + "~1 ";
                }
                if (column.getType().equals("categorical")) {
                    query = query + "+" + column.getId() + ":\"" + column.getValue() + "\" ";
                }
            }
        }
        //System.out.println(query);
        return query;
    }

    public ArrayList<ColumnRecordModel> filterUnusedColumns(ArrayList<ColumnRecordModel> columns){
        ArrayList<ColumnRecordModel> tmpResult = new ArrayList<ColumnRecordModel>();

        for (ColumnRecordModel column : columns){

            switch(column.getType()){
                case "name":
                    tmpResult.add(column);
                    break;
                case "date":
                    tmpResult.add(column);
                    break;
                case "ibge":
                    tmpResult.add(column);
                    break;
                case "categorical":
                    tmpResult.add(column);
                    break;
            }
        }
        return tmpResult;
    }

}
