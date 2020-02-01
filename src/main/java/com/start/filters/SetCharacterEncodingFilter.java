package com.start.filters;

import lombok.Data;

import javax.servlet.*;
import java.io.IOException;

@Data
public class SetCharacterEncodingFilter implements Filter {
    private String encoding = null;
    private boolean ignore = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
        this.ignore = Boolean.getBoolean(filterConfig.getInitParameter("ignore"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.ignore || request.getCharacterEncoding() == null) {
            String characterEncoding = this.selectEncoding(request);
            if (characterEncoding != null) {
                request.setCharacterEncoding(characterEncoding);
            }
        }

        chain.doFilter(request, response);
    }

    protected String selectEncoding(ServletRequest request) {
        return this.encoding;
    }
}