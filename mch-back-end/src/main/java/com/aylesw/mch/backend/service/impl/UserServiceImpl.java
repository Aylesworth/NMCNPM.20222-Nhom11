package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.RegisterDto;
import com.aylesw.mch.backend.dto.UserDto;
import com.aylesw.mch.backend.exception.ApiException;
import com.aylesw.mch.backend.exception.ResourceNotFoundException;
import com.aylesw.mch.backend.model.Role;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.model.UserChange;
import com.aylesw.mch.backend.model.UserRegistration;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.repository.UserChangeRepository;
import com.aylesw.mch.backend.repository.UserRegistrationRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import com.aylesw.mch.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final UserRegistrationRepository userRegistrationRepository;
	private final UserChangeRepository userChangeRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;

	@PersistenceContext
	private final EntityManager entityManager;

	@Override
	public void addRole(String email, String roleName) {
		Role role = roleRepository.findByName(roleName).orElseThrow();
		User user = userRepository.findByEmail(email).orElseThrow();
		user.getRoles().add(role);
		userRepository.save(user);
	}

	@Override
	public void approveUserRegistration(Long userRegistrationId) {
		UserRegistration userRegistration = userRegistrationRepository.findById(userRegistrationId)
				.orElseThrow(() -> new ResourceNotFoundException("User registration", "id", userRegistrationId));
		createUser(mapper.map(userRegistration, RegisterDto.class));
	}

	@Override
	public void approveUserChange(Long userChangeId) {
		UserChange userChange = userChangeRepository.findById(userChangeId)
				.orElseThrow(() -> new ResourceNotFoundException("User change", "id", userChangeId));
		updateUser(userChange.getUser().getId(), mapper.map(userChange, RegisterDto.class));
	}

	@Override
	public List<UserDto> search(String keyword) {
		return userRepository.findByKeyword(keyword).stream()
				.map(user -> mapper.map(user, UserDto.class))
				.toList();
	}

	@Override
	public UserDto getUser(Long id) {
		return mapper.map(userRepository.findById(id).orElseThrow(), UserDto.class);
	}

	@Override
	public void createUser(RegisterDto registerDto) {
		if (userRepository.existsByEmail(registerDto.getEmail()))
			throw new ApiException(HttpStatus.BAD_REQUEST, "Email already used");

		User user = mapper.map(registerDto, User.class);
		user.setId(null);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userRepository.save(user);
		addRole(user.getEmail(), "USER");
	}

	@Override
	public void updateUser(Long id, RegisterDto registerDto) {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

		User user = mapper.map(registerDto, User.class);
		user.setId(id);

		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

}
