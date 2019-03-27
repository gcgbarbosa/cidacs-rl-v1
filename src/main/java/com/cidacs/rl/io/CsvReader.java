package com.cidacs.rl.io;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class CsvReader {


    public Iterable<CSVRecord> getCsvIterable(String csvPath) {
        Reader in = null;
        try {
            in = new FileReader(csvPath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            /*
            for (CSVRecord record : records) {
                String name = record.get("name");
                String email = record.get("email");
                String phone = record.get("phone");
                System.out.println(name+"-"+email+"-"+phone);
            }*/
            return records;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
