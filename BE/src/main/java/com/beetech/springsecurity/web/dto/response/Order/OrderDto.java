package com.beetech.springsecurity.web.dto.response.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private long totalPrice;
    private String displayId;
    private String status;
    private LocalDate orderDate;
}
