package com.example.loginsystemwithemailverification.token;

import com.example.loginsystemwithemailverification.emailVerification.EmailService;
import com.example.loginsystemwithemailverification.user.AppUser;
import com.example.loginsystemwithemailverification.user.AppUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AppUserService userService;

    public void sendVerificationEmail(AppUser appUser) {
        String token = generateConfirmationToken();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .appUser(appUser)
                .build();

        ConfirmationToken savedToken = tokenRepository.save(confirmationToken);
        String link = "http://localhost:8080/api/v1/users/confirm?token=" + savedToken.getToken();

        emailService.sendEmail(appUser.getEmail(), appUser.getFirstName(), link);
    }


    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken userToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid token"));

        if (userToken.getConfirmedAt() != null) {
            throw new IllegalStateException("account already verified");
        }

        if (userToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            sendVerificationEmail(userToken.getAppUser());
            throw new IllegalStateException("token is expired. A new verification token will be sent to your email");
        }

        userToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(userToken);
        userService.enableUser(userToken);
    }


    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }
}
