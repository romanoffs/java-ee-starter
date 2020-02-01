package com.start.services.impl;

import com.start.dao.UserDAO;
import com.start.entities.User;
import com.start.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Stateless
@Local(UserService.class)
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    @Override
    public Set<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userDAO.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String userName) {
        return userDAO.findByEmail(userName);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }
}
