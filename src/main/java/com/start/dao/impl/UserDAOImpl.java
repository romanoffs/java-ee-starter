package com.start.dao.impl;

import com.start.dao.UserDAO;
import com.start.entities.User;
import lombok.val;

import javax.ejb.Stateless;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

    public UserDAOImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUserName(String name) {
        Objects.requireNonNull(name);
        val res = em.createNamedQuery("User.findByUserName", entityClass)
                .setParameter("name", name).getResultList();
        return Optional.ofNullable(
                res.isEmpty() ? null : res.get(0)
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Objects.requireNonNull(email);
        val res = em.createNamedQuery("User.findByEmail", entityClass)
                .setParameter("email", email).getResultList();
        return Optional.ofNullable(
                res.isEmpty() ? null : res.get(0)
        );
    }
}
