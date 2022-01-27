package ru.yandex.praktikum;

import io.restassured.RestAssured;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    // два поля класса: для JSONObject и ожидаемого результата
    JSONObject fields;
    int expected;

    // конструктор с двумя параметрами
    public OrderCreateTest(JSONObject fields, int expected) {
        this.fields = fields;
        this.expected = expected;
    }

    // метод для получения тестовых данных
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        Order order = new Order();
        JSONObject fields1 = order.fieldsOrderCreate( new String[]{"BLACK"});
        JSONObject fields2 = order.fieldsOrderCreate( new String[]{"GREY"});
        JSONObject fields3 = order.fieldsOrderCreate( new String[]{"BLACK", "GREY"});
        JSONObject fields4 = order.fieldsOrderCreate( new String[]{});

        return new Object[][]{
                {fields1, 201},
                {fields2, 201},
                {fields3, 201},
                {fields4, 201}
        };
    }

    // тест
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void orderCreateTest() {
        Order order = new Order();
        int actual = order.orderCreate(fields);
        assertEquals(expected, actual);
    }


}

