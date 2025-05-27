package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    public static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/testdata/testdata.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data properties", e);
        }
    }

    public static String get(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
}
