package com.example.ewallet.domains.user.model.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
    @Column(name = "email")
    private String value;

    public Email() {}

    public Email(String value) {
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.value = value;
    }

    // Getters, setters, equals, and hashCode methods
}