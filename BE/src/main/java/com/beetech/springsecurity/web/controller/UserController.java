package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.UserService;
import com.beetech.springsecurity.web.dto.request.User.UserChangePasswordDto;
import com.beetech.springsecurity.web.dto.request.User.UserRequestDto;
import com.beetech.springsecurity.web.dto.request.User.UserUpdateDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import com.beetech.springsecurity.web.dto.response.User.UserDto;
import jakarta.validation.Valid;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;


    @GetMapping("/search")
    public ResponseEntity getUserList(@Valid @RequestBody UserRequestDto userRequestDto,
                                      BindingResult bindingResult, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        List<UserDto> response = userService.searchUsers(userRequestDto, pageable);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/profile")
    public ResponseEntity profile() {

        return ResponseEntity.ok(userService.userProfile());
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = userService.updateUser(userUpdateDto);

        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {

        return ResponseEntity.ok(userService.userChangePassword(userChangePasswordDto));
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

    @PostMapping("/delete-user")
    public ResponseEntity deleteUser() {
        return ResponseEntity.ok(userService.deleteAccount());
    }
}
