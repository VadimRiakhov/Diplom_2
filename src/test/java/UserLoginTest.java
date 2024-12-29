import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserCreateData;
import model.UserGeneratorData;
import model.UserLoginData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {
    UserApi userApi;
    String accessToken="";
    UserLoginData userLoginData;

    // создание пользователя
    @Before
    public void setUp(){
        userApi = new UserApi();
        UserCreateData userCreateData = UserGeneratorData.getRandomUser();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // получаем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
        userLoginData = new UserLoginData(userCreateData);
    }

    @Test
    @DisplayName("Авторизация пользователя с валидными данными")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void loginUserValidCredentialsResponseOkTest(){
        // логин пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.loginUser(userLoginData);
        // проверяем ответ сервера
        userApi.checkResponseForUserLoginValidCredentials(response);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorized")
    public void loginUserWrongEmailResponseUnauthorizedTest(){
        String wrongEmail = RandomStringUtils.randomAlphabetic(8)+"@yandex.ru";
        userLoginData.setEmail(wrongEmail);
        // логин пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.loginUser(userLoginData);
        // проверяем ответ сервера
        userApi.checkResponseForUserLoginInvalidCredentials(response);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorized")
    public void loginUserWrongPasswordResponseUnauthorizedTest(){
        String wrongPassword = RandomStringUtils.randomAlphabetic(8);
        userLoginData.setPassword(wrongPassword);
        // логин пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.loginUser(userLoginData);
        // проверяем ответ сервера
        userApi.checkResponseForUserLoginInvalidCredentials(response);
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
