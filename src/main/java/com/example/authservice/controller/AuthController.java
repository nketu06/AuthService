package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.SignUpRequestDto;
import com.example.authservice.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    private UserDto signUp(@RequestBody SignUpRequestDto  signUpRequestDto) {

    }

    @PostMapping("login")
    private UserDto login(@RequestBody LoginRequestDto loginRequestDto) {

    }
}
