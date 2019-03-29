package com.cidacs.rl.config;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import com.cidacs.rl.config.ColumnConfigModel;

public class ConfigReader {
    public ConfigModel readConfig(){
        ConfigModel configModel = new ConfigModel();

        Properties config = new Properties();

        String propFileName = "assets/config.properties";
        InputStream configFileStream;

        configFileStream = ConfigReader.class.getClassLoader().getResourceAsStream(propFileName);

        try {
            config.load(configFileStream);
            
            // read dba, dbb and dbIndex
            configModel.setDbA(config.getProperty("db_a"));
            configModel.setDbB(config.getProperty("db_b"));
            configModel.setDbIndex(config.getProperty("db_index"));

            // read all columns
            for(int i=0; i<10; i++){
                String id = config.getProperty(i+"_id");
                String type = config.getProperty(i+"_type");
                String indexA = config.getProperty(i+"_index_a");
                String indexB = config.getProperty(i+"_index_b");
                
                // if any of the columns is missing the columns is thrown away
                if(id==null || type==null || indexA==null || indexB==null || config.getProperty(i+"_wheight")==null){
                    break;
                }

                double weight = Double.valueOf(config.getProperty(i+"_wheight"));
                
                configModel.addColumn(new ColumnConfigModel(id, type, indexA, indexB, weight));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configModel;
    }
}
