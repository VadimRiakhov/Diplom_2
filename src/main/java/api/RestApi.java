package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static constants.UrlConstants.STELLARBURGERS_MAIN_PAGE_URL;

public class RestApi {
    protected RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(STELLARBURGERS_MAIN_PAGE_URL)
                .setContentType(ContentType.JSON)
                .build()
                .filter(new AllureRestAssured())
                .log().all();
    }
}
