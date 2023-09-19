package com.beetech.springsecurity.web.dto.request.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserChangePasswordDto {
    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

}
