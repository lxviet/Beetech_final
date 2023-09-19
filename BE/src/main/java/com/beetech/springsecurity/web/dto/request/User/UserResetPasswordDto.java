package com.beetech.springsecurity.web.dto.request.User;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResetPasswordDto {
    private String changePasswordToken;

    @NotEmpty
    @Size(min = 8, max = 255)
    private String newPassword;
    @NotEmpty
    @Size(min = 8, max = 255)
    private String confirmPassword;
}
