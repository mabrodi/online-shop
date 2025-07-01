package org.dimchik.dao;

import org.dimchik.entity.User;

public interface IUserDao {
    User findByEmailAndPassword(String email, String password);
}
