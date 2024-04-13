package com.example.loginsystemwithemailverification.registration;

import com.example.loginsystemwithemailverification.exceptions.DuplicationResourceException;
import com.example.loginsystemwithemailverification.token.ConfirmationTokenService;
import com.example.loginsystemwithemailverification.user.AppUser;
import com.example.loginsystemwithemailverification.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.loginsystemwithemailverification.user.AppUserRole.USER;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService tokenService;

    public String registerUser(UserRegistrationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        AppUser appUser = AppUser.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(encodedPassword)
                .role(USER)
                .build();

        appUser = userService.createUser(appUser);
        tokenService.sendVerificationEmail(appUser);
        return "check your email";
    }

    public void confirmToken(String token) {
        tokenService.confirmToken(token);
    }
}
