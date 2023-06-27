package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.ChangePasswordDto;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
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
import io.github.aylesw.mch.backend.service.NotificationService;
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
    private final NotificationService notificationService;
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

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(User.builder().email(userRegistration.getEmail()).build())
                .title("Tài khoản được phê duyệt")
                .message("""
                        <html>
                            <p>Xin chào <i>%s</i>,</p>
                            <p>Tài khoản của bạn trên hệ thống <b><i>Quản lý Sức khỏe Mẹ và Bé</i></b> đã được phê duyệt. 
                            Hiện tại bạn có thể đăng nhập vào hệ thống thông qua email đã đăng ký.</p>
                            <p>Nếu có bất kỳ thắc mắc nào liên quan đến hệ thống, bạn có thể gửi thư trực tiếp cho chúng tôi thông qua địa chỉ email này.</p>
                            <p>Mong bạn có những trải nghiệm tốt nhất.<br>Đội ngũ phát triển</p>
                        </html>
                        """.formatted(userRegistration.getFullName()))
                .build();

        notificationService.createEmailNotification(notificationDetails);
    }

    @Override
    public void rejectUserRegistration(Long userRegistrationId, String reason) {
        UserRegistration userRegistration = userRegistrationRepository.findById(userRegistrationId)
                .orElseThrow(() -> new ResourceNotFoundException("User registration", "id", userRegistrationId));

        if (userRegistration.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "User registration already approved");

        userRegistrationRepository.delete(userRegistration);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(User.builder().email(userRegistration.getEmail()).build())
                .title("Tài khoản bị từ chối phê duyệt")
                .message("""
                        <html>
                            <p>Xin chào <i>%s</i>,</p>
                            <p>Chúng tôi rất tiếc khi phải thông báo rằng tài khoản của bạn trên hệ thống 
                            <b><i>Quản lý Sức khỏe Mẹ và Bé</i></b> đã bị từ chối phê duyệt với lý do: <i>%s</i></p>
                            <p>Mặc dù vậy, bạn hoàn toàn có thể chỉnh sửa thông tin cho hợp lệ và đăng ký lại tài khoản một lần nữa.</p>
                            <p>Nếu có bất kỳ thắc mắc nào liên quan đến hệ thống, bạn có thể gửi thư trực tiếp cho chúng tôi thông qua địa chỉ email này.</p>
                            <p>Chúc bạn một ngày vui vẻ.<br>Đội ngũ phát triển</p>
                        </html>
                        """.formatted(userRegistration.getFullName(), reason))
                .build();

        notificationService.createEmailNotification(notificationDetails);
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
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.getRoles().clear();
        userRepository.save(user);
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
