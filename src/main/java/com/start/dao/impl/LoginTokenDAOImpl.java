package com.start.dao.impl;

import com.start.dao.LoginTokenDAO;
import com.start.entities.LoginToken;
import lombok.val;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Stateless
@Local(LoginTokenDAO.class)
public class LoginTokenDAOImpl extends GenericDAOImpl<LoginToken, Long> implements LoginTokenDAO {

    public LoginTokenDAOImpl() {
        super(LoginToken.class);
    }

    @Override
    public Optional<LoginToken> findByHashAndType(byte[] hash, LoginToken.TokenType type) {
        Objects.requireNonNull(type);
        val res = em.createNamedQuery("LoginToken.findByHashAndType", entityClass)
                .setParameter("hash", hash)
                .setParameter("type", type)
                .getResultList();
        return Optional.ofNullable(
                res.isEmpty() ? null : res.get(0)
        );
    }

    @Override
    public void removeByHashAndType(byte[] hash, LoginToken.TokenType type) {
        Objects.requireNonNull(type);
        em.createNamedQuery("LoginToken.removeByHashAndType")
                .setParameter("hash", hash)
                .setParameter("type", type)
                .executeUpdate();
    }

    @Override
    public void removeExpired() {
        em.createNamedQuery("LoginToken.removeExpired")
                .setParameter("now", Instant.now())
                .executeUpdate();
    }
}
