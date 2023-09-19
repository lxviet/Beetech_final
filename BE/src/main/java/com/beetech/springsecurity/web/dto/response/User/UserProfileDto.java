package com.beetech.springsecurity.web.dto.response.User;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileDto {
    private String fullName;
    private String username;
    private LocalDate birthDay;
}
