package org.dimchik.service;

import org.dimchik.dao.IUserDao;
import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.service.validation.UserValidator;

public class UserService implements IUserService {
    private final IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String email, String password) {
        UserValidator.validate(email, password);
        User user = userDao.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
