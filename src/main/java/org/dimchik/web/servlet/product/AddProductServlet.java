package org.dimchik.web.servlet.product;

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

public class AddProductServlet extends HttpServlet {
    private final AuthService authService;
    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public AddProductServlet(AuthService authService, ProductService productService, TemplateEngine templateEngine) {
        this.authService = authService;
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("currentUser", authService.getCurrentUser(req));

        String html = templateEngine.processTemplate("productFormCreate.html", data);
        RenderHtmlUtil.renderHtml(resp, html);
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
            ErrorRendererUtil.render(resp, "Invalid input: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ErrorRendererUtil.render(resp, e);
        }
    }
}
