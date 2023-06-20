package com.aylesw.mch.backend.service;


import com.aylesw.mch.backend.dto.RegisterDto;
import com.aylesw.mch.backend.dto.UserDto;

import java.util.List;

public interface UserService {

    void addRole(String email, String roleName);

    void approveUserRegistration(Long userRegistrationId);

    void approveUserChange(Long userChangeId);

    List<UserDto> search(String keyword);

    UserDto getUser(Long id);

    void createUser(RegisterDto registerDto);

    void updateUser(Long id, RegisterDto registerDto);

    void deleteUser(Long id);
}
