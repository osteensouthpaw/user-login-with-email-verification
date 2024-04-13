package com.example.loginsystemwithemailverification.user;

public record UserDto(
        String firstName,
        String lastName,
        String email,
        AppUserRole role,
        boolean isEnabled
) {
}
