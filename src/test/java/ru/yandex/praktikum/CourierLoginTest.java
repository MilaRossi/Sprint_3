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

public class CourierLoginTest {
    Integer courierId = 0;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Логин курьера")
    public void loginCourierAndCheckResponse200() {
        // создание пары логин-пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateLoginPassword();
        // создание курьера
        courier.createCourier(credentials)
                .then().assertThat().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // логин курьера
        Response responseLoginCourier = courier.loginCourier(credentials);
        responseLoginCourier.then().assertThat().statusCode(200);
        // извлекаем id курьера
        courierId = responseLoginCourier.then().extract().path("id");
        assertThat("id",notNullValue());

    }

    @Test
    @DisplayName("Получить ошибку логина курьера без пароля")
    public void loginCourierAndCheckResponse400() {
        // создание пары логин - пустой пароль
        Courier courier = new Courier();
        JSONObject json = courier.generateEmptyPassword();
        // логин без пароля
        Response responseLoginCourierWithEmptyPassword = courier.loginCourier(json);
        responseLoginCourierWithEmptyPassword.then().assertThat().statusCode(400).and().assertThat().body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Получить ошибку логина курьера с несуществующей парой логин - пароль")
    public void loginCourierAndCheckResponse404() {
        // создание пары логин - пароль
        Courier courier = new Courier();
        JSONObject json = courier.generateLoginPassword();
        // логин с несуществующей парой логин - пароль
        Response responseLoginCourierWithNonExistentCredentials = courier.loginCourier(json);
        responseLoginCourierWithNonExistentCredentials.then().assertThat().statusCode(404).and().assertThat().body("message",equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteCourier() {
        if (courierId > 0) {
            given().header("Content-type", "application/json")
                    .delete("/api/v1/courier/" + courierId.toString());

        }
    }

}
