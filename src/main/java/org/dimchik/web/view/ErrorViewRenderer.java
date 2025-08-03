package org.dimchik.web.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorViewRenderer {
    private final TemplateRenderer templateRenderer;

    public ErrorViewRenderer(TemplateRenderer templateEngine) {
        this.templateRenderer = templateEngine;
    }

    public void renderBadRequest(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {
        renderError(req, resp, message, HttpServletResponse.SC_BAD_REQUEST);
    }

    private void renderError(HttpServletRequest req, HttpServletResponse resp, String message, int status) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("contextPath", req.getContextPath());

        String html = templateRenderer.processTemplate("error.html", data);

        HtmlResponseWriter.renderHtml(resp, html, status);
    }

    public void renderInternalServerError(HttpServletRequest req, HttpServletResponse resp, Exception e) throws IOException {
        renderError(req, resp, "Unexpected error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
