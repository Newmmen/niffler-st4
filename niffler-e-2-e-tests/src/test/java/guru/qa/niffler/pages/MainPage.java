package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    @Step("open friends page")
    public MainPage openFriendsPage() {
        $("img[src='/images/friends.svg']").click();
        return this;
    }

    @Step("Check friend has income request sign")
    public MainPage checkRequestSign() {
        $("a[href='/friends']")
                .$(".header__sign").shouldBe(visible);
        return this;
    }
}