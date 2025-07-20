package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;
import org.dimchik.util.RenderHtmlUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartServlet extends HttpServlet {
    private final AuthService authService;
    private final CartService cartService;
    private final TemplateEngine templateEngine;

    public CartServlet(AuthService authService, CartService cartService, TemplateEngine templateEngine) {
        this.authService = authService;
        this.cartService = cartService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = authService.getCurrentUser(req);

        Map<String, Object> data = new HashMap<>();
        data.put("currentUser", user);
        data.put("carts", cartService.getAllCartsByUserId(user.getId()));

        String html = templateEngine.processTemplate("carts.html", data);
        RenderHtmlUtil.renderHtml(resp, html);
    }
}
