package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private static final String CONFIG_FILE_PATH = "/test.properties";

    static {

        try(InputStream propFile = PropertyManager.class.getResourceAsStream(CONFIG_FILE_PATH)) {
            Properties p = new Properties(System.getProperties());
            p.load(propFile);                               //add new properties
            System.setProperties(p);                        // set the system properties
        } catch (IOException e) {e.printStackTrace();}
    }

}
