package com.beetech.springsecurity.web.dto.request.User;

import com.beetech.springsecurity.web.anotation.ValidDateFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    @NotEmpty
    @Email
    @Size(max = 255)
    private String loginId;

    @NotEmpty
    @Size(min = 8, max = 32)
    private String password;

    @NotEmpty
    @Size(min = 8, max = 255)
    private String fullName;

    @ValidDateFormat(message = "Invalid date format. Use yyyy-MM-dd.")
    private String birthday;
}
