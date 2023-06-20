package com.aylesw.mch.backend.controller;

import com.aylesw.mch.backend.dto.AuthenticationRequest;
import com.aylesw.mch.backend.dto.AuthenticationResponse;
import com.aylesw.mch.backend.dto.RegisterDto;
import com.aylesw.mch.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
}
