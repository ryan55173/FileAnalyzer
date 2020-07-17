import java.io.*;
import java.util.Properties;

public class AnalyzerConfig {

    // Objects
    private File configFile;
    private Properties configProperties;

    // Constructor and initialization
    AnalyzerConfig() {
        initProperties();
        configFile = new File("data\\analyzer_config.cfg");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                writeDefaults();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            readProperties();
        }
    }
    private void initProperties() {
        configProperties = new Properties();
        // Set default properties
        configProperties.setProperty("rootPath", "none");
    }
    private void writeDefaults() {
        try {
            FileWriter writer = new FileWriter(this.configFile);
            this.configProperties.store(writer, "Analyzer Data");
            writer.close();
            System.out.println("Wrote default properties to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readProperties() {
        try {
            FileReader reader = new FileReader(this.configFile);
            this.configProperties.load(reader);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void writeProperties() {
        try {
            FileWriter writer = new FileWriter(this.configFile);
            this.configProperties.store(writer, "Analyzer Data");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Methods
    public void setConfigProperty(String propertyKey, String propertyValue) {
        this.configProperties.setProperty(propertyKey, propertyValue);
        writeProperties();
    }
    public String getConfigProperty(String propertyKey) {
        String propValue = this.configProperties.getProperty(propertyKey);
        return propValue;
    }

}
