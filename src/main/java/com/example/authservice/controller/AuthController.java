package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.SignUpRequestDto;
import com.example.authservice.dto.UserDto;
import com.example.authservice.dto.ValidateTokenRequestDto;
import com.example.authservice.exceptions.WrongPasswordException;
import com.example.authservice.models.Role;
import com.example.authservice.models.User;
import com.example.authservice.service.AuthService;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired AuthService authService;

  @PostMapping("/signup")
  private ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
    User user =
        authService.signup(
            signUpRequestDto.getName(),
            signUpRequestDto.getEmailId(),
            signUpRequestDto.getPassword(),
            signUpRequestDto.getPhoneNumber());

    return new ResponseEntity<>(UserToUserDto(user), HttpStatus.CREATED);
  }

  @PostMapping("login")
  private ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto)
      throws WrongPasswordException {
    Pair<User, String> loginResponse =
        authService.login(loginRequestDto.getEmailId(), loginRequestDto.getPassword());
    User user = loginResponse.a;
    String token = loginResponse.b;
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add(HttpHeaders.SET_COOKIE, token);
    return new ResponseEntity<>(UserToUserDto(user), headers, HttpStatus.OK);
  }

  private UserDto UserToUserDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmailId(user.getEmailId());

    List<String> roles_string = new ArrayList<>();
    for (Role role : user.getRoles()) {
      roles_string.add(role.getValue());
    }

    userDto.setRoles(roles_string);
    return userDto;
  }

  @PostMapping("/validateToken")
  private Boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
    return authService.validateToken(validateTokenRequestDto.getToken());
  }
}
