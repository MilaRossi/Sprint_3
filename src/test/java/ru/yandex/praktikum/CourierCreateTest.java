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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
        Response response1 = courier.createCourier(credentials);
        Integer expectedStatusCodeCreateCourier = 201;
        Integer actualStatusCodeCreateCourier = response1.statusCode();
        assertEquals("Код ответа отличен от 201", expectedStatusCodeCreateCourier, actualStatusCodeCreateCourier);
        Boolean actualAnswer = response1.then().extract().path("ok");
        assertTrue("В ответе ок", actualAnswer);
        // логин курьера
        Response response2 = courier.loginCourier(credentials);
//        Integer expectedStatusCodeLoginCourier = 200;
//        Integer actualStatusCodeLoginCourier = response2.statusCode();
//        assertEquals("Код ответа отличен от 200", expectedStatusCodeLoginCourier, actualStatusCodeLoginCourier);
        // извлекаем id курьера
        courierId = response2.then().extract().path("id");
//        assertNotNull("ID не получен", courierId);
//        // удаляем курьера
//        courier.deleteCourier(id).then().assertThat().statusCode(200)
//                .and()
//                .assertThat().body("ok", equalTo(true));


    }

    @Test
    @DisplayName("Получить ошибку создания курьера с пустым паролем")

    public void createCourierAndCheckResponse400() {
        // создание пары логин - пустой пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateEmptyPassword();
        // создание курьера
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials.toString())
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(400)
                        .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }


    @Test
    @DisplayName("Получить ошибку создания курьера с зарезервированной парой логин-пароль")

    public void createCourierAndCheckResponse409() {
        // создание пары логин-пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateLoginPassword();
        // создание курьера
        Response responseFirst = given().header("Content-type", "application/json")
                .and()
                .body(credentials.toString())
                .when()
                .post("/api/v1/courier");
        responseFirst.then().assertThat().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));
        // повторное создание курьера
        Response responseSecond = given().header("Content-type", "application/json").and().body(credentials.toString()).when().post("/api/v1/courier");
        responseSecond.then().assertThat().statusCode(409)
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
