package mch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mch.dto.ChangePasswordRequest;
import mch.dto.SimpleResponse;
import mch.dto.UserDto;
import mch.service.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
		UserDto userDto = userService.getUserById(id);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/users")
	public ResponseEntity<SimpleResponse> updateUser(@Valid @RequestBody UserDto userDto) {
		userService.updateUser(userDto);
		return ResponseEntity.ok(new SimpleResponse("User updated successfully"));
	}

	@PutMapping("/users/{id}/change-password")
	public ResponseEntity<SimpleResponse> changePassword(@PathVariable Long id,
			@Valid @RequestBody ChangePasswordRequest request) {
		userService.changePassword(id, request);
		return ResponseEntity.ok(new SimpleResponse("Password changed successfully"));
	}
}
