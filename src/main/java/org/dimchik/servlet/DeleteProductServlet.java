package org.dimchik.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.impl.ProductServiceImpl;
import org.dimchik.util.ErrorRendererUtil;

import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    private final ProductServiceImpl productService;

    public DeleteProductServlet(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getPathInfo().substring(1));
            productService.deleteProduct(id);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            ErrorRendererUtil.render(resp, "Invalid product id", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ErrorRendererUtil.render(resp, e);
        }
    }
}
