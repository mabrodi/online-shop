package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.context.Session;
import org.dimchik.web.view.HtmlResponseWriter;
import org.dimchik.web.view.TemplateRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartServlet extends HttpServlet {
    private final TemplateRenderer templateRenderer;

    public CartServlet(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    public CartServlet() {
        this(ServiceLocator.getService(TemplateRenderer.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = (Session) req.getAttribute("currentSession");

        Map<String, Object> data = new HashMap<>();
        data.put("currentUser", session.getUser());
        data.put("sizeCart", session.getCart().size());
        data.put("products", session.getCart().getProducts());

        String html = templateRenderer.processTemplate("carts.html", data);
        HtmlResponseWriter.renderHtml(resp, html);
    }
}
