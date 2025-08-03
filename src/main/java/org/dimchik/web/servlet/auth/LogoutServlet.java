package org.dimchik.web.servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.SecurityService;
import org.dimchik.web.session.SessionCookieHandler;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private final SecurityService securityService;
    private final SessionCookieHandler sessionCookieHandler;

    public LogoutServlet(SecurityService securityService, SessionCookieHandler sessionCookieHandler) {
        this.securityService = securityService;
        this.sessionCookieHandler = sessionCookieHandler;
    }

    public LogoutServlet() {
        this(ServiceLocator.getService(SecurityService.class), ServiceLocator.getService(SessionCookieHandler.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = sessionCookieHandler.extractId(req);
        securityService.logout(token);
        sessionCookieHandler.clear(resp);

        resp.sendRedirect("/login");
    }
}
