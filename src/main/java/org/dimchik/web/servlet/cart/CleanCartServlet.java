package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.CartService;
import org.dimchik.context.Session;

import java.io.IOException;

public class CleanCartServlet extends HttpServlet {
    private final CartService cartService;

    public CleanCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public CleanCartServlet() {
        this(ServiceLocator.getService(CartService.class));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = (Session) req.getAttribute("currentSession");
        cartService.clear(session);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
