package sn.groupeisi.flexipay.utils;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    public static Properties loadConfig(){
        Properties properties = new Properties();
        try(FileInputStream fis = new FileInputStream(CONFIG_FILE)){
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la configuration.", e);
        }
        return properties;
    }
}
