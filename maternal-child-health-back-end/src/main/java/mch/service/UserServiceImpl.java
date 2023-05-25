package mch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mch.dto.UserDto;
import mch.dto.mapper.UserDtoMapper;
import mch.model.Role;
import mch.model.User;
import mch.repository.RoleRepository;
import mch.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final UserDtoMapper userDtoMapper;

	@Override
	public void addRoleToUser(String email, String roleName) {
		Role role = roleRepository.findByName(roleName).orElseThrow();
		User user = userRepository.findByEmail(email).orElseThrow();
		user.getRoles().add(role);
		userRepository.save(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(user -> userDtoMapper.toDto(user))
				.collect(Collectors.toList());
	}

	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow();
		return userDtoMapper.toDto(user);
	}

	@Override
	public void updateUser(UserDto userDto) {
		User user = userRepository.findById(userDto.getId()).orElseThrow();
		user = userDtoMapper.merge(user, userDto);
		userRepository.save(user);
	}

}
