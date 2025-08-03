package org.dimchik.web.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.ProductService;
import org.dimchik.context.Session;
import org.dimchik.web.view.ErrorViewRenderer;
import org.dimchik.web.view.HtmlResponseWriter;
import org.dimchik.web.view.TemplateRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductServlet extends HttpServlet {
    private final ProductService productService;
    private final TemplateRenderer templateRenderer;
    private final ErrorViewRenderer errorViewRenderer;

    public UpdateProductServlet(ProductService productService, TemplateRenderer templateRenderer, ErrorViewRenderer errorViewRenderer) {
        this.productService = productService;
        this.templateRenderer = templateRenderer;
        this.errorViewRenderer = errorViewRenderer;
    }

    public UpdateProductServlet() {
        this(
                ServiceLocator.getService(ProductService.class),
                ServiceLocator.getService(TemplateRenderer.class),
                ServiceLocator.getService(ErrorViewRenderer.class)
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = (Session) req.getAttribute("currentSession");

        try {
            long id = Long.parseLong(req.getPathInfo().substring(1));
            Map<String, Object> data = new HashMap<>();
            data.put("currentUser", session.getUser());
            data.put("sizeCart", session.getCart().size());
            data.put("product", productService.getProductById(id));

            String html = templateRenderer.processTemplate("productFormUpdate.html", data);
            HtmlResponseWriter.renderHtml(resp, html);
        } catch (IllegalArgumentException e) {
            errorViewRenderer.renderBadRequest(resp, "Invalid product id");
        } catch (Exception e) {
            errorViewRenderer.renderInternalServerError(resp, e);
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
            errorViewRenderer.renderBadRequest(resp, "Invalid input: " + e.getMessage());
        } catch (Exception e) {
            errorViewRenderer.renderInternalServerError(resp, e);
        }
    }
}
