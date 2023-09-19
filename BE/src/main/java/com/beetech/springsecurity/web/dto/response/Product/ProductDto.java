package com.beetech.springsecurity.web.dto.response.Product;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int id;
    private String sku;
    private String productName;
    private String detail_info;
    private long price;

    // Constructors, getters, setters, and toString
}
