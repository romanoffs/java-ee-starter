package com.start.services.impl;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.start.exceptions.ResourceNotFound;
import com.start.dao.LoginTokenDAO;
import com.start.entities.LoginToken;
import com.start.entities.User;
import com.start.services.LoginTokenService;
import com.start.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

@Stateless
@Local(LoginTokenService.class)
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class LoginTokenServiceImpl implements LoginTokenService {

    private UserService userService;
    private LoginTokenDAO loginTokenDAO;

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Optional<LoginToken> findByHashAndType(
            String token,
            LoginToken.TokenType type
    ) {
        val hashFunction = Hashing.sha512();
        val hashTokenBytes = hashFunction.newHasher()
                .putString(token, Charsets.UTF_8)
                .hash()
                .asBytes();
        return loginTokenDAO.findByHashAndType(hashTokenBytes, type);
    }

    @Override
    public String generate(
            String email,
            String ipAddress,
            String description,
            LoginToken.TokenType tokenType
    ) {
        // 14 DAYS
        val expiration = Instant.now().plus(14, DAYS);
        return generate(email, ipAddress, description, tokenType, expiration);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public String generate(
            String email,
            String ipAddress,
            String description,
            LoginToken.TokenType tokenType,
            Instant expiration
    ) {
        User user = userService.findByEmail(email)
                .orElseThrow(ResourceNotFound::new);

        val rawToken = UUID.randomUUID().toString();

        val hashFunction = Hashing.sha512();
        val hashTokenBytes = hashFunction.newHasher()
                .putString(rawToken, Charsets.UTF_8)
                .hash()
                .asBytes();

        LoginToken loginToken = LoginToken
                .builder()
                .tokenHash(hashTokenBytes)
                .description(description)
                .expiration(expiration)
                .type(tokenType)
                .ipAddress(ipAddress)
                .user(user)
                .build();

        // Save to Database
        loginTokenDAO.save(loginToken);

        return rawToken;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void remove(String loginToken) {

        val hashFunction = Hashing.sha512();
        val hashTokenBytes = hashFunction.newHasher()
                .putString(loginToken, Charsets.UTF_8)
                .hash()
                .asBytes();

        loginTokenDAO.removeByHashAndType(hashTokenBytes, LoginToken.TokenType.REMEMBER_ME);
    }

    @Schedule(
            minute = "*/5",
            hour = "*",
            persistent = false,
            info = "Remove expired tokens"
    )
    public void removeExpired() {
        loginTokenDAO.removeExpired();
    }
}
