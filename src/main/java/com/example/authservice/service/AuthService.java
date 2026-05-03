package com.example.authservice.service;

import com.example.authservice.exceptions.UserAlreadyFoundException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.exceptions.WrongPasswordException;
import com.example.authservice.models.Role;
import com.example.authservice.models.State;
import com.example.authservice.models.User;
import com.example.authservice.repository.RoleRepo;
import com.example.authservice.repository.UserRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired private RoleRepo roleRepo;
  @Autowired private UserRepo userRepo;

  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

  public User signup(String name, String emailId, String password, String phoneNumber) {

    Optional<User> UserOptional = userRepo.getUserByEmailId(emailId);
    if (UserOptional.isPresent()) {
      throw new UserAlreadyFoundException("User with email " + emailId + " already exists");
    }
    User newUser = new User();
    newUser.setName(name);
    newUser.setEmailId(emailId);
    newUser.setPassword(bCryptPasswordEncoder.encode(password));
    newUser.setPhoneNumber(phoneNumber);
    newUser.setCreatedAt(new Date());
    // check and assign default role to user
    Role role =null;
    Optional<Role> roleOptional = roleRepo.findRoleByValue("NON_ADMIN");
    if (roleOptional.isEmpty()) {
      role = new Role();
      role.setValue("NON_ADMIN");
      role.setCreatedAt(new Date());
      role.setState(State.ACTIVE);
      roleRepo.save(role);
    }else {
      role = roleOptional.get();
    }
    List<Role> roles = newUser.getRoles();
    roles.add(role);
    newUser.setRoles(roles);
    return userRepo.save(newUser);
  }

  public User login(String emailId, String password) throws WrongPasswordException {
    Optional<User> UserOptional = userRepo.getUserByEmailId(emailId);
    if (UserOptional.isPresent()) {
      User user = UserOptional.get();
      if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
        return user;
      }
      throw new WrongPasswordException("Wrong password for user with email " + emailId);
    }
    throw new UserNotFoundException("User with email " + emailId + " not found");
  }
}
