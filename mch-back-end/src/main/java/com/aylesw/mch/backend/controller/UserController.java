package com.aylesw.mch.backend.controller;

import com.aylesw.mch.backend.dto.RegisterDto;
import com.aylesw.mch.backend.dto.UserDto;
import com.aylesw.mch.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/approve-registration")
    public ResponseEntity<String> approveUserRegistration(@RequestParam(value = "id", required = true) Long userRegistrationId) {
        userService.approveUserRegistration(userRegistrationId);
        return ResponseEntity.ok("User registration approved");
    }

    @PostMapping("/approve-change")
    public ResponseEntity<String> approveUserChange(@RequestParam(value = "id", required = true) Long userChangeId) {
        userService.approveUserChange(userChangeId);
        return ResponseEntity.ok("User profile change approved");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestParam(value = "q", required = true) String keyword) {
        return ResponseEntity.ok(userService.search(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterDto registerDto) {
        userService.createUser(registerDto);
        return ResponseEntity.ok("User created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id,
                                             @Valid @RequestBody RegisterDto registerDto) {
        userService.updateUser(id, registerDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
