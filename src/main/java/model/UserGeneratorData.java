package model;

import net.datafaker.Faker;
import io.qameta.allure.Step;

public class UserGeneratorData {

    private static Faker faker = new Faker();

    @Step("Генерация пользователя с полями name, password, email")
    public static UserCreateData getRandomUser(){
        String name = faker.name().firstName();
        String password = faker.internet().password(6, 10, true);
        String email = faker.internet().emailAddress();
        return new UserCreateData(name, password, email);
    }

    @Step("Генерация пользователя без поля name")
    public static UserCreateData getRandomUserWithoutName(){
        return  UserCreateData.builder()
                .password(faker.internet().password(6, 10, true))
                .email(faker.internet().emailAddress())
                .build();
    }

    @Step("Генерация пользователя без поля password")
    public static UserCreateData getRandomUserWithoutPassword(){
        return  UserCreateData.builder()
                .name(faker.name().firstName())
                .email(faker.internet().emailAddress())
                .build();
    }

    @Step("Генерация пользователя без поля email")
    public static UserCreateData getRandomUserWithoutEmail(){
        return  UserCreateData.builder()
                .name(faker.name().firstName())
                .password(faker.internet().password(6, 10, true))
                .build();
    }

    @Step("Генерация пользователя только с полем name")
    public static UserCreateData getRandomUserWithNameOnly(){
        return  UserCreateData.builder()
                .name(faker.name().firstName())
                .build();
    }

    @Step("Генерация пользователя только с полем password")
    public static UserCreateData getRandomUserWithPasswordOnly(){
        String password = faker.internet().password(6, 10, true);
        return  UserCreateData.builder()
                .password(password)
                .build();
    }

    @Step("Генерация пользователя только с полем email")
    public static UserCreateData getRandomUserWithEmailOnly(){
        return  UserCreateData.builder()
                .email(faker.internet().emailAddress())
                .build();
    }

}
