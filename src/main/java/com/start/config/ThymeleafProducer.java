package com.start.config;

import com.start.annotations.TemplateEngineA;
import lombok.val;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class ThymeleafProducer {

    @Produces
    @Singleton
    public ServletContextTemplateResolver servletContextTemplateResolver(ServletContext servletContext) {
        // log.debug("producing lazy template resolver...");
        val resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        return resolver;
    }

    @Produces
    @Singleton
    @TemplateEngineA
    public TemplateEngine templateEngine(
            ServletContextTemplateResolver resolver
    ) {
        // log.debug("producing lazy template engine...");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }


}