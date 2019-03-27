package com.cidacs.rl.config;

public class ConfigReader {
    private String configPath;

    public ConfigModel readConfig(){
        ConfigModel configModel = new ConfigModel();


        return configModel;
        /*
        for further alteration
        Properties config = new Properties();

        String propFileName = "assets/config.properties";
        InputStream configFileStream;

        configFileStream = Main.class.getClassLoader().getResourceAsStream(propFileName);



        try {
            config.load(configFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }*/
        // write your code here
    }
}
