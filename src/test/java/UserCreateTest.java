import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserCreateData;
import model.UserGeneratorData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserCreateTest {

    UserApi userApi;
    String accessToken="";
    @Before
    public void setUp(){
        userApi = new UserApi();
    }

    @Test
    @DisplayName("Создание пользователя, который уже есть в системе")
    @Description("Негативный тест, сервер должен вернуть 403 Forbidden")
    public void createUserExistingCredentialsResponseForbiddenTest(){
        UserCreateData userCreateData = UserGeneratorData.getRandomUser();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // получаем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
        // повторно создаем пользователя и получаем ответ от сервера
        response = userApi.createUser(userCreateData);
        // проверяем ответ сервера
        userApi.checkResponseForUserCreateExistingCredentials(response);
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Негативный тест, сервер должен вернуть 403 Forbidden")
    public void createUserWithoutEmailResponseForbiddenTest(){
        UserCreateData userCreateData = UserGeneratorData.getRandomUserWithoutEmail();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // проверяем ответ сервера
        userApi.checkResponseForUserCreateWithoutMandatoryField(response);
    }


    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Негативный тест, сервер должен вернуть 403 Forbidden")
    public void createUserWithoutPasswordResponseForbiddenTest(){
        UserCreateData userCreateData = UserGeneratorData.getRandomUserWithoutPassword();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // проверяем ответ сервера
        userApi.checkResponseForUserCreateWithoutMandatoryField(response);
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Негативный тест, сервер должен вернуть 403 Forbidden")
    public void createUserWithoutNameResponseForbiddenTest(){
        UserCreateData userCreateData = UserGeneratorData.getRandomUserWithoutName();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // проверяем ответ сервера
        userApi.checkResponseForUserCreateWithoutMandatoryField(response);
    }

    // удаление созданного пользователя
    @After
    public void cleanUp(){
        if(!accessToken.isEmpty()) {
            userApi.deleteUser(accessToken);
            accessToken = "";
        }
    }
}
