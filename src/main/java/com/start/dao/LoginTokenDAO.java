package com.start.dao;

import com.start.entities.LoginToken;

import java.util.Optional;

public interface LoginTokenDAO extends GenericDAO<LoginToken, Long> {

    Optional<LoginToken> findByHashAndType(byte[] hash, LoginToken.TokenType type);

    void removeByHashAndType(byte[] hash, LoginToken.TokenType type);

    void removeExpired();
}
