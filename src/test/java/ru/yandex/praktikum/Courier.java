package ru.yandex.praktikum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;

public class Courier {

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

    public Response createCourier(JSONObject creds ) {
        return given().header("Content-type", "application/json")
                .body(creds.toString()).when().post("/api/v1/courier");
    }


    public Response loginCourier(JSONObject creds ) {
        return given().header("Content-type", "application/json")
                .body(creds.toString()).when().post("/api/v1/courier/login");
    }

    public Response deleteCourier(Integer id) {
        return given().header("Content-type", "application/json")
                .delete("/api/v1/courier/" + id.toString());

    }

}