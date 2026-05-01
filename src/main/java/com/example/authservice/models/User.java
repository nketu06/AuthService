package com.example.authservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="users")
@Getter
@Setter
public class User extends BaseModel {
  private String name;
  private String emailId;
  private String password;
  private String phoneNumber;

  @ManyToMany()
  private List<Role> roles = new ArrayList<>();
}
