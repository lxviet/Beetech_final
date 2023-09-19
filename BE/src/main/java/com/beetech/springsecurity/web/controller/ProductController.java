package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.ProductService;
import com.beetech.springsecurity.web.dto.request.Product.ProductCreateDto;
import com.beetech.springsecurity.web.dto.response.Product.ProductDto;
import com.beetech.springsecurity.web.dto.request.Product.ProductUpdateDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {

        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable int productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PostMapping(value = "/create-product",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createProduct(@RequestBody @ModelAttribute
                                                                 ProductCreateDto productCreateDto,
                                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        ResponseDto response = productService.createProduct(productCreateDto);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/update-product")
    public ResponseEntity updateProduct(
            @RequestBody @ModelAttribute
            ProductUpdateDto productUpdateDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = productService.updateProduct(productUpdateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @DeleteMapping("/delete/{productId}")
//    public ResponseEntity<UserManagerResponse> deteleProduct(@PathVariable int productId) {
//
//        return ResponseEntity.ok(productService.deleteProduct(productId));
//    }
    public String ErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for (FieldError error : bindingResult.getFieldErrors()) {

            errorMessage.append(i + ".  ").append(error.getDefaultMessage()).append(System.lineSeparator());
            i++;
        }
        return errorMessage.toString();
    }



}
