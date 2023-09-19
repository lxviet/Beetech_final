package com.beetech.springsecurity.web.dto.request.User;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {

    private String username;
    private String password;
}
