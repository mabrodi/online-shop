package org.dimchik.web.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.ProductService;
import org.dimchik.context.Session;
import org.dimchik.web.view.ErrorViewRenderer;
import org.dimchik.web.view.HtmlResponseWriter;
import org.dimchik.web.view.TemplateRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateRenderer templateRenderer;
    private final ErrorViewRenderer errorViewRenderer;

    public AddProductServlet(ProductService productService, TemplateRenderer templateRenderer, ErrorViewRenderer errorViewRenderer) {
        this.productService = productService;
        this.templateRenderer = templateRenderer;
        this.errorViewRenderer = errorViewRenderer;
    }

    public AddProductServlet() {
        this(
                ServiceLocator.getService(ProductService.class),
                ServiceLocator.getService(TemplateRenderer.class),
                ServiceLocator.getService(ErrorViewRenderer.class)
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Session session = (Session) req.getAttribute("currentSession");

        Map<String, Object> data = new HashMap<>();
        data.put("currentUser", session.getUser());
        data.put("sizeCart", session.getCart().size());

        String html = templateRenderer.processTemplate("productFormCreate.html", data);
        HtmlResponseWriter.renderHtml(resp, html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");

            productService.addProduct(name, price, description);
            resp.sendRedirect("/products");

        } catch (IllegalArgumentException e) {
            errorViewRenderer.renderBadRequest(resp, "Invalid input: " + e.getMessage());
        } catch (Exception e) {
            errorViewRenderer.renderInternalServerError(resp, e);
        }
    }
}
