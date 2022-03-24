package stepDefinition.api;

import com.sun.istack.NotNull;
import io.cucumber.java.ru.*;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import utils.ApiTools;
import utils.JsonTools;
import utils.MessageMaker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static hooks.CucumberHooks.result;

/** Класс с шагами для api тестов.
 * <p>Шаги используются в feature файле кукумбера</p>
 * <p>Важно! Входные данные читаются в настроечном файле кукумбера - отдельный запуск feature файла выдаст ошибку.</p>
 * <p>Используются хуки для кукумбер сценариев.</p>
 * @see hooks.CucumberHooks
 * @author Maksim_Kachaev
 * */
public class StepsApi {

    public JSONObject jsonFromRequest1;
    public String lastMortyEpisode;
    public String lastCharacter;
    public String lastCharacterLocationSpecies;
    public String fileName;
    public JSONObject jsonObj;
    public JSONObject requestBody;
    public JSONObject jsonFromRequest2;
    public String urlTest1 = System.getProperty("urlTest1");
    public String urlTest2 = System.getProperty("urlTest2");

    /**задаем соответствие: адрес сайта - тег сценария для работы с ним.
     * <p>Используется для запрета выбора шагов для непредназначенных адресов сайтов</p>*/
    public Map<String, String> urlTags = Stream.of(new Object[][] {
                { urlTest1, "@Morty"  },
                { urlTest2, "@Reqres" },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

    /**<p>Выдаст ошибку если тег текущего сценария не предназначен для работы с выбранным адресом сайта.
     * Соответсвия url-tag задаются в мапе "urlTags"</p>*/
    @Дано("сайт \"(rickandmorty|httpsreqresin)\" для отправки api запросов.$")
    public void given(String temp) throws IOException {
        String givenUrl;
        if (temp.equals("rickandmorty")) {
            givenUrl = urlTest1;
        } else if (temp.equals("httpsreqresin")) {
            givenUrl= urlTest2;
        } else {
            throw new IOException("Передано неверное значение!");
        }
        String tagMustBe = urlTags.get(givenUrl);
        boolean isTagInScenarioTags = System.getProperty("currentScenarioTags").contains(tagMustBe);
        String message = MessageMaker.get1(temp,givenUrl,tagMustBe);
        result(givenUrl);
        Assertions.assertTrue(isTagInScenarioTags, message);
    }

    @То("получить информацию по персонажу Морти отправив \"(/api/character/2)\" запрос.$")
    public void getMortyInfo(String api) {
        this.jsonFromRequest1 = ApiTools.sendGetRequest(urlTest1, api);
        result(jsonFromRequest1.toString());
        Assertions.assertNotNull(jsonFromRequest1);
    }

    @И("выбрать из ответа последний эпизод, где появлялся Морти.$")
    public void getLastMortyEpisode(){
        int size = jsonFromRequest1.getJSONArray("episode").length();
        this.lastMortyEpisode = jsonFromRequest1.getJSONArray("episode").getString(size-1);
        result(lastMortyEpisode);
        Assertions.assertNotNull(lastMortyEpisode);
    }

    @И("получить из списка последнего эпизода последнего персонажа.$")
    public void getlastCharacter(){
        JSONObject json = ApiTools.sendGetRequest(urlTest1, lastMortyEpisode.replace(urlTest1, ""));
        int size = json.getJSONArray("characters").length();
        this.lastCharacter = json.getJSONArray("characters").getString(size-1);
        result(lastCharacter);
        Assertions.assertFalse(lastCharacter.isEmpty());
    }

    @И("получить данные по местонахождению и расе этого персонажа.$")
    public void getlastCharacterLocationSpecies(){
        JSONObject json = ApiTools.sendGetRequest(urlTest1, lastCharacter);
        String lastCharacterSpecies = json.getString("species");
        String lastCharacterLocation = json.getJSONObject("location").getString("name");
        this.lastCharacterLocationSpecies = lastCharacterSpecies + " || " + lastCharacterLocation;
        result(lastCharacterLocationSpecies);
        boolean cond1 = lastCharacterSpecies.isEmpty();
        boolean cond2 = lastCharacterLocation.isEmpty();
        Assertions.assertFalse(cond1 & cond2);
    }

    @Тогда("проверить, этот персонаж той же расы, но находится не там где Морти.$")
    public void isSameCharacterLocationSpecies(){
        JSONObject jsonMorty = jsonFromRequest1;
        List<String> list = new ArrayList<>(Arrays.asList(lastCharacterLocationSpecies.split(" \\|\\| ")));
        String lastCharacterSpecies = list.get(0);
        String lastCharacterLocation = list.get(1);
        String mortiSpecies = jsonMorty.getString("species");
        String mortiLocation = jsonMorty.getJSONObject("location").getString("name");
        boolean sameLocation = mortiLocation.equals(lastCharacterLocation);
        boolean sameSpecies = mortiSpecies.equals(lastCharacterSpecies);
        String message = "!mortiLocation.equals(lastCharacterLocation)= "+!sameLocation+
                          "mortiSpecies.equals(lastCharacterSpecies) = " + sameSpecies;
        Assertions.assertTrue(!sameLocation & sameSpecies, message);
    }


    @То("создать json объект.$")
    public void createJson(@NotNull Map<String, String> arg){
        for (Map.Entry<String, String> entry: arg.entrySet()) {
            JSONObject json = new JSONObject();
            json.put(entry.getKey(), entry.getValue());
            this.jsonObj = json;
            Assertions.assertNotNull(jsonObj);
        }
    }

    @И("из json объекта создать в проекте файл \"([^\"]*).json\".$")
    public void createJson(@NotNull String givenFileName) {
        this.fileName = givenFileName + ".json";
        boolean isFileCreated = (JsonTools.jsonFileWriter(jsonObj, fileName));     //Создали файл
        Assertions.assertTrue(isFileCreated, "Errors in file creation");
    }

    @Затем("изменить созданный json файл.$")
    public void getResponseBodyFromFileAndChange(@NotNull Map<String, String> arg){
        this.requestBody = JsonTools.jsonFileReader(fileName);      //Прочитали файл
        Assertions.assertNotNull(requestBody, "Errors in reading file");
        for (Map.Entry<String, String> entry: arg.entrySet()) {
            requestBody.put(entry.getKey(),entry.getValue());
        }
    }

    @И("отправить post запрос с телом ранее созданного файла и api \"(/api/users)\".$")
    public void sendTestRequest(@NotNull String api) {
        jsonFromRequest2 = ApiTools.sendPostRequest(requestBody, urlTest2, api);
        result(jsonFromRequest2.toString());
        Assertions.assertNotNull(jsonFromRequest2);
    }

    @Тогда("проверить что полученный с сервера ответ имеет валидные по значениям key и value.$")
    public void checkResponse(){
        JSONObject jsonResult = jsonFromRequest2;
        Assertions.assertEquals("Tomato", jsonResult.getString("name"));
        Assertions.assertEquals("Eat maket", jsonResult.getString("job"));
        Assertions.assertNotEquals("325", jsonResult.getString("id"), "Не валидный ID - ");
        Assertions.assertNotEquals("2021-08-03T10:22:44.071Z", jsonResult.getString("createdAt"));
    }

}



