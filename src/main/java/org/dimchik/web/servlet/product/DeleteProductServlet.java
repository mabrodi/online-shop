package org.dimchik.web.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.ProductService;

import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService;

    public DeleteProductServlet(ProductService productService) {
        this.productService = productService;
    }

    public DeleteProductServlet() {
        this(ServiceLocator.getService(ProductService.class));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input = req.getPathInfo().substring(1);

        try {
            long id = Long.parseLong(input);
            productService.deleteProduct(id);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product id: " + input, e);
        }
    }
}
