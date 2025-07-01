package org.dimchik.service;

import org.dimchik.entity.User;

public interface IUserService {
    public User login(String email, String password);
}
