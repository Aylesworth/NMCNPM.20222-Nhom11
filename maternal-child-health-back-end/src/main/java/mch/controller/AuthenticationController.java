package mch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mch.dto.AuthenticationRequest;
import mch.dto.AuthenticationResponse;
import mch.dto.RegistrationRequest;
import mch.dto.SimpleResponse;
import mch.service.AuthenticationService;

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
	public ResponseEntity<SimpleResponse> register(
			@Valid @RequestBody RegistrationRequest request) throws Exception {
		
		authenticationService.register(request);
		
		return ResponseEntity.ok(new SimpleResponse("Account created successfully"));
	}
}
