package com.banking.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.dto.UserRequestDto;
import com.banking.entity.User;
import com.banking.service.UserService;

@RestController
//@RequestMapping("/v1/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequestDto user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }
    

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}


