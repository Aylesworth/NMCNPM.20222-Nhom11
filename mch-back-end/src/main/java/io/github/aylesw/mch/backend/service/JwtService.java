package io.github.aylesw.mch.backend.service;

import io.github.aylesw.mch.backend.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface JwtService {
    String generateToken(User user, Collection<SimpleGrantedAuthority> authorities);

    String generateRefreshToken(User user, Collection<SimpleGrantedAuthority> authorities);
}
