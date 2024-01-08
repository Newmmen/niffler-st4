package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    @Step("Do login with data")
    public LoginPage doLoginWithData(String username, String password) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(username);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();
        return this;
    }

    @Step("user exit from application")
    public LoginPage closeBrowser() {
        Selenide.closeWebDriver();
        return this;
    }
}
