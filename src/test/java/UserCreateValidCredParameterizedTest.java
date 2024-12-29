import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserCreateData;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class UserCreateValidCredParameterizedTest {

    // имя пользователя
    private String name;
    // пароль пользователя
    private String password;
    // email пользователя
    private String email;

    UserApi userApi;
    String accessToken="";

    public UserCreateValidCredParameterizedTest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }


    @Parameterized.Parameters
    public static Object[][] getData(){
        return new Object[][]{
                {"Ivan", "123456Ivan", "ivan123456@yandex.ru"},
                {"IvanIvanIvanIvanIvanIvanIvanIvanIvan", "123456Ivan", "ivan123456@yandex.ru"},
                {"@#$%^&*", "123456Ivan", "ivan123456@yandex.ru"},
                {"Ivan", "123456Ivan", "ivan123456ivan123456ivan123456ivan123456ivan123456ivan123456ivan123456@yandex.ru"},
                {"Ivan", "123456Ivan", "#$%^&*@yandex.ru"},
                {"Ivan", "1111111111111111111111111111111111111", "ivan123456@yandex.ru"},
                {"Ivan", "@#$%^&*", "ivan123456@yandex.ru"}
        };
    }


    // создание пользователя с валидными учетными данными
    @Test
    @DisplayName("Регистрация пользователя с валидными данными")
    @Description("Успешная регистрации нового пользователя с валидными данными")
    public void createUserValidCredentialsResponseOkTest(){
        userApi = new UserApi();
        // создаем пользователя
        UserCreateData userCreateData = new UserCreateData(name, password, email);
        ValidatableResponse response = userApi.createUser(userCreateData);
        // проверяем ответ от сервера
        userApi.checkResponseForUserCreateValidCredentials(response);
        // сохраняем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
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
