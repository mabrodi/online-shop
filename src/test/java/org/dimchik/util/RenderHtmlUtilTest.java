package org.dimchik.util;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RenderHtmlUtilTest {
    @Test
    public void renderHtmlWritesCorrectContent() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        RenderHtmlUtil.renderHtml(response, "Hello!");

        assertTrue(stringWriter.toString().contains("Hello!"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void renderHtmlWritesCorrectContentResponseBadRequest() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        RenderHtmlUtil.renderHtml(response, "Hello!", HttpServletResponse.SC_BAD_REQUEST);

        assertTrue(stringWriter.toString().contains("Hello!"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void renderHtmlEmptyStringDoesNotThrow() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        assertDoesNotThrow(() -> RenderHtmlUtil.renderHtml(response, ""));
    }
}