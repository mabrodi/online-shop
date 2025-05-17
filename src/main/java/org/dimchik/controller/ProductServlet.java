package org.dimchik.controller;

import org.dimchik.service.ProductService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public ProductServlet() {
        this(new ProductService(), new TemplateEngine());
    }

    public ProductServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("products", productService.getAllProducts());

        String html = templateEngine.processTemplate("products.html", data);
        resp.getWriter().write(html);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
