package com.beetech.springsecurity.web.dto.response.Cart;

import com.beetech.springsecurity.domain.entity.Product;
import com.beetech.springsecurity.web.dto.response.Product.ProductDto;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class CartDto {

    private int version_no;
    private long total_price;
    private List<CartDetailDto> cartDetailDtos;

}
