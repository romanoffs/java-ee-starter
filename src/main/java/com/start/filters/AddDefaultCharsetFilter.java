package com.start.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.nio.charset.Charset;

public class AddDefaultCharsetFilter implements Filter {

    private static final String DEFAULT_ENCODING = "ISO-8859-1";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
        if (this.encoding != null && this.encoding.length() != 0 && !this.encoding.equalsIgnoreCase("default")) {
            if (this.encoding.equalsIgnoreCase("system")) {
                this.encoding = Charset.defaultCharset().name();
            } else if (!Charset.isSupported(this.encoding)) {
                throw new IllegalArgumentException("addDefaultCharset.unsupportedCharset");
            }
        } else {
            this.encoding = DEFAULT_ENCODING;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            ResponseWrapper wrapped = new AddDefaultCharsetFilter.ResponseWrapper((HttpServletResponse) response, this.encoding);
            chain.doFilter(request, wrapped);
        } else {
            chain.doFilter(request, response);
        }
    }

    public static class ResponseWrapper extends HttpServletResponseWrapper {
        private String encoding;

        public ResponseWrapper(HttpServletResponse response, String encoding) {
            super(response);
            this.encoding = encoding;
        }

        @Override
        public void setContentType(String contentType) {
            if (contentType != null && !contentType.isEmpty()) {
                if (!contentType.contains("charset=")) {
                    super.setContentType(contentType + ";charset=" + this.encoding);
                } else {
                    super.setContentType(contentType);
                    this.encoding = this.getCharacterEncoding();
                }
            } else {
                super.setContentType(contentType);
            }
        }

        @Override
        public void setHeader(String name, String value) {
            if (name.trim().equalsIgnoreCase("content-type")) {
                this.setContentType(value);
            } else {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (name.trim().equalsIgnoreCase("content-type")) {
                this.setContentType(value);
            } else {
                super.addHeader(name, value);
            }

        }

        @Override
        public void setCharacterEncoding(String charset) {
            super.setCharacterEncoding(charset);
            this.encoding = charset;
        }
    }
}
