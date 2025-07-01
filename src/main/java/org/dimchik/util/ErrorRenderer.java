package org.dimchik.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorRenderer {
    private static final TemplateEngine templateEngine = new TemplateEngine();

    public static void render(HttpServletResponse resp, String message) throws IOException {
        render(resp, message, HttpServletResponse.SC_BAD_REQUEST);
    }

    public static void render(HttpServletResponse resp, String message, int status) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);

        String html = templateEngine.processTemplate("error.html", data);

        ServletUtil.renderHtml(resp, html, status);
    }

    public static void render(HttpServletResponse resp, Exception e) throws IOException {
        render(resp, "Unexpected error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
