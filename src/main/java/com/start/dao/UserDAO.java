package com.start.dao;

import com.start.entities.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User, Long> {
    Optional<User> findByUserName(String name);

    Optional<User> findByEmail(String email);
}
