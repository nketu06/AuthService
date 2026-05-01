package com.example.authservice.repository;

import com.example.authservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {}
