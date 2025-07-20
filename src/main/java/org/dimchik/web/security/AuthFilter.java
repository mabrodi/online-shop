package org.dimchik.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.AuthService;

import java.io.IOException;

public class AuthFilter implements Filter {

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
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

        if (authService.isLoggedIn(request)) {
            chain.doFilter(req, res);
            return;
        }

        response.sendRedirect("/login");
    }
}
