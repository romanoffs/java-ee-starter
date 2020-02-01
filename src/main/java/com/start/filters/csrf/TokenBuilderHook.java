package com.start.filters.csrf;

import java.io.Closeable;

public interface TokenBuilderHook extends Closeable {
    /**
     * Compute a CSRF token. The token should be
     * (cryptographically strong) pseudo-random value.
     *
     * @param executionContext the execution context.
     * @return CSRF token.
     */
    String buildToken(ExecutionContext executionContext);
}
