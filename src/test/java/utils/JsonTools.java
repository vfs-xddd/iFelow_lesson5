package utils;

import io.qameta.allure.Step;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**Класс для работы с json файлами.
 * <p>Дополнительные зависимости не используются.</p>
 * @author Maksim_Kachaev */
public class JsonTools {

    /**путь к папке с Json файлами*/
    private static final String path = "src/test/resources/jsons/";

    /**Читать json файл
     * <p>В качестве исходного пути к папке с json файлами использует классовую переменную "path"</p>
     * @param fileName имя файла без расширения
     * @return готовый json объект*/
    @Step("Прочитать Json файл")
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
    /**Создать Json файл
     * <p>В качестве исходного пути к папке с json файлами использует классовую переменную "path"</p>
     * @param jsonData json объект с данными {key:value}
     * @param fileName имя файла без расширения
     * @return true если успешно создан файл, false в противном случае*/
    @Step("Создать Json файл \"{fileName}\"")
    public static boolean jsonFileWriter(JSONObject jsonData, String fileName) {
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
