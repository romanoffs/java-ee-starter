package com.start.filters.csrf;

import javax.servlet.ServletResponse;
import java.io.Closeable;

public interface ResponseBuilderHook extends Closeable {

    /**
     * Modify if needed the httpResponse
     * (that can be found in the <code>executionParameter</code>)
     * parameter using {@link ExecutionContext#getHttpResponse()} depending
     * on the CSRFCheckStatus.
     *
     * @param executionContext the execution context.
     * @param status the status of the CSRF check of the request.
     */
    ServletResponse buildResponse(ExecutionContext executionContext, CSRFStatus status);
}