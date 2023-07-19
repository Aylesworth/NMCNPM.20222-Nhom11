package io.github.aylesw.mch.backend.service;


import io.github.aylesw.mch.backend.dto.ChangePasswordDto;
import io.github.aylesw.mch.backend.dto.RegisterDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.dto.UserIdentity;
import io.github.aylesw.mch.backend.entity.UserChange;
import io.github.aylesw.mch.backend.entity.UserRegistration;

import java.util.List;

public interface UserService {

    UserIdentity getUserIdentity(String token);

    void addRole(String email, String roleName);

    void requestUserChange(Long id, UserDto userDto);

    List<UserRegistration> getAllPendingRegistrations();

    List<UserChange> getAllPendingChanges();

    void approveUserRegistration(Long userRegistrationId);

    void rejectUserRegistration(Long userRegistrationId, String reason);

    void approveUserChange(Long userChangeId);

    void rejectUserChange(Long userChangeId, String reason);

    List<UserDto> search(String keyword);

    List<UserDto> getAllUsers();

    UserDto getUser(Long id);

    void createUser(RegisterDto registerDto);

    void updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    void changePassword(Long id, ChangePasswordDto changePasswordDto);
}
