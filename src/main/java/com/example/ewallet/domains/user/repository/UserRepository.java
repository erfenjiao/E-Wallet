package com.example.ewallet.domains.user.repository;

import com.example.ewallet.domains.user.model.user.Email;
import com.example.ewallet.domains.user.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(Email email);
    User save(User user);
}