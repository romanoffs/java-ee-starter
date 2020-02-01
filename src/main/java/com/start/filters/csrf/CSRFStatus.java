package com.start.filters.csrf;

import lombok.Getter;

@Getter
public enum CSRFStatus {
    COOKIE_NOT_PRESENT("There is no CSRF cookie in the request but it "
            + " should be . This it looks like a technical problem on our side."
            + "Check how (and if) the cookie is created and added to the response."),
    HEADER_TOKEN_NOT_PRESENT(
            "There is no CSRF header in the "
                    + "request but it should be. This looks like a CSRF attack."),
    COOKIE_TOKEN_AND_HEADER_TOKEN_MISMATCH(
            "There is a difference between token stored in cookie "
                    + "and the token stored in header."),
    COOKIE_TOKEN_AND_HEADER_TOKEN_MATCH("CSRF header and cookie are mathing.");

    private final String statusMessage;

    CSRFStatus(String statusMessage) {
        this.statusMessage = statusMessage;

    }
}