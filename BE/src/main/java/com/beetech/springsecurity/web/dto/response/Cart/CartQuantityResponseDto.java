package com.beetech.springsecurity.web.dto.response.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartQuantityResponseDto {
    private int version_no;
    private int total_quantity;
}
