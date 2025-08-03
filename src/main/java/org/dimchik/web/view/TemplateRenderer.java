package org.dimchik.web.view;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateRenderer {
    private final Configuration configuration;

    public TemplateRenderer() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        configuration.setLocale(Locale.US);
    }

    public String processTemplate(String templateName) {
        return processTemplate(templateName, Collections.emptyMap());
    }

    public String processTemplate(String templateName, Map<String, Object> data) {
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate(templateName);
            template.process(data, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Failed to process template: " + templateName, e);
        }
    }
}
