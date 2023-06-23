package io.github.aylesw.mch.backend.service;


import io.github.aylesw.mch.backend.dto.ChangePasswordDto;
import io.github.aylesw.mch.backend.dto.RegisterDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.model.UserChange;
import io.github.aylesw.mch.backend.model.UserRegistration;

import java.util.List;

public interface UserService {

    void addRole(String email, String roleName);

    void requestUserChange(Long id, UserDto userDto);

    List<UserRegistration> getAllPendingRegistrations();

    List<UserChange> getAllPendingChanges();

    void approveUserRegistration(Long userRegistrationId);

    void approveUserChange(Long userChangeId);

    List<UserDto> search(String keyword);

    UserDto getUser(Long id);

    void createUser(RegisterDto registerDto);

    void updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    void changePassword(Long id, ChangePasswordDto changePasswordDto);
}
