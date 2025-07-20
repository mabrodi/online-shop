package org.dimchik.web.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.AuthService;
import org.dimchik.service.ProductService;
import org.dimchik.util.ErrorRendererUtil;
import org.dimchik.util.RenderHtmlUtil;
import org.dimchik.util.TemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductServlet extends HttpServlet {
    private final AuthService authService;
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public UpdateProductServlet(AuthService authService, ProductService productService, TemplateEngine templateEngine) {
        this.authService = authService;
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getPathInfo().substring(1));
            Map<String, Object> data = new HashMap<>();
            data.put("currentUser", authService.getCurrentUser(req));
            data.put("product", productService.getProductById(id));

            String html = templateEngine.processTemplate("productFormUpdate.html", data);
            RenderHtmlUtil.renderHtml(resp, html);
        } catch (IllegalArgumentException e) {
            ErrorRendererUtil.render(resp, "Invalid product id", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ErrorRendererUtil.render(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getPathInfo().substring(1));
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");

            productService.updateProduct(id, name, price, description);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            ErrorRendererUtil.render(resp, "Invalid input: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ErrorRendererUtil.render(resp, e);
        }
    }
}
