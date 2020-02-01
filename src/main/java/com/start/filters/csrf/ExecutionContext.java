package com.start.filters.csrf;

import lombok.Getter;
import lombok.Value;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class containing the execution context.
 * A new instance of this class will be instantiated for each execution
 * of {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, FilterChain)}
 * method.
 * Each instance of execution context will contain the following objects:
 * <ul>
 *  <li> the original http request; {@link ExecutionContext#getHttpRequest()}  </li>
 *  <li> the original http response; {@link ExecutionContext#getHttpResponse()} </li>
 *  <li> the original filter chain; {@link ExecutionContext#getChain()} ()} </li>
 *  <li> the original CSRF Cookie (this is a Java8 Optional);{@link ExecutionContext#getCsrfCookies()}  </li>
 *  <li> the name of the CSRF Cookie; {@link ExecutionContext#getCsrfCookieName()} </li>
 *  <li> the name of the CSRF header; {@link ExecutionContext#getCsrfHeaderName()} </li>
 *  <li> an instance of {@link TokenBuilderHook}; {@link ExecutionContext#getTokenBuilder()} ()} </li>
 *  <li> an instance of {@link ResourceCheckerHook}; {@link ExecutionContext#getResourceChecker()} ()} </li>
 *  <li> an instance of {@link ResponseBuilderHook}; {@link ExecutionContext#getResponseBuilder()} ()} </li>
 * </ul>
 *
 * @author Adrian CITU
 */

@Value
@Getter
public class ExecutionContext {

    private final ResourceCheckerHook resourceChecker;
    private final ResponseBuilderHook responseBuilder;
    private final TokenBuilderHook tokenBuilder;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;
    private final FilterChain chain;
    private final List<Cookie> csrfCookies;
    private final String csrfCookieName;
    private final String csrfHeaderName;

    public ExecutionContext(final ResourceCheckerHook resourceChecker,
                            final ResponseBuilderHook responseBuilder,
                            final TokenBuilderHook tokenBuilder,
                            final HttpServletRequest httpRequest,
                            final HttpServletResponse httpResponse,
                            final FilterChain chain,
                            final String csrfCookieName,
                            final String csrfHeaderName) {
        this.resourceChecker = resourceChecker;
        this.responseBuilder = responseBuilder;
        this.tokenBuilder = tokenBuilder;
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.chain = chain;
        this.csrfCookieName = csrfCookieName;
        this.csrfHeaderName = csrfHeaderName;
        this.csrfCookies = Util.getCookiesByName(httpRequest, csrfCookieName);
    }
}