package com.start.filters.csrf.impl;

import com.start.filters.csrf.*;
import com.start.filters.csrf.CSRFStatus;
import com.start.filters.csrf.ExecutionContext;
import com.start.filters.csrf.ResponseBuilderHook;
import com.start.filters.csrf.Util;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.ws.rs.ForbiddenException;
import java.io.IOException;

@ApplicationScoped
public class DefaultResponseBuilderHookImpl implements ResponseBuilderHook {

    /**
     * If the {@link CSRFStatus} is {@link CSRFStatus#COOKIE_TOKEN_AND_HEADER_TOKEN_MISMATCH}
     * then replace the actual CSRF Cookie with a new one using the {@link TokenBuilderHook}
     * taken from the {@link ExecutionContext}.
     * In all the other cases a {@link SecurityException} will be thrown.
     *
     * @param executionContext the execution context.
     * @param status           the status of the CSRF check of the request.
     */
    @Override
    public ServletResponse buildResponse(
            final ExecutionContext executionContext,
            final CSRFStatus status
    ) {
        switch (status) {
            case COOKIE_NOT_PRESENT:
            case HEADER_TOKEN_NOT_PRESENT:
            case COOKIE_TOKEN_AND_HEADER_TOKEN_MISMATCH:
//                throw new SecurityException(status.getStatusMessage());
                throw new ForbiddenException(status.getStatusMessage());
            case COOKIE_TOKEN_AND_HEADER_TOKEN_MATCH:
                replaceCSRFCookieToResponse(executionContext);
                return executionContext.getHttpResponse();
            default:
                return executionContext.getHttpResponse();
        }
    }

    private void replaceCSRFCookieToResponse(final ExecutionContext executionContext) {

        val oldCookies = executionContext.getCsrfCookies();
        for (Cookie cookie : oldCookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
        }

        Util.createNewCsrfCookieAndAddItToResponse(executionContext);
    }

    @Override
    public void close() throws IOException {
        return;
    }
}
