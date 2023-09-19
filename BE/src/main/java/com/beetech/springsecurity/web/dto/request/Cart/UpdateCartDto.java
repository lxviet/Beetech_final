package com.beetech.springsecurity.web.dto.request.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartDto {
    private String loginId;
    private String token;
    private int quantity;
    private int productId;
    private int versionNo;
}
