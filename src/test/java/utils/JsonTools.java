package utils;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonTools {
    /**путь к папке с Json файлами*/
    private static final String path = "src/test/resources/jsons/";

    @DisplayName("Прочитать Json файл")
    public static JSONObject jsonFileReader(String fileName){
        try {
            File file = new File(fileName);
            String absolutePath = file.getAbsolutePath().replace(fileName, "") + path + fileName;
            return new JSONObject(new String(Files.readAllBytes(Paths.get(absolutePath))));
        }
        catch (IOException e) {
            System.out.println("some IO Parser errors");
            e.printStackTrace();
            return null;
        }
    }

    @DisplayName("Создать Json файл")
    public static Boolean jsonFileWriter(JSONObject jsonData, String fileName) {
        String absolutePath = new File(fileName).getAbsolutePath().replace(fileName, "") + path + fileName;
        try {
            FileWriter file = new FileWriter(absolutePath);
            file.write(jsonData.toString());
            file.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("some IO errors");
            e.printStackTrace();
            return false;
        }

    }


}
