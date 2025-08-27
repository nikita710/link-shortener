package com.example.linkshortner.repository;

import com.example.linkshortner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmail(String email);
}
