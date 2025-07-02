package org.dimchik.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.impl.UserServiceImpl;
import org.dimchik.util.ErrorRendererUtil;
import org.dimchik.util.RenderHtmlUtil;
import org.dimchik.util.SessionUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private final TemplateEngine templateEngine;
    private final UserServiceImpl userService;

    public LoginServlet(TemplateEngine templateEngine, UserServiceImpl userService) {
        this.templateEngine = templateEngine;
        this.userService = userService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        String html = templateEngine.processTemplate("login.html", data);
        RenderHtmlUtil.renderHtml(resp, html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.login(email, password);

            SessionUtil.login(req, user);
            resp.sendRedirect("/products");

        } catch (IllegalArgumentException e) {
            ErrorRendererUtil.render(resp, "Invalid email or password");
        }
    }
}
