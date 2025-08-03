package org.dimchik.service.base;

import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.service.UserService;
import org.dimchik.crypto.CryptoHash;

public class UserServiceBase implements UserService {
    private final UserDao userDao;

    public UserServiceBase(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user == null || !CryptoHash.isMatch(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
