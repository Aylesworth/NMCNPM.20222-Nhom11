package io.github.aylesw.mch.backend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.aylesw.mch.backend.config.DateTimeUtils;
import io.github.aylesw.mch.backend.dto.*;
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


    @Override
    public UserIdentity getUserIdentity(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String email = decodedJWT.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow();
        return UserIdentity.builder()
                .id(user.getId())
                .name(user.getFullName())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList())
                .build();
    }

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
        userChange.setRequested(DateTimeUtils.currentTimestamp());
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

        userRegistration.setApproved(DateTimeUtils.currentTimestamp());
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

        userChange.setApproved(DateTimeUtils.currentTimestamp());
        userChangeRepository.save(userChange);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(userChange.getUser())
                .title("Thay đổi được phê duyệt")
                .message("Yêu cầu thay đổi thông tin cá nhân của bạn đã được phê duyệt.")
                .time(DateTimeUtils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public void rejectUserChange(Long userChangeId, String reason) {
        UserChange userChange = userChangeRepository.findById(userChangeId)
                .orElseThrow(() -> new ResourceNotFoundException("User change", "id", userChangeId));

        if (userChange.getApproved() != null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "User profile change already approved");

        userChangeRepository.delete(userChange);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(userChange.getUser())
                .title("Thay đổi bị từ chối phê duyệt")
                .message("Yêu cầu thay đổi thông tin cá nhân của bạn đã bị từ chối với lý do: %s".formatted(reason))
                .time(DateTimeUtils.currentTimestamp())
                .build();

        notificationService.createSystemNotification(notificationDetails);
    }

    @Override
    public List<UserDto> search(String keyword) {
        return userRepository.findByKeyword(keyword).stream()
                .filter(user -> !user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList().contains("ADMIN"))
                .map(user -> mapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList().contains("ADMIN"))
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
