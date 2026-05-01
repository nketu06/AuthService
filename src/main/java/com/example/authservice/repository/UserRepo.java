package com.example.authservice.repository;

import com.example.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> getUserByEmailId(String email);
    User save(User user);
}
