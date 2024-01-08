package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.model.UserType.INCOMING_REQUEST;
import static guru.qa.niffler.model.UserType.SENT_REQUEST;
import static guru.qa.niffler.model.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest {
    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();
    FriendsPage friendsPage = new FriendsPage();

    @Test
    @DisplayName("Check user with friend has correct text in friend's table")
    void checkUserWithSentRequestHasNoFriendsInTable(@User(SENT_REQUEST) UserJson user) {
        loginPage.doLoginWithData(user.username(), user.testData().password());
        mainPage.openFriendsPage();
        friendsPage.checkFriendTableHasCorrectDescriptionByText("There are no friends yet!");
    }

    @Test
    @DisplayName("Check user with friend has correct text in friend's table")
    void checkUserWithFriendHasCorrectTextInFriendsTable(@User(WITH_FRIENDS) UserJson user) {
        loginPage.doLoginWithData(user.username(), user.testData().password());
        mainPage.openFriendsPage();
        friendsPage.checkFriendTableHasCorrectDescriptionByText("You are friends");
    }

    @Test
    @DisplayName("Check user with incoming request has sent request username in friend's table")
    void checkUserWithIncomingRequestHasSentRequestUserInFriendsTable(@User(SENT_REQUEST) UserJson sentRequestUser,
                                                                      @User(INCOMING_REQUEST)
                                                                              UserJson incomeRequestUser) {
        loginPage.doLoginWithData(sentRequestUser.username(), sentRequestUser.testData().password());
        mainPage.openFriendsPage();
        friendsPage.checkFriendTableHasCorrectDescriptionByText("There are no friends yet!");
        loginPage.closeBrowser()
                .doLoginWithData(incomeRequestUser.username(), incomeRequestUser.testData().password());
        mainPage.checkRequestSign()
                .openFriendsPage();
        friendsPage.checkFriendTableHasCorrectDescriptionByText(sentRequestUser.username());
    }
}
