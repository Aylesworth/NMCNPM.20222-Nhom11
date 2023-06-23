package io.github.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.*;
import io.github.aylesw.mch.backend.dto.*;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    void register(RegisterDto registerDto);

    void requestPasswordReset(String email);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    void requestEmailVerification(String email);

    void verifyEmail(VerifyEmailDto verifyEmailDto);
}
