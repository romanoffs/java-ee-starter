package com.start.filters.csrf.impl;

import com.start.filters.csrf.ExecutionContext;
import com.start.filters.csrf.ResourceCheckerHook;
import com.start.filters.csrf.ResourceStatus;
import com.start.filters.csrf.Util;
import lombok.extern.java.Log;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@Log
@ApplicationScoped
public class DefaultResourceCheckerHookImpl implements ResourceCheckerHook {

    private static final String RESOURCE = "Resource ";

    @Override
    public void close() throws IOException {
        //nothing to be done
    }

    @Override
    public ResourceStatus checkResourceStatus(ExecutionContext executionContext) {
        val request = executionContext.getHttpRequest();

        if (Util.GET_HTTP_METHOD.equals(request.getMethod()) ) {
            log.info(RESOURCE + request.getPathInfo()
                    + " should NOT be CSRF protected");
            return ResourceStatus.MUST_NOT_BE_PROTECTED;
        }

        if (!executionContext.getCsrfCookies().isEmpty()) {
            log.info(RESOURCE + request.getPathInfo()
                    + " should be CSRF protected and a check will be done");

            return ResourceStatus.MUST_BE_PROTECTED_AND_COOKIE_ATTACHED;
        } else {
            log.info(RESOURCE + request.getPathInfo()
                    + " should be CSRF protected and a cookie will be added");

            return ResourceStatus.MUST_BE_PROTECTED_BUT_NO_COOKIE_ATTACHED;
        }
    }

}
