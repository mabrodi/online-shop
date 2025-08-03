package org.dimchik.web.view;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorViewRenderer {
    private final TemplateRenderer templateRenderer;

    public ErrorViewRenderer(TemplateRenderer templateEngine) {
        this.templateRenderer = templateEngine;
    }

    public void renderBadRequest(HttpServletResponse resp, String message) throws IOException {
        renderError(resp, message, HttpServletResponse.SC_BAD_REQUEST);
    }

    private void renderError(HttpServletResponse resp, String message, int status) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);

        String html = templateRenderer.processTemplate("error.html", data);

        HtmlResponseWriter.renderHtml(resp, html, status);
    }

    public void renderInternalServerError(HttpServletResponse resp, Exception e) throws IOException {
        renderError(resp, "Unexpected error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
