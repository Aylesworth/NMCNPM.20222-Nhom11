package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.AuthenticationRequest;
import com.aylesw.mch.backend.dto.AuthenticationResponse;
import com.aylesw.mch.backend.dto.RegisterDto;
import com.aylesw.mch.backend.exception.ApiException;
import com.aylesw.mch.backend.model.UserRegistration;
import com.aylesw.mch.backend.model.Role;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.UserRegistrationRepository;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import com.aylesw.mch.backend.service.AuthenticationService;
import com.aylesw.mch.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRegistrationRepository userRegistrationRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final ModelMapper mapper;

	@Override
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

	@Override
	public void register(RegisterDto registerDto) {
		if (userRepository.findByEmail(registerDto.getEmail()).isPresent())
			throw new ApiException(HttpStatus.BAD_REQUEST, "Email already used");

		UserRegistration userRegistration = mapper.map(registerDto, UserRegistration.class);
		userRegistration.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		userRegistrationRepository.save(userRegistration);
	}

}
