package com.beetech.springsecurity.web.dto.response.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDate;

@Getter
@Setter
public class UserDto {
    private int id;
    private String loginId;
    private String userName;
    private LocalDate birthDay;
    private long totalPrice;

    private List<UserOrderDto> orderDtos;
}
