package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    @Step("Do login with data")
    public void doLoginWithData(String username, String password) {
        openMainPage();
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(username);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();
    }

    private void openMainPage() {
        Selenide.open("http://127.0.0.1:3000/main");
    }
}
