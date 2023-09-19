package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.entity.Order;
import com.beetech.springsecurity.domain.service.CategoryService;
import com.beetech.springsecurity.domain.service.OrderService;
import com.beetech.springsecurity.web.dto.request.Order.CreateOrderDto;
import com.beetech.springsecurity.web.dto.request.Order.OrderUpdateDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity createOrder(@RequestBody  CreateOrderDto createOrderDto) {
        ResponseDto response = orderService.createOrder(createOrderDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update-order")
    public ResponseEntity updateOrder(@RequestBody @Valid OrderUpdateDto orderUpdateDto,
                                      BindingResult bindingResult
                                     ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = orderService.updateOrder(orderUpdateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    public String ErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for (FieldError error : bindingResult.getFieldErrors()) {

            errorMessage.append(i + ".  ").append(error.getDefaultMessage()).append(System.lineSeparator());
            i++;
        }
        return errorMessage.toString();
    }
    @GetMapping()
    public ResponseEntity userGetOrder(){
        return ResponseEntity.ok(orderService.currentUserOrder());
    }
}
