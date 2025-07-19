package org.dimchik.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;

public class AuthServiceImpl implements AuthService {
    public User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (User) session.getAttribute("user") : null;
    }

    public void login(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
    }

    public boolean isLoggedIn(HttpServletRequest req) {
        return getCurrentUser(req) != null;
    }

    public void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
