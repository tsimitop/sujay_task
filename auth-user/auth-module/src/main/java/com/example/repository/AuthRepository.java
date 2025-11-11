package com.example.repository;

import com.example.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByFirstName(String firstName);
    Optional<Auth> findByEmail(String email);
    boolean existsByEmail(String email);
}
