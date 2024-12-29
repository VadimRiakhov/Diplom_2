package model;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGeneratorData {
    @Step("Генерация пользователя с полями name, password, email")
    public static UserCreateData getRandomUser(){
        String name = RandomStringUtils.randomAlphabetic(8);
        String password = RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(8)+"@yandex.ru";
        return new UserCreateData(name, password, email);
    }

    @Step("Генерация пользователя без поля name")
    public static UserCreateData getRandomUserWithoutName(){
        return  UserCreateData.builder()
                .password(RandomStringUtils.randomAlphabetic(8))
                .email(RandomStringUtils.randomAlphabetic(8)+"@yandex.ru")
                .build();
    }

    @Step("Генерация пользователя без поля password")
    public static UserCreateData getRandomUserWithoutPassword(){
        return  UserCreateData.builder()
                .name(RandomStringUtils.randomAlphabetic(8))
                .email(RandomStringUtils.randomAlphabetic(8)+"@yandex.ru")
                .build();
    }

    @Step("Генерация пользователя без поля email")
    public static UserCreateData getRandomUserWithoutEmail(){
        return  UserCreateData.builder()
                .name(RandomStringUtils.randomAlphabetic(8))
                .password(RandomStringUtils.randomAlphabetic(8))
                .build();
    }

    @Step("Генерация пользователя только с полем name")
    public static UserCreateData getRandomUserWithNameOnly(){
        return  UserCreateData.builder()
                .name(RandomStringUtils.randomAlphabetic(8))
                .build();
    }

    @Step("Генерация пользователя только с полем password")
    public static UserCreateData getRandomUserWithPasswordOnly(){
        return  UserCreateData.builder()
                .password(RandomStringUtils.randomAlphabetic(8))
                .build();
    }

    @Step("Генерация пользователя только с полем email")
    public static UserCreateData getRandomUserWithEmailOnly(){
        return  UserCreateData.builder()
                .email(RandomStringUtils.randomAlphabetic(8)+"@yandex.ru")
                .build();
    }

}
