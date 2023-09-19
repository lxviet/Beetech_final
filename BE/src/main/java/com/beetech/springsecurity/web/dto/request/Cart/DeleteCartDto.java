package com.beetech.springsecurity.web.dto.request.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartDto {
    private String token;
    private int versionNo;
    private int detailId;
    private int clearCart;



}
