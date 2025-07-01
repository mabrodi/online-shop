package org.dimchik.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.ProductService;
import org.dimchik.util.ErrorRenderer;
import org.dimchik.util.ServletUtil;
import org.dimchik.util.SessionUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public AddProductServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("currentUser", SessionUtil.getCurrentUser(req));

        String html = templateEngine.processTemplate("productFormCreate.html", data);
        ServletUtil.renderHtml(resp, html);
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
            ErrorRenderer.render(resp, "Invalid input: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ErrorRenderer.render(resp, e);
        }
    }
}
