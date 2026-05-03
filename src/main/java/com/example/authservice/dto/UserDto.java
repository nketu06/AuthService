package com.example.authservice.dto;

import java.util.List;

import com.example.authservice.models.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
  private Long id;
  private String name;
  private String emailId;
  private List<String> roles;

}
