package ru.yandex.praktikum;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;


public class GenerateLoginPasswordForCourier {
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

}