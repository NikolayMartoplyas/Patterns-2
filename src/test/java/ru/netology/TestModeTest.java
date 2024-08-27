package ru.netology;

//import lombok.var;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class TestModeTest {

    @BeforeEach
    void setUp(){
        open("http:localhost:9999");
    }

    @Test
    void userPresence(){
//        наличие пользователя, позитивный тест
        var registredUser = UserGenerator.Registration.generateUser("en", "active");
        SelenideElement form = $(".form");
        form.$("[data-test-id='login'] input").setValue(registredUser.getLogin());
        form.$("[data-test-id='password'] input").setValue(registredUser.getPassword());
        form.$("[data-test-id='action-login']").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
    @Test
    void userIsBlocked(){
//        пользователь заблокирован
        var registredUser = UserGenerator.Registration.generateUser("en", "blocked");
        SelenideElement form = $(".form");
        form.$("[data-test-id='login'] input").setValue(registredUser.getLogin());
        form.$("[data-test-id='password'] input").setValue(registredUser.getPassword());
        form.$(".button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
    @Test
    void invalidLogin(){
        var registredUser = UserGenerator.Registration.generateUser("en", "active");
        var invalidLogin = UserGenerator.Registration.generateLogin("en");
        SelenideElement form = $(".form");
        form.$("[data-test-id='login'] input").setValue(invalidLogin);
        form.$("[data-test-id='password'] input").setValue(registredUser.getPassword());
        form.$(".button").click();

    }
}
