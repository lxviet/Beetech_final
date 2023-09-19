package com.beetech.springsecurity.web.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    @NotEmpty
    @Email
    @Size(max = 255)
    String loginId;


    @NotEmpty
    @Size(min = 8, max = 32)
    String password;
}
