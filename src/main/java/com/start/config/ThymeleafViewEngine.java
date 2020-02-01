package com.start.config;


import com.start.annotations.AuthUser;
import com.start.annotations.TemplateEngineA;
import com.start.entities.User;
import lombok.NoArgsConstructor;
import lombok.val;
import org.eclipse.krazo.engine.ViewEngineBase;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.engine.ViewEngineContext;
import javax.mvc.engine.ViewEngineException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@ApplicationScoped
@NoArgsConstructor
public class ThymeleafViewEngine extends ViewEngineBase {

    @Inject
    private ServletContext servletContext;

    @Inject
    @AuthUser
    private User authUser;

    @Inject
    @TemplateEngineA
    private TemplateEngine templateEngine;

    @Override
    public boolean supports(final String view) {
        return !view.contains(".");
    }

    @Override
    public void processView(ViewEngineContext context) throws ViewEngineException {

        val req = context.getRequest(HttpServletRequest.class);
        val res = context.getResponse(HttpServletResponse.class);

        try {
            val webContext = new WebContext(req, res, servletContext, req.getLocale());
            webContext.setVariables(context.getModels().asMap());
            webContext.setLocale(Locale.forLanguageTag("ru"));
            webContext.setVariable("authUser", authUser);
            val view = context.getView();
            req.setAttribute("view", view);
            templateEngine.process(view, webContext, res.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ViewEngineException(e);
        }
    }
}
