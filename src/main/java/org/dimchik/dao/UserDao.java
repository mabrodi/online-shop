package org.dimchik.dao;

import org.dimchik.entity.User;

public interface UserDao {
    User findByEmail(String email);
}
