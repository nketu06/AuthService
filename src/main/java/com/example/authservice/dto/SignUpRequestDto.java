package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
  private String name;
  private String emailId;
  private String password;
  private String phoneNumber;
}
