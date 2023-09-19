package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.CartService;
import com.beetech.springsecurity.web.dto.request.Cart.*;
import com.beetech.springsecurity.web.dto.request.CartQuantityDto;
import com.beetech.springsecurity.web.dto.response.Cart.CartDto;
import com.beetech.springsecurity.web.dto.response.Cart.CartQuantityResponseDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCarts(@RequestBody  CartRequestDto cartRequestDto) {

        var cartDto = cartService.getCarts(cartRequestDto);
        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/add-cart")
    public ResponseEntity addProductToCart(@RequestBody AddProductToCartDto addProductToCartDto) {
        ResponseDto response = cartService.addProductToCart(addProductToCartDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/sync-cart")
    public ResponseEntity SyncCart(@RequestBody SyncCartDto syncCartDto) {
        ResponseDto response = cartService.syncCart(syncCartDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @GetMapping("/cart-quantity")
    public ResponseEntity<CartQuantityResponseDto> getCartQuantity(@RequestBody CartQuantityDto cartQuantityDto) {
        return ResponseEntity.ok(cartService.getCartQuantity(cartQuantityDto));
    }


    @PostMapping("/delete-cart")
    public ResponseEntity<ResponseDto> deleteCart(@RequestBody DeleteCartDto deleteCartDto){
        ResponseDto response = cartService.deleteCart(deleteCartDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @PostMapping("/update-cart")
//    public ResponseEntity updateCart(@RequestBody UpdateCartDto updateCartDto){
//
//        ResponseDto response = cartService.updateCart(updateCartDto);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }

}
