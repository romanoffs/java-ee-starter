package com.start.filters.csrf;


import lombok.Getter;
import lombok.extern.java.Log;
import lombok.val;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
public class GenericCSRFStatelessFilter implements Filter {

    private static final String CSRF_HEADER_NAME_PARAMETER = "csrfHeaderName";
    private static final String CSRF_COOKIE_NAME_PARAMETER = "csrfCookieName";

    @Inject
    private TokenBuilderHook tokenBuilder;
    @Inject
    private ResourceCheckerHook resourceChecker;
    @Inject
    private ResponseBuilderHook responseBuilder;

    @Getter
    private String csrfHeaderName = "XSRF-TOKEN";
    @Getter
    private String csrfCookieName = "X-XSRF-TOKEN";

    /**
     * Instantiate the different hooks:
     * {@link TokenBuilderHook}, {@link ResourceCheckerHook},
     * {@link ResponseBuilderHook} and initialize the values of the
     * {@link GenericCSRFStatelessFilter#csrfCookieName} and
     * {@link GenericCSRFStatelessFilter#csrfHeaderName} from the
     * configuration file (web.xml).
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        csrfCookieName =
                filterConfig.getInitParameter(CSRF_COOKIE_NAME_PARAMETER) != null ?
                        filterConfig.getInitParameter(CSRF_COOKIE_NAME_PARAMETER)
                        : "XSRF-TOKEN";

        csrfHeaderName =
                filterConfig.getInitParameter(CSRF_HEADER_NAME_PARAMETER) != null ?
                        filterConfig.getInitParameter(CSRF_HEADER_NAME_PARAMETER)
                        : "X-XSRF-TOKEN";
    }

    /**
     * For each request:
     * <li>
     *     <ul>
     *         create an {@link ExecutionContext}
     *     </ul>
     *     <ul>
     *         check the status of the requested http resource using {@link ResourceCheckerHook}
     *     </ul>
     *     <ul>
     *         if the resource status is {@link ResourceStatus#MUST_NOT_BE_PROTECTED}
     *         or {@link ResourceStatus#MUST_BE_PROTECTED_BUT_NO_COOKIE_ATTACHED} then
     *         create a CSRF cookie having as token the token generates by {@link TokenBuilderHook}
     *     </ul>
     *     <ul>
     *         if the resource status is {@link ResourceStatus#MUST_BE_PROTECTED_AND_COOKIE_ATTACHED}
     *         then compute the {@link CSRFStatus} of the resource and then use the
     *         {@link ResponseBuilderHook} to return the response to the client.
     *     </ul>
     * </li>
     */
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    )
            throws IOException, ServletException {

        ServletResponse returnResponse = response;

        val executionContext = new ExecutionContext(
                resourceChecker,
                responseBuilder,
                tokenBuilder,
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                chain,
                getCsrfCookieName(),
                getCsrfHeaderName()
        );

        val resourceStatus = resourceChecker.checkResourceStatus(executionContext);

        switch (resourceStatus) {
            case MUST_NOT_BE_PROTECTED:
            case MUST_BE_PROTECTED_BUT_NO_COOKIE_ATTACHED:
                Util.createNewCsrfCookieAndAddItToResponse(executionContext);
                break;
            case MUST_BE_PROTECTED_AND_COOKIE_ATTACHED:
                val status = computeCSRFStatus((HttpServletRequest) request);
                returnResponse = responseBuilder.buildResponse(executionContext, status);
                break;
            default:
                break;
        }

        chain.doFilter(request, returnResponse);
    }


    private CSRFStatus computeCSRFStatus(final HttpServletRequest httpRequest) {

        if (httpRequest.getHeader(getCsrfHeaderName()) == null) {
            return CSRFStatus.HEADER_TOKEN_NOT_PRESENT;
        }

        val cookies = Util.getCookiesByName(httpRequest, getCsrfCookieName());

        if (cookies.isEmpty()) {
            return CSRFStatus.COOKIE_NOT_PRESENT;
        }

        if (cookies.stream()
                .anyMatch(cookie ->
                        cookie.getValue() != null
                                && cookie.getValue()
                                .equals(httpRequest.getHeader(getCsrfHeaderName())))) {
            return CSRFStatus.COOKIE_TOKEN_AND_HEADER_TOKEN_MATCH;
        }

        return CSRFStatus.COOKIE_TOKEN_AND_HEADER_TOKEN_MISMATCH;
    }


    @Override
    public void destroy() {
        try {
            tokenBuilder.close();
        } catch (final IOException e) {
            log.warning("Cannot close properly TokenBuilderHook instance.");
        }

        try {
            resourceChecker.close();
        } catch (final IOException e) {
            log.warning("Cannot close properly TokenBuilderHook instance.");
        }
        try {
            responseBuilder.close();
        } catch (final IOException e) {
            log.warning("Cannot close properly TokenBuilderHook instance.");
        }
    }
}