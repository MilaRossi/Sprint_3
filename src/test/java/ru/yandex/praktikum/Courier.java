package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;

public class Courier {
    @Step("создание пары логин-пароль")
    public JSONObject generateLoginPassword() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        JSONObject json = new JSONObject();
        json.put("login", courierLogin);
        json.put("password",courierPassword );
        return json;

    }

    public JSONObject generateEmptyPassword() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        JSONObject json = new JSONObject();
        json.put("login", courierLogin);
        json.put("password","" );
        return json;
    }
    @Step("создание курьера")
    public Response createCourier(JSONObject creds ) {
        return given().header("Content-type", "application/json")
                .body(creds.toString()).when().post("/api/v1/courier");
    }

    @Step("логин курьера")
    public Response loginCourier(JSONObject creds ) {
        return given().header("Content-type", "application/json")
                .body(creds.toString()).when().post("/api/v1/courier/login");
    }
//    @Step("удаляем курьера")
//    public Response deleteCourier(Integer id) {
//        return given().header("Content-type", "application/json")
//                .delete("/api/v1/courier/" + id.toString());
//
//    }

}