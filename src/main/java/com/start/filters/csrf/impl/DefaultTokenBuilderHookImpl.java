package com.start.filters.csrf.impl;

import com.start.filters.csrf.ExecutionContext;
import com.start.filters.csrf.TokenBuilderHook;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.UUID;

@ApplicationScoped
public class DefaultTokenBuilderHookImpl implements TokenBuilderHook {

    /**
     * Compute the CSRF token as a random {@link UUID}.
     */
    @Override
    public String buildToken(final ExecutionContext executionContext) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void close() throws IOException {
        //nothing to be done
    }

}
