package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginData {
    // пароль пользователя
    private String password;
    // email пользователя
    private String email;

    // конструктор создания экзмепляра из объекта класса UserCreateData
    public UserLoginData(UserCreateData userCreateData){
        this.password = userCreateData.getPassword();
        this.email = userCreateData.getEmail();
    }


}
