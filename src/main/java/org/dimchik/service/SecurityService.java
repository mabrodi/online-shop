package org.dimchik.service;

import org.dimchik.entity.User;
import org.dimchik.context.Session;

public interface SecurityService {
    String login(String email, String password);

    void logout(String token);

    boolean isLoggable(String token);

    Session getCurrentSession(String token);
}
