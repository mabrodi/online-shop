package org.dimchik.controller;

import org.dimchik.service.ProductService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.util.ServletUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public ProductServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String search = req.getParameter("search");
        Map<String, Object> data = new HashMap<>();

        if (search != null && !search.isBlank()) {
            data.put("products", productService.searchProducts(search));
            data.put("query", search);
        } else {
            data.put("products", productService.getAllProducts());
        }

        String html = templateEngine.processTemplate("products.html", data);
        ServletUtil.renderHtml(resp, html);
    }
}
