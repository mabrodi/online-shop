package org.dimchik.dao;

import org.dimchik.entity.User;

public interface UserDao {
    User findByEmailAndPassword(String email, String password);
}
