package com.example.authservice.repository;

import com.example.authservice.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> getUserByEmailId(String email);

  User save(User user);
}
