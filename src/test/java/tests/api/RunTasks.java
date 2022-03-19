package tests.api;

import hooks.testConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RunTasks extends testConfig {

    public final String url = System.getProperty("url");     //base url for all api
    public final String defaultPath = "./src/test/java/";

    @DisplayName("Отправить аpi запрос")
    @Description( "api запрос: {api}")
    public JSONObject sendGetRequest(String api) {

//        RequestSpecification requestSpec = new RequestSpecBuilder().build()
//                .given().baseUri("https://rickandmortyapi.com")
//                .contentType(ContentType.JSON)
//                .log().all();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        Response response1 = given()
                .baseUri(url)
                .contentType(ContentType.JSON)
                //.log().all()
                .when()
                .get(api)
                .then()
                .spec(responseSpec).extract().response();

        String resp1 = response1.getBody().asString();
        return new JSONObject(resp1);
    }

    @DisplayName("Создать Json файл")
    public Boolean jsonFileWriter(JSONObject json) {
        String path = defaultPath + "tests/api/requestBody.json";
        try {
            FileWriter file = new FileWriter(path);
            file.write(json.toString());
            file.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("some IO errors");
            e.printStackTrace();
            return false;
        }

    }

    @DisplayName("Прочитать Json файл")
    public JSONObject jsonFileReader(){
        String filePath = defaultPath + "tests/api/requestBody.json";
        try {
            JSONParser jsonParser = new JSONParser();
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) jsonParser.parse(new FileReader(filePath));
            JSONObject jsonConverted = new JSONObject(json.toJSONString());
            return jsonConverted;
        }
        catch (IOException | ParseException e) {
            System.out.println("some IO Parser errors");
            e.printStackTrace();
            return null;
        }

    }

    @Tag("1api")
    @Test
    @DisplayName("Погружение в API")
    @Description("Найти информацию по персонажу Морти Смит.")
    public void getMortyInfo(){
        JSONObject json = sendGetRequest("/api/character/2");
        String lastMortyEpisode = getLastMortyEpisode(json);
        print("последний эпизод с морти = "+lastMortyEpisode);
        String lastCharacter = getlastCharacter(lastMortyEpisode);
        print("последний персонажа из последнего эпизода = "+lastMortyEpisode);
        String lastCharacterLocationSpecies = getlastCharacterLocationSpecies(lastCharacter);
        print("данные по местонахождению и расе этого персонажа = "+lastCharacterLocationSpecies);
        List <Boolean> list = isSameCharacterLocationSpecies(json,lastCharacterLocationSpecies);
        print("same locations? - "+ list.get(0)+ " || same species? - "+ list.get(1));
    }

    @Step
    @DisplayName("Выбрать из ответа последний эпизод, где появлялся Морти")
    public String getLastMortyEpisode(JSONObject json){
        int size = json.getJSONArray("episode").length();
        return json.getJSONArray("episode").getString(size-1);
    }

    @Step
    @DisplayName("Вывести ответ:")
    @Description("Вывести ответ: {report}")
    public void print(String report){
        System.out.println(report);
    }

    @Step
    @DisplayName("Получить из списка последнего эпизода последнего персонажа")
    public String getlastCharacter(String lastMortyEpisode){
        JSONObject json = sendGetRequest(lastMortyEpisode.replace(url, ""));
        int size = json.getJSONArray("characters").length();
        return json.getJSONArray("characters").getString(size-1);
    }

    @Step
    @DisplayName("Получить данные по местонахождению и расе этого персонажа")
    public String getlastCharacterLocationSpecies(String lastCharacter){
        JSONObject json = sendGetRequest(lastCharacter);
        String lastCharacterSpecies = json.getString("species");
        String lastCharacterLocation = json.getJSONObject("location").getString("name");
        return lastCharacterSpecies + " || " + lastCharacterLocation;
    }

    @Step
    @DisplayName("Проверить, этот персонаж той же расы и находится там же где и Морти?")
    public List<Boolean> isSameCharacterLocationSpecies(JSONObject jsonMorty, String lastCharacterLocationSpecies){
        List<String> list = new ArrayList<>(Arrays.asList(lastCharacterLocationSpecies.split(" \\|\\| ")));
        String lastCharacterSpecies = list.get(0);
        String lastCharacterLocation = list.get(1);
        String mortiSpecies = jsonMorty.getString("species");
        String mortiLocation = jsonMorty.getJSONObject("location").getString("name");
        return new ArrayList<>(Arrays.asList(mortiLocation.equals(lastCharacterLocation), mortiSpecies.equals(lastCharacterSpecies)));
    }

    @Tag("2api")
    @Test
    @DisplayName("Углубление в API")
    public void task2() {
        createJson();
        JSONObject responseBody = getResponseBodyFromFileAndChange();
        JSONObject resJson = createTestResponse(responseBody);
        checkResponse(resJson);
    }


    @Step
    @DisplayName("Создать в проекте файл с расширением .Json ")
    @Description("передать в файл { \"name\": \"Potato\" } ")
    public void createJson(){
        JSONObject json = new JSONObject();
        json.put("name", "Potato");
        if (!jsonFileWriter(json)) System.out.println("didnt write a file");                                              //Создали файл
    }

    @Step
    @DisplayName("Изменить json файл")
    @Description("передать в файл { \"name\": \"Tomato\", \"job\": \"Eat maket\" } ")
    public JSONObject getResponseBodyFromFileAndChange(){
        JSONObject requestBody = jsonFileReader(); if (requestBody == null) return null;     //Прочитали файл
        requestBody.put("name", "Tomato");
        requestBody.put("job", "Eat maket" );                                             //Создали файл
        return requestBody;
    }

    @Step
    @DisplayName("Создать запрос")
    @Description("Создать тестовый запрос {}")
    public JSONObject createTestResponse(JSONObject requestBody){
        Response response2 = given()
                .baseUri("https://reqres.in")
                .contentType("application/json;charset=UTF-8")
                //.log().all()
                .when()
                .body(requestBody.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                //.log().all()
                .extract().response();

        return new JSONObject(response2.getBody().asString());
    }

    @Step
    @DisplayName("Проверить ответ сервера")
    @Description("Свериться, что полученный response имеет валидные данные по значениям key и value.")
    public void checkResponse(JSONObject jsonResult){
        Assertions.assertEquals("Tomato", jsonResult.getString("name"));
        Assertions.assertEquals("Eat maket", jsonResult.getString("job"));
        Assertions.assertEquals("325", jsonResult.getString("id"), "Не валидный ID - ");
        Assertions.assertEquals("2021-08-03T10:22:44.071Z", jsonResult.getString("createdAt"));
    }
}



