package com.AppCorrida.AppCorrida.repositories;

import com.AppCorrida.AppCorrida.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
    Optional<User> findById(Long id);
    Page<User> findAll(Pageable pageable);
}