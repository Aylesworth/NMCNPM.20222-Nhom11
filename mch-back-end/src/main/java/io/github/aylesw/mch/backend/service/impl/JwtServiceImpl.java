package io.github.aylesw.mch.backend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.aylesw.mch.backend.entity.User;
import io.github.aylesw.mch.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${secret-key}")
    private String secretKey;

    @Override
    public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities) {
        System.out.println(authorities.size());
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create().withSubject(user.getEmail()).withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withClaim("roles",
                        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("id", user.getId()).sign(algorithm);
    }

    @Override
    public String generateRefreshToken(User user, Collection<SimpleGrantedAuthority> authorities) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create().withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .withClaim("roles",
                        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("id", user.getId()).sign(algorithm);
    }
}