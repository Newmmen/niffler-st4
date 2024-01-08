package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingPages {

    @Step("click first spending in table by description {spendingDescription}")
    public SpendingPages clickFirstSpendingByDescription(String spendingDescription) {
        $(".spendings-table tbody")
                .$$("tr")
                .find(text(spendingDescription))
                .$("td")
                .click();
        return this;
    }

    @Step("check spending table size is zero")
    public SpendingPages checkSpendingTableSizeEqualsZero() {
        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(size(0));
        return this;
    }

    @Step("Delete selected spending")
    public SpendingPages deleteSelectedSpending() {
        $(byText("Delete selected"))
                .click();
        return this;
    }
}