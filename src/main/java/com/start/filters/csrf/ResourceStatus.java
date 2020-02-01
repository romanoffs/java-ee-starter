package com.start.filters.csrf;

public enum ResourceStatus {
    MUST_NOT_BE_PROTECTED,
    MUST_BE_PROTECTED_BUT_NO_COOKIE_ATTACHED,
    MUST_BE_PROTECTED_AND_COOKIE_ATTACHED
}
