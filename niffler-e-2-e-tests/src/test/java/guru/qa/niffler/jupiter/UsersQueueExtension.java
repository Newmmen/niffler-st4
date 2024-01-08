package guru.qa.niffler.jupiter;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static guru.qa.niffler.model.UserType.INCOMING_REQUEST;
import static guru.qa.niffler.model.UserType.SENT_REQUEST;
import static guru.qa.niffler.model.UserType.WITH_FRIENDS;


public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace USERS_NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Map<UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> incomingRequest = new ConcurrentLinkedQueue<>();
        Queue<UserJson> sentRequest = new ConcurrentLinkedQueue<>();
        friendsQueue.add(user("friend", "123456", WITH_FRIENDS));
        friendsQueue.add(user("friend1", "123456", WITH_FRIENDS));
        incomingRequest.add(user("incomeFriend1", "123456", INCOMING_REQUEST));
        sentRequest.add(user("sentRequest1", "123456", SENT_REQUEST));
        USERS.put(WITH_FRIENDS, friendsQueue);
        USERS.put(INCOMING_REQUEST, incomingRequest);
        USERS.put(SENT_REQUEST, sentRequest);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());

      Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(methods::add);

        List<Parameter> parameters = methods.stream()
                .map(Executable::getParameters)
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();

        Map<UserType, UserJson> usersForSave = new HashMap<>();
        for (Parameter parameter : parameters) {
            UserType userType = parameter.getAnnotation(User.class).value();
            if (usersForSave.containsKey(userType)) {
                continue;
            }

            UserJson testCandidate = null;
            Queue<UserJson> queue = USERS.get(userType);
            while (testCandidate == null) {
                testCandidate = queue.poll();
            }

            usersForSave.put(userType, testCandidate);
        }
        context.getStore(USERS_NAMESPACE).put(context.getUniqueId(), usersForSave);

    }


    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map<UserType, UserJson> usersFromTest = (Map<UserType, UserJson>) context.getStore(USERS_NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        for (UserType userType : usersFromTest.keySet()) {
            USERS.get(userType).add(usersFromTest.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserJson) extensionContext.getStore(USERS_NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(User.class).get().value());
    }

    private static UserJson user(String username, String password, UserType userType) {
        return new UserJson(
                null,
                username,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                new TestData(
                        password,
                        userType
                )
        );
    }
}
