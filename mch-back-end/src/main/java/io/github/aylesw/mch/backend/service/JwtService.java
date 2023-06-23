package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface JwtService {
    public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities);

    public String generateRefreshToken(User user, Collection<SimpleGrantedAuthority> authorities);
}
