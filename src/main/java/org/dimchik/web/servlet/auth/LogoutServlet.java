package org.dimchik.web.servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.AuthService;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private final AuthService authService;

    public LogoutServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authService.logout(req);

        resp.sendRedirect("/login");
    }
}
