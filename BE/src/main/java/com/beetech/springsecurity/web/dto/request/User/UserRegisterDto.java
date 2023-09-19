package com.beetech.springsecurity.web.dto.request.User;


import com.beetech.springsecurity.web.anotation.ValidDateFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegisterDto {
    @NotEmpty
    @Email(message = "Login ID must be a valid email address")
    @Size(max = 255)
    String loginId;

    @NotEmpty
    @Size(min = 8, max = 32)
    String password;

    @NotEmpty
    @Size(min = 8, max = 255)
    String userName;

    @NotNull
    @ValidDateFormat(message = "Invalid date format. Use yyyy-MM-dd.")
    String birthDay;
}
