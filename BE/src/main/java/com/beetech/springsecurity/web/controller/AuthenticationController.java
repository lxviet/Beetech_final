package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.AuthService;
import com.beetech.springsecurity.domain.service.UserService;
import com.beetech.springsecurity.web.dto.request.User.UserLoginDto;
import com.beetech.springsecurity.web.dto.request.User.UserRecoverPasswordDto;
import com.beetech.springsecurity.web.dto.request.User.UserRegisterDto;
import com.beetech.springsecurity.web.dto.request.User.UserResetPasswordDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity register(@Valid @RequestBody UserRegisterDto userRegisterDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = authService.register(userRegisterDto);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/request-password")
    @Transactional
    public ResponseEntity recoverPassword(@RequestBody
                                          UserRecoverPasswordDto userRecoverPasswordDto,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = authService.recoverPassword(userRecoverPasswordDto);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/reset-password")
    @Transactional
    public ResponseEntity resetPassword(@Valid @RequestBody UserResetPasswordDto userResetPasswordDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        ResponseDto response = authService.resetPassword(userResetPasswordDto);

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

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = ErrorMessage(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }


        ResponseDto response = authService.login(userLoginDto);
        return ResponseEntity.status(response.getStatus()).body(response);


    }


}
