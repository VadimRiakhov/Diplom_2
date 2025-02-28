package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.OrderCreateData;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class OrderApi extends RestApi{

    @Step("Создание заказа")
    public ValidatableResponse createOrder(OrderCreateData order, String accessToken){
        return given()
                .spec(requestSpecification())
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(EndPoints.ORDER_CREATE)
                .then();
    }

    @Step("Получение списка заказов пользователя")
    public ValidatableResponse getOrdersByUser(String accessToken){
        return given()
                .spec(requestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(EndPoints.ORDER_GET_BY_USER)
                .then();
    }

    @Step("Проверка статуса и тела ответа при создании заказа с валидными ингредиентами")
    public void checkResponseForOrderCreateWithValidIngredients(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(matchesJsonSchemaInClasspath("orderCreateSchema.json"));
    }

    @Step("Проверка статуса и тела ответа при создание заказа без ингредиентов")
    public void checkResponseForOrderCreateWithoutIngredients(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка статуса и тела ответа при создание заказа c неверным id ингредиентов")
    public void checkResponseForOrderCreateWithInvalidIngredients(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Проверка статуса и тела ответа при получении списка заказов авторизованного пользователя")
    public void checkResponseForGetOrdersByAuthorizedUser(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(matchesJsonSchemaInClasspath("ordersGetByUserSchema.json"));
    }

    @Step("Проверка статуса и тела ответа при получении списка заказов неавторизованного пользователя")
    public void checkResponseForGetOrdersByUnauthorizedUser(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
