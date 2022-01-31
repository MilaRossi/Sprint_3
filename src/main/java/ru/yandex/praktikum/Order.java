package ru.yandex.praktikum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;

public class Order {

    public JSONObject fieldsOrderCreate(String[] color) {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String phone = RandomStringUtils.randomAlphabetic(10);
        String comment = RandomStringUtils.randomAlphabetic(10);
        JSONObject json = new JSONObject();
        json.put("firstName", firstName);
        json.put("lastName", lastName);
        json.put("address", address);
        json.put("metroStation", 4);
        json.put("phone", phone);
        json.put("rentTime", 2);
        json.put("deliveryDate", "2022-01-01");
        json.put("comment", comment);
        if(color.length != 0) {
            json.put("color", color);

        }
        return json;

    }

    @Step("Создание заказа")
    public boolean orderCreate(JSONObject fields) {
        Response response = given().header("Content-type", "application/json")
                .body(fields.toString()).when().post("/api/v1/orders");
        Integer track = response.then().extract().path("track");
        Integer statusCode = response.statusCode();
        if(statusCode == 201 && track > 0) {
            return true;
        }
        return false;
    }

    @Step("получить заказы")
    public Response getOrders() {
        return given().get("/api/v1/orders");
    }

}
