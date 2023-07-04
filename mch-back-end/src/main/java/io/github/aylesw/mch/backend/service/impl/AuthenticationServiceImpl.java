package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.config.DateTimeUtils;
import io.github.aylesw.mch.backend.dto.*;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.model.AuthCode;
import io.github.aylesw.mch.backend.model.Role;
import io.github.aylesw.mch.backend.model.User;
import io.github.aylesw.mch.backend.model.UserRegistration;
import io.github.aylesw.mch.backend.repository.AuthCodeRepository;
import io.github.aylesw.mch.backend.repository.RoleRepository;
import io.github.aylesw.mch.backend.repository.UserRegistrationRepository;
import io.github.aylesw.mch.backend.repository.UserRepository;
import io.github.aylesw.mch.backend.service.AuthenticationService;
import io.github.aylesw.mch.backend.service.JwtService;
import io.github.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRegistrationRepository userRegistrationRepository;
    private final AuthCodeRepository authCodeRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();

        List<Role> roles = roleRepository.getRolesOfUser(user.getId());
        Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

        return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
    }

    @Override
    public void register(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already used");

        UserRegistration userRegistration = mapper.map(registerDto, UserRegistration.class);
        userRegistration.setRequested(DateTimeUtils.currentTimestamp());
        userRegistrationRepository.save(userRegistration);
    }

    @Override
    public void requestPasswordReset(String email) {
        if (!userRepository.existsByEmail(email))
            throw new ApiException(HttpStatus.BAD_REQUEST, "User with email " + email + " doesn't exist");

        String authCode = generateAuthCode(email, "Password reset");
        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(User.builder().email(email).build())
                .title("Mã xác minh")
                .message("<html><p>Mã đặt lại mật khẩu của bạn là: <b>" + authCode + "</b></p></html>")
                .build();

        notificationService.createEmailNotification(notificationDetails);
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.findByEmail(resetPasswordDto.getUserEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User with email " + resetPasswordDto.getUserEmail() + " doesn't exist"));

        ApiException ex = new ApiException(HttpStatus.UNAUTHORIZED, "Authentication code invalid or expired");

        AuthCode authCode = authCodeRepository.findByValueUnexpiredAtTime(
                resetPasswordDto.getAuthCode(),
                DateTimeUtils.currentTimestamp()
        ).orElseThrow(() -> ex);

        if (!authCode.getUserEmail().equals(user.getEmail())
                || !authCode.getUsedFor().equals("Password reset")
                || authCode.getUsed())
            throw ex;

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
        authCode.setUsed(true);
        authCodeRepository.save(authCode);
    }

    @Override
    public void requestEmailVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User with email " + email + " doesn't exist"));

        if (user.getVerified())
            throw new ApiException(HttpStatus.BAD_REQUEST, "User email already verified");

        String authCode = generateAuthCode(email, "Email verification");
        NotificationDetails notificationDetails = NotificationDetails.builder()
                .user(User.builder().email(email).build())
                .title("Mã xác minh")
                .message("<html><p>Mã xác minh email của bạn là: <b>" + authCode + "</b></p></html>")
                .build();

        notificationService.createEmailNotification(notificationDetails);
    }

    @Override
    public void verifyEmail(VerifyEmailDto verifyEmailDto) {
        User user = userRepository.findByEmail(verifyEmailDto.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User with email " + verifyEmailDto.getEmail() + " doesn't exist"));

        if (user.getVerified())
            throw new ApiException(HttpStatus.BAD_REQUEST, "User email already verified");

        ApiException ex = new ApiException(HttpStatus.UNAUTHORIZED, "Authentication code invalid or expired");

        AuthCode authCode = authCodeRepository.findByValueUnexpiredAtTime(
                verifyEmailDto.getAuthCode(),
                DateTimeUtils.currentTimestamp()
        ).orElseThrow(() -> ex);

        if (!authCode.getUserEmail().equals(user.getEmail())
                || !authCode.getUsedFor().equals("Email verification")
                || authCode.getUsed())
            throw ex;

        user.setVerified(true);
        userRepository.save(user);
        authCode.setUsed(true);
        authCodeRepository.save(authCode);
    }

    private String generateAuthCode(String userEmail, String usedFor) {
        Random random = new Random();

        String value = random.ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

        if (authCodeRepository.findByValueUnexpiredAtTime(value, DateTimeUtils.currentTimestamp()).isPresent())
            return generateAuthCode(userEmail, usedFor);

        AuthCode authCode = AuthCode.builder()
                .value(value)
                .userEmail(userEmail)
                .usedFor(usedFor)
                .expiresAt(Timestamp.valueOf(LocalDateTime.now().plusSeconds(5 * 60)))
                .used(false)
                .build();
        authCodeRepository.save(authCode);

        return value;
    }

}
