package org.dimchik.web.servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.SecurityService;
import org.dimchik.web.view.ErrorViewRenderer;
import org.dimchik.web.view.HtmlResponseWriter;
import org.dimchik.web.view.TemplateRenderer;
import org.dimchik.web.session.SessionCookieHandler;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final SecurityService securityService;
    private final TemplateRenderer templateRenderer;
    private final ErrorViewRenderer errorViewRenderer;
    private final SessionCookieHandler sessionCookieHandler;

    public LoginServlet(SecurityService securityService, TemplateRenderer templateRenderer,
                        ErrorViewRenderer errorViewRenderer, SessionCookieHandler sessionCookieHandler) {
        this.securityService = securityService;
        this.templateRenderer = templateRenderer;
        this.errorViewRenderer = errorViewRenderer;
        this.sessionCookieHandler = sessionCookieHandler;
    }

    public LoginServlet() {
        this(
                ServiceLocator.getService(SecurityService.class),
                ServiceLocator.getService(TemplateRenderer.class),
                ServiceLocator.getService(ErrorViewRenderer.class),
                ServiceLocator.getService(SessionCookieHandler.class)
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = templateRenderer.processTemplate("login.html");
        HtmlResponseWriter.renderHtml(resp, html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            String token = securityService.login(email, password);
            sessionCookieHandler.set(resp, token);
            resp.sendRedirect("/products");

        } catch (IllegalArgumentException e) {
            errorViewRenderer.renderBadRequest(resp, "Invalid email or password");
        }
    }
}
