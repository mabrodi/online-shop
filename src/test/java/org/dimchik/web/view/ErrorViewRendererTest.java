package org.dimchik.web.view;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorViewRendererTest {
    private ErrorViewRenderer errorViewRenderer;

    @BeforeEach
    void setUp() {
        TemplateRenderer templateRenderer = new TemplateRenderer();
        errorViewRenderer = new ErrorViewRenderer(templateRenderer);
    }

    @Test
    public void renderHtmlErrorWithBadRequestStatus() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        errorViewRenderer.renderBadRequest(response, "Hello!");

        assertTrue(stringWriter.toString().contains("Hello!"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void renderHtmlErrorWithIllegalArgumentException() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        errorViewRenderer.renderInternalServerError(response, new IllegalArgumentException("test"));

        assertTrue(stringWriter.toString().contains("test"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}