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

public class UpdateProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public UpdateProductServlet() {
        this(new ProductService(), new TemplateEngine());
    }

    public UpdateProductServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getPathInfo().substring(1));

        System.out.println(id);
        Map<String, Object> data = new HashMap<>();
        data.put("product", productService.getProductById(id));

        String html = templateEngine.processTemplate("productFormUpdate.html", data);
        resp.getWriter().write(html);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getPathInfo().substring(1));
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        System.out.println(id + " " + name + " " + price);

        productService.updateProduct(id, name, price);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
