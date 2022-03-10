import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class test {

    RequestSpecification requestSpec = new RequestSpecBuilder().build()
            .given().baseUri("https://rickandmortyapi.com")
            .contentType(ContentType.JSON)
            .log().all();

    ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();


    //@Tag("1api")
   // @Test
    @DisplayName("Характер Морти")
    public void morti() {

        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/2")
                .then()
                .spec(responseSpec).extract().response();


        String resp1 = response1.getBody().asString();
        System.out.println(resp1);
    }

    //@Tag("2api")
    //@Test
    @DisplayName("test")
    public void test2() {
        String body = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "morpheus");
        requestBody.put("job", "leader");

        Response response3 = given()
                .baseUri("https://reqres.in/")
                .contentType("application/json;charset=UTF-8")
                .log().all()
                .when()
                .body(requestBody.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();

        JSONObject json = new JSONObject(response3);
        Assertions.assertEquals(json.getString("name"), "morpheus");
        Assertions.assertEquals(json.getString("job"), "leader");
    }
}

//    String resp2 = response1.getBody().asString();
//    JSONObject json = new JSONObject(resp2);
//    int count = json.getJSONObject("info").getInt("count");
//    int jsonsize = json.getJSONArray("results").length();
//    String name = json.getJSONArray("results").getJSONObject(jsonsize-1).getJSONObject("origin").getString("name");




