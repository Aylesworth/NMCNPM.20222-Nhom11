package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.*;
import io.github.aylesw.mch.backend.service.AuthenticationService;
import io.github.aylesw.mch.backend.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to Maternal Child Health Management RESTful API");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterDto registerDto) {

        authenticationService.register(registerDto);

        return ResponseEntity.ok("User account waiting for approval");
    }

    @PostMapping("/auth/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@NotEmpty @RequestBody String email) {
        authenticationService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset code sent to " + email);
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        authenticationService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("User password reset successfully");
    }

    @PostMapping("/auth/request-email-verification")
    public ResponseEntity<String> requestEmailVerification(@NotEmpty @RequestBody String email) {
        authenticationService.requestEmailVerification(email);
        return ResponseEntity.ok("Email verification code sent to " + email);
    }

    @PostMapping("/auth/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailDto verifyEmailDto) {
        authenticationService.verifyEmail(verifyEmailDto);
        return ResponseEntity.ok("User email verified successfully");
    }
}
