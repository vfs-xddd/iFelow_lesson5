package utils;

import com.sun.istack.NotNull;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;

/**Класс для апи хуков.
 * <p>Содержит методы для отправки запросов.</p>
 * @author Maksim_Kachaev */
public class ApiTools {

    /**Get-запрос
     * @param baseUrl базовый http адрес
     * @param api апи к запросу
     * @return ответ сервера в виде готового json*/
    @Step("api запрос:")
    @DisplayName("Отправить get запрос")
    public static JSONObject sendGetRequest(@NotNull String baseUrl, @NotNull String api) {

//        RequestSpecification requestSpec = new RequestSpecBuilder().build()
//                .given().baseUri("https://rickandmortyapi.com")
//                .contentType(ContentType.JSON)
//                .log().all();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        Response response1 = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                //.log().all()
                .when()
                .get(api)
                .then()
                .spec(responseSpec).extract().response();

        String resp1 = response1.getBody().asString();
        return new JSONObject(resp1);
    }

    /**Post-запрос
     * @param baseUrl базовый http адрес
     * @param requestBody тело запроса
     * @param api апи к запросу
     * @return ответ сервера в виде готового json*/
    @Step("Отправить post запрос:")
    @DisplayName("Отправить post запрос")
    public static JSONObject sendPostRequest(@NotNull JSONObject requestBody, @NotNull String baseUrl, @NotNull String api){
        Response response2 = given()
                .baseUri(baseUrl)
                .contentType("application/json;charset=UTF-8")
                //.log().all()
                .when()
                .body(requestBody.toString())
                .post(api)
                .then()
                .statusCode(201)
                //.log().all()
                .extract().response();

        return new JSONObject(response2.getBody().asString());
    }

}
