package com.beetech.springsecurity.web.dto.request.User;

import com.beetech.springsecurity.web.anotation.ValidDateFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String userName;

    @Email
    @Size(max = 255)
    private String loginId;
    @ValidDateFormat(message = "Invalid date format. Use yyyy-MM-dd.")
    private String startBirthDay;
    @ValidDateFormat(message = "Invalid date format. Use yyyy-MM-dd.")
    private String endBirthDay;

    private long totalPrice;

}
