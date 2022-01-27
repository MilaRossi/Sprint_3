package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
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
        Response response = courier.loginCourier(credentials);
        response.then().assertThat().statusCode(200);
        // извлекаем id курьера
        Integer id = response.then().extract().path("id");
        assertThat("id",notNullValue());
        // удаляем курьера
        courier.deleteCourier(id).then().assertThat().statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));

    }

    @Test
    public void loginCourierAndCheckResponse400() {
        // создание пары логин - пустой пароль
        Courier courier = new Courier();
        JSONObject json = courier.generateEmptyPassword();
        // логин без пароля
        Response response = courier.loginCourier(json);
        response.then().assertThat().statusCode(400).and().assertThat().body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginCourierAndCheckResponse404() {
        // создание пары логин - пароль
        Courier courier = new Courier();
        JSONObject json = courier.generateLoginPassword();
        // логин с несуществующей парой логин - пароль
        Response response = courier.loginCourier(json);
        response.then().assertThat().statusCode(404).and().assertThat().body("message",equalTo("Учетная запись не найдена"));
    }



}
