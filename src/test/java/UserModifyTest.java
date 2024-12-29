import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserCreateData;
import model.UserGeneratorData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserModifyTest {
    UserApi userApi;
    String accessToken="";
    // создание пользователя
    @Before
    public void setUp(){
        userApi = new UserApi();
        // генерируем случайного пользователя
        UserCreateData userCreateData = UserGeneratorData.getRandomUser();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // получаем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение поля email с авторизацией")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void modifyUserEmailWithAuthorizationResponseOkTest(){
        // генерируем случайного пользователя только с полем email
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithEmailOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser(accessToken, userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля password с авторизацией")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void modifyUserPasswordWithAuthorizationResponseOkTest(){
        // генерируем случайного пользователя только с полем password
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithPasswordOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser(accessToken, userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля name с авторизацией")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void modifyUserNameWithAuthorizationResponseOkTest(){
        // генерируем случайного пользователя только с полем name
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithNameOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser(accessToken, userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля email без авторизациии")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorized")
    public void modifyUserEmailWithoutAuthorizationResponseUnauthorizedTest(){
        // генерируем случайного пользователя только с полем password
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithEmailOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser("", userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля password без авторизациии")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorized")
    public void modifyUserPasswordWithoutAuthorizationResponseUnauthorizedTest(){
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithPasswordOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser("", userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля name без авторизациии")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorized")
    public void modifyUserNameWithoutAuthorizationResponseUnauthorizedTest(){
        // генерируем случайного пользователя только с полем name
        UserCreateData userModifyData = UserGeneratorData.getRandomUserWithNameOnly();
        // изменяем данные пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.modifyUser("", userModifyData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Изменение поля email с указанием существующего email")
    @Description("Негативный тест, сервер должен вернуть 403 Forbidden")
    public void modifyUserWithIndicateExistingEmailResponseForbiddenTest(){
        // создаем еще одного пользователя
        UserCreateData otherUserCreateData = UserGeneratorData.getRandomUser();
        ValidatableResponse response = userApi.createUser(otherUserCreateData);
        // получаем токен, чтобы затем удалить пользователя
        String otherAccessToken = response.extract().path("accessToken");
        // пробуем изменить данные первого пользователя, подставляя данные нового пользователя
        ValidatableResponse otherResponse = userApi.modifyUser(accessToken, otherUserCreateData);
        // проверяем ответ сервера
        userApi.checkResponseForUserModifyWithIndicateExistingEmail(otherResponse);
        // удаляем нового пользователя
        userApi.deleteUser(otherAccessToken);
    }

    // удаление пользователя
    @After
    public void cleanUp(){
        if(!accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
            accessToken = "";
        }
    }
}
