package com.example.loginsystemwithemailverification.registration;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
