package org.dimchik.service.impl;

import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.service.UserService;
import org.dimchik.service.validation.UserValidator;
import org.dimchik.util.CryptoHash;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String email, String password) {
        UserValidator.validate(email, password);
        User user = userDao.findByEmail(email);
        if (user == null || !CryptoHash.isMatch(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
