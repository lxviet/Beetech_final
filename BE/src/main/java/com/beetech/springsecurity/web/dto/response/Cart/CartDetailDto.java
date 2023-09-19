package com.beetech.springsecurity.web.dto.response.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private int quantity;
    private ProductInCartDto productInCartDto;
}
