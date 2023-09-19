package com.beetech.springsecurity.web.dto.request.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {
    private int id;
    private String displayId;
    private int status;
}
