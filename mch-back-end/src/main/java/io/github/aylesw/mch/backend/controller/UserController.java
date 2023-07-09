package io.github.aylesw.mch.backend.controller;

import io.github.aylesw.mch.backend.dto.ChangePasswordDto;
import io.github.aylesw.mch.backend.dto.RegisterDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.dto.UserIdentity;
import io.github.aylesw.mch.backend.model.UserChange;
import io.github.aylesw.mch.backend.model.UserRegistration;
import io.github.aylesw.mch.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/my-identity")
    public ResponseEntity<UserIdentity> getUserIdentity(
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getUserIdentity(authorizationHeader.substring(7)));
    }

    @PostMapping("/{id}/request-change")
    public ResponseEntity<String> requestUserChange(@PathVariable("id") Long id,
                                                    @Valid @RequestBody UserDto userDto) {
        userService.requestUserChange(id, userDto);
        return ResponseEntity.ok("User profile change waiting for approval");
    }

    @GetMapping("/pending-registrations")
    public ResponseEntity<List<UserRegistration>> getAllPendingRegistrations() {
        return ResponseEntity.ok(userService.getAllPendingRegistrations());
    }

    @GetMapping("/pending-changes")
    public ResponseEntity<List<UserChange>> getAllPendingChanges() {
        return ResponseEntity.ok(userService.getAllPendingChanges());
    }

    @PostMapping("/approve-registration")
    public ResponseEntity<String> approveUserRegistration(@RequestParam(value = "id", required = true) Long userRegistrationId) {
        userService.approveUserRegistration(userRegistrationId);
        return ResponseEntity.ok("User registration approved");
    }

    @PostMapping("/reject-registration")
    public ResponseEntity<String> rejectUserRegistration(
            @RequestParam(value = "id", required = true) Long userRegistrationId,
            @RequestParam(value = "reason", required = true) String reason) {
        userService.rejectUserRegistration(userRegistrationId, reason);
        return ResponseEntity.ok("User registration rejected");
    }

    @PostMapping("/approve-change")
    public ResponseEntity<String> approveUserChange(@RequestParam(value = "id", required = true) Long userChangeId) {
        userService.approveUserChange(userChangeId);
        return ResponseEntity.ok("User profile change approved");
    }

    @PostMapping("/reject-change")
    public ResponseEntity<String> rejectUserChange(
            @RequestParam(value = "id", required = true) Long userChangeId,
            @RequestParam(value = "reason", required = true) String reason) {
        userService.rejectUserChange(userChangeId, reason);
        return ResponseEntity.ok("User profile change rejected");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestParam(value = "q", required = true) String keyword) {
        return ResponseEntity.ok(userService.search(keyword));
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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
                                             @Valid @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id,
                                                 @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(id, changePasswordDto);
        return ResponseEntity.ok("User password changed successfully");
    }
}
