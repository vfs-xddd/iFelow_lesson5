import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class Run {
    public final String url = "https://rickandmortyapi.com";     //base url for all api
    public final String defaultPath = "./src/test/java/";

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

    public Boolean jsonFileWriter(JSONObject json) {
        String path = defaultPath + "requestBody.json";
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

    public org.json.simple.JSONObject jsonFileReader(){
        String filePath = defaultPath + "requestBody.json";
        try {
            JSONParser jsonParser = new JSONParser();
            return (org.json.simple.JSONObject) jsonParser.parse(new FileReader(filePath));
        }
        catch (IOException | ParseException e) {
            System.out.println("some IO Parser errors");
            e.printStackTrace();
            return null;
        }

    }


    //@Tag("1api")
    //@Test
    @DisplayName("Характер Морти")
    public void morti() {
        JSONObject json = sendGetRequest("/api/character/2");
        int size = json.getJSONArray("episode").length();
        String lastMortyEpisode = json.getJSONArray("episode").getString(size-1);
        String mortiSpecies = json.getString("species");
        String mortiLocation = json.getJSONObject("location").getString("name");
            System.out.println(lastMortyEpisode);
        json = sendGetRequest(lastMortyEpisode.replace(url, ""));
        size = json.getJSONArray("characters").length();
        String lastCharacter = json.getJSONArray("characters").getString(size-1);
            System.out.println(lastCharacter);
        json = sendGetRequest(lastCharacter);
        String lastCharacterSpecies = json.getString("species");
        String lastCharacterLocation = json.getJSONObject("location").getString("name");
            System.out.println(lastCharacterSpecies + " || " + lastCharacterLocation);
            System.out.println("same locations? - " + mortiLocation.equals(lastCharacterLocation));
            System.out.println("same species? - " + mortiSpecies.equals(lastCharacterSpecies));
    }

    @Tag("2api")
    @Test
    @DisplayName("test")
    public void test2() {
        String body = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Potato");
        if (!jsonFileWriter(requestBody)) return;
        org.json.simple.JSONObject json = jsonFileReader();
        if (json == null) return;
        System.out.println(json.get("name"));

//        JSONObject requestBody = new JSONObject();
//        requestBody.put("name", "morpheus");
//        requestBody.put("job", "leader");
//
//        Response response3 = given()
//                .baseUri("https://reqres.in/")
//                .contentType("application/json;charset=UTF-8")
//                .log().all()
//                .when()
//                .body(requestBody.toString())
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .log().all()
//                .extract().response();
//
//        JSONObject json = new JSONObject(response3);
//        Assertions.assertEquals(json.getString("name"), "morpheus");
//        Assertions.assertEquals(json.getString("job"), "leader");



//    String resp2 = response1.getBody().asString();
//    JSONObject json = new JSONObject(resp2);
//    int count = json.getJSONObject("info").getInt("count");
//    int jsonsize = json.getJSONArray("results").length();
//    String name = json.getJSONArray("results").getJSONObject(jsonsize-1).getJSONObject("origin").getString("name");
    }
}



