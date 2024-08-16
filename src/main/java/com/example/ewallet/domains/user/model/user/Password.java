package com.example.ewallet.domains.user.model.user;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
//@AllArgsConstructor
@NoArgsConstructor
public class Password {
    private String encryptedPassword;

    public Password(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    private String encrypt(String plainPassword) {
        // Placeholder for encryption logic
        return plainPassword; // Implement real encryption
    }

    public boolean matches(String plainPassword) {
        return Objects.equals(this.encryptedPassword, encrypt(plainPassword));
    }

    // Getters and setters
}
