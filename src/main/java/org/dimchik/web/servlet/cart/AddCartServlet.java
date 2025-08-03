package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.CartService;
import org.dimchik.context.Session;

import java.io.IOException;

public class AddCartServlet extends HttpServlet {
    private final CartService cartService;

    public AddCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public AddCartServlet() {
        this(ServiceLocator.getService(CartService.class));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = (Session) req.getAttribute("currentSession");
        String input = req.getPathInfo().substring(1);

        try {
            long productId = Long.parseLong(input);
            cartService.addProduct(session, productId);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product id: " + input, e);
        }
    }
}
