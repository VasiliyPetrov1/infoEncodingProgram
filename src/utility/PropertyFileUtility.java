package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileUtility {

    public static final String PROP_FILE_PATH = "src/resources/entropyEvaluationProgram.properties";

    public static Object getPropertyFromFile(String propertyName) {

        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(PROP_FILE_PATH);){
            properties.load(fis);
            return properties.getProperty(propertyName);
        } catch(FileNotFoundException e){
            System.out.println("Can't find property file");
        } catch(IOException e){
            System.out.println("Problems with loading data from property file");
        }

        return "";
    }

}
