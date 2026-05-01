package com.example.authservice.service;

import com.example.authservice.exceptions.UserAlreadyFoundException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.exceptions.WrongPasswordException;
import com.example.authservice.models.User;
import com.example.authservice.repository.RoleRepo;
import com.example.authservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;

    public User signup(String name,
                       String emailId,
                       String password,
                       String phoneNumber) {

        Optional<User> UserOptional = userRepo.getUserByEmailId(emailId);
        if (UserOptional.isPresent()) {
            throw new UserAlreadyFoundException("User with email " + emailId + " already exists");
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmailId(emailId);
        newUser.setPassword(password);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setCreatedAt(new Date());
        // check and assign default role to user
        userRepo.save(newUser);

        return newUser;

    }

    public User login(
            String emailId,
            String password) throws WrongPasswordException {
        Optional<User> UserOptional = userRepo.getUserByEmailId(emailId);
        if (UserOptional.isPresent()) {
            User user = UserOptional.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
            throw new WrongPasswordException("Wrong password for user with email " + emailId);
        }
        throw new UserNotFoundException("User with email " + emailId + " not found");
    }

}
