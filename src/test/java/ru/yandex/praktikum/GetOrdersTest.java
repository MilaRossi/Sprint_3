package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class GetOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получить заказы и проверить ответ")

    // получить заказы
    public void getOrdersAndCheckResponse() {
        Order order = new Order();
        Response response = order.getOrders();

        ArrayList orders = response.then().extract().path("orders");
        assertNotNull("Пустой список заказов", orders.size());

    }

}
