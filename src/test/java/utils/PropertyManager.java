package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Класс обработчик входных данных.
 * <p>Использует System properties.</p>
 *
 * @author Maksim_Kachaev
 * @see System
 * */
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
