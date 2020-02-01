package com.start.services;

import com.start.entities.LoginToken;

import java.time.Instant;
import java.util.Optional;

public interface LoginTokenService {
    Optional<LoginToken> findByHashAndType(String token, LoginToken.TokenType type);

    String generate(String email, String ipAddress, String description, LoginToken.TokenType tokenType);

    String generate(String email, String ipAddress, String description, LoginToken.TokenType tokenType, Instant expiration);

    void remove(String loginToken);
}
