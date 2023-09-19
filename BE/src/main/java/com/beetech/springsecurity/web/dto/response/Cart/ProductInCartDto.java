package com.beetech.springsecurity.web.dto.response.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCartDto {
    private String sku;
    private String productName;
    private long price;
}
