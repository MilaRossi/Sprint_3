package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierAndCheckResponseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierAndCheckResponse201() {
        GenerateLoginPasswordForCourier courier = new GenerateLoginPasswordForCourier();
        JSONObject json = courier.generateLoginPassword();
        Response response = given().header("Content-type", "application/json").and().body(json.toString()).when().post("/api/v1/courier");
        response.then().assertThat().statusCode(201)
            .and().assertThat().body("ok", equalTo(true));

    }

    @Test
    public void createCourierAndCheckResponse400() {
        GenerateLoginPasswordForCourier courier = new GenerateLoginPasswordForCourier();
        JSONObject json = courier.generateEmptyPassword();
        Response response = given().header("Content-type", "application/json").and().body(json.toString()).when().post("/api/v1/courier");
        response.then().assertThat().statusCode(400).
            and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }


    @Test
    public void createCourierAndCheckResponse409() {
        GenerateLoginPasswordForCourier courier = new GenerateLoginPasswordForCourier();
        JSONObject json = courier.generateLoginPassword();
        Response responseFirst = given().header("Content-type", "application/json").and().body(json.toString()).when().post("/api/v1/courier");
        responseFirst.then().assertThat().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));
        Response responseSecond = given().header("Content-type", "application/json").and().body(json.toString()).when().post("/api/v1/courier");
        responseSecond.then().assertThat().statusCode(409)
            .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

}
