package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;


import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto generateUser(String local, String status) {
            RegistrationDto user = new RegistrationDto(generateLogin(local), generatePassword(local), status);
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(user) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 ОК

            return user;
        }

        public static RegistrationDto getUser(String local, String status) {
            RegistrationDto user = new RegistrationDto(generateLogin(local), generatePassword(local), status);
            return user;
        }

        public static String generateLogin(String local) {
            Faker faker = new Faker(new Locale(local));
            return faker.name().fullName();
        }

        public static String generatePassword(String local) {
            Faker faker = new Faker(new Locale(local));
            return faker.internet().password();
        }

        @Value
        public static class RegistrationDto {
            String login;
            String password;
            String status;
        }
    }
}