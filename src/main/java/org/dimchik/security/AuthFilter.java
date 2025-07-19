package org.dimchik.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.util.SessionUtil;

import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (path.endsWith("/login") || path.endsWith("/login.html")) {
            chain.doFilter(req, res);
            return;
        }

        if (SessionUtil.isLoggedIn(request)) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect("/login");
        }
    }
}
