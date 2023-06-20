package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.AuthenticationRequest;
import com.aylesw.mch.backend.dto.AuthenticationResponse;
import com.aylesw.mch.backend.dto.RegisterDto;

public interface AuthenticationService {
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    public void register(RegisterDto registerDto);
}
