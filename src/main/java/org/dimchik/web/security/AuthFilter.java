package org.dimchik.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.SecurityService;
import org.dimchik.web.session.SessionCookieHandler;

import java.io.IOException;

public class AuthFilter implements Filter {

    private final SecurityService securityService;
    private final SessionCookieHandler sessionCookieHandler;

    public AuthFilter(SecurityService securityService, SessionCookieHandler sessionCookieHandler) {
        this.securityService = securityService;
        this.sessionCookieHandler = sessionCookieHandler;
    }

    public AuthFilter() {
        this(ServiceLocator.getService(SecurityService.class), ServiceLocator.getService(SessionCookieHandler.class));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if ("/login".equals(path) || "/login.html".equals(path)) {
            chain.doFilter(req, res);
            return;
        }

        String token = sessionCookieHandler.extractId(request);
        if (securityService.isLoggable(token)) {
            request.setAttribute("currentSession", securityService.getCurrentSession(token));

            chain.doFilter(req, res);
            return;
        }

        response.sendRedirect("/login");
    }
}
