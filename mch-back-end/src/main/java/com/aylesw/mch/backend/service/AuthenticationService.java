package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.AuthenticationRequest;
import com.aylesw.mch.backend.dto.AuthenticationResponse;
import com.aylesw.mch.backend.model.DangKyUser;
import com.aylesw.mch.backend.model.Role;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.DangKyUserRepository;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final DangKyUserRepository dangKyUserRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
				authenticationRequest.getPassword()));
		User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();

		List<Role> roles = roleRepository.getRolesOfUser(user.getId());
		Collection<SimpleGrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		
		var jwtToken = jwtService.generateToken(user, authorities);
		var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
		
		return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
	}

	public void register(DangKyUser dangKyUser) {
		if (userRepository.findByEmail(dangKyUser.getEmail()).isPresent())
			throw new RuntimeException("Account already exists");

		dangKyUser.setPassword(passwordEncoder.encode(dangKyUser.getPassword()));
		dangKyUser.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		dangKyUserRepository.save(dangKyUser);
	}

}
