package com.beetech.springsecurity.web.dto.request.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {
    private int versionNo;
    private String address;
    private String district;
    private String phoneNumber;
    private String city;

}
