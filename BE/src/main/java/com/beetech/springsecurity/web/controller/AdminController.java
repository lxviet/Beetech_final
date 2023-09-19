package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.UserService;
import com.beetech.springsecurity.web.dto.request.User.UserDeleteDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import com.beetech.springsecurity.web.dto.response.User.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin controller provide api for an admin
 */
@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;


    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.deleteUserByAdmin(email));
    }

    @PostMapping("/lockUser/{email}")
    public ResponseEntity lockUser(@PathVariable String email) {

        ResponseDto response = userService.lockUser(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}