package org.dimchik.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.ProductService;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public AddProductServlet() {
        this(new ProductService(), new TemplateEngine());
    }

    public AddProductServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        String html = templateEngine.processTemplate("productFormCreate.html", data);
        resp.getWriter().write(html);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        productService.addProduct(name, price);

        resp.sendRedirect("/products");
    }


}
