package com.example.authservice.service;

import com.example.authservice.exceptions.UserAlreadyFoundException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.exceptions.WrongPasswordException;
import com.example.authservice.models.Role;
import com.example.authservice.models.Session;
import com.example.authservice.models.State;
import com.example.authservice.models.User;
import com.example.authservice.repository.RoleRepo;
import com.example.authservice.repository.SessionRepo;
import com.example.authservice.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.util.*;
import javax.crypto.SecretKey;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired private RoleRepo roleRepo;
  @Autowired private UserRepo userRepo;
  @Autowired private SessionRepo sessionRepo;

  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired private SecretKey secretKey;

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
    Role role = null;
    Optional<Role> roleOptional = roleRepo.findRoleByValue("NON_ADMIN");
    if (roleOptional.isEmpty()) {
      role = new Role();
      role.setValue("NON_ADMIN");
      role.setCreatedAt(new Date());
      role.setState(State.ACTIVE);
      roleRepo.save(role);
    } else {
      role = roleOptional.get();
    }
    List<Role> roles = newUser.getRoles();
    roles.add(role);
    newUser.setRoles(roles);
    return userRepo.save(newUser);
  }

  public Pair<User, String> login(String emailId, String password) throws WrongPasswordException {
    Optional<User> UserOptional = userRepo.getUserByEmailId(emailId);
    if (UserOptional.isPresent()) {
      User user = UserOptional.get();
      if (bCryptPasswordEncoder.matches(password, user.getPassword())) {

        // Generate token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        List<String> roles_string = new ArrayList<>();
        for (Role role : user.getRoles()) {
          roles_string.add(role.getValue());
        }
        payload.put("permissions", roles_string);
        payload.put("iat", System.currentTimeMillis());
        payload.put("exp", System.currentTimeMillis() + 100000);
        payload.put("issued_by", "scaler");

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setCreatedAt(new Date());
        session.setState(State.ACTIVE);
        session.setUser(user);
        sessionRepo.save(session);

        return new Pair<>(user, token);
      }
      throw new WrongPasswordException("Wrong password for user with email " + emailId);
    }
    throw new UserNotFoundException("User with email " + emailId + " not found");
  }

  public Boolean validateToken(String token) {
    Optional<Session> optionalSession = sessionRepo.findByToken(token);
    if (optionalSession.isPresent()) {
      JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
      Claims claims = jwtParser.parseSignedClaims(token).getPayload();

      Long expiry = (Long) claims.get("exp");
      Long currentTime = System.currentTimeMillis();
      System.out.println("expiry: " + expiry);
      System.out.println("currentTime: " + currentTime);
      if (currentTime <= expiry) {
        return true;
      }
      Session session = optionalSession.get();
      session.setState(State.INACTIVE);
      sessionRepo.save(session);
      return false;
    }
    return false;
  }
}
