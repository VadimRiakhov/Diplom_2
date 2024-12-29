import api.OrderApi;
import api.UserApi;
import constants.Ingredients;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.OrderCreateData;
import model.UserCreateData;
import model.UserGeneratorData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderCreateTest {
    UserApi userApi;
    String accessToken="";

    // создание пользователя
    @Before
    public void setUp(){
        userApi = new UserApi();
        // генерируем пользователя
        UserCreateData userCreateData = UserGeneratorData.getRandomUser();
        // создаем пользователя и получаем ответ от сервера
        ValidatableResponse response = userApi.createUser(userCreateData);
        // получаем токен для последующего удаления пользователя
        accessToken = response.extract().path("accessToken");
    }

    // создание заказа авторизованным пользователем с валидными ингредиентами
    @Test
    @DisplayName("Создание заказа авторизованным пользователем с валидными ингредиентами")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void createOrderByAuthorizedUserResponseOkTest(){
        String[] ingredients = new String[]{Ingredients.BIOKOTLETA, Ingredients.BULKA_R2_D2, Ingredients.SOUS_GALAKTICHESKI};
        OrderApi orderApi = new OrderApi();
        OrderCreateData orderCreateData = new OrderCreateData(ingredients);
        // создаем заказ и получаем ответ от сервера
        ValidatableResponse response = orderApi.createOrder(orderCreateData, accessToken);
        // проверяем ответ сервера
        orderApi.checkResponseForOrderCreateWithValidIngredients(response);
    }

    // создание заказа неавторизованным пользователем с валидными ингредиентами
    @Test
    @DisplayName("Создание заказа неавторизованным пользователем с валидными ингредиентами")
    @Description("Позитивный тест, сервер должен вернуть 200 Ok")
    public void createOrderByUnauthorizedUserResponseOkTest(){
        String[] ingredients = new String[]{Ingredients.BIOKOTLETA, Ingredients.BULKA_R2_D2, Ingredients.SOUS_GALAKTICHESKI};
        OrderApi orderApi = new OrderApi();
        OrderCreateData orderCreateData = new OrderCreateData(ingredients);
        // создаем заказ и получаем ответ от сервера
        ValidatableResponse response = orderApi.createOrder(orderCreateData, "");
        // проверяем ответ сервера
        orderApi.checkResponseForOrderCreateWithValidIngredients(response);
    }

    // создание заказа авторизованным пользователем без ингредиентов
    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    @Description("Негативный тест, сервер должен вернуть 400 Bad Request")
    public void createOrderWithoutIngredientsResponseBadRequestTest(){
        String[] ingredients = new String[]{};
        OrderApi orderApi = new OrderApi();
        OrderCreateData orderCreateData = new OrderCreateData(ingredients);
        // создаем заказ и получаем ответ от сервера
        ValidatableResponse response = orderApi.createOrder(orderCreateData, accessToken);
        // проверяем ответ сервера
        orderApi.checkResponseForOrderCreateWithoutIngredients(response);
    }

    // создание заказа авторизованным пользователем с неправильным id ингредиентов
    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неправильным id ингредиентов")
    @Description("Негативный тест, сервер должен вернуть код 500")
    public void createOrderWithInvalidIngredientsResponseInternalServerErrorTest(){
        String invalidIngredientId = RandomStringUtils.randomAlphabetic(24);
        String[] ingredients = new String[]{invalidIngredientId};
        OrderApi orderApi = new OrderApi();
        OrderCreateData orderCreateData = new OrderCreateData(ingredients);
        // создаем заказ и получаем ответ от сервера
        ValidatableResponse response = orderApi.createOrder(orderCreateData, accessToken);
        // проверяем ответ сервера
        orderApi.checkResponseForOrderCreateWithInvalidIngredients(response);
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
