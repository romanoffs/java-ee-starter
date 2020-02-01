package com.start.services;

import com.start.entities.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Set<User> getAllUsers();

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String userName);

    User save(User user);

    void delete(User user);
}
