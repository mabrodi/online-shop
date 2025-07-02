package org.dimchik.util;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ErrorRendererUtilTest {
    @Test
    public void renderHtmlErrorWithBadRequestStatus() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        ErrorRendererUtil.render(response, "Hello!");

        assertTrue(stringWriter.toString().contains("Hello!"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void renderHtmlErrorWithInternalServerError() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        ErrorRendererUtil.render(response, "Hello!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        assertTrue(stringWriter.toString().contains("Hello!"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void renderHtmlErrorWithIllegalArgumentException() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        ErrorRendererUtil.render(response, new IllegalArgumentException("test"));

        assertTrue(stringWriter.toString().contains("test"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}