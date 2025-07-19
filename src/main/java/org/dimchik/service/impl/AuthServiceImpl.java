package org.dimchik.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (User) session.getAttribute("user") : null;
    }

    @Override
    public void login(HttpServletRequest req, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(30 * 60); // 30 min
        session.setAttribute("user", user);
    }

    @Override
    public boolean isLoggedIn(HttpServletRequest req) {
        return getCurrentUser(req) != null;
    }

    @Override
    public void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
