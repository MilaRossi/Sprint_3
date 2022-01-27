package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierCreateTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierAndCheckResponse201() {
        // создание пары логин-пароль
        Courier courier = new Courier();
        JSONObject credentials = courier.generateLoginPassword();
        // создание курьера
        Response response1 = courier.createCourier(credentials);
        response1.then().assertThat().statusCode(201)
            .and().assertThat().body("ok", equalTo(true));
        // логин курьера
        Response response2 = courier.loginCourier(credentials);
        response2.then().assertThat().statusCode(200);
        // извлекаем id курьера
        Integer id = response2.then().extract().path("id");
        assertThat("id",notNullValue());
        // удаляем курьера
        courier.deleteCourier(id).then().assertThat().statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));


    }

    @Test
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

}
