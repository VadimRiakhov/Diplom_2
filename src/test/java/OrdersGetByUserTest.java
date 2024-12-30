import api.OrderApi;
import api.UserApi;
import constants.Ingredients;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.OrderCreateData;
import model.UserCreateData;
import model.UserGeneratorData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrdersGetByUserTest {
    private UserApi userApi;
    private OrderApi orderApi;
    private String accessToken="";
    // создание пользователя
    @Before
    public void setUp(){
        userApi = new UserApi();
        orderApi = new OrderApi();
        // создаем пользователя
        UserCreateData userCreateData = UserGeneratorData.getRandomUser();
        ValidatableResponse response = userApi.createUser(userCreateData);
        // получаем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
        //создаем заказ с токеном созданного пользователя
        String[] ingredients = new String[]{Ingredients.BIOKOTLETA, Ingredients.BULKA_R2_D2, Ingredients.SOUS_GALAKTICHESKI};
        OrderCreateData orderCreateData = new OrderCreateData(ingredients);
        orderApi.createOrder(orderCreateData, accessToken);
        //создаем второй заказ с токеном созданного пользователя
        ingredients = new String[]{Ingredients.KRATORNAYA_BULKA_N200I, Ingredients.FILE_TETRAODONTIMFORMA, Ingredients.SPACE_SAUCE};
        orderCreateData = new OrderCreateData(ingredients);
        orderApi.createOrder(orderCreateData, accessToken);
    }

    // получение списка заказов авторизованного пользователя
    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Позитивный тест должен вернуть максимум 50 заказов пользователя")
    public void getOrdersOfAuthorizedUserResponseOkTest(){
        // получаем список заказов
        ValidatableResponse response = orderApi.getOrdersByUser(accessToken);
        // проверяем ответ сервера
        orderApi.checkResponseForGetOrdersByAuthorizedUser(response);
    }

    // получение списка заказов неавторизованного пользователя
    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    @Description("Негативный тест, сервер должен вернуть 401 Unauthorised")
    public void getOrdersOfUnauthorizedUserResponseUnauthorizedTest(){
        // получаем список заказов
        ValidatableResponse response = orderApi.getOrdersByUser("");
        // проверяем ответ сервера
        orderApi.checkResponseForGetOrdersByUnauthorizedUser(response);
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
