package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class CourierCreateTest {

    Integer courierId = 0;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создать курьера")
    public void createCourierAndCheckResponse201() {
        // создание пары логин-пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateLoginPassword();
        // создание курьера
        Response responseCreateCourier = courier.createCourier(credentials);
        Integer expectedStatusCodeCreateCourier = 201;
        Integer actualStatusCodeCreateCourier = responseCreateCourier.statusCode();
        assertEquals("Код ответа отличен от 201", expectedStatusCodeCreateCourier, actualStatusCodeCreateCourier);
        Boolean actualAnswer = responseCreateCourier.then().extract().path("ok");
        assertTrue("В ответе ок", actualAnswer);
        // логин курьера
        Response responseLoginCourier = courier.loginCourier(credentials);
        // извлекаем id курьера
        courierId = responseLoginCourier.then().extract().path("id");

    }

    @Test
    @DisplayName("Получить ошибку создания курьера с пустым паролем")
    public void createCourierAndCheckResponse400() {
        // создание пары логин - пустой пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateEmptyPassword();
        // создание курьера
        Response responseCreateCourierWithEmptyPassword = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials.toString())
                .when()
                .post("/api/v1/courier");
        responseCreateCourierWithEmptyPassword.then().assertThat().statusCode(400)
                        .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }


    @Test
    @DisplayName("Получить ошибку создания курьера с зарезервированной парой логин-пароль")
    public void createCourierAndCheckResponse409() {
        // создание пары логин-пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateLoginPassword();
        // создание курьера
        Response responseCreateCourier = given().header("Content-type", "application/json")
                .and()
                .body(credentials.toString())
                .when()
                .post("/api/v1/courier");
        responseCreateCourier.then().assertThat().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));
        // повторное создание курьера
        Response responseCreateCourierWithReservedCredentials = given().header("Content-type", "application/json").and().body(credentials.toString()).when().post("/api/v1/courier");
        responseCreateCourierWithReservedCredentials.then().assertThat().statusCode(409)
            .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @After
    public void deleteCourier() {
        if (courierId > 0) {
            given().header("Content-type", "application/json")
                    .delete("/api/v1/courier/" + courierId.toString());
        }
    }

}
