package org.dimchik.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.ProductService;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService;

    public DeleteProductServlet() {
        this(new ProductService());
    }

    public DeleteProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getPathInfo().substring(1));
        productService.deleteProduct(id);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
