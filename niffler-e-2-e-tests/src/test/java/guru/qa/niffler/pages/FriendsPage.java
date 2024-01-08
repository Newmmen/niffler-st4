package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    @Step("check friend's table has appropriate description")
    public FriendsPage checkFriendTableHasCorrectDescriptionByText(String descriptionText) {
        $(".main-content__section")
                .$(byText(descriptionText))
                .shouldBe(visible);
        return this;
    }
}