package com.example.authservice.repository;

import com.example.authservice.models.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session, Long> {

  Optional<Session> findByToken(String token);
}
