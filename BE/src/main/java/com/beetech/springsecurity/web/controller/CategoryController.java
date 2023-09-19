package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.CategoryService;
import com.beetech.springsecurity.web.dto.request.Category.CategoryCreateDto;
import com.beetech.springsecurity.web.dto.response.Category.CategoryDto;
import com.beetech.springsecurity.web.dto.request.Category.CategoryUpdateDto;
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
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int categoryId) {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }



    @PostMapping(value = "/create-category",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCategory(@RequestBody @ModelAttribute
                                                          CategoryCreateDto categoryCreateDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        ResponseDto response = categoryService.createCategory(categoryCreateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping(value ="/update-category",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCategory(@RequestBody @ModelAttribute
                                                              CategoryUpdateDto categoryDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        ResponseDto response = categoryService.updateCategory(categoryDto.getId(), categoryDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable int categoryId) {
        ResponseDto response = categoryService.deleteCategory(categoryId);
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
}
