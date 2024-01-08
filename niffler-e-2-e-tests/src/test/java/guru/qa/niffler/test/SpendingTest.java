package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.DeleteUserSpendingsIfExist;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.SpendingPages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendingTest {
    private final LoginPage loginPage = new LoginPage();
    private final SpendingPages spendingPages = new SpendingPages();
    private final String category = "AnotherTest1";

    static {
        Configuration.browserSize = "3000x1980";
    }

    @BeforeEach
    void prepareForTest() {
        loginPage.doLoginWithData("DUCK", "Admin");
    }

    @GenerateCategory(
            username = "DUCK",
            description = category
    )

    @DeleteUserSpendingsIfExist(
            username = "DUCK"
    )
    @GenerateSpend(
            username = "DUCK",
            description = "QA.GURU Advanced 4",
            category = category,
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        spendingPages.clickFirstSpendingByDescription(spend.description())
                .deleteSelectedSpending()
                .checkSpendingTableSizeEqualsZero();
    }
}
