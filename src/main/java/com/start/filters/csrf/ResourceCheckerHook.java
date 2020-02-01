package com.start.filters.csrf;

import java.io.Closeable;

/**
 * Hook that will be used to decide if a resource should be protected against
 * CSRF attacks. Each method have as parameter an {@link ExecutionContext}; the
 * active resource can be retrieved from the active http request that can be
 * retrieved using {@link ExecutionContext#getHttpRequest()}.
 *
 * The implementations should be thread safe because only one instance by CSRF
 * filter instance will be created.
 *
 * @author Adrian CITU
 */
public interface ResourceCheckerHook extends Closeable {

    /**
     * @param executionContext the execution context
     * @return {@link ResourceStatus#MUST_NOT_BE_PROTECTED} if the resource
     * should not be CSRF protected, {@link ResourceStatus#MUST_BE_PROTECTED_BUT_NO_COOKIE_ATTACHED}
     * if the resource should be CSRF protected but there is no CSRF cookie presented
     * in the http request (usually it happens when the resource is accessed the first time),
     * {@link ResourceStatus#MUST_BE_PROTECTED_AND_COOKIE_ATTACHED} if the resource
     * should be CSRF protected and a CSRF cookie is present in the http request.
     *
     */
    ResourceStatus checkResourceStatus(ExecutionContext executionContext);
}
