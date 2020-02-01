package com.start.filters.csrf;

import lombok.experimental.UtilityClass;
import lombok.val;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Util {

    public static final String GET_HTTP_METHOD = "GET";

    /**
     * @param req        the http request
     * @param cookieName the cookie name
     * @return Retrieve a {@link List} of cookies having the name specified.
     */
    public static List<Cookie> getCookiesByName(
            final HttpServletRequest req,
            final String cookieName
    ) {
        if (req.getCookies() == null || req.getCookies().length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Create a secured cookie using as name the
     * and as content the token generated using the {@link ExecutionContext#getTokenBuilder()}.
     *
     * @param ec the execution context to use
     */
    public static void createNewCsrfCookieAndAddItToResponse(ExecutionContext ec) {
        val newCookie = new Cookie(
                ec.getCsrfCookieName(),
                ec.getTokenBuilder().buildToken(ec)
        );

        newCookie.setSecure(false);
        newCookie.setHttpOnly(false);
        newCookie.setPath("/");

        ec.getHttpResponse().addCookie(newCookie);
    }
}
