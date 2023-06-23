package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.ChangePasswordDto;
import io.github.aylesw.mch.backend.dto.RegisterDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.exception.ResourceNotFoundException;
import io.github.aylesw.mch.backend.model.Role;
import io.github.aylesw.mch.backend.model.User;
import io.github.aylesw.mch.backend.model.UserChange;
import io.github.aylesw.mch.backend.model.UserRegistration;
import io.github.aylesw.mch.backend.repository.RoleRepository;
import io.github.aylesw.mch.backend.repository.UserChangeRepository;
import io.github.aylesw.mch.backend.repository.UserRegistrationRepository;
import io.github.aylesw.mch.backend.repository.UserRepository;
import io.github.aylesw.mch.backend.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
    private final AuthenticationManager authenticationManager;

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
    public void requestUserChange(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        UserChange userChange = mapper.map(userDto, UserChange.class);
        userChange.setUser(user);
        userChange.setRequested(Utils.currentTimestamp());
        userChangeRepository.save(userChange);
    }

    @Override
    public List<UserRegistration> getAllPendingRegistrations() {
        return userRegistrationRepository.findNotApproved();
    }

    @Override
    public List<UserChange> getAllPendingChanges() {
        return userChangeRepository.findNotApproved();
    }

    @Override
    public void approveUserRegistration(Long userRegistrationId) {
        UserRegistration userRegistration = userRegistrationRepository.findById(userRegistrationId)
                .orElseThrow(() -> new ResourceNotFoundException("User registration", "id", userRegistrationId));

        if (userRegistration.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "User registration already approved");

        createUser(mapper.map(userRegistration, RegisterDto.class));

        userRegistration.setApproved(Utils.currentTimestamp());
        userRegistrationRepository.save(userRegistration);
    }

    @Override
    public void approveUserChange(Long userChangeId) {
        UserChange userChange = userChangeRepository.findById(userChangeId)
                .orElseThrow(() -> new ResourceNotFoundException("User change", "id", userChangeId));

        if (userChange.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "User profile change already approved");

        updateUser(userChange.getUser().getId(), mapper.map(userChange, UserDto.class));

        userChange.setApproved(Utils.currentTimestamp());
        userChangeRepository.save(userChange);
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
        user.setVerified(false);

        userRepository.save(user);
        addRole(user.getEmail(), "USER");
    }

    @Override
    public void updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        String password = user.getPassword();
        Boolean verified = user.getVerified();

        user = mapper.map(userDto, User.class);
        user.setId(id);
        user.setPassword(password);
        user.setVerified(verified);

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long id, ChangePasswordDto changePasswordDto) {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), changePasswordDto.getOldPassword()));
       } catch (AuthenticationException e) {
           throw new ApiException(HttpStatus.BAD_REQUEST, "Old password does not match");
       }

       user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
       userRepository.save(user);
    }
}
