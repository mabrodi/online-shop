package org.dimchik.service.base;

import org.dimchik.config.PropertyReader;
import org.dimchik.entity.User;
import org.dimchik.service.SecurityService;
import org.dimchik.context.Session;
import org.dimchik.service.UserService;

import java.util.*;

public class SecurityServiceBase implements SecurityService {

    private final int cookieMaxAge;
    private final Map<String, Session> sessionMap = new HashMap<>();
    private final UserService userService;

    public SecurityServiceBase(UserService userService, int cookieMaxAge) {
        this.userService = userService;
        this.cookieMaxAge = cookieMaxAge;
    }

    @Override
    public String login(String email, String password) {
        User user = userService.authenticate(email, password);

        String token = UUID.randomUUID().toString();
        Session session = new Session(user, cookieMaxAge);
        sessionMap.put(token, session);

        return token;
    }

    @Override
    public void logout(String token) {
        sessionMap.remove(token);
    }

    @Override
    public boolean isLoggable(String token) {
        Session session = sessionMap.get(token);
        if (session == null) {
            return false;
        }

        if (session.isExpired()) {
            sessionMap.remove(token);
            return false;
        }

        return true;
    }

    @Override
    public Session getCurrentSession(String token) {
        return sessionMap.get(token);
    }
}
