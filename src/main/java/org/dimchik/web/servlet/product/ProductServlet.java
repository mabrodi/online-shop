package org.dimchik.web.servlet.product;

import jakarta.servlet.ServletException;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.ProductService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Session;
import org.dimchik.web.view.HtmlResponseWriter;
import org.dimchik.web.view.TemplateRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateRenderer templateRenderer;

    public ProductServlet(ProductService productService, TemplateRenderer templateRenderer) {
        this.productService = productService;
        this.templateRenderer = templateRenderer;
    }

    public ProductServlet() {
        this(ServiceLocator.getService(ProductService.class), ServiceLocator.getService(TemplateRenderer.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String search = req.getParameter("search");
        Session session = (Session) req.getAttribute("currentSession");

        Map<String, Object> data = new HashMap<>();

        data.put("currentUser", session.getUser());
        data.put("sizeCart", session.getCart().size());
        data.put("contextPath", req.getContextPath());

        if (search != null && !search.isBlank()) {
            data.put("products", productService.searchProducts(search));
            data.put("query", search);
        } else {
            data.put("products", productService.getAllProducts());
        }

        String html = templateRenderer.processTemplate("products.html", data);
        HtmlResponseWriter.renderHtml(resp, html);
    }
}
