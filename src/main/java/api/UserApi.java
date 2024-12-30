package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.UserCreateData;
import model.UserLoginData;
import org.apache.http.HttpStatus;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserApi extends RestApi{

    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserCreateData user){
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(EndPoints.USER_CREATE)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserLoginData user){
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(EndPoints.USER_LOGIN)
                .then();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse modifyUser(String accessToken, UserCreateData user){
        return given()
                .spec(requestSpecification())
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(EndPoints.USER_MODIFY)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(requestSpecification())
                .header("Authorization", accessToken)
                .when()
                .delete(EndPoints.USER_DELETE)
                .then()
                .log().all();
    }

    @Step("Проверка статуса и тела ответа при создании пользователя с валидными данными")
    public void checkResponseForUserCreateValidCredentials(ValidatableResponse response){
        // проверка статуса ответа и соответствие ответа схеме Json
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(matchesJsonSchemaInClasspath("userCreateSchema.json"));
    }

    @Step("Проверка статуса и тела ответа при создании пользователя без обязательного поля")
    public void checkResponseForUserCreateWithoutMandatoryField(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка статуса и тела ответа при создании пользователя с повторяющимися учетными данными")
    public void checkResponseForUserCreateExistingCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка статуса и тела ответа при авторизации пользователя с валидными данными")
    public void checkResponseForUserLoginValidCredentials(ValidatableResponse response){
        // проверка статуса ответа и соответствие ответа схеме Json
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(matchesJsonSchemaInClasspath("userCreateSchema.json"));
    }

    @Step("Проверка статуса и тела ответа при авторизации пользователя с невалидными данными")
    public void checkResponseForUserLoginInvalidCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка статуса и тела ответа при изменении данных пользователя с авторизацией")
    public void checkResponseForUserModifyWithAuthorization(ValidatableResponse response, String name, String email){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("user.name", equalTo(name))
                .body("user.email", equalTo(email))
                .body(matchesJsonSchemaInClasspath("userModifySchema.json"));
    }

    @Step("Проверка статуса и тела ответа при изменении данных пользователя без авторизации")
    public void checkResponseForUserModifyWithoutAuthorization(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка статуса и тела ответа при изменении данных пользователя и указании существующего email")
    public void checkResponseForUserModifyWithIndicateExistingEmail(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("User with such email already exists"));
    }
}
