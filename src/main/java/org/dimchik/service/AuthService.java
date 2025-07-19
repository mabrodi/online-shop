package org.dimchik.service;

import jakarta.servlet.http.HttpServletRequest;
import org.dimchik.entity.User;

public interface AuthService {
    User getCurrentUser(HttpServletRequest req);

    void login(HttpServletRequest req, User user);

    boolean isLoggedIn(HttpServletRequest req);

    void logout(HttpServletRequest req);
}
