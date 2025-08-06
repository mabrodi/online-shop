package org.dimchik.service.base;

import org.dimchik.entity.User;
import org.dimchik.service.SecurityService;
import org.dimchik.context.Session;
import org.dimchik.service.UserService;

import java.util.*;

public class SecurityServiceBase implements SecurityService {

    private final int cookieMaxAge;
    private final List<Session> sessionList;
    private final UserService userService;

    public SecurityServiceBase(UserService userService, int cookieMaxAge) {
        this.userService = userService;
        this.cookieMaxAge = cookieMaxAge;
        sessionList = new ArrayList<>();
    }

    @Override
    public String login(String email, String password) {
        User user = userService.authenticate(email, password);

        String token = UUID.randomUUID().toString();
        Session session = new Session(user, token, cookieMaxAge);
        sessionList.add(session);

        return token;
    }

    @Override
    public void logout(String token) {
        sessionList.removeIf(session -> session.getToken().equals(token));
    }

    @Override
    public boolean isLoggable(String token) {
        Iterator<Session> iterator = sessionList.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session.getToken().equals(token)) {
                if (session.isExpired()) {
                    iterator.remove();
                    return false;
                }
                return true;
            }
        }

        return false;
    }


    @Override
    public Session getCurrentSession(String token) {
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                return session;
            }
        }

        return null;
    }
}
