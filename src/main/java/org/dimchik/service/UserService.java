package org.dimchik.service;

import org.dimchik.entity.User;

public interface UserService {
    public User login(String email, String password);
}
