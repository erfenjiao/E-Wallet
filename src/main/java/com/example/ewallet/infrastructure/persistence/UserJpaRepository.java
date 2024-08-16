package com.example.ewallet.infrastructure.persistence;

import com.example.ewallet.domains.user.model.user.Email;
import com.example.ewallet.domains.user.model.user.User;
//import com.example.ewallet.domains.user.model.user.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(Email email);
}