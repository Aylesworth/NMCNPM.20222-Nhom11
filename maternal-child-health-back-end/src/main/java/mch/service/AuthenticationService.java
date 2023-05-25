package mch.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mch.dto.AuthenticationRequest;
import mch.dto.AuthenticationResponse;
import mch.dto.RegistrationRequest;
import mch.model.Role;
import mch.model.User;
import mch.repository.RoleRepository;
import mch.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
				authenticationRequest.getPassword()));
		User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();

		List<Role> roles = roleRepository.getRolesOfUser(user.getId());
		System.out.println(roles.size());
		Collection<SimpleGrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		
		var jwtToken = jwtService.generateToken(user, authorities);
		var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
		
		return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
	}

	public void register(RegistrationRequest registrationRequest) throws Exception {
		if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) 
			throw new Exception("Account already exists");
			
		User user = User.builder()
				.email(registrationRequest.getEmail())
				.password(passwordEncoder.encode(registrationRequest.getPassword()))
				.fullName(registrationRequest.getFullName())
				.dob(registrationRequest.getDob())
				.sex(registrationRequest.getSex())
				.phoneNumber(registrationRequest.getPhoneNumber())
				.address(registrationRequest.getAddress())
				.status("pending")
				.roles(new HashSet<>())
				.build();
		user = userRepository.save(user);
		userService.addRoleToUser(user.getEmail(), "ROLE_USER");
	}

}
