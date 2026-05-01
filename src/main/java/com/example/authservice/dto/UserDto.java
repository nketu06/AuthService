package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String emailId;
    private List<String> roles;
}
