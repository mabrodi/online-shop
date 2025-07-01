package org.dimchik.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServletUtil {
    public static void renderHtml(HttpServletResponse resp, String html) throws IOException {
        renderHtml(resp, html,  HttpServletResponse.SC_OK);
    }

    public static void renderHtml(HttpServletResponse resp, String html, int status) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(status);
        resp.getWriter().write(html);
    }
}
