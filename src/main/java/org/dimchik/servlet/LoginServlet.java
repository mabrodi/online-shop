package org.dimchik.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.UserService;
import org.dimchik.util.ErrorRenderer;
import org.dimchik.util.ServletUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private final TemplateEngine templateEngine;
    private final UserService userService;

    public LoginServlet(TemplateEngine templateEngine, UserService userService) {
        this.templateEngine = templateEngine;
        this.userService = userService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        String html = templateEngine.processTemplate("login.html", data);
        ServletUtil.renderHtml(resp, html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.login(email, password);

            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            resp.sendRedirect("/products");

        } catch (IllegalArgumentException e) {
            ErrorRenderer.render(resp, "Invalid email or password");
        }
    }
}
