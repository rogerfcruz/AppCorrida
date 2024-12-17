package com.AppCorrida.AppCorrida.repositories;

import com.AppCorrida.AppCorrida.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
}
