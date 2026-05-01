package com.example.authservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
@Getter
@Setter
public class User extends BaseModel {
  private String name;
  private String emailId;
  private String password;
  private String phoneNumber;

  @ManyToMany() private List<Role> roles = new ArrayList<>();
}
